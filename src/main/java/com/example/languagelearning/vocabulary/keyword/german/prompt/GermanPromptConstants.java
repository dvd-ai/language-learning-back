package com.example.languagelearning.vocabulary.keyword.german.prompt;

public final class GermanPromptConstants {

    public static final String DE_VERBS_JSON_OUTPUT_PART1 = """
            The output should be only in a json format.
            Here is the detailed look at the output:
                                    
            {
                "germanVerbsContainer": [{}]
            }
                                    
            germanVerbsContainer has array of objects that have these fields:
            -germanWord (if the verb is reflexive, add "sich" before the verb. Example: "sich freuen" - is a reflexive verb )
            """;
    public static final String DE_VERBS_JSON_OUTPUT_PART2 = """
             -perfectTenseForm (Partizip II form with 'hat' or 'ist')
            -pastTenseForm (Präteritumform)
            -germanDefinition
            -case_ (The value of the field is one of the cases that goes after the verb like 'Dativ', 'Akkusativ', 'Genitiv' or ''. Examples: Example1: 'Er gehört mir.' Here goes Dativ.
             Example2: 'Ich schwimme im Pool' Here goes no case ''.
            -germanExampleSentence
            -isColloquial (whether the word is used in only a colloquial language in German. The value is boolean)
            -isSeparable (whether the verb is separable in German. Example: 'aufräumen' is a separable verb. The value is boolean)
            """;

    public static final String DE_NOUNS_JSON_OUTPUT_PART1 = """
            The output should be only in a json format.
            Here is the detailed look at the output:
                                    
            {
                "germanNounsContainer": [{}]
            }
                                    
            the germanNounsContainer has array of objects that have these fields:
            -germanWord (the singular form with the german gender article 'der', 'die' or 'das')
            -pluralForm (the plural form of the german noun, or '' if the pluralForm doesn't exist in German)
            """;
    public static final String DE_NOUNS_JSON_OUTPUT_PART2 = """
            -germanDefinition
             -germanExampleSentence
             -isColloquial (whether the word is used in only a colloquial language in German. The value is boolean)
             """;

    public static final String DE_ADJECTIVES_JSON_OUTPUT_PART1 = """
            The output should be only in a json format.
            Here is the detailed look at the output:
                                    
            {
                "germanAdjectivesContainer" : [{}]
            }
                                    
            the germanAdjectivesContainer has array of objects that have these fields:
            -germanWord
            """;

    public static final String DE_ADJECTIVES_JSON_OUTPUT_PART2 = """
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

    public static final String DE_IDIOMS_JSON_OUTPUT_PART1 = """
            The output should be only in a json format.
            Here is the detailed look at the output:
                                    
            {
                "germanIdiomsContainer": [{}]
            }
                                    
            the germanIdiomsContainer has array of objects that have these fields:
            -germanIdiom
            """;

    public static final String DE_IDIOMS_JSON_OUTPUT_PART2 = """
            -germanDefinition
            -germanExampleSentence
            """;

    public static final String DE_COLLOCATIONS_JSON_OUTPUT_PART1 = """
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
            """;

    public static final String DE_COLLOCATIONS_JSON_OUTPUT_PART2 = """
            -germanDefinition
            -germanExampleSentence
            """;

    public static final String DE_PREPOSITIONAL_VERBS_JSON_OUTPUT_PART1 = """
            The output should be only in a json format.
                                Here is the detailed look at the output:
                                                        
                                {
                                    "germanPrepositionalVerbsContainer" : [{}]
                                }
                                                        
                                the prepositionalVerbsContainer has array of objects that have these fields:
                                -germanPrepositionalVerb (if the verb is reflexive, add "sich" before the verb. Example: "sich freuen über" - is a reflexive prepositional verb)
                                -caseAfterThePreposition (The value is 'Akkusativ', 'Dativ' or 'Genitiv')
                                -isSeparable (whether the verb is separable in German. Example: 'ankommen auf' is a separable prepositional verb. The value is boolean)
                                """;

    public static final String DE_PREPOSITIONAL_VERBS_JSON_OUTPUT_PART2 = """
            -germanDefinition
             -germanExampleSentence
             -isColloquial (whether the word is used in only a colloquial language. The value is boolean
             """;
}
