package com.example.languagelearning.vocabulary.common.german;

import com.example.languagelearning.openai.OpenAiService;
import com.example.languagelearning.vocabulary.keyword.common.prompt.VocabularyByTextPromptParameters;
import com.example.languagelearning.vocabulary.keyword.common.util.VocabularyJsonUtil;
import com.example.languagelearning.vocabulary.keyword.german.prompt.GermanVocabularyByTextPromptProcessor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static com.example.languagelearning.util.CompletableFutureUtil.tryToExtractSingleCompletedFutureElement;

@Service
public class GermanVocabularyCfJsonUtil {

    private final VocabularyJsonUtil vocabularyJsonUtil;

    public GermanVocabularyCfJsonUtil(VocabularyJsonUtil vocabularyJsonUtil) {
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

    public GermanVocabularyJsonContainer tryToExtractGermanVocabularyJsonContainer(CompletableFuture<GermanVocabularyCfJsonContainer> cfJsonContainer) {
        GermanVocabularyCfJsonContainer container = cfJsonContainer.join();
        String extractedVerbs = tryToExtractSingleCompletedFutureElement(container.verbs());
        String extractedNouns = tryToExtractSingleCompletedFutureElement(container.nouns());
        String extractedAdjectives = tryToExtractSingleCompletedFutureElement(container.adjectives());
        String extractedCollocations = tryToExtractSingleCompletedFutureElement(container.collocations());
        String extractedIdioms = tryToExtractSingleCompletedFutureElement(container.idioms());
        String extractedPrepositionalVerbs = tryToExtractSingleCompletedFutureElement(container.prepositionalVerbs());

        return new GermanVocabularyJsonContainer(
                extractedVerbs, extractedNouns, extractedAdjectives,
                extractedCollocations, extractedIdioms, extractedPrepositionalVerbs
        );
    }
}
