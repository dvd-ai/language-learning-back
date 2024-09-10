package com.example.languagelearning.vocabulary.keyword.common.util;

import com.example.languagelearning.openai.OpenAiService;
import com.example.languagelearning.vocabulary.by_text.common.VocabularyByTextPromptParameters;
import com.example.languagelearning.vocabulary.keyword.common.prompt.VocabularyKeywordPromptParameters;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Service
public class VocabularyJsonUtil {

    @Async
    public CompletableFuture<String> getSpeechPartJson(OpenAiService openAiService, VocabularyKeywordPromptParameters promptParameters,
                                                       Function<VocabularyKeywordPromptParameters, String> speechPartDefinition) {
        return openAiService.defaultAsyncCall(speechPartDefinition.apply(promptParameters));
    }

    @Async
    public CompletableFuture<String> getSpeechPartJson(OpenAiService openAiService, VocabularyByTextPromptParameters promptParameters,
                                                       Function<VocabularyByTextPromptParameters, String> speechPartDefinition) {
        return openAiService.defaultAsyncCall(speechPartDefinition.apply(promptParameters));
    }
}
