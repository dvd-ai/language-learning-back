package com.example.languagelearning.vocabulary.keyword.german;

import com.example.languagelearning.error.ApplicationException;
import com.example.languagelearning.openai.OpenAiService;
import com.example.languagelearning.vocabulary.keyword.common.VocabularyKeywordService;
import com.example.languagelearning.vocabulary.keyword.common.dto.Subtopic1NestingLevelBlockContainer;
import com.example.languagelearning.vocabulary.keyword.common.dto.VocabularyByTextRequestDto;
import com.example.languagelearning.vocabulary.keyword.common.dto.VocabularyTopicComparator;
import com.example.languagelearning.vocabulary.keyword.common.dto.VocabularyTopicDto;
import com.example.languagelearning.vocabulary.keyword.common.prompt.VocabularyByTextPromptParameters;
import com.example.languagelearning.vocabulary.keyword.common.prompt.VocabularyKeywordPromptParameters;
import com.example.languagelearning.vocabulary.keyword.common.prompt.VocabularySubtopic1LevelPromptProcessor;

import com.example.languagelearning.vocabulary.keyword.german.dto.GermanVocabularyTopic;
import com.example.languagelearning.vocabulary.keyword.german.dto.container.*;
import com.example.languagelearning.vocabulary.keyword.german.entity.GermanVocabularyTopicEntity;
import com.example.languagelearning.vocabulary.keyword.german.prompt.GermanVocabularyByTextPromptProcessor;
import com.example.languagelearning.vocabulary.keyword.german.dto.GermanVocabularyTopicDto;
import com.example.languagelearning.vocabulary.keyword.german.prompt.GermanVocabularyPromptProcessor;
import com.example.languagelearning.vocabulary.keyword.german.repo.GermanVocabularyTopicEntityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.example.languagelearning.common.LanguageUtil.normalizeLocale;
import static com.example.languagelearning.util.CompletableFutureUtil.extractValuesFromCompletableFutures;
import static com.example.languagelearning.util.CompletableFutureUtil.tryToExtractSingleCompletedFutureElement;
import static com.example.languagelearning.util.TextUtil.breakTextIntoSentencesParts;
import static com.example.languagelearning.util.TextUtil.breakTextIntoSortedParagraphs;
import static com.example.languagelearning.vocabulary.keyword.common.util.VocabularyKeywordUtil.getSpeechPartJson;
import static com.example.languagelearning.vocabulary.keyword.german.GermanVocabularyTopicPostProcessor.performCleanup;


@Service
public class GermanVocabularyKeywordService implements VocabularyKeywordService {

    private final ObjectMapper objectMapper;
    private final GermanVocabularyTopicEntityService vocabularyTopicEntityService;

    private final VocabularySubtopic1LevelPromptProcessor subtopic1LevelPromptProcessor;
    private final GermanVocabularyMapper vocabularyMapper;

    public GermanVocabularyKeywordService(ObjectMapper objectMapper, GermanVocabularyTopicEntityService vocabularyTopicEntityService, VocabularySubtopic1LevelPromptProcessor subtopic1LevelPromptProcessor, GermanVocabularyMapper vocabularyMapper) {
        this.objectMapper = objectMapper;
        this.vocabularyTopicEntityService = vocabularyTopicEntityService;
        this.subtopic1LevelPromptProcessor = subtopic1LevelPromptProcessor;
        this.vocabularyMapper = vocabularyMapper;
    }

    @Override
    public String getLanguage() {
        return normalizeLocale(Locale.GERMAN);
    }

    @Override
    public List<? extends VocabularyTopicDto> processByKeyword(String keyword, OpenAiService openAiService, String translationLanguage) throws JsonProcessingException {
        List<GermanVocabularyTopicDto> vocabularyTopics = getExistingVocabularyTopics(keyword, translationLanguage);
        if (!vocabularyTopics.isEmpty())
            return vocabularyTopics.stream().sorted(new VocabularyTopicComparator()).toList();

        var subtopicBlockEntries = getSubtopic1NestingLevelBlockContainer(keyword, getLanguage(), openAiService);

        List<CompletableFuture<GermanVocabularyTopic>> topicsCompletableFutures = new ArrayList<>();

        for (var subtopicBlock : subtopicBlockEntries.entries()) {
            for (var level1topicName : subtopicBlock.getSubtopic1LevelNames()) {
                var promptParameters = new VocabularyKeywordPromptParameters(keyword, subtopicBlock.getSubtopic0LevelName(), level1topicName, translationLanguage);
                topicsCompletableFutures.add(getCompleteTopicCompletableFuture(openAiService, promptParameters));
            }
        }

        extractValuesFromCompletableFutures(topicsCompletableFutures);
        List<GermanVocabularyTopicDto> existingVocabularyTopics = getExistingVocabularyTopics(keyword, translationLanguage);
        return existingVocabularyTopics.stream().sorted(new VocabularyTopicComparator()).toList();
    }

    @Override
    public void updateVocabularyTopic(VocabularyTopicDto vocabularyTopicDto) {
        var germanVocabularyTopicDto = (GermanVocabularyTopicDto) vocabularyTopicDto;
        var germanVocabularyTopicEntity = vocabularyMapper.mapToEntity(germanVocabularyTopicDto);

        vocabularyTopicEntityService.updateVocabularyTopicEntity(germanVocabularyTopicEntity);
    }

    @Override
    public List<? extends VocabularyTopicDto> getVocabularyByText(VocabularyByTextRequestDto requestDto, OpenAiService openAiService) {
        Map<Integer, String> sortedParagraphs = breakTextIntoSortedParagraphs(requestDto.textContent());
        List<CompletableFuture<GermanVocabularyTopic>> topicsCompletableFutures = new ArrayList<>();

        for (Map.Entry<Integer, String>mapEntry : sortedParagraphs.entrySet()) {
            VocabularyByTextPromptParameters promptParameters = new VocabularyByTextPromptParameters(requestDto, mapEntry.getValue(), mapEntry.getKey());
            if (mapEntry.getValue().length() < 900) {
                topicsCompletableFutures.add(getCompleteTopicByTextCompletableFuture(openAiService, promptParameters));
            } else {
                topicsCompletableFutures.add(getCollectedCfVocabularyTopic(openAiService, promptParameters));
            }
        }

        List<GermanVocabularyTopic> vocabularyTopics = new ArrayList<>(extractValuesFromCompletableFutures(topicsCompletableFutures));
        performCleanup(vocabularyTopics);
        List<GermanVocabularyTopicEntity>entities = vocabularyTopicEntityService.addTopicEntities(vocabularyMapper.mapToEntities(vocabularyTopics, requestDto.translationLanguage()));
        return vocabularyMapper.mapToDtos(entities);
    }

    @Async
    private CompletableFuture<GermanVocabularyTopic> getCompleteTopicByTextCompletableFuture(OpenAiService openAiService, VocabularyByTextPromptParameters promptParameters) {
        CompletableFuture<String> verbs = getSpeechPartJson(openAiService, promptParameters, GermanVocabularyByTextPromptProcessor::getPromptForVerbs);
        CompletableFuture<String> nouns = getSpeechPartJson(openAiService, promptParameters, GermanVocabularyByTextPromptProcessor::getPromptForNouns);
        CompletableFuture<String> adjectives = getSpeechPartJson(openAiService, promptParameters, GermanVocabularyByTextPromptProcessor::getPromptForAdjectives);
        CompletableFuture<String> collocations = getSpeechPartJson(openAiService, promptParameters, GermanVocabularyByTextPromptProcessor::getPromptForCollocations);
        CompletableFuture<String> idioms = getSpeechPartJson(openAiService, promptParameters, GermanVocabularyByTextPromptProcessor::getPromptForIdioms);
        CompletableFuture<String> prepositionalVerbs = getSpeechPartJson(openAiService, promptParameters, GermanVocabularyByTextPromptProcessor::getPromptForPrepositionalVerbs);

        return CompletableFuture.allOf(verbs, nouns, adjectives, collocations, idioms, prepositionalVerbs)
                .thenApply(parts -> createTopicFromPartsByText(
                        verbs, nouns, adjectives, collocations, idioms,
                        prepositionalVerbs, promptParameters)
                );
    }

    @Async
    private CompletableFuture<GermanVocabularyTopic> getCollectedCfVocabularyTopic(OpenAiService openAiService, VocabularyByTextPromptParameters promptParameters) {

        List<String>sentencesParts = breakTextIntoSentencesParts(promptParameters.text());
        List<CompletableFuture<GermanVocabularyTopic>> topicPartsCompletableFutures = new ArrayList<>();

        for (String sentencesPart : sentencesParts) {
            topicPartsCompletableFutures.add(
                    getCompleteTopicByTextCompletableFuture(
                            openAiService,
                            new VocabularyByTextPromptParameters(
                                    promptParameters.requestDto(), sentencesPart, promptParameters.textNumber()
                            )
                    )
            );
        }

        List<GermanVocabularyTopic> vocabularyTopicParts = extractValuesFromCompletableFutures(topicPartsCompletableFutures);
        var resultTopic =  getAccumulatedVocabularyTopic(vocabularyTopicParts, promptParameters);
        return CompletableFuture.completedFuture(resultTopic);
    }

    private GermanVocabularyTopic getAccumulatedVocabularyTopic(List<GermanVocabularyTopic> vocabularyTopicParts, VocabularyByTextPromptParameters promptParameters) {
        GermanVocabularyTopic resultTopic = new GermanVocabularyTopic(
                promptParameters.requestDto().textTopicLabel()
                        .concat(".P")
                        .concat(promptParameters.textNumber().toString()),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>()
        );

        for (GermanVocabularyTopic vt : vocabularyTopicParts) {
            resultTopic.getVerbs().addAll(vt.getVerbs());
            resultTopic.getNouns().addAll(vt.getNouns());
            resultTopic.getAdjectives().addAll(vt.getAdjectives());
            resultTopic.getCollocations().addAll(vt.getCollocations());
            resultTopic.getIdioms().addAll(vt.getIdioms());
            resultTopic.getPrepositionalVerbs().addAll(vt.getPrepositionalVerbs());
        }

        return resultTopic;
    }


    private GermanVocabularyTopic createTopicFromPartsByText(CompletableFuture<String> verbs, CompletableFuture<String> nouns, CompletableFuture<String> adjectives,
                                                              CompletableFuture<String> collocations, CompletableFuture<String> idioms,
                                                              CompletableFuture<String> prepositionalVerbs,
                                                              VocabularyByTextPromptParameters topicParameters) {

        String extractedVerbs = tryToExtractSingleCompletedFutureElement(verbs);
        String extractedNouns = tryToExtractSingleCompletedFutureElement(nouns);
        String extractedAdjectives = tryToExtractSingleCompletedFutureElement(adjectives);
        String extractedCollocations = tryToExtractSingleCompletedFutureElement(collocations);
        String extractedIdioms = tryToExtractSingleCompletedFutureElement(idioms);
        String extractedPrepositionalVerbs = tryToExtractSingleCompletedFutureElement(prepositionalVerbs);

        try {
            return new GermanVocabularyTopic(
                    topicParameters.requestDto().textTopicLabel()
                            .concat(".P")
                            .concat(topicParameters.textNumber().toString()),
                    objectMapper.readValue(extractedVerbs, GermanVerbsContainer.class).germanVerbsContainer(),
                    objectMapper.readValue(extractedNouns, GermanNounsContainer.class).germanNounsContainer(),
                    objectMapper.readValue(extractedAdjectives, GermanAdjectivesContainer.class).germanAdjectivesContainer(),
                    objectMapper.readValue(extractedCollocations, GermanCollocationsContainer.class).germanCollocationsContainer(),
                    objectMapper.readValue(extractedIdioms, GermanIdiomsContainer.class).germanIdiomsContainer(),
                    objectMapper.readValue(extractedPrepositionalVerbs, GermanPrepositionalVerbsContainer.class).germanPrepositionalVerbsContainer()
            );
        } catch (JsonProcessingException e) {
            throw new ApplicationException(e.getMessage());
        }

    }

    private Subtopic1NestingLevelBlockContainer getSubtopic1NestingLevelBlockContainer(String keyword, String targetLanguage, OpenAiService openAiService) throws JsonProcessingException {
        return objectMapper.readValue(
                openAiService.customCall(subtopic1LevelPromptProcessor.getSubtopic1LevelNames(keyword, targetLanguage),
                        OpenAiChatOptions.builder()
                                .withModel("gpt-4o")
                                .build()
                ),
                Subtopic1NestingLevelBlockContainer.class
        );
    }

    private List<GermanVocabularyTopicDto> getExistingVocabularyTopics(String keyword, String translationLanguage) {

        List<GermanVocabularyTopicEntity> entityTopics = vocabularyTopicEntityService.findTopicsByKeywordAndTranslationLanguage(keyword, translationLanguage);

        if (entityTopics.isEmpty())
            return List.of();

        GermanVocabularyMapper vocabularyMapper = new GermanVocabularyMapper();
        return entityTopics
                .stream()
                .map(vocabularyMapper::mapToDto)
                .toList();
    }

    @Async
    private CompletableFuture<GermanVocabularyTopic> getCompleteTopicCompletableFuture(OpenAiService openAiService, VocabularyKeywordPromptParameters promptParameters) {
        CompletableFuture<String> verbs = getSpeechPartJson(openAiService, promptParameters, GermanVocabularyPromptProcessor::getPromptForSubtopic1LevelVerbs);
        CompletableFuture<String> nouns = getSpeechPartJson(openAiService, promptParameters, GermanVocabularyPromptProcessor::getPromptForSubtopic1LevelNouns);
        CompletableFuture<String> adjectives = getSpeechPartJson(openAiService, promptParameters, GermanVocabularyPromptProcessor::getPromptForSubtopic1LevelAdjectives);
        CompletableFuture<String> collocations = getSpeechPartJson(openAiService, promptParameters, GermanVocabularyPromptProcessor::getPromptForSubtopic1LevelCollocations);
        CompletableFuture<String> idioms = getSpeechPartJson(openAiService, promptParameters, GermanVocabularyPromptProcessor::getPromptForSubtopic1LevelIdioms);
        CompletableFuture<String> prepositionalVerbs = getSpeechPartJson(openAiService, promptParameters, GermanVocabularyPromptProcessor::getPromptForSubtopic1LevelPrepositionalVerbs);

        return CompletableFuture.allOf(verbs, nouns, adjectives, collocations, idioms, prepositionalVerbs)
                .thenApply(parts -> createAndSaveTopicFromParts(
                        verbs, nouns, adjectives, collocations, idioms,
                        prepositionalVerbs, promptParameters)
                );
    }

    private GermanVocabularyTopic createAndSaveTopicFromParts(CompletableFuture<String> verbs, CompletableFuture<String> nouns, CompletableFuture<String> adjectives,
                                                              CompletableFuture<String> collocations, CompletableFuture<String> idioms,
                                                              CompletableFuture<String> prepositionalVerbs, VocabularyKeywordPromptParameters topicParameters) {

        String extractedVerbs = tryToExtractSingleCompletedFutureElement(verbs);
        String extractedNouns = tryToExtractSingleCompletedFutureElement(nouns);
        String extractedAdjectives = tryToExtractSingleCompletedFutureElement(adjectives);
        String extractedCollocations = tryToExtractSingleCompletedFutureElement(collocations);
        String extractedIdioms = tryToExtractSingleCompletedFutureElement(idioms);
        String extractedPrepositionalVerbs = tryToExtractSingleCompletedFutureElement(prepositionalVerbs);

        try {
            GermanVocabularyTopic germanVocabularyTopic = new GermanVocabularyTopic(
                    topicParameters
                            .keyword().concat(".")
                            .concat(topicParameters.subtopic0Level()).concat(".")
                            .concat(topicParameters.subtopic1Level()),
                    objectMapper.readValue(extractedVerbs, GermanVerbsContainer.class).germanVerbsContainer(),
                    objectMapper.readValue(extractedNouns, GermanNounsContainer.class).germanNounsContainer(),
                    objectMapper.readValue(extractedAdjectives, GermanAdjectivesContainer.class).germanAdjectivesContainer(),
                    objectMapper.readValue(extractedCollocations, GermanCollocationsContainer.class).germanCollocationsContainer(),
                    objectMapper.readValue(extractedIdioms, GermanIdiomsContainer.class).germanIdiomsContainer(),
                    objectMapper.readValue(extractedPrepositionalVerbs, GermanPrepositionalVerbsContainer.class).germanPrepositionalVerbsContainer()
            );

            vocabularyTopicEntityService.addTopicEntity(new GermanVocabularyTopicEntity(germanVocabularyTopic, topicParameters.translationLanguage()));
            return germanVocabularyTopic;

        } catch (JsonProcessingException e) {
            throw new ApplicationException(e.getMessage());
        }

    }
}
