package com.example.languagelearning.vocabulary.crud.common;

import com.example.languagelearning.vocabulary.common.VocabularyTopicDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

import static com.example.languagelearning.common.LanguageUtil.normalizeLocale;

@RestController
public class VocabularyCrudControllerImpl implements VocabularyCrudController{

    private final VocabularyCrudManager vocabularyCrudManager;

    public VocabularyCrudControllerImpl(VocabularyCrudManager vocabularyCrudManager) {
        this.vocabularyCrudManager = vocabularyCrudManager;
    }

    @Override
    public VocabularyTopicDto getVocabularyTopic(@RequestParam Long id, @RequestParam Locale targetLanguage) {
        //validate input
        return vocabularyCrudManager.getVocabularyTopic(id, normalizeLocale(targetLanguage));
    }

    @Override
    public Page<VocabularyTopicDto> getAllVocabularyTopics(@RequestParam(required = false) String search, @RequestParam Locale targetLanguage, Pageable pageable) {
        //validate input
        String normalizedTargetLanguage = normalizeLocale(targetLanguage);

        if (search != null && !search.isEmpty())
            return vocabularyCrudManager.getVocabularyTopicsBySearch(normalizedTargetLanguage, search, pageable);
        else return vocabularyCrudManager.getAllVocabularyTopics(normalizedTargetLanguage, pageable);
    }
}
