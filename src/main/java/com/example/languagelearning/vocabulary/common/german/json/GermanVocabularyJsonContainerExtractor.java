package com.example.languagelearning.vocabulary.common.german.json;

import java.util.concurrent.CompletableFuture;

import static com.example.languagelearning.util.CompletableFutureUtil.tryToExtractSingleCompletedFutureElement;

public class GermanVocabularyJsonContainerExtractor {

    private GermanVocabularyJsonContainerExtractor() {
    }

    public static GermanVocabularyJsonContainer extract(CompletableFuture<GermanVocabularyCfJsonContainer> cfJsonContainer) {
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
