package com.example.languagelearning.vocabulary.common;

import com.example.languagelearning.vocabulary.common.dto.VocabularyTopic;
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

    @GetMapping
    public List<VocabularyTopic> getVocabularyByKeyword(@RequestParam String keyword, @RequestParam Locale targetLanguage,
                                                        @RequestParam Locale translationLanguage) {
        return vocabularyManager.getSeveralVocabularyByKeyword(keyword, targetLanguage, translationLanguage);
    }
}
