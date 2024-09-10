package com.example.languagelearning.vocabulary.by_text.english.prompt;

import com.example.languagelearning.vocabulary.by_text.common.VocabularyByTextPromptParameters;

import static com.example.languagelearning.vocabulary.keyword.english.prompt.EnglishPromptConstants.*;

public class EnglishVocabularyByTextPromptProcessor {

    private EnglishVocabularyByTextPromptProcessor() {
    }

    public static String getPromptForVerbs(VocabularyByTextPromptParameters promptParameters) {

        return "Give me only the verbs for " + promptParameters.requestDto().minLanguageLevel() + "-" + promptParameters.requestDto().maxLanguageLevel() + " english learners based on a text." +
                "The text: [" +
                promptParameters.text() +
                "]" +
                EN_VERBS_JSON_OUTPUT_PART1 +
                "\n-wordTranslation (the value of the field is in " + promptParameters.requestDto().translationLanguage() + ")" +
                EN_VERBS_JSON_OUTPUT_PART2 +
                "\nif there are no verbs in the text or there are no suitable verbs for the provided language levels then " +
                "'englishVerbsContainer' array must have 0 elements";

    }

    public static String getPromptForNouns(VocabularyByTextPromptParameters promptParameters) {

        return "Give me only the nouns for " + promptParameters.requestDto().minLanguageLevel() + "-" + promptParameters.requestDto().maxLanguageLevel() + " english learners based on a text." +
                "The text: [" +
                promptParameters.text() +
                "]" +
                EN_NOUNS_JSON_OUTPUT_PART1 +
                "\n-wordTranslation (the value of the field is in " + promptParameters.requestDto().translationLanguage() + ")" +
                EN_NOUNS_JSON_OUTPUT_PART2 +
                "\nif there are no nouns in the text or there are no suitable nouns for the provided language levels then " +
                "'englishNounsContainer' array must have 0 elements";
    }

    public static String getPromptForAdjectives(VocabularyByTextPromptParameters promptParameters) {

        return "Give me only the adjectives for " + promptParameters.requestDto().minLanguageLevel() + "-" + promptParameters.requestDto().maxLanguageLevel() + " english learners based on a text." +
                "The text: [" +
                promptParameters.text() +
                "]" +
                EN_ADJECTIVES_JSON_OUTPUT_PART1 +
                "\n-wordTranslation (the value of the field is in " + promptParameters.requestDto().translationLanguage() + ")" +
                EN_ADJECTIVES_JSON_OUTPUT_PART2 +
                "\nif there are no adjectives in the text or there are no suitable adjectives for the provided language levels then " +
                "'englishAdjectivesContainer' array must have 0 elements";
    }

    public static String getPromptForIdioms(VocabularyByTextPromptParameters promptParameters) {

        return "Give me only the idioms for " + promptParameters.requestDto().minLanguageLevel() + "-" + promptParameters.requestDto().maxLanguageLevel() + " english learners based on a text." +
                "The text: [" +
                promptParameters.text() +
                "]" +
                EN_IDIOMS_JSON_OUTPUT_PART1 +
                "\n-idiomTranslation (the value of the field is in " + promptParameters.requestDto().translationLanguage() + ")" +
                EN_IDIOMS_JSON_OUTPUT_PART2 +
                "\nif there are no idioms in the text or there are no suitable idioms for the provided language levels then " +
                "'englishIdiomsContainer' array must have 0 elements";
    }

    public static String getPromptForCollocations(VocabularyByTextPromptParameters promptParameters) {

        return "Give me only the collocations for " + promptParameters.requestDto().minLanguageLevel() + "-" + promptParameters.requestDto().maxLanguageLevel() + " english learners based on a text." +
                "The text: [" +
                promptParameters.text() +
                "]" +
                EN_COLLOCATIONS_JSON_OUTPUT_PART1 +
                "\n-collocationTranslation (the value of the field is in " + promptParameters.requestDto().translationLanguage() + ")" +
                EN_COLLOCATIONS_JSON_OUTPUT_PART2 +
                "\nif there are no collocations in the text or there are no suitable collocations for the provided language levels then " +
                "'englishCollocationsContainer' array must have 0 elements";
    }

    public static String getPromptForPrepositionalVerbs(VocabularyByTextPromptParameters promptParameters) {

        return "Give me only the prepositional verbs for " + promptParameters.requestDto().minLanguageLevel() + "-" + promptParameters.requestDto().maxLanguageLevel() + " english learners based on a text." +
                "The text: [" +
                promptParameters.text() +
                "]" +
                EN_PREPOSITIONAL_VERBS_JSON_OUTPUT_PART1 +
                "\n-translation (the value of the field is in " + promptParameters.requestDto().translationLanguage() + ")" +
                EN_PREPOSITIONAL_VERBS_JSON_OUTPUT_PART2 +
                "\nif there are no prepositional verbs in the text or there are no suitable prepositional verbs for the provided language levels then " +
                "'englishPrepositionalVerbsContainer' array must have 0 elements";
    }

    public static String getPromptForPhrasalVerbs(VocabularyByTextPromptParameters promptParameters) {

        return "Give me only the phrasal verbs for " + promptParameters.requestDto().minLanguageLevel() + "-" + promptParameters.requestDto().maxLanguageLevel() + " english learners based on a text." +
                "The text: [" +
                promptParameters.text() +
                "]" +
                EN_PHRASAL_VERBS_JSON_OUTPUT_PART1 +
                "\n-translation (the value of the field is in " + promptParameters.requestDto().translationLanguage() + ")" +
                EN_PHRASAL_VERBS_JSON_OUTPUT_PART2 +
                "\nif there are no phrasal verbs in the text or there are no suitable phrasal verbs for the provided language levels then " +
                "'englishPhrasalVerbsContainer' array must have 0 elements";
    }
}
