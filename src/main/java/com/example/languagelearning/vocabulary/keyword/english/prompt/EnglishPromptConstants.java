package com.example.languagelearning.vocabulary.keyword.english.prompt;

public final class EnglishPromptConstants {

    private EnglishPromptConstants() {
    }

    public static final String EN_VERBS_JSON_OUTPUT_PART1 = """
            The output should be only in a json format.
            Here is the detailed look at the output:
            
            {
                "englishVerbsContainer": [{}]
            }
            
            verbsContainer has array of objects that have these fields:
            -englishWord
            """;
    public static final String EN_VERBS_JSON_OUTPUT_PART2 = """
            -presentPerfectForm
            -pastSimpleForm
            -englishDefinition
            -preposition (if it usually comes after the verb (or used together), if not then place "")
            -englishExampleSentence
            -isColloquial (whether the word is used in only a colloquial language. The value is boolean)
            """;

    public static final String EN_NOUNS_JSON_OUTPUT_PART1 = """
            The output should be only in a json format.
            Here is the detailed look at the output:
            
            {
                "englishNounsContainer": [{}]
            }
            
            the nounsContainer has array of objects that have these fields:
            -englishWord
            """;
    public static final String EN_NOUNS_JSON_OUTPUT_PART2 = """
            -englishDefinition
            -preposition (if it usually comes after the noun usually (or used together), if not then place "")
            -englishExampleSentence
            -isColloquial (whether the word is used in only a colloquial language. The value is boolean)
            """;

    public static final String EN_ADJECTIVES_JSON_OUTPUT_PART1 = """
            The output should be only in a json format.
            Here is the detailed look at the output:
            
            {
                "englishAdjectivesContainer": [{}]
            }
            
            the adjectivesContainer has array of objects that have these fields:
            -englishWord
            """;

    public static final String EN_ADJECTIVES_JSON_OUTPUT_PART2 = """
            -englishDefinition
            -preposition (if it usually comes after the adjective (or used together), if not then place "")
            -englishExampleSentence
            -isColloquial (whether the word is used in only a colloquial language. The value is boolean)
            """;

    public static final String EN_IDIOMS_JSON_OUTPUT_PART1 = """
            The output should be only in a json format.
            Here is the detailed look at the output:
            
            {
                "englishIdiomsContainer": [{}]
            }
            
            the idiomsContainer has array of objects that have these fields:
            -englishIdiom
            """;

    public static final String EN_IDIOMS_JSON_OUTPUT_PART2 = """
            -englishDefinition
            -englishExampleSentence
            """;

    public static final String EN_COLLOCATIONS_JSON_OUTPUT_PART1 = """
            The output should be only in a json format.
            Here is the detailed look at the output:
            
            {
                "englishCollocationsContainer" : [{}]
            }
            
            the collocationsContainer has array of objects that have these fields:
            -englishCollocation
            """;

    public static final String EN_COLLOCATIONS_JSON_OUTPUT_PART2 = """
            -englishDefinition
            -englishExampleSentence
            """;

    public static final String EN_PREPOSITIONAL_VERBS_JSON_OUTPUT_PART1 = """
            The output should be only in a json format.
                                Here is the detailed look at the output:
            
                                {
                                    "englishPrepositionalVerbsContainer" : [{}]
                                }
            
                                the prepositionalVerbsContainer has array of objects that have these fields:
                                -englishPrepositionalVerb
            """;

    public static final String EN_PREPOSITIONAL_VERBS_JSON_OUTPUT_PART2 = """
            -englishDefinition
            -englishExampleSentence
            -isColloquial (whether the word is used in only a colloquial language. The value is boolean
            """;

    public static final String EN_PHRASAL_VERBS_JSON_OUTPUT_PART1 = """
            The output should be only in a json format.
                        Here is the detailed look at the output:
            
                        {
                            "englishPhrasalVerbsContainer" : [{}]
                        }
            
                        the phrasalVerbsContainer has array of objects that have these fields:
                        -englishPhrasalVerb
            """;

    public static final String EN_PHRASAL_VERBS_JSON_OUTPUT_PART2 = """
            -englishDefinition
            -preposition (if it usually comes after the verb (or used together,
             like in the phrasal verb 'to run away from', 'from' in this case is the preposition), if not then place "")
            -englishExampleSentence
            """;

}

