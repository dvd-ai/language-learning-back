package com.example.languagelearning.vocabulary.keyword.german;

import com.example.languagelearning.error.ApplicationException;
import com.example.languagelearning.openai.OpenAiService;
import com.example.languagelearning.vocabulary.keyword.common.VocabularyKeywordService;
import com.example.languagelearning.vocabulary.keyword.common.dto.Subtopic1NestingLevelBlockContainer;
import com.example.languagelearning.vocabulary.keyword.common.dto.VocabularyTopic;
import com.example.languagelearning.vocabulary.keyword.common.prompt.VocabularyKeywordPromptParameters;
import com.example.languagelearning.vocabulary.keyword.common.prompt.VocabularySubtopic1LevelPromptProcessor;
import com.example.languagelearning.vocabulary.keyword.german.dto.GermanVocabularyTopic;
import com.example.languagelearning.vocabulary.keyword.german.dto.container.*;
import com.example.languagelearning.vocabulary.keyword.german.entity.GermanVocabularyTopicEntity;
import com.example.languagelearning.vocabulary.keyword.german.prompt.GermanVocabularyPromptProcessor;
import com.example.languagelearning.vocabulary.keyword.german.repo.GermanVocabularyTopicEntityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.example.languagelearning.common.LanguageUtil.normalizeLocale;
import static com.example.languagelearning.util.CompletableFutureUtil.extractValuesFromCompletableFutures;
import static com.example.languagelearning.util.CompletableFutureUtil.tryToExtractSingleCompletedFutureElement;
import static com.example.languagelearning.vocabulary.keyword.common.util.VocabularyKeywordUtil.getSpeechPartJson;

@Service
public class GermanVocabularyKeywordService implements VocabularyKeywordService {

    private final ObjectMapper objectMapper;
    private final GermanVocabularyTopicEntityService vocabularyTopicEntityService;

    private final VocabularySubtopic1LevelPromptProcessor subtopic1LevelPromptProcessor;

    public GermanVocabularyKeywordService(ObjectMapper objectMapper, GermanVocabularyTopicEntityService vocabularyTopicEntityService, VocabularySubtopic1LevelPromptProcessor subtopic1LevelPromptProcessor) {
        this.objectMapper = objectMapper;
        this.vocabularyTopicEntityService = vocabularyTopicEntityService;
        this.subtopic1LevelPromptProcessor = subtopic1LevelPromptProcessor;
    }

    @Override
    public String getLanguage() {
        return normalizeLocale(Locale.GERMAN);
    }

    @Override
    public List<? extends VocabularyTopic> processByKeyword(String keyword, OpenAiService openAiService, String translationLanguage) throws JsonProcessingException {
        Optional<List<GermanVocabularyTopic>> vocabularyTopicsOptional = getExistingVocabularyTopics(keyword, translationLanguage);
        if (vocabularyTopicsOptional.isPresent())
            return vocabularyTopicsOptional.get();

        var subtopicBlockEntries = getSubtopic1NestingLevelBlockContainer(keyword, getLanguage(), openAiService);

        List<CompletableFuture<GermanVocabularyTopic>> topicsCompletableFutures = new ArrayList<>();

        for (var subtopicBlock : subtopicBlockEntries.entries()) {
            for (var level1topicName : subtopicBlock.getSubtopic1LevelNames()) {
                var promptParameters = new VocabularyKeywordPromptParameters(keyword, subtopicBlock.getSubtopic0LevelName(), level1topicName, translationLanguage);
                topicsCompletableFutures.add(getCompleteTopicCompletableFuture(openAiService, promptParameters));
            }
        }

        return extractValuesFromCompletableFutures(topicsCompletableFutures);
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

    private Optional<List<GermanVocabularyTopic>> getExistingVocabularyTopics(String keyword, String translationLanguage) {

        List<GermanVocabularyTopicEntity> entityTopics = vocabularyTopicEntityService.findTopicsByKeywordAndTranslationLanguage(keyword, translationLanguage);

        if (entityTopics.isEmpty())
            return Optional.empty();

        GermanVocabularyMapper vocabularyMapper = new GermanVocabularyMapper();
        List<GermanVocabularyTopic> topics = entityTopics
                .stream()
                .map(vocabularyMapper::mapToDto)
                .toList();

        return Optional.of(topics);
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
