package com.example.languagelearning.vocabulary.keyword.german.prompt;

import com.example.languagelearning.vocabulary.keyword.common.prompt.VocabularyKeywordPromptParameters;
import org.springframework.stereotype.Service;

@Service
public class GermanVocabularyPromptProcessor {

    public static String getPromptForSubtopic1LevelVerbs(VocabularyKeywordPromptParameters promptParameters) {

        return "Give me up to 12 german verbs from the topic '" + promptParameters.keyword() + "." + promptParameters.subtopic0Level() + "." + promptParameters.subtopic1Level() + "' that are commonly used in German." +
                """
                        Sort the verbs alphabetically. The output should be only in a json format.
                        Here is the detailed look at the output:
                                                
                        {
                            "germanVerbsContainer": [{}]
                        }
                                                
                        germanVerbsContainer has array of objects that have these fields:
                        -germanWord (if the verb is reflexive, add "sich" before the verb. Example: "sich freuen" - is a reflexive verb )
                        """ +
                "\n-wordTranslation (the value of the field is in " + promptParameters.translationLanguage() + ")" +
                """
                        -perfectTenseForm (Partizip II form)
                        -pastTenseForm (Präteritumform)
                        -germanDefinition
                        -case_ (The value of the field is one of the cases that goes after the verb like 'Dativ', 'Akkusativ', 'Genitiv' or ''. Examples: Example1: 'Er gehört mir.' Here goes Dativ.
                         Example2: 'Ich schwimme im Pool' Here goes no case ''.
                        -germanExampleSentence
                        -isColloquial (whether the word is used in only a colloquial language in German. The value is boolean)
                        -isSeparable (whether the verb is separable in German. Example: 'aufräumen' is a separable verb. The value is boolean)
                        """;
    }

    public static String getPromptForSubtopic1LevelNouns(VocabularyKeywordPromptParameters promptParameters) {

        return "Give me up to 12 german nouns from the topic '" + promptParameters.keyword() + "." + promptParameters.subtopic0Level() + "." + promptParameters.subtopic1Level() + "' that are commonly used in German." +
                """
                        Sort the nouns alphabetically. The output should be only in a json format.
                        Here is the detailed look at the output:
                                                
                        {
                            "germanNounsContainer": [{}]
                        }
                                                
                        the germanNounsContainer has array of objects that have these fields:
                        -germanWord (the singular form with the german gender article 'der', 'die' or 'das')
                        -pluralForm (the plural form of the german noun, or '' if the pluralForm doesn't exist in German)
                        """ +
                "\n-wordTranslation (the value of the field is in " + promptParameters.translationLanguage() + ")" +
                """
                        -germanDefinition
                        -germanExampleSentence
                        -isColloquial (whether the word is used in only a colloquial language in German. The value is boolean)
                        """;
    }

    public static String getPromptForSubtopic1LevelAdjectives(VocabularyKeywordPromptParameters promptParameters) {

        return "Give me up to 12 german adjectives from the topic '" + promptParameters.keyword() + "." + promptParameters.subtopic0Level() + "." + promptParameters.subtopic1Level() + "' that are commonly used in German." +
                """
                        Sort the adjectives alphabetically. The output should be only in a json format.
                        Here is the detailed look at the output:
                                                
                        {
                            "germanAdjectivesContainer" : [{}]
                        }
                                                
                        the germanAdjectivesContainer has array of objects that have these fields:
                        -germanWord
                        """ +
                "\n-wordTranslation (the value of the field is in " + promptParameters.translationLanguage() + ")" +
                """
                        -germanDefinition
                        -preposition (if it usually comes after the adjective or used together
                        (Examples:
                            Ex. 1: 'Ich bin mit dir nicht zufrieden.' Here goes 'mit' preposition;
                            Ex. 2: 'Sie ist gut in Mathematik.' Here goes 'in' preposition;
                            Ex. 3: 'Der Tisch ist grün.' Here goes no preposition. In this case the value is ''.
                        ))
                        -germanExampleSentence
                        -case_ (The value of the field is one of the cases like 'Dativ', 'Akkusativ', 'Genitiv' or ''. Examples:
                        Examples with adjectives and the cases that go direct after them:
                         'Das Auto ist das Geld wert.' Here goes Akkusativ case;
                         'Die Geschichte ist allen bekannt.'  Here goes Dativ case;
                         'Er ist des Lesens nicht kundig.' Here goes Genitiv case.
                        Examples with adjectives with prepositions:
                          'Ich bin mit dir nicht zufrieden'. Here goes Dativ case (after 'zufrieden mit');
                          'Katie ist auf den Test vorbereitet.' Here goes Akkusativ (after 'vorbereitet auf').
                        )
                        Example with no case:
                          'Der Tisch ist grün.' Here goes no case '' (for the adjective 'grün' goes no case).
                        -isColloquial (whether the word is used in only a colloquial language in German. The value is boolean)
                        """;
    }

    public static String getPromptForSubtopic1LevelIdioms(VocabularyKeywordPromptParameters promptParameters) {

        return "Give me up to 5 German idioms from the topic '" + promptParameters.keyword() + "." + promptParameters.subtopic0Level() + "." + promptParameters.subtopic1Level() + "' that are commonly used in German." +
                """
                        The output should be only in a json format.
                        Here is the detailed look at the output:
                                                
                        {
                            "germanIdiomsContainer": [{}]
                        }
                                                
                        the germanIdiomsContainer has array of objects that have these fields:
                        -germanIdiom
                        """ +
                "\n-idiomTranslation (the value of the field is in " + promptParameters.translationLanguage() + ")" +
                """
                        -germanDefinition
                        -germanExampleSentence
                        """;
    }

    public static String getPromptForSubtopic1LevelCollocations(VocabularyKeywordPromptParameters promptParameters) {

        return "Give me up to 10 German collocations from the topic '" + promptParameters.keyword() + "." + promptParameters.subtopic0Level() + "." + promptParameters.subtopic1Level() + "' that are commonly used in German." +
                """
                        The output should be only in a json format.
                        Here is the detailed look at the output:
                                                
                        {
                            "germanCollocationsContainer" : [{}]
                        }
                                                
                        the collocationsContainer has array of objects that have these fields:
                        -germanCollocation (examples:
                            Example 1 (adjectives + nouns): große Sorge;
                            Example 2 (verb + noun): Abstand nehmen;
                            Example 3 (adverb + adjective / participle): weit entfernt, stark gesunken;
                            Example 4 (verbs + prepositional phrases or Funktionsverbgefüge in German): in Betracht ziehen;
                            Example 5 (noun + noun, which in German often occur in form of compound nouns): die Affenhitze.
                        )
                        """ +
                "\n-collocationTranslation (the value of the field is in " + promptParameters.translationLanguage() + ")" +
                """
                        -germanDefinition
                        -germanExampleSentence
                        """;
    }

    public static String getPromptForSubtopic1LevelPrepositionalVerbs(VocabularyKeywordPromptParameters promptParameters) {

        return "Give me up to 10 German prepositional verbs from the topic '" + promptParameters.keyword() + "." + promptParameters.subtopic0Level() + "." + promptParameters.subtopic1Level() + "' that are commonly used in German." +
                """
                        Sort the verbs alphabetically. The output should be only in a json format.
                        Here is the detailed look at the output:
                                                
                        {
                            "germanPrepositionalVerbsContainer" : [{}]
                        }
                                                
                        the prepositionalVerbsContainer has array of objects that have these fields:
                        -germanPrepositionalVerb (if the verb is reflexive, add "sich" before the verb. Example: "sich freuen über" - is a reflexive prepositional verb)
                        -caseAfterThePreposition (The value is 'Akkusativ', 'Dativ' or 'Genitiv')
                        -isSeparable (whether the verb is separable in German. Example: 'ankommen auf' is a separable prepositional verb. The value is boolean)
                        """ +
                "\n-translation (the value of the field is in " + promptParameters.translationLanguage() + ")" +
                """
                        -germanDefinition
                        -germanExampleSentence
                        -isColloquial (whether the word is used in only a colloquial language. The value is boolean
                        """;
    }
}
