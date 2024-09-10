package com.example.languagelearning.vocabulary.keyword.german;

import com.example.languagelearning.error.ApplicationException;
import com.example.languagelearning.openai.OpenAiService;
import com.example.languagelearning.vocabulary.common.german.json.GermanVocabularyCfJsonContainer;
import com.example.languagelearning.vocabulary.common.german.json.GermanVocabularyJsonContainer;
import com.example.languagelearning.vocabulary.keyword.common.prompt.VocabularyKeywordPromptParameters;
import com.example.languagelearning.vocabulary.keyword.german.dto.GermanVocabularyTopic;
import com.example.languagelearning.vocabulary.keyword.german.dto.container.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static com.example.languagelearning.vocabulary.by_text.common.VocabularyNameFormatter.getVocabularyNameSeparatedByDots;
import static com.example.languagelearning.vocabulary.common.german.json.GermanVocabularyJsonContainerExtractor.extract;


@Service
public class GermanVocabularyKeywordCfTopicUtil {
    private final ObjectMapper objectMapper;
    private final GermanVocabularyKeywordCfJsonUtil cfJsonUtil;

    public GermanVocabularyKeywordCfTopicUtil(ObjectMapper objectMapper, GermanVocabularyKeywordCfJsonUtil cfJsonUtil) {
        this.objectMapper = objectMapper;
        this.cfJsonUtil = cfJsonUtil;
    }

    @Async
    public CompletableFuture<GermanVocabularyTopic> getCompleteTopicCompletableFuture(OpenAiService openAiService, VocabularyKeywordPromptParameters promptParameters) {
        CompletableFuture<GermanVocabularyCfJsonContainer> jsonContainer = cfJsonUtil.getJsonContainer(openAiService, promptParameters);

        return CompletableFuture.allOf(jsonContainer)
                .thenApply(parts -> createTopicFromParts(
                        jsonContainer, promptParameters)
                );
    }


    private GermanVocabularyTopic createTopicFromParts(CompletableFuture<GermanVocabularyCfJsonContainer> cfJsonContainer,
                                                       VocabularyKeywordPromptParameters topicParameters) {
        GermanVocabularyJsonContainer container = extract(cfJsonContainer);

        try {
            return new GermanVocabularyTopic(
                    getVocabularyNameSeparatedByDots(topicParameters),
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
