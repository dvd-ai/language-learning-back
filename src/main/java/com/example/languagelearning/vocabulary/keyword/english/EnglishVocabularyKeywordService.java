package com.example.languagelearning.vocabulary.keyword.english;

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
import com.example.languagelearning.vocabulary.keyword.english.dto.*;
import com.example.languagelearning.vocabulary.keyword.english.dto.container.*;
import com.example.languagelearning.vocabulary.keyword.english.entity.EnglishVocabularyTopicEntity;
import com.example.languagelearning.vocabulary.keyword.english.prompt.EnglishVocabularyByTextPromptProcessor;
import com.example.languagelearning.vocabulary.keyword.english.prompt.EnglishVocabularyPromptProcessor;
import com.example.languagelearning.vocabulary.keyword.english.repo.EnglishVocabularyTopicEntityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.example.languagelearning.common.LanguageUtil.normalizeLocale;
import static com.example.languagelearning.util.CompletableFutureUtil.extractValuesFromCompletableFutures;
import static com.example.languagelearning.util.CompletableFutureUtil.tryToExtractSingleCompletedFutureElement;
import static com.example.languagelearning.util.TextUtil.breakTextIntoSentencesParts;
import static com.example.languagelearning.util.TextUtil.breakTextIntoSortedParagraphs;
import static com.example.languagelearning.vocabulary.keyword.common.util.VocabularyKeywordUtil.getSpeechPartJson;
import static com.example.languagelearning.vocabulary.keyword.english.EnglishVocabularyTopicPostProcessor.performCleanup;
import static java.util.Comparator.comparing;

@Service
public class EnglishVocabularyKeywordService implements VocabularyKeywordService {

    private final ObjectMapper objectMapper;
    private final EnglishVocabularyTopicEntityService vocabularyTopicEntityService;

    private final EnglishVocabularyMapper vocabularyMapper;
    private final VocabularySubtopic1LevelPromptProcessor subtopic1LevelPromptProcessor;

    public EnglishVocabularyKeywordService(ObjectMapper objectMapper, EnglishVocabularyTopicEntityService vocabularyTopicEntityService, EnglishVocabularyMapper vocabularyMapper, VocabularySubtopic1LevelPromptProcessor subtopic1LevelPromptProcessor) {
        this.objectMapper = objectMapper;
        this.vocabularyTopicEntityService = vocabularyTopicEntityService;
        this.vocabularyMapper = vocabularyMapper;
        this.subtopic1LevelPromptProcessor = subtopic1LevelPromptProcessor;
    }

    @Override
    public String getLanguage() {
        return normalizeLocale(Locale.ENGLISH);
    }

    @Override
    public List<? extends VocabularyTopicDto> processByKeyword(String keyword, OpenAiService openAiService, String translationLanguage) throws JsonProcessingException {
        List<EnglishVocabularyTopicDto> vocabularyTopics = getExistingVocabularyTopics(keyword, translationLanguage);
        if (!vocabularyTopics.isEmpty()) {
            return vocabularyTopics.stream().sorted(new VocabularyTopicComparator()).toList();
        }


        var subtopicBlockEntries = getSubtopic1NestingLevelBlockContainer(keyword, getLanguage(), openAiService);

        List<CompletableFuture<EnglishVocabularyTopic>> topicsCompletableFutures = new ArrayList<>();

        for (var subtopicBlock : subtopicBlockEntries.entries()) {
            for (var level1topicName : subtopicBlock.getSubtopic1LevelNames()) {
                var promptParameters = new VocabularyKeywordPromptParameters(keyword, subtopicBlock.getSubtopic0LevelName(), level1topicName, translationLanguage);
                topicsCompletableFutures.add(getCompleteTopicCompletableFuture(openAiService, promptParameters));
            }
        }

        extractValuesFromCompletableFutures(topicsCompletableFutures);

        List<EnglishVocabularyTopicDto> existingVocabularyTopics = getExistingVocabularyTopics(keyword, translationLanguage);
        return existingVocabularyTopics.stream().sorted(new VocabularyTopicComparator()).toList();
    }

    @Override
    public void updateVocabularyTopic(VocabularyTopicDto vocabularyTopicDto) {
        var englishVocabularyTopicDto = (EnglishVocabularyTopicDto) vocabularyTopicDto;
        var englishVocabularyTopicEntity = vocabularyMapper.mapToEntity(englishVocabularyTopicDto);
        vocabularyTopicEntityService.updateVocabularyTopicEntity(englishVocabularyTopicEntity);
    }

    @Override
    public List<? extends VocabularyTopicDto> getVocabularyByText(VocabularyByTextRequestDto requestDto, OpenAiService openAiService) {
        Map<Integer, String>sortedParagraphs = breakTextIntoSortedParagraphs(requestDto.textContent());
        List<CompletableFuture<EnglishVocabularyTopic>> topicsCompletableFutures = new ArrayList<>();

        for (Map.Entry<Integer, String>mapEntry : sortedParagraphs.entrySet()) {
            VocabularyByTextPromptParameters promptParameters = new VocabularyByTextPromptParameters(requestDto, mapEntry.getValue(), mapEntry.getKey());
            if (mapEntry.getValue().length() < 900) {
                topicsCompletableFutures.add(getCompleteTopicByTextCompletableFuture(openAiService, promptParameters));
            } else {
                topicsCompletableFutures.add(getCollectedCfVocabularyTopic(openAiService, promptParameters));
            }
        }

        List<EnglishVocabularyTopic> vocabularyTopics = new ArrayList<>(extractValuesFromCompletableFutures(topicsCompletableFutures));
        performCleanup(vocabularyTopics);
        List<EnglishVocabularyTopicEntity>entities = vocabularyTopicEntityService.addTopicEntities(vocabularyMapper.mapToEntities(vocabularyTopics, requestDto.translationLanguage()));
        return vocabularyMapper.mapToDtos(entities);
    }

    @Async
    private CompletableFuture<EnglishVocabularyTopic> getCollectedCfVocabularyTopic(OpenAiService openAiService, VocabularyByTextPromptParameters promptParameters) {

        List<String>sentencesParts = breakTextIntoSentencesParts(promptParameters.text());
        List<CompletableFuture<EnglishVocabularyTopic>> topicPartsCompletableFutures = new ArrayList<>();

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

        List<EnglishVocabularyTopic> vocabularyTopicParts = extractValuesFromCompletableFutures(topicPartsCompletableFutures);
        var resultTopic =  getAccumulatedVocabularyTopic(vocabularyTopicParts, promptParameters);
        return CompletableFuture.completedFuture(resultTopic);
    }

    private EnglishVocabularyTopic getAccumulatedVocabularyTopic(List<EnglishVocabularyTopic> vocabularyTopicParts, VocabularyByTextPromptParameters promptParameters) {
        EnglishVocabularyTopic resultTopic = new EnglishVocabularyTopic(
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                promptParameters.requestDto().textTopicLabel()
                        .concat(".P")
                        .concat(promptParameters.textNumber().toString())
        );

        for (EnglishVocabularyTopic vt : vocabularyTopicParts) {
            resultTopic.getVerbs().addAll(vt.getVerbs());
            resultTopic.getNouns().addAll(vt.getNouns());
            resultTopic.getAdjectives().addAll(vt.getAdjectives());
            resultTopic.getCollocations().addAll(vt.getCollocations());
            resultTopic.getIdioms().addAll(vt.getIdioms());
            resultTopic.getPrepositionalVerbs().addAll(vt.getPrepositionalVerbs());
            resultTopic.getPhrasalVerbs().addAll(vt.getPhrasalVerbs());
        }

        return resultTopic;
    }




    private Subtopic1NestingLevelBlockContainer getSubtopic1NestingLevelBlockContainer(String keyword, String targetLanguage, OpenAiService openAiService) throws JsonProcessingException {
        return objectMapper.readValue(
                openAiService.customCall(subtopic1LevelPromptProcessor.getSubtopic1LevelNames(keyword, targetLanguage),
                        OpenAiChatOptions.builder()
                                .withModel("gpt-4-turbo")
                                .build()
                ),
                Subtopic1NestingLevelBlockContainer.class
        );
    }

    private List<EnglishVocabularyTopicDto> getExistingVocabularyTopics(String keyword, String translationLanguage) {

        List<EnglishVocabularyTopicEntity> entityTopics = vocabularyTopicEntityService.findTopicsByKeywordAndTranslationLanguage(keyword, translationLanguage);

        if (entityTopics.isEmpty())
            return List.of();

        return entityTopics
                .stream()
                .map(vocabularyMapper::mapToDto)
                .toList();
    }

    @Async
    private CompletableFuture<EnglishVocabularyTopic> getCompleteTopicByTextCompletableFuture(OpenAiService openAiService, VocabularyByTextPromptParameters promptParameters) {
        CompletableFuture<String> verbs = getSpeechPartJson(openAiService, promptParameters, EnglishVocabularyByTextPromptProcessor::getPromptForVerbs);
        CompletableFuture<String> nouns = getSpeechPartJson(openAiService, promptParameters, EnglishVocabularyByTextPromptProcessor::getPromptForNouns);
        CompletableFuture<String> adjectives = getSpeechPartJson(openAiService, promptParameters, EnglishVocabularyByTextPromptProcessor::getPromptForAdjectives);
        CompletableFuture<String> collocations = getSpeechPartJson(openAiService, promptParameters, EnglishVocabularyByTextPromptProcessor::getPromptForCollocations);
        CompletableFuture<String> idioms = getSpeechPartJson(openAiService, promptParameters, EnglishVocabularyByTextPromptProcessor::getPromptForIdioms);
        CompletableFuture<String> prepositionalVerbs = getSpeechPartJson(openAiService, promptParameters, EnglishVocabularyByTextPromptProcessor::getPromptForPrepositionalVerbs);
        CompletableFuture<String> phrasalVerbs = getSpeechPartJson(openAiService, promptParameters, EnglishVocabularyByTextPromptProcessor::getPromptForPhrasalVerbs);

        return CompletableFuture.allOf(verbs, nouns, adjectives, collocations, idioms, prepositionalVerbs, phrasalVerbs)
                .thenApply(parts -> createTopicFromPartsByText(
                        verbs, nouns, adjectives, collocations, idioms,
                        prepositionalVerbs, phrasalVerbs, promptParameters)
                );
    }

    private EnglishVocabularyTopic createTopicFromPartsByText(CompletableFuture<String> verbs, CompletableFuture<String> nouns, CompletableFuture<String> adjectives,
                                                               CompletableFuture<String> collocations, CompletableFuture<String> idioms,
                                                               CompletableFuture<String> prepositionalVerbs, CompletableFuture<String> phrasalVerbs,
                                                               VocabularyByTextPromptParameters topicParameters) {

        String extractedVerbs = tryToExtractSingleCompletedFutureElement(verbs);
        String extractedNouns = tryToExtractSingleCompletedFutureElement(nouns);
        String extractedAdjectives = tryToExtractSingleCompletedFutureElement(adjectives);
        String extractedCollocations = tryToExtractSingleCompletedFutureElement(collocations);
        String extractedIdioms = tryToExtractSingleCompletedFutureElement(idioms);
        String extractedPrepositionalVerbs = tryToExtractSingleCompletedFutureElement(prepositionalVerbs);
        String extractedPhrasalVerbs = tryToExtractSingleCompletedFutureElement(phrasalVerbs);

        try {
            return new EnglishVocabularyTopic(

                    objectMapper.readValue(extractedVerbs, EnglishVerbsContainer.class).englishVerbsContainer(),
                    objectMapper.readValue(extractedNouns, EnglishNounsContainer.class).englishNounsContainer(),
                    objectMapper.readValue(extractedAdjectives, EnglishAdjectivesContainer.class).englishAdjectivesContainer(),
                    objectMapper.readValue(extractedCollocations, EnglishCollocationsContainer.class).englishCollocationsContainer(),
                    objectMapper.readValue(extractedIdioms, EnglishIdiomsContainer.class).englishIdiomsContainer(),
                    objectMapper.readValue(extractedPrepositionalVerbs, EnglishPrepositionalVerbsContainer.class).englishPrepositionalVerbsContainer(),
                    objectMapper.readValue(extractedPhrasalVerbs, EnglishPhrasalVerbsContainer.class).englishPhrasalVerbsContainer(),
                    topicParameters.requestDto().textTopicLabel()
                            .concat(".P")
                            .concat(topicParameters.textNumber().toString())
            );
        } catch (JsonProcessingException e) {
            throw new ApplicationException(e.getMessage());
        }

    }

    @Async
    private CompletableFuture<EnglishVocabularyTopic> getCompleteTopicCompletableFuture(OpenAiService openAiService, VocabularyKeywordPromptParameters promptParameters) {
        CompletableFuture<String> verbs = getSpeechPartJson(openAiService, promptParameters, EnglishVocabularyPromptProcessor::getPromptForSubtopic1LevelVerbs);
        CompletableFuture<String> nouns = getSpeechPartJson(openAiService, promptParameters, EnglishVocabularyPromptProcessor::getPromptForSubtopic1LevelNouns);
        CompletableFuture<String> adjectives = getSpeechPartJson(openAiService, promptParameters, EnglishVocabularyPromptProcessor::getPromptForSubtopic1LevelAdjectives);
        CompletableFuture<String> collocations = getSpeechPartJson(openAiService, promptParameters, EnglishVocabularyPromptProcessor::getPromptForSubtopic1LevelCollocations);
        CompletableFuture<String> idioms = getSpeechPartJson(openAiService, promptParameters, EnglishVocabularyPromptProcessor::getPromptForSubtopic1LevelIdioms);
        CompletableFuture<String> prepositionalVerbs = getSpeechPartJson(openAiService, promptParameters, EnglishVocabularyPromptProcessor::getPromptForSubtopic1LevelPrepositionalVerbs);
        CompletableFuture<String> phrasalVerbs = getSpeechPartJson(openAiService, promptParameters, EnglishVocabularyPromptProcessor::getPromptForSubtopic1LevelPhrasalVerbs);

        return CompletableFuture.allOf(verbs, nouns, adjectives, collocations, idioms, prepositionalVerbs, phrasalVerbs)
                .thenApply(parts -> createAndSaveTopicFromParts(
                        verbs, nouns, adjectives, collocations, idioms,
                        prepositionalVerbs, phrasalVerbs, promptParameters)
                );
    }

    private EnglishVocabularyTopic createAndSaveTopicFromParts(CompletableFuture<String> verbs, CompletableFuture<String> nouns, CompletableFuture<String> adjectives,
                                                               CompletableFuture<String> collocations, CompletableFuture<String> idioms,
                                                               CompletableFuture<String> prepositionalVerbs, CompletableFuture<String> phrasalVerbs,
                                                               VocabularyKeywordPromptParameters topicParameters) {

        String extractedVerbs = tryToExtractSingleCompletedFutureElement(verbs);
        String extractedNouns = tryToExtractSingleCompletedFutureElement(nouns);
        String extractedAdjectives = tryToExtractSingleCompletedFutureElement(adjectives);
        String extractedCollocations = tryToExtractSingleCompletedFutureElement(collocations);
        String extractedIdioms = tryToExtractSingleCompletedFutureElement(idioms);
        String extractedPrepositionalVerbs = tryToExtractSingleCompletedFutureElement(prepositionalVerbs);
        String extractedPhrasalVerbs = tryToExtractSingleCompletedFutureElement(phrasalVerbs);

        try {
            EnglishVocabularyTopic englishVocabularyTopic = new EnglishVocabularyTopic(

                    objectMapper.readValue(extractedVerbs, EnglishVerbsContainer.class).englishVerbsContainer(),
                    objectMapper.readValue(extractedNouns, EnglishNounsContainer.class).englishNounsContainer(),
                    objectMapper.readValue(extractedAdjectives, EnglishAdjectivesContainer.class).englishAdjectivesContainer(),
                    objectMapper.readValue(extractedCollocations, EnglishCollocationsContainer.class).englishCollocationsContainer(),
                    objectMapper.readValue(extractedIdioms, EnglishIdiomsContainer.class).englishIdiomsContainer(),
                    objectMapper.readValue(extractedPrepositionalVerbs, EnglishPrepositionalVerbsContainer.class).englishPrepositionalVerbsContainer(),
                    objectMapper.readValue(extractedPhrasalVerbs, EnglishPhrasalVerbsContainer.class).englishPhrasalVerbsContainer(),
                    topicParameters
                            .keyword().concat(".")
                            .concat(topicParameters.subtopic0Level()).concat(".")
                            .concat(topicParameters.subtopic1Level())
            );

            vocabularyTopicEntityService.addTopicEntity(new EnglishVocabularyTopicEntity(englishVocabularyTopic, topicParameters.translationLanguage()));
            return englishVocabularyTopic;

        } catch (JsonProcessingException e) {
            throw new ApplicationException(e.getMessage());
        }

    }
}
