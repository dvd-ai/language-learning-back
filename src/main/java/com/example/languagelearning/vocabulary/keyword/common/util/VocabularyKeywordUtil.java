package com.example.languagelearning.vocabulary.keyword.common.util;

import com.example.languagelearning.openai.OpenAiService;
import com.example.languagelearning.vocabulary.keyword.common.prompt.VocabularyByTextPromptParameters;
import com.example.languagelearning.vocabulary.keyword.common.prompt.VocabularyKeywordPromptParameters;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class VocabularyKeywordUtil {

    private VocabularyKeywordUtil() {
    }

    @Async
    public static CompletableFuture<String> getSpeechPartJson(OpenAiService openAiService, VocabularyKeywordPromptParameters promptParameters,
                                                              Function<VocabularyKeywordPromptParameters, String> speechPartDefinition) {
        return openAiService.defaultAsyncCall(speechPartDefinition.apply(promptParameters));
    }

    @Async
    public static CompletableFuture<String> getSpeechPartJson(OpenAiService openAiService, VocabularyByTextPromptParameters promptParameters,
                                                              Function<VocabularyByTextPromptParameters, String> speechPartDefinition) {
        return openAiService.defaultAsyncCall(speechPartDefinition.apply(promptParameters));
    }
}
