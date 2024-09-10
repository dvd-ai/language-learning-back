package com.example.languagelearning.vocabulary.common.english.json;

import java.util.concurrent.CompletableFuture;

import static com.example.languagelearning.util.CompletableFutureUtil.tryToExtractSingleCompletedFutureElement;

public class EnglishVocabularyJsonContainerExtractor {
    private EnglishVocabularyJsonContainerExtractor() {
    }

    public static EnglishVocabularyJsonContainer extract(CompletableFuture<EnglishVocabularyCfJsonContainer> cfJsonContainer) {
        EnglishVocabularyCfJsonContainer container = cfJsonContainer.join();
        String extractedVerbs = tryToExtractSingleCompletedFutureElement(container.verbs());
        String extractedNouns = tryToExtractSingleCompletedFutureElement(container.nouns());
        String extractedAdjectives = tryToExtractSingleCompletedFutureElement(container.adjectives());
        String extractedCollocations = tryToExtractSingleCompletedFutureElement(container.collocations());
        String extractedIdioms = tryToExtractSingleCompletedFutureElement(container.idioms());
        String extractedPrepositionalVerbs = tryToExtractSingleCompletedFutureElement(container.prepositionalVerbs());
        String extractedPhrasalVerbs = tryToExtractSingleCompletedFutureElement(container.phrasalVerbs());

        return new EnglishVocabularyJsonContainer(
                extractedVerbs, extractedNouns, extractedAdjectives,
                extractedCollocations, extractedIdioms, extractedPrepositionalVerbs, extractedPhrasalVerbs
        );
    }
}
