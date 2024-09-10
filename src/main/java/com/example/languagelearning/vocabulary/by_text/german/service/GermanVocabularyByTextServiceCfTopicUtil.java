package com.example.languagelearning.vocabulary.by_text.german.service;

import com.example.languagelearning.error.ApplicationException;
import com.example.languagelearning.openai.OpenAiService;
import com.example.languagelearning.vocabulary.common.german.json.GermanVocabularyCfJsonContainer;
import com.example.languagelearning.vocabulary.common.german.json.GermanVocabularyJsonContainer;
import com.example.languagelearning.vocabulary.by_text.common.VocabularyByTextPromptParameters;
import com.example.languagelearning.vocabulary.keyword.german.dto.GermanVocabularyTopic;
import com.example.languagelearning.vocabulary.keyword.german.dto.container.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static com.example.languagelearning.vocabulary.by_text.common.VocabularyNameFormatter.getParagraphedVocabularyName;
import static com.example.languagelearning.vocabulary.common.german.json.GermanVocabularyJsonContainerExtractor.extract;

@Service
public class GermanVocabularyByTextServiceCfTopicUtil {

    private final ObjectMapper objectMapper;
    private final GermanVocabularyByTextCfJsonUtil cfJsonUtil;

    public GermanVocabularyByTextServiceCfTopicUtil(ObjectMapper objectMapper, GermanVocabularyByTextCfJsonUtil cfJsonUtil) {
        this.objectMapper = objectMapper;
        this.cfJsonUtil = cfJsonUtil;
    }

    @Async
    public CompletableFuture<GermanVocabularyTopic> getCompleteTopicByTextCompletableFuture(OpenAiService openAiService, VocabularyByTextPromptParameters promptParameters) {
        CompletableFuture<GermanVocabularyCfJsonContainer>jsonContainer = cfJsonUtil.getJsonContainer(openAiService, promptParameters);

        return CompletableFuture.allOf(jsonContainer)
                .thenApply(parts -> createTopicFromPartsByText(
                        jsonContainer, promptParameters)
                );
    }

    private GermanVocabularyTopic createTopicFromPartsByText(CompletableFuture<GermanVocabularyCfJsonContainer> cfJsonContainer,
                                                             VocabularyByTextPromptParameters topicParameters) {
        GermanVocabularyJsonContainer container = extract(cfJsonContainer);
        try {
            return new GermanVocabularyTopic(
                    getParagraphedVocabularyName(topicParameters),
                    objectMapper.readValue(container.verbs(), GermanVerbsContainer.class).germanVerbsContainer(),
                    objectMapper.readValue(container.nouns(), GermanNounsContainer.class).germanNounsContainer(),
                    objectMapper.readValue(container.adjectives(), GermanAdjectivesContainer.class).germanAdjectivesContainer(),
                    objectMapper.readValue(container.collocations(), GermanCollocationsContainer.class).germanCollocationsContainer(),
                    objectMapper.readValue(container.idioms(), GermanIdiomsContainer.class).germanIdiomsContainer(),
                    objectMapper.readValue(container.prepositionalVerbs(), GermanPrepositionalVerbsContainer.class).germanPrepositionalVerbsContainer()
            );
        } catch (JsonProcessingException e) {
            throw new ApplicationException(e.getMessage());
        }
    }
}
