package com.example.languagelearning.vocabulary.keyword.common.controller;

import com.example.languagelearning.vocabulary.keyword.common.dto.VocabularyTopic;
import com.example.languagelearning.vocabulary.keyword.english.dto.EnglishVocabularyTopic;
import com.example.languagelearning.vocabulary.keyword.german.dto.GermanVocabularyTopic;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Locale;

@Tag(name = "Vocabulary Keyword API")
@RequestMapping("vocabulary/keyword")
public interface VocabularyKeywordController {

    @Operation(summary = "Get vocabulary subtopics by keyword, target language, and translation language",
            description = "Fetches up to 24 new or already saved vocabulary subtopics based on a keyword (e.g., 'Clothes'), a targetLanguage, and a translationLanguage.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved vocabulary topics",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(
                                    oneOf = {
                                            EnglishVocabularyTopic.class,
                                            GermanVocabularyTopic.class
                                    },
                                    type = "object"),
                            uniqueItems = true),
                    examples = {
                            @ExampleObject(name = "keyword = Crime, targetLanguage = en, translationLanguage = ru", ref = "#/components/examples/EnglishVocabularyTopicExample"),
                            @ExampleObject(name = "keyword = Kriminelle Welt, targetLanguage = de, translationLanguage = en", ref = "#/components/examples/GermanVocabularyTopicExample"),
                    }))
    @GetMapping
    List<? extends VocabularyTopic> getVocabularyByKeyword(@RequestParam String keyword, @RequestParam Locale targetLanguage,
                                                           @RequestParam Locale translationLanguage);
}
