package com.example.languagelearning.vocabulary.by_text.common;

import com.example.languagelearning.vocabulary.keyword.common.prompt.VocabularyByTextPromptParameters;

public class VocabularyNameFormatter {

    private VocabularyNameFormatter() {
    }
    public static String getParagraphedVocabularyName(VocabularyByTextPromptParameters topicParameters) {
        return topicParameters.requestDto().textTopicLabel()
                .concat(".P")
                .concat(topicParameters.textNumber().toString());
    }
}
