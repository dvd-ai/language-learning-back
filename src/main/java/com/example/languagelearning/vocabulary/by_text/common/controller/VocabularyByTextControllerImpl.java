package com.example.languagelearning.vocabulary.by_text.common.controller;

import com.example.languagelearning.vocabulary.by_text.common.VocabularyByTextManager;
import com.example.languagelearning.vocabulary.by_text.common.dto.VocabularyByTextRequestDto;
import com.example.languagelearning.vocabulary.keyword.common.dto.VocabularyTopicDto;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VocabularyByTextControllerImpl implements VocabularyByTextController{

    private final VocabularyByTextManager vocabularyManager;

    public VocabularyByTextControllerImpl(VocabularyByTextManager vocabularyManager) {
        this.vocabularyManager = vocabularyManager;
    }

    @Override
    public List<? extends VocabularyTopicDto> getVocabularyByText(VocabularyByTextRequestDto requestDto) {
        //validation
        return vocabularyManager.getVocabularyByText(requestDto);
    }
}
