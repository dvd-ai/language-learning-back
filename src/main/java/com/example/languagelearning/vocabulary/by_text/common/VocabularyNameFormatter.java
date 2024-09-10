package com.example.languagelearning.vocabulary.by_text.common;

import com.example.languagelearning.vocabulary.keyword.common.prompt.VocabularyKeywordPromptParameters;

public class VocabularyNameFormatter {

    private VocabularyNameFormatter() {
    }
    public static String getParagraphedVocabularyName(VocabularyByTextPromptParameters topicParameters) {
        return topicParameters.requestDto().textTopicLabel()
                .concat(".P")
                .concat(topicParameters.textNumber().toString());
    }

    public static String getVocabularyNameSeparatedByDots(VocabularyKeywordPromptParameters topicParameters) {
        return topicParameters
                .keyword().concat(".")
                .concat(topicParameters.subtopic0Level()).concat(".")
                .concat(topicParameters.subtopic1Level());

    }
}
