package com.example.languagelearning.vocabulary.common;

import com.example.languagelearning.vocabulary.common.dto.VocabularyTopic;
import com.example.languagelearning.vocabulary.english.dto.EnglishVocabularyTopic;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/vocabulary")
public class VocabularyController {

    private final VocabularyManager vocabularyManager;

    public VocabularyController(VocabularyManager vocabularyManager) {

        this.vocabularyManager = vocabularyManager;
    }

    @Operation(summary = "Get vocabulary subtopics by keyword, target language, and translation language",
            description = "Fetches up to 24 new or already saved vocabulary subtopics based on a keyword (e.g., 'Clothes'), a targetLanguage, and a translationLanguage.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved vocabulary topics",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(
                                    oneOf = {
                                            EnglishVocabularyTopic.class,
                                            },
                                    type = "object"),
                            uniqueItems = true),
                    examples = {
                            @ExampleObject(name = "targetLanguage = en", ref = "#/components/examples/EnglishVocabularyTopicExample"),
                    }))


    @GetMapping
    public List<VocabularyTopic> getVocabularyByKeyword(@RequestParam String keyword, @RequestParam Locale targetLanguage,
                                                        @RequestParam Locale translationLanguage) {
        return vocabularyManager.getSeveralVocabularyByKeyword(keyword, targetLanguage, translationLanguage);
    }
}
