package com.example.languagelearning.vocabulary.by_text.common.controller;

import com.example.languagelearning.vocabulary.by_text.common.dto.VocabularyByTextRequestDto;
import com.example.languagelearning.vocabulary.common.VocabularyTopicDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("vocabulary/by-text")
public interface VocabularyByTextController {

    @PostMapping
    List<? extends VocabularyTopicDto> getVocabularyByText(@RequestBody VocabularyByTextRequestDto requestDto);
}
