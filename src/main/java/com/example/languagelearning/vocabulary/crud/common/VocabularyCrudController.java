package com.example.languagelearning.vocabulary.crud.common;

import com.example.languagelearning.vocabulary.common.VocabularyTopicDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Locale;


@RequestMapping("/vocabulary")
public interface VocabularyCrudController {

    @GetMapping
    VocabularyTopicDto getVocabularyTopic(@RequestParam Long id, @RequestParam Locale targetLanguage);


    @GetMapping("/all")
    Page<VocabularyTopicDto>getAllVocabularyTopics(@RequestParam(required = false) String search, @RequestParam Locale targetLanguage,
                                                   Pageable pageable);
}
