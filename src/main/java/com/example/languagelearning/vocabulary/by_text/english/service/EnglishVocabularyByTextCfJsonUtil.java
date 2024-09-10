package com.example.languagelearning.vocabulary.by_text.english.service;

import com.example.languagelearning.openai.OpenAiService;
import com.example.languagelearning.vocabulary.by_text.common.VocabularyByTextPromptParameters;
import com.example.languagelearning.vocabulary.by_text.english.prompt.EnglishVocabularyByTextPromptProcessor;
import com.example.languagelearning.vocabulary.common.english.json.EnglishVocabularyCfJsonContainer;
import com.example.languagelearning.vocabulary.keyword.common.util.VocabularyJsonUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class EnglishVocabularyByTextCfJsonUtil {

    private final VocabularyJsonUtil vocabularyJsonUtil;

    public EnglishVocabularyByTextCfJsonUtil(VocabularyJsonUtil vocabularyJsonUtil) {
        this.vocabularyJsonUtil = vocabularyJsonUtil;
    }

    @Async
    public CompletableFuture<EnglishVocabularyCfJsonContainer> getJsonContainer(OpenAiService openAiService, VocabularyByTextPromptParameters promptParameters) {
        CompletableFuture<String> verbs = vocabularyJsonUtil.getSpeechPartJson(openAiService, promptParameters, EnglishVocabularyByTextPromptProcessor::getPromptForVerbs);
        CompletableFuture<String> nouns = vocabularyJsonUtil.getSpeechPartJson(openAiService, promptParameters, EnglishVocabularyByTextPromptProcessor::getPromptForNouns);
        CompletableFuture<String> adjectives = vocabularyJsonUtil.getSpeechPartJson(openAiService, promptParameters, EnglishVocabularyByTextPromptProcessor::getPromptForAdjectives);
        CompletableFuture<String> collocations = vocabularyJsonUtil.getSpeechPartJson(openAiService, promptParameters, EnglishVocabularyByTextPromptProcessor::getPromptForCollocations);
        CompletableFuture<String> idioms = vocabularyJsonUtil.getSpeechPartJson(openAiService, promptParameters, EnglishVocabularyByTextPromptProcessor::getPromptForIdioms);
        CompletableFuture<String> prepositionalVerbs = vocabularyJsonUtil.getSpeechPartJson(openAiService, promptParameters, EnglishVocabularyByTextPromptProcessor::getPromptForPrepositionalVerbs);
        CompletableFuture<String> phrasalVerbs = vocabularyJsonUtil.getSpeechPartJson(openAiService, promptParameters, EnglishVocabularyByTextPromptProcessor::getPromptForPhrasalVerbs);

        return CompletableFuture.allOf(verbs, nouns, adjectives, collocations, idioms, prepositionalVerbs)
                .thenApply(parts -> new EnglishVocabularyCfJsonContainer(verbs, nouns, adjectives, collocations, idioms, prepositionalVerbs, phrasalVerbs));
    }
}
