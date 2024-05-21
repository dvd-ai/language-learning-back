package com.example.languagelearning.vocabulary.keyword.common.controller;

import com.example.languagelearning.vocabulary.keyword.common.VocabularyKeywordManager;
import com.example.languagelearning.vocabulary.keyword.common.dto.VocabularyTopic;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

@Profile("dev-front")
@RestController
public class VocabularyKeywordControllerDevFront implements VocabularyKeywordController{

    private final VocabularyKeywordManager vocabularyManager;

    public VocabularyKeywordControllerDevFront(VocabularyKeywordManager vocabularyManager) {

        this.vocabularyManager = vocabularyManager;
    }

    @Override
    public List<? extends VocabularyTopic> getVocabularyByKeyword(@RequestParam String keyword, @RequestParam Locale targetLanguage,
                                                                  @RequestParam Locale translationLanguage) {
        return vocabularyManager.getSeveralVocabularyByKeyword(keyword, targetLanguage, translationLanguage);
    }
}
