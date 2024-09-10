package com.example.languagelearning.vocabulary.by_text.german.service;

import com.example.languagelearning.openai.OpenAiService;
import com.example.languagelearning.vocabulary.by_text.common.VocabularyByTextPromptParameters;
import com.example.languagelearning.vocabulary.by_text.german.GermanVocabularyByTextPromptProcessor;
import com.example.languagelearning.vocabulary.common.german.json.GermanVocabularyCfJsonContainer;
import com.example.languagelearning.vocabulary.keyword.common.util.VocabularyJsonUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;


@Service
public class GermanVocabularyByTextCfJsonUtil {

    private final VocabularyJsonUtil vocabularyJsonUtil;

    public GermanVocabularyByTextCfJsonUtil(VocabularyJsonUtil vocabularyJsonUtil) {
        this.vocabularyJsonUtil = vocabularyJsonUtil;
    }

    @Async
    public CompletableFuture<GermanVocabularyCfJsonContainer> getJsonContainer(OpenAiService openAiService, VocabularyByTextPromptParameters promptParameters) {
        CompletableFuture<String> verbs = vocabularyJsonUtil.getSpeechPartJson(openAiService, promptParameters, GermanVocabularyByTextPromptProcessor::getPromptForVerbs);
        CompletableFuture<String> nouns = vocabularyJsonUtil.getSpeechPartJson(openAiService, promptParameters, GermanVocabularyByTextPromptProcessor::getPromptForNouns);
        CompletableFuture<String> adjectives = vocabularyJsonUtil.getSpeechPartJson(openAiService, promptParameters, GermanVocabularyByTextPromptProcessor::getPromptForAdjectives);
        CompletableFuture<String> collocations = vocabularyJsonUtil.getSpeechPartJson(openAiService, promptParameters, GermanVocabularyByTextPromptProcessor::getPromptForCollocations);
        CompletableFuture<String> idioms = vocabularyJsonUtil.getSpeechPartJson(openAiService, promptParameters, GermanVocabularyByTextPromptProcessor::getPromptForIdioms);
        CompletableFuture<String> prepositionalVerbs = vocabularyJsonUtil.getSpeechPartJson(openAiService, promptParameters, GermanVocabularyByTextPromptProcessor::getPromptForPrepositionalVerbs);

        return CompletableFuture.allOf(verbs, nouns, adjectives, collocations, idioms, prepositionalVerbs)
                .thenApply(parts -> new GermanVocabularyCfJsonContainer(verbs, nouns, adjectives, collocations, idioms, prepositionalVerbs));
    }
}
