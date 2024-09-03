package com.example.languagelearning.vocabulary.by_text.german;

import com.example.languagelearning.common.GermanLanguage;
import com.example.languagelearning.error.ApplicationException;
import com.example.languagelearning.openai.OpenAiService;
import com.example.languagelearning.vocabulary.by_text.common.VocabularyByTextService;
import com.example.languagelearning.vocabulary.by_text.common.dto.VocabularyByTextRequestDto;
import com.example.languagelearning.vocabulary.common.german.GermanVocabularyMapper;
import com.example.languagelearning.vocabulary.common.VocabularyTopicDto;
import com.example.languagelearning.vocabulary.keyword.common.prompt.VocabularyByTextPromptParameters;
import com.example.languagelearning.vocabulary.keyword.german.dto.GermanVocabularyTopic;
import com.example.languagelearning.vocabulary.keyword.german.dto.container.*;
import com.example.languagelearning.vocabulary.keyword.german.entity.GermanVocabularyTopicEntity;
import com.example.languagelearning.vocabulary.keyword.german.prompt.GermanVocabularyByTextPromptProcessor;
import com.example.languagelearning.vocabulary.keyword.german.repo.GermanVocabularyTopicEntityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.example.languagelearning.util.CompletableFutureUtil.extractValuesFromCompletableFutures;
import static com.example.languagelearning.util.CompletableFutureUtil.tryToExtractSingleCompletedFutureElement;
import static com.example.languagelearning.util.TextUtil.breakTextIntoSentencesParts;
import static com.example.languagelearning.util.TextUtil.breakTextIntoSortedParagraphs;
import static com.example.languagelearning.vocabulary.common.german.GermanVocabularyTopicPostProcessor.performCleanup;
import static com.example.languagelearning.vocabulary.keyword.common.util.VocabularyKeywordUtil.getSpeechPartJson;

@Service
public class GermanVocabularyByTextService extends GermanLanguage implements VocabularyByTextService {

    private final GermanVocabularyMapper vocabularyMapper;

    private final ObjectMapper objectMapper;
    private final GermanVocabularyTopicEntityService vocabularyTopicEntityService;

    public GermanVocabularyByTextService(GermanVocabularyMapper vocabularyMapper, ObjectMapper objectMapper, GermanVocabularyTopicEntityService vocabularyTopicEntityService) {
        this.vocabularyMapper = vocabularyMapper;
        this.objectMapper = objectMapper;
        this.vocabularyTopicEntityService = vocabularyTopicEntityService;
    }

    @Override
    public List<? extends VocabularyTopicDto> getVocabularyByText(VocabularyByTextRequestDto requestDto, OpenAiService openAiService) {
        Map<Integer, String> sortedParagraphs = breakTextIntoSortedParagraphs(requestDto.textContent());
        List<CompletableFuture<GermanVocabularyTopic>> topicsCompletableFutures = new ArrayList<>();

        for (Map.Entry<Integer, String> mapEntry : sortedParagraphs.entrySet()) {
            VocabularyByTextPromptParameters promptParameters = new VocabularyByTextPromptParameters(requestDto, mapEntry.getValue(), mapEntry.getKey());
            if (mapEntry.getValue().length() < 900) {
                topicsCompletableFutures.add(getCompleteTopicByTextCompletableFuture(openAiService, promptParameters));
            } else {
                topicsCompletableFutures.add(getCollectedCfVocabularyTopic(openAiService, promptParameters));
            }
        }

        List<GermanVocabularyTopic> vocabularyTopics = new ArrayList<>(extractValuesFromCompletableFutures(topicsCompletableFutures));
        performCleanup(vocabularyTopics);
        List<GermanVocabularyTopicEntity> entities = vocabularyTopicEntityService.addTopicEntities(vocabularyMapper.mapToEntities(vocabularyTopics, requestDto.translationLanguage()));
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

        List<String> sentencesParts = breakTextIntoSentencesParts(promptParameters.text());
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
        var resultTopic = getAccumulatedVocabularyTopic(vocabularyTopicParts, promptParameters);
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
}
