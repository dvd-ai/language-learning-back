package com.example.languagelearning.vocabulary.keyword.english;

import com.example.languagelearning.error.ApplicationException;
import com.example.languagelearning.openai.OpenAiService;
import com.example.languagelearning.vocabulary.common.english.json.EnglishVocabularyCfJsonContainer;
import com.example.languagelearning.vocabulary.common.english.json.EnglishVocabularyJsonContainer;
import com.example.languagelearning.vocabulary.keyword.common.prompt.VocabularyKeywordPromptParameters;
import com.example.languagelearning.vocabulary.keyword.english.dto.EnglishVocabularyTopic;
import com.example.languagelearning.vocabulary.keyword.english.dto.container.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static com.example.languagelearning.vocabulary.by_text.common.VocabularyNameFormatter.getVocabularyNameSeparatedByDots;
import static com.example.languagelearning.vocabulary.common.english.json.EnglishVocabularyJsonContainerExtractor.extract;

@Service
public class EnglishVocabularyKeywordCfTopicUtil {

    private final ObjectMapper objectMapper;
    private final EnglishVocabularyKeywordCfJsonUtil cfJsonUtil;

    public EnglishVocabularyKeywordCfTopicUtil(ObjectMapper objectMapper, EnglishVocabularyKeywordCfJsonUtil cfJsonUtil) {
        this.objectMapper = objectMapper;
        this.cfJsonUtil = cfJsonUtil;
    }

    @Async
    public CompletableFuture<EnglishVocabularyTopic> getCompleteTopicCompletableFuture(OpenAiService openAiService, VocabularyKeywordPromptParameters promptParameters) {
        CompletableFuture<EnglishVocabularyCfJsonContainer> jsonContainer = cfJsonUtil.getJsonContainer(openAiService, promptParameters);

        return CompletableFuture.allOf(jsonContainer)
                .thenApply(parts -> createTopicFromParts(
                        jsonContainer, promptParameters)
                );
    }


    private EnglishVocabularyTopic createTopicFromParts(CompletableFuture<EnglishVocabularyCfJsonContainer> cfJsonContainer,
                                                        VocabularyKeywordPromptParameters topicParameters) {
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
                    getVocabularyNameSeparatedByDots(topicParameters)
            );

        } catch (JsonProcessingException e) {
            throw new ApplicationException(e.getMessage());
        }

    }
}
