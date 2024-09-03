package com.example.languagelearning.vocabulary.by_text.german;

import com.example.languagelearning.error.ApplicationException;
import com.example.languagelearning.openai.OpenAiService;
import com.example.languagelearning.vocabulary.common.german.GermanVocabularyCfJsonContainer;
import com.example.languagelearning.vocabulary.common.german.GermanVocabularyCfJsonUtil;
import com.example.languagelearning.vocabulary.common.german.GermanVocabularyJsonContainer;
import com.example.languagelearning.vocabulary.keyword.common.prompt.VocabularyByTextPromptParameters;
import com.example.languagelearning.vocabulary.keyword.german.dto.GermanVocabularyTopic;
import com.example.languagelearning.vocabulary.keyword.german.dto.container.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static com.example.languagelearning.vocabulary.by_text.common.VocabularyNameFormatter.getParagraphedVocabularyName;

@Service
public class GermanVocabularyByTextServiceCfUtil {

    private final ObjectMapper objectMapper;
    private final GermanVocabularyCfJsonUtil cfJsonUtil;

    public GermanVocabularyByTextServiceCfUtil(ObjectMapper objectMapper, GermanVocabularyCfJsonUtil cfJsonUtil) {
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
        GermanVocabularyJsonContainer container = cfJsonUtil.tryToExtractGermanVocabularyJsonContainer(cfJsonContainer);
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
