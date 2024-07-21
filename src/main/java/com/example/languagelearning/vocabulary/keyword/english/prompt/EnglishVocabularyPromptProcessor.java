package com.example.languagelearning.vocabulary.keyword.english.prompt;

import com.example.languagelearning.vocabulary.keyword.common.prompt.VocabularyKeywordPromptParameters;

public final class EnglishVocabularyPromptProcessor {
    public static String getPromptForSubtopic1LevelVerbs(VocabularyKeywordPromptParameters promptParameters) {

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
                "\n-wordTranslation (the value of the field is in " + promptParameters.translationLanguage() + ")" +
                """
                        -presentPerfectForm
                        -pastSimpleForm
                        -englishDefinition
                        -preposition (if it usually comes after the verb (or used together), if not then place "")
                        -englishExampleSentence
                        -isColloquial (whether the word is used in only a colloquial language. The value is boolean)
                        """;
    }

    public static String getPromptForSubtopic1LevelNouns(VocabularyKeywordPromptParameters promptParameters) {

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
                "\n-wordTranslation (the value of the field is in " + promptParameters.translationLanguage() + ")" +
                """
                        -englishDefinition
                        -preposition (if it usually comes after the noun usually (or used together), if not then place "")
                        -englishExampleSentence
                        -isColloquial (whether the word is used in only a colloquial language. The value is boolean)
                        """;
    }

    public static String getPromptForSubtopic1LevelAdjectives(VocabularyKeywordPromptParameters promptParameters) {

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
                "\n-wordTranslation (the value of the field is in " + promptParameters.translationLanguage() + ")" +
                """
                        -englishDefinition
                        -preposition (if it usually comes after the adjective (or used together), if not then place "")
                        -englishExampleSentence
                        -isColloquial (whether the word is used in only a colloquial language. The value is boolean)
                        """;
    }

    public static String getPromptForSubtopic1LevelIdioms(VocabularyKeywordPromptParameters promptParameters) {

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
                "\n-idiomTranslation (the value of the field is in " + promptParameters.translationLanguage() + ")" +
                """
                        -englishDefinition
                        -englishExampleSentence
                        """;
    }

    public static String getPromptForSubtopic1LevelCollocations(VocabularyKeywordPromptParameters promptParameters) {

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
                "\n-collocationTranslation (the value of the field is in " + promptParameters.translationLanguage() + ")" +
                """
                        -englishDefinition
                        -englishExampleSentence
                        """;
    }

    public static String getPromptForSubtopic1LevelPrepositionalVerbs(VocabularyKeywordPromptParameters promptParameters) {

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
                "\n-translation (the value of the field is in " + promptParameters.translationLanguage() + ")" +
                """
                        -englishDefinition
                        -englishExampleSentence
                        -isColloquial (whether the word is used in only a colloquial language. The value is boolean
                        """;
    }

    public static String getPromptForSubtopic1LevelPhrasalVerbs(VocabularyKeywordPromptParameters promptParameters) {

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
                "\n-translation (the value of the field is in " + promptParameters.translationLanguage() + ")" +
                """
                        -englishDefinition
                        -preposition (if it usually comes after the verb (or used together,
                         like in the phrasal verb 'to run away from', 'from' in this case is the preposition), if not then place "")
                        -englishExampleSentence
                        """;
    }
}
