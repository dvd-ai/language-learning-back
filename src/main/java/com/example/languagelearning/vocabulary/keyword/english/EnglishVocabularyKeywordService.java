package com.example.languagelearning.vocabulary.keyword.english;

import com.example.languagelearning.error.ApplicationException;
import com.example.languagelearning.openai.OpenAiService;
import com.example.languagelearning.vocabulary.keyword.common.VocabularyKeywordService;
import com.example.languagelearning.vocabulary.keyword.common.dto.Subtopic1NestingLevelBlockContainer;
import com.example.languagelearning.vocabulary.keyword.common.dto.VocabularyTopicDto;
import com.example.languagelearning.vocabulary.keyword.common.prompt.VocabularyKeywordPromptParameters;
import com.example.languagelearning.vocabulary.keyword.common.prompt.VocabularySubtopic1LevelPromptProcessor;
import com.example.languagelearning.vocabulary.keyword.english.dto.EnglishVocabularyTopic;
import com.example.languagelearning.vocabulary.keyword.english.dto.EnglishVocabularyTopicDto;
import com.example.languagelearning.vocabulary.keyword.english.dto.container.*;
import com.example.languagelearning.vocabulary.keyword.english.entity.EnglishVocabularyTopicEntity;
import com.example.languagelearning.vocabulary.keyword.english.prompt.EnglishVocabularyPromptProcessor;
import com.example.languagelearning.vocabulary.keyword.english.repo.EnglishVocabularyTopicEntityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import static com.example.languagelearning.common.LanguageUtil.normalizeLocale;
import static com.example.languagelearning.util.CompletableFutureUtil.extractValuesFromCompletableFutures;
import static com.example.languagelearning.util.CompletableFutureUtil.tryToExtractSingleCompletedFutureElement;
import static com.example.languagelearning.vocabulary.keyword.common.util.VocabularyKeywordUtil.getSpeechPartJson;

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
        if (!vocabularyTopics.isEmpty())
            return vocabularyTopics;

        var subtopicBlockEntries = getSubtopic1NestingLevelBlockContainer(keyword, getLanguage(), openAiService);

        List<CompletableFuture<EnglishVocabularyTopic>> topicsCompletableFutures = new ArrayList<>();

        for (var subtopicBlock : subtopicBlockEntries.entries()) {
            for (var level1topicName : subtopicBlock.getSubtopic1LevelNames()) {
                var promptParameters = new VocabularyKeywordPromptParameters(keyword, subtopicBlock.getSubtopic0LevelName(), level1topicName, translationLanguage);
                topicsCompletableFutures.add(getCompleteTopicCompletableFuture(openAiService, promptParameters));
            }
        }

        extractValuesFromCompletableFutures(topicsCompletableFutures);
        return getExistingVocabularyTopics(keyword, translationLanguage);
    }

    @Override
    public void updateVocabularyTopic(VocabularyTopicDto vocabularyTopicDto) {
        var englishVocabularyTopicDto = (EnglishVocabularyTopicDto) vocabularyTopicDto;
        var englishVocabularyTopicEntity = vocabularyMapper.mapToEntity(englishVocabularyTopicDto);
        vocabularyTopicEntityService.updateVocabularyTopicEntity(englishVocabularyTopicEntity);
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

        EnglishVocabularyMapper vocabularyMapper = new EnglishVocabularyMapper();

        return entityTopics
                .stream()
                .map(vocabularyMapper::mapToDto)
                .toList();
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
