package com.example.languagelearning.vocabulary.keyword.german.prompt;

import com.example.languagelearning.vocabulary.keyword.common.prompt.VocabularyByTextPromptParameters;

import static com.example.languagelearning.vocabulary.keyword.german.prompt.GermanPromptConstants.*;

public class GermanVocabularyByTextPromptProcessor {
    private GermanVocabularyByTextPromptProcessor() {
    }

    public static String getPromptForVerbs(VocabularyByTextPromptParameters promptParameters) {

        return "Give me only the verbs for " + promptParameters.requestDto().minLanguageLevel() + "-" +  promptParameters.requestDto().maxLanguageLevel() + " german learners based on a text." +
                "The text: [" +
                promptParameters.text() +
                "]" +
                DE_VERBS_JSON_OUTPUT_PART1 +
                "\n-wordTranslation (the value of the field is in " + promptParameters.requestDto().translationLanguage() + ")" +
                DE_VERBS_JSON_OUTPUT_PART2 +
                "\nif there are no verbs in the text or there are no suitable verbs for the provided language levels then " +
                "'germanVerbsContainer' array must have 0 elements";

    }

    public static String getPromptForNouns(VocabularyByTextPromptParameters promptParameters) {

        return "Give me only the nouns for " + promptParameters.requestDto().minLanguageLevel() + "-" +  promptParameters.requestDto().maxLanguageLevel() + " german learners based on a text." +
                "The text: [" +
                promptParameters.text() +
                "]" +
                DE_NOUNS_JSON_OUTPUT_PART1 +
                "\n-wordTranslation (the value of the field is in " + promptParameters.requestDto().translationLanguage() + ")" +
                DE_NOUNS_JSON_OUTPUT_PART2 +
                "\nif there are no nouns in the text or there are no suitable nouns for the provided language levels then " +
                "'germanNounsContainer' array must have 0 elements";
    }

    public static String getPromptForAdjectives(VocabularyByTextPromptParameters promptParameters) {

        return "Give me only the adjectives for " + promptParameters.requestDto().minLanguageLevel() + "-" +  promptParameters.requestDto().maxLanguageLevel() + " german learners based on a text." +
                "The text: [" +
                promptParameters.text() +
                "]" +
                DE_ADJECTIVES_JSON_OUTPUT_PART1 +
                "\n-wordTranslation (the value of the field is in " + promptParameters.requestDto().translationLanguage() + ")" +
                DE_ADJECTIVES_JSON_OUTPUT_PART2 +
                "\nif there are no adjectives in the text or there are no suitable adjectives for the provided language levels then " +
                "'germanAdjectivesContainer' array must have 0 elements";
    }

    public static String getPromptForIdioms(VocabularyByTextPromptParameters promptParameters) {

        return "Give me only the idioms for " + promptParameters.requestDto().minLanguageLevel() + "-" +  promptParameters.requestDto().maxLanguageLevel() + " german learners based on a text." +
                "The text: [" +
                promptParameters.text() +
                "]" +
                DE_IDIOMS_JSON_OUTPUT_PART1 +
                "\n-idiomTranslation (the value of the field is in " + promptParameters.requestDto().translationLanguage() + ")" +
                DE_IDIOMS_JSON_OUTPUT_PART2 +
                "\nif there are no idioms in the text or there are no suitable idioms for the provided language levels then " +
                "'germanIdiomsContainer' array must have 0 elements";
    }

    public static String getPromptForCollocations(VocabularyByTextPromptParameters promptParameters) {

        return "Give me only the collocations for " + promptParameters.requestDto().minLanguageLevel() + "-" +  promptParameters.requestDto().maxLanguageLevel() + " german learners based on a text." +
                "The text: [" +
                promptParameters.text() +
                "]" +
                DE_COLLOCATIONS_JSON_OUTPUT_PART1 +
                "\n-collocationTranslation (the value of the field is in " + promptParameters.requestDto().translationLanguage() + ")" +
                DE_COLLOCATIONS_JSON_OUTPUT_PART2 +
                "\nif there are no collocations in the text or there are no suitable collocations for the provided language levels then " +
                "'germanCollocationsContainer' array must have 0 elements";
    }

    public static String getPromptForPrepositionalVerbs(VocabularyByTextPromptParameters promptParameters) {

        return "Give me only the prepositional verbs for " + promptParameters.requestDto().minLanguageLevel() + "-" +  promptParameters.requestDto().maxLanguageLevel() + " german learners based on a text." +
                "The text: [" +
                promptParameters.text() +
                "]" +
                DE_PREPOSITIONAL_VERBS_JSON_OUTPUT_PART1 +
                "\n-translation (the value of the field is in " + promptParameters.requestDto().translationLanguage() + ")" +
                DE_PREPOSITIONAL_VERBS_JSON_OUTPUT_PART2 +
                "\nif there are no prepositional verbs in the text or there are no suitable prepositional verbs for the provided language levels then " +
                "'germanPrepositionalVerbsContainer' array must have 0 elements";
    }
}
