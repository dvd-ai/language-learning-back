package com.example.languagelearning.vocabulary.keyword.common.controller;

import com.example.languagelearning.vocabulary.keyword.common.VocabularyKeywordManager;
import com.example.languagelearning.vocabulary.keyword.common.dto.VocabularyTopicDto;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

import static com.example.languagelearning.common.LanguageUtil.normalizeLocale;

@RestController
public class VocabularyKeywordControllerImpl implements VocabularyKeywordController {

    private final VocabularyKeywordManager vocabularyManager;
    private final VocabularyKeywordValidator vocabularyKeywordValidator;

    public VocabularyKeywordControllerImpl(VocabularyKeywordManager vocabularyManager, VocabularyKeywordValidator vocabularyKeywordValidator) {

        this.vocabularyManager = vocabularyManager;
        this.vocabularyKeywordValidator = vocabularyKeywordValidator;
    }

    @Override
    public List<? extends VocabularyTopicDto> getVocabularyByKeyword(@RequestParam String keyword, @RequestParam Locale targetLanguage,
                                                                     @RequestParam Locale translationLanguage) {
        vocabularyKeywordValidator.validate(translationLanguage);
        return vocabularyManager.getSeveralVocabularyByKeyword(
                keyword,
                normalizeLocale(targetLanguage),
                normalizeLocale(translationLanguage)
        );
    }

    @Override
    @PutMapping
    public void updateTopics(@RequestBody List<? extends VocabularyTopicDto> topics) {
        vocabularyKeywordValidator.checkAllTranslationLanguages(topics);
        vocabularyManager.updateTopics(topics);
    }
}
