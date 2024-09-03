package com.example.languagelearning.vocabulary.by_text.common;

import com.example.languagelearning.openai.OpenAiService;
import com.example.languagelearning.vocabulary.keyword.common.prompt.VocabularyByTextPromptParameters;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;


import static com.example.languagelearning.util.CompletableFutureUtil.extractValuesFromCompletableFutures;
import static com.example.languagelearning.util.TextUtil.breakTextIntoSentencesParts;

@Service
public class VocabularyTopicCollectorUtil {

    @Async
    public <T> CompletableFuture<T> getCollectedCfVocabularyTopic(OpenAiService openAiService, VocabularyByTextPromptParameters promptParameters,
                                                                                        BiFunction<List<T>, VocabularyByTextPromptParameters, T> accumulator,
                                                                  BiFunction<OpenAiService, VocabularyByTextPromptParameters, CompletableFuture<T>>vocabularyTopicCreator
    ) {

        List<String> sentencesParts = breakTextIntoSentencesParts(promptParameters.text());
        List<CompletableFuture<T>> topicPartsCompletableFutures = new ArrayList<>();

        for (String sentencesPart : sentencesParts) {
            topicPartsCompletableFutures.add(
                    vocabularyTopicCreator.apply(
                            openAiService,
                            new VocabularyByTextPromptParameters(
                                    promptParameters.requestDto(), sentencesPart, promptParameters.textNumber()
                            )
                    )
            );
        }

        List<T> vocabularyTopicParts = extractValuesFromCompletableFutures(topicPartsCompletableFutures);
        var resultTopic = accumulator.apply(vocabularyTopicParts, promptParameters);
        return CompletableFuture.completedFuture(resultTopic);
    }
}
