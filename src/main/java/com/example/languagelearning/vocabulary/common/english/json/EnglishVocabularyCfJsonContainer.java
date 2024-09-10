package com.example.languagelearning.vocabulary.common.english.json;

import java.util.concurrent.CompletableFuture;

public record EnglishVocabularyCfJsonContainer(
        CompletableFuture<String> verbs,
        CompletableFuture<String>nouns,
        CompletableFuture<String>adjectives,
        CompletableFuture<String>collocations,
        CompletableFuture<String>idioms,
        CompletableFuture<String>prepositionalVerbs,
        CompletableFuture<String>phrasalVerbs
) {

}
