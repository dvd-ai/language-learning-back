package com.example.languagelearning.vocabulary.crud.common;

import com.example.languagelearning.common.Language;
import com.example.languagelearning.vocabulary.common.VocabularyTopicDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VocabularyCrudService extends Language {
    VocabularyTopicDto getVocabularyTopic(Long id, String targetLanguage);

    Page<VocabularyTopicDto> getAllVocabularyTopics(Pageable pageable);
    Page<VocabularyTopicDto> getVocabularyTopicsBySearch(String search, Pageable pageable);
}
