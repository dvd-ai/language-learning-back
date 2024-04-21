package com.example.languagelearning.vocabulary.english;

import com.example.languagelearning.error.ApplicationException;
import com.example.languagelearning.openai.OpenAiService;
import com.example.languagelearning.vocabulary.common.VocabularyService;
import com.example.languagelearning.vocabulary.common.dto.Subtopic1NestingLevelBlockContainer;
import com.example.languagelearning.vocabulary.common.dto.VocabularyTopic;
import com.example.languagelearning.vocabulary.english.dto.EnglishVocabularyTopic;
import com.example.languagelearning.vocabulary.english.dto.container.*;
import com.example.languagelearning.vocabulary.english.entity.EnglishVocabularyTopicEntity;
import com.example.languagelearning.vocabulary.english.prompt.EnglishVocabularyPromptParameters;
import com.example.languagelearning.vocabulary.english.repo.EnglishVocabularyRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import static com.example.languagelearning.util.CompletableFutureUtil.extractValuesFromCompletableFutures;
import static com.example.languagelearning.util.CompletableFutureUtil.tryToExtractSingleCompletedFutureElement;
import static com.example.languagelearning.vocabulary.english.prompt.EnglishVocabularyPromptProcessor.*;

@Service
public class EnglishVocabularyService implements VocabularyService {

    private final ObjectMapper objectMapper;
    private final EnglishVocabularyRepo vocabularyRepo;


    public EnglishVocabularyService(ObjectMapper objectMapper, EnglishVocabularyRepo vocabularyRepo) {
        this.objectMapper = objectMapper;
        this.vocabularyRepo = vocabularyRepo;
    }

    @Override
    public Locale getVocabularyLanguage() {
        return Locale.ENGLISH;
    }

    @Override
    public List<VocabularyTopic> processByKeyword(String keyword, OpenAiService openAiService, Locale translationLanguage) throws JsonProcessingException {

        var subtopicBlockEntries = objectMapper.readValue(
                openAiService.customCall(getPromptForSubtopic1LevelNames(keyword),
                        OpenAiChatOptions.builder()
                                .withModel("gpt-4-turbo-2024-04-09")
                                .build()
                ),
                Subtopic1NestingLevelBlockContainer.class
        );

        List<CompletableFuture<VocabularyTopic>> topicsCompletableFutures = new ArrayList<>();

        for (var subtopicBlock : subtopicBlockEntries.entries()) {
            for (var level1topicName : subtopicBlock.getSubtopic1LevelNames()) {
                var promptParameters = new EnglishVocabularyPromptParameters(keyword, subtopicBlock.getSubtopic0LevelName(), level1topicName, translationLanguage);
                topicsCompletableFutures.add(getCompleteTopicCompletableFuture(openAiService, promptParameters));
            }
        }

        List<VocabularyTopic> result = extractValuesFromCompletableFutures(topicsCompletableFutures);
        return result;
    }

    @Async
    private CompletableFuture<VocabularyTopic> getCompleteTopicCompletableFuture(OpenAiService openAiService, EnglishVocabularyPromptParameters promptParameters) {
        CompletableFuture<String> verbs = getVerbs(openAiService, promptParameters);
        CompletableFuture<String> nouns = getNouns(openAiService, promptParameters);
        CompletableFuture<String> adjectives = getAdjectives(openAiService, promptParameters);
        CompletableFuture<String> collocations = getCollocations(openAiService, promptParameters);
        CompletableFuture<String> idioms = getIdioms(openAiService, promptParameters);
        CompletableFuture<String> prepositionalVerbs = getPrepositionalVerbs(openAiService, promptParameters);
        CompletableFuture<String> phrasalVerbs = getPhrasalVerbs(openAiService, promptParameters);

        return CompletableFuture.allOf(verbs, nouns, adjectives, collocations, idioms, prepositionalVerbs, phrasalVerbs)
                .thenApply(parts -> createAndSaveTopicFromParts(
                        verbs, nouns, adjectives, collocations, idioms,
                        prepositionalVerbs, phrasalVerbs, promptParameters)
                );
    }

    private EnglishVocabularyTopic createAndSaveTopicFromParts(CompletableFuture<String> verbs, CompletableFuture<String> nouns, CompletableFuture<String> adjectives,
                                                               CompletableFuture<String> collocations, CompletableFuture<String> idioms,
                                                               CompletableFuture<String> prepositionalVerbs, CompletableFuture<String> phrasalVerbs,
                                                               EnglishVocabularyPromptParameters topicParameters) {

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

            vocabularyRepo.save(new EnglishVocabularyTopicEntity(englishVocabularyTopic));
            return englishVocabularyTopic;

        } catch (JsonProcessingException e) {
            throw new ApplicationException(e.getMessage());
        }

    }

    @Async
    public CompletableFuture<String> getNouns(OpenAiService openAiService, EnglishVocabularyPromptParameters promptParameters) {
        return openAiService.defaultAsyncCall(getPromptForSubtopic1LevelNouns(promptParameters));
    }

    @Async
    public CompletableFuture<String> getVerbs(OpenAiService openAiService, EnglishVocabularyPromptParameters promptParameters) {
        return openAiService.defaultAsyncCall(getPromptForSubtopic1LevelVerbs(promptParameters));
    }

    @Async
    public CompletableFuture<String> getAdjectives(OpenAiService openAiService, EnglishVocabularyPromptParameters promptParameters) {
        return openAiService.defaultAsyncCall(getPromptForSubtopic1LevelAdjectives(promptParameters));
    }

    @Async
    public CompletableFuture<String> getIdioms(OpenAiService openAiService, EnglishVocabularyPromptParameters promptParameters) {
        return openAiService.defaultAsyncCall(getPromptForSubtopic1LevelIdioms(promptParameters));
    }

    @Async
    public CompletableFuture<String> getCollocations(OpenAiService openAiService, EnglishVocabularyPromptParameters promptParameters) {
        return openAiService.defaultAsyncCall(getPromptForSubtopic1LevelCollocations(promptParameters));
    }

    @Async
    public CompletableFuture<String> getPrepositionalVerbs(OpenAiService openAiService, EnglishVocabularyPromptParameters promptParameters) {
        return openAiService.defaultAsyncCall(getPromptForSubtopic1LevelPrepositionalVerbs(promptParameters));
    }

    @Async
    public CompletableFuture<String> getPhrasalVerbs(OpenAiService openAiService, EnglishVocabularyPromptParameters promptParameters) {
        return openAiService.defaultAsyncCall(getPromptForSubtopic1LevelPhrasalVerbs(promptParameters));
    }
}
