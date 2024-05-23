package com.example.languagelearning.vocabulary.keyword.common.controller;

import com.example.languagelearning.vocabulary.keyword.common.VocabularyKeywordManager;
import com.example.languagelearning.vocabulary.keyword.common.dto.VocabularyTopic;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

import static com.example.languagelearning.common.LanguageUtil.normalizeLocale;

@Profile("dev-front")
@RestController
public class VocabularyKeywordControllerDevFront implements VocabularyKeywordController {

    private final VocabularyKeywordManager vocabularyManager;
    private final VocabularyKeywordValidator vocabularyKeywordValidator;

    public VocabularyKeywordControllerDevFront(VocabularyKeywordManager vocabularyManager, VocabularyKeywordValidator vocabularyKeywordValidator) {
        this.vocabularyManager = vocabularyManager;
        this.vocabularyKeywordValidator = vocabularyKeywordValidator;
    }

    @Override
    public List<? extends VocabularyTopic> getVocabularyByKeyword(@RequestParam String keyword, @RequestParam Locale targetLanguage,
                                                                  @RequestParam Locale translationLanguage) {
        vocabularyKeywordValidator.validate(translationLanguage);
        return vocabularyManager.getSeveralVocabularyByKeyword(keyword, normalizeLocale(targetLanguage), normalizeLocale(translationLanguage));
    }
}
