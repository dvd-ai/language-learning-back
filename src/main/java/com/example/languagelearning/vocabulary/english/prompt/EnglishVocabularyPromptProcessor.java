package com.example.languagelearning.vocabulary.english.prompt;

public final class EnglishVocabularyPromptProcessor {
    public static String getPromptForSubtopic1LevelNames(String keyword) {
        return "Break the word " + keyword + " into vocabulary subtopics with one level of nesting in  only JSON format. " +
                """
     The output follows this json structure:
   {
        "entries": [{"subtopic0LevelName": "", "subtopic1LevelNames": []}]
   }

    Note, that the size of entries array is up to 6, the size of subtopic1LevelNames is up to 4,
     that contains strings
                        """;
    }

    public static String getPromptForSubtopic1LevelVerbs(EnglishVocabularyPromptParameters promptParameters) {

        return "Give me up to 12 verbs from the topic '" + promptParameters.keyword() + "." + promptParameters.subtopic0Level() + "." + promptParameters.subtopic1Level() + "' that are commonly used in English." +
                """
                        Sort the verbs alphabetically. The output should be only in a json format.
                        Here is the detailed look at the output:
                                                
                        {
                            "englishVerbsContainer": [{}]
                        }
                                                
                        verbsContainer has array of objects that have these fields:
                        -englishWord
                        """ +
                "\n-wordTranslation (the value of the field is in " + promptParameters.translationLanguage().getDisplayLanguage() + ")" +
                        """
                        -presentPerfectForm
                        -pastSimpleForm
                        -englishDefinition
                        -preposition (if it has)
                        -englishExampleSentence
                        -isFormal (whether the word is used in a formal language. The value is true if it's formal, false - if not)
                        """;
    }

    public static String getPromptForSubtopic1LevelNouns(EnglishVocabularyPromptParameters promptParameters) {

        return "Give me up to 12 nouns from the topic '" + promptParameters.keyword() + "." + promptParameters.subtopic0Level() + "." + promptParameters.subtopic1Level() + "' that are commonly used in English." +
                """
                        Sort the nouns alphabetically. The output should be only in a json format.
                        Here is the detailed look at the output:
                                                
                        {
                            "englishNounsContainer": [{}]
                        }
                                                
                        the nounsContainer has array of objects that have these fields:
                        -englishWord
                        """ +
                "\n-wordTranslation (the value of the field is in " + promptParameters.translationLanguage().getDisplayLanguage() + ")" +
                """
                -englishDefinition
                -preposition (if it has)
                -englishExampleSentence
                -isFormal (whether the word is used in a formal language. The value is true if it's formal, false - if not)
                """;
    }

    public static String getPromptForSubtopic1LevelAdjectives(EnglishVocabularyPromptParameters promptParameters) {

        return "Give me up to 12 adjectives from the topic '" + promptParameters.keyword() + "." + promptParameters.subtopic0Level() + "." + promptParameters.subtopic1Level() + "' that are commonly used in English." +
                """
                        Sort the adjectives alphabetically. The output should be only in a json format.
                        Here is the detailed look at the output:
                                                
                        {
                            "englishAdjectivesContainer" : [{}]
                        }
                                                
                        the adjectivesContainer has array of objects that have these fields:
                        -englishWord
                        """ +
                "\n-wordTranslation (the value of the field is in " + promptParameters.translationLanguage().getDisplayLanguage() + ")" +
                """
                -englishDefinition
                -preposition (if it has)
                -englishExampleSentence
                -isFormal (whether the word is used in a formal language. The value is true if it's formal, false - if not)
                """;
    }

    public static String getPromptForSubtopic1LevelIdioms(EnglishVocabularyPromptParameters promptParameters) {

        return "Give me up to 5 idioms from the topic '" + promptParameters.keyword() + "." + promptParameters.subtopic0Level() + "." + promptParameters.subtopic1Level() + "' that are commonly used in English." +
                """
                        The output should be only in a json format.
                        Here is the detailed look at the output:
                                                
                        {
                            "englishIdiomsContainer": [{}]
                        }
                                                
                        the idiomsContainer has array of objects that have these fields:
                        -englishIdiom
                        """ +
                "\n-idiomTranslation (the value of the field is in " + promptParameters.translationLanguage().getDisplayLanguage() + ")" +
                """
                -englishDefinition
                -englishExampleSentence
                """;
    }

    public static String getPromptForSubtopic1LevelCollocations(EnglishVocabularyPromptParameters promptParameters) {

        return "Give me up to 10 collocations from the topic '" + promptParameters.keyword() + "." + promptParameters.subtopic0Level() + "." + promptParameters.subtopic1Level() + "' that are commonly used in English." +
                """
                        The output should be only in a json format.
                        Here is the detailed look at the output:
                                                
                        {
                            "englishCollocationsContainer" : [{}]
                        }
                                                
                        the collocationsContainer has array of objects that have these fields:
                        -englishCollocation
                        """ +
                "\n-collocationTranslation (the value of the field is in " + promptParameters.translationLanguage().getDisplayLanguage() + ")" +
                """
                -englishDefinition
                -englishExampleSentence
                """;
    }

    public static String getPromptForSubtopic1LevelPrepositionalVerbs(EnglishVocabularyPromptParameters promptParameters) {

        return "Give me up to 10 prepositional verbs from the topic '" + promptParameters.keyword() + "." + promptParameters.subtopic0Level() + "." + promptParameters.subtopic1Level() + "' that are commonly used in English." +
                """
                        Sort the verbs alphabetically. The output should be only in a json format.
                        Here is the detailed look at the output:
                                                
                        {
                            "englishPrepositionalVerbsContainer" : [{}]
                        }
                                                
                        the prepositionalVerbsContainer has array of objects that have these fields:
                        -englishPrepositionalVerb
                        """ +
                "\n-translation (the value of the field is in " + promptParameters.translationLanguage().getDisplayLanguage() + ")" +
                """
                -preposition
                -englishDefinition
                -englishExampleSentence
                -isFormal (whether the word is used in a formal language. The value is true if it's formal, false - if not)
                """;
    }

    public static String getPromptForSubtopic1LevelPhrasalVerbs(EnglishVocabularyPromptParameters promptParameters) {

        return "Give me up to 10 phrasal verbs from the topic '" + promptParameters.keyword() + "." + promptParameters.subtopic0Level() + "." + promptParameters.subtopic1Level() + "' that are commonly used in English." +
                """
                        Sort the verbs alphabetically. The output should be only in a json format.
                        Here is the detailed look at the output:
                                                
                        {
                            "englishPhrasalVerbsContainer" : [{}]
                        }
                                                
                        the phrasalVerbsContainer has array of objects that have these fields:
                        -englishPhrasalVerb
                        """ +
                "\n-translation (the value of the field is in " + promptParameters.translationLanguage().getDisplayLanguage() + ")" +
                """
                -englishDefinition
                -englishExampleSentence
                """;
    }
}
