package com.example.languagelearning.vocabulary.keyword.common.prompt;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("!prod")
@Service
public class VocabularySubtopic1LevelPromptProcessorNotProd implements VocabularySubtopic1LevelPromptProcessor {

    @Override
    public String getSubtopic1LevelNames(String keyword, String targetLanguage) {
        return "Break the word " + keyword + " into vocabulary subtopics with one level of nesting in  only JSON format. " +
                """
                          The output follows this json structure:
                        {
                             "entries": [{"subtopic0LevelName": "", "subtopic1LevelNames": []}]
                        }

                         Note, that the size of entries array is up to 1, the size of subtopic1LevelNames is up to 1,
                          that contains strings. The values must be in the language: 
                                             """ + targetLanguage;
    }
}
