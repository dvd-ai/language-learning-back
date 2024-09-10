package com.example.languagelearning.vocabulary.common.german.json;

import java.util.concurrent.CompletableFuture;

public record GermanVocabularyCfJsonContainer(
        CompletableFuture<String> verbs,
        CompletableFuture<String> nouns,
        CompletableFuture<String> adjectives,
        CompletableFuture<String> collocations,
        CompletableFuture<String> idioms,
        CompletableFuture<String> prepositionalVerbs
) {

}
