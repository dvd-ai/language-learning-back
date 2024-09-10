package com.example.languagelearning.vocabulary.keyword.german;

import com.example.languagelearning.openai.OpenAiService;

import com.example.languagelearning.vocabulary.common.german.json.GermanVocabularyCfJsonContainer;
import com.example.languagelearning.vocabulary.keyword.common.prompt.VocabularyKeywordPromptParameters;
import com.example.languagelearning.vocabulary.keyword.common.util.VocabularyJsonUtil;

import com.example.languagelearning.vocabulary.keyword.german.prompt.GermanVocabularyPromptProcessor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class GermanVocabularyKeywordCfJsonUtil {
    private final VocabularyJsonUtil vocabularyJsonUtil;

    public GermanVocabularyKeywordCfJsonUtil(VocabularyJsonUtil vocabularyJsonUtil) {
        this.vocabularyJsonUtil = vocabularyJsonUtil;
    }

    @Async
    public CompletableFuture<GermanVocabularyCfJsonContainer> getJsonContainer(OpenAiService openAiService, VocabularyKeywordPromptParameters promptParameters) {
        CompletableFuture<String> verbs = vocabularyJsonUtil.getSpeechPartJson(openAiService, promptParameters, GermanVocabularyPromptProcessor::getPromptForSubtopic1LevelVerbs);
        CompletableFuture<String> nouns = vocabularyJsonUtil.getSpeechPartJson(openAiService, promptParameters, GermanVocabularyPromptProcessor::getPromptForSubtopic1LevelNouns);
        CompletableFuture<String> adjectives = vocabularyJsonUtil.getSpeechPartJson(openAiService, promptParameters, GermanVocabularyPromptProcessor::getPromptForSubtopic1LevelAdjectives);
        CompletableFuture<String> collocations = vocabularyJsonUtil.getSpeechPartJson(openAiService, promptParameters, GermanVocabularyPromptProcessor::getPromptForSubtopic1LevelCollocations);
        CompletableFuture<String> idioms = vocabularyJsonUtil.getSpeechPartJson(openAiService, promptParameters, GermanVocabularyPromptProcessor::getPromptForSubtopic1LevelIdioms);
        CompletableFuture<String> prepositionalVerbs = vocabularyJsonUtil.getSpeechPartJson(openAiService, promptParameters, GermanVocabularyPromptProcessor::getPromptForSubtopic1LevelPrepositionalVerbs);


        return CompletableFuture.allOf(verbs, nouns, adjectives, collocations, idioms, prepositionalVerbs)
                .thenApply(parts -> new GermanVocabularyCfJsonContainer(verbs, nouns, adjectives, collocations, idioms, prepositionalVerbs));
    }
}
