package com.example.languagelearning.vocabulary.keyword.english;

import com.example.languagelearning.openai.OpenAiService;
import com.example.languagelearning.vocabulary.common.english.json.EnglishVocabularyCfJsonContainer;
import com.example.languagelearning.vocabulary.keyword.common.prompt.VocabularyKeywordPromptParameters;
import com.example.languagelearning.vocabulary.keyword.common.util.VocabularyJsonUtil;
import com.example.languagelearning.vocabulary.keyword.english.prompt.EnglishVocabularyPromptProcessor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class EnglishVocabularyKeywordCfJsonUtil {

    private final VocabularyJsonUtil vocabularyJsonUtil;

    public EnglishVocabularyKeywordCfJsonUtil(VocabularyJsonUtil vocabularyJsonUtil) {
        this.vocabularyJsonUtil = vocabularyJsonUtil;
    }

    @Async
    public CompletableFuture<EnglishVocabularyCfJsonContainer> getJsonContainer(OpenAiService openAiService, VocabularyKeywordPromptParameters promptParameters) {
        CompletableFuture<String> verbs = vocabularyJsonUtil.getSpeechPartJson(openAiService, promptParameters, EnglishVocabularyPromptProcessor::getPromptForSubtopic1LevelVerbs);
        CompletableFuture<String> nouns = vocabularyJsonUtil.getSpeechPartJson(openAiService, promptParameters, EnglishVocabularyPromptProcessor::getPromptForSubtopic1LevelNouns);
        CompletableFuture<String> adjectives = vocabularyJsonUtil.getSpeechPartJson(openAiService, promptParameters, EnglishVocabularyPromptProcessor::getPromptForSubtopic1LevelAdjectives);
        CompletableFuture<String> collocations = vocabularyJsonUtil.getSpeechPartJson(openAiService, promptParameters, EnglishVocabularyPromptProcessor::getPromptForSubtopic1LevelCollocations);
        CompletableFuture<String> idioms = vocabularyJsonUtil.getSpeechPartJson(openAiService, promptParameters, EnglishVocabularyPromptProcessor::getPromptForSubtopic1LevelIdioms);
        CompletableFuture<String> prepositionalVerbs = vocabularyJsonUtil.getSpeechPartJson(openAiService, promptParameters, EnglishVocabularyPromptProcessor::getPromptForSubtopic1LevelPrepositionalVerbs);
        CompletableFuture<String> phrasalVerbs = vocabularyJsonUtil.getSpeechPartJson(openAiService, promptParameters, EnglishVocabularyPromptProcessor::getPromptForSubtopic1LevelPhrasalVerbs);

        return CompletableFuture.allOf(verbs, nouns, adjectives, collocations, idioms, prepositionalVerbs)
                .thenApply(parts -> new EnglishVocabularyCfJsonContainer(verbs, nouns, adjectives, collocations, idioms, prepositionalVerbs, phrasalVerbs));
    }
}
