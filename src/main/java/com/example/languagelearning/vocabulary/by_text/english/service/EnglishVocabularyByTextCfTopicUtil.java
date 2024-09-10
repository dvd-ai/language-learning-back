package com.example.languagelearning.vocabulary.by_text.english.service;

import com.example.languagelearning.error.ApplicationException;
import com.example.languagelearning.openai.OpenAiService;
import com.example.languagelearning.vocabulary.common.english.json.EnglishVocabularyCfJsonContainer;
import com.example.languagelearning.vocabulary.common.english.json.EnglishVocabularyJsonContainer;
import com.example.languagelearning.vocabulary.by_text.common.VocabularyByTextPromptParameters;

import com.example.languagelearning.vocabulary.keyword.common.util.VocabularyJsonUtil;
import com.example.languagelearning.vocabulary.keyword.english.dto.EnglishVocabularyTopic;
import com.example.languagelearning.vocabulary.keyword.english.dto.container.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static com.example.languagelearning.vocabulary.by_text.common.VocabularyNameFormatter.getParagraphedVocabularyName;
import static com.example.languagelearning.vocabulary.common.english.json.EnglishVocabularyJsonContainerExtractor.extract;

@Service
public class EnglishVocabularyByTextCfTopicUtil {

    private final ObjectMapper objectMapper;
    private final EnglishVocabularyByTextCfJsonUtil cfJsonUtil;

    public EnglishVocabularyByTextCfTopicUtil(ObjectMapper objectMapper, EnglishVocabularyByTextCfJsonUtil cfJsonUtil, VocabularyJsonUtil vocabularyJsonUtil) {
        this.objectMapper = objectMapper;
        this.cfJsonUtil = cfJsonUtil;
    }
    
    @Async
    public CompletableFuture<EnglishVocabularyTopic> getCompleteTopicByTextCompletableFuture(OpenAiService openAiService, VocabularyByTextPromptParameters promptParameters) {
        CompletableFuture<EnglishVocabularyCfJsonContainer>jsonContainer = cfJsonUtil.getJsonContainer(openAiService, promptParameters);

        return CompletableFuture.allOf(jsonContainer)
                .thenApply(parts -> createTopicFromPartsByText(
                        jsonContainer, promptParameters)
                );
    }

    private EnglishVocabularyTopic createTopicFromPartsByText(CompletableFuture<EnglishVocabularyCfJsonContainer> cfJsonContainer,
                                                             VocabularyByTextPromptParameters topicParameters) {
        EnglishVocabularyJsonContainer container = extract(cfJsonContainer);
        try {
            return new EnglishVocabularyTopic(
                    objectMapper.readValue(container.verbs(), EnglishVerbsContainer.class).englishVerbsContainer(),
                    objectMapper.readValue(container.nouns(), EnglishNounsContainer.class).englishNounsContainer(),
                    objectMapper.readValue(container.adjectives(), EnglishAdjectivesContainer.class).englishAdjectivesContainer(),
                    objectMapper.readValue(container.collocations(), EnglishCollocationsContainer.class).englishCollocationsContainer(),
                    objectMapper.readValue(container.idioms(), EnglishIdiomsContainer.class).englishIdiomsContainer(),
                    objectMapper.readValue(container.prepositionalVerbs(), EnglishPrepositionalVerbsContainer.class).englishPrepositionalVerbsContainer(),
                    objectMapper.readValue(container.phrasalVerbs(), EnglishPhrasalVerbsContainer.class).englishPhrasalVerbsContainer(),
                    getParagraphedVocabularyName(topicParameters)
            );
        } catch (JsonProcessingException e) {
            throw new ApplicationException(e.getMessage());
        }
    }
}
