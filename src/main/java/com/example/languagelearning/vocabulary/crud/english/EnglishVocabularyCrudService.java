package com.example.languagelearning.vocabulary.crud.english;

import com.example.languagelearning.common.EnglishLanguage;
import com.example.languagelearning.error.ApplicationException;
import com.example.languagelearning.vocabulary.common.VocabularyTopicDto;
import com.example.languagelearning.vocabulary.common.english.EnglishVocabularyMapper;
import com.example.languagelearning.vocabulary.crud.common.VocabularyCrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EnglishVocabularyCrudService extends EnglishLanguage implements VocabularyCrudService {

    private final EnglishVocabularyCrudRepo englishVocabularyCrudRepo;
    private final EnglishVocabularyMapper englishVocabularyMapper;

    public EnglishVocabularyCrudService(EnglishVocabularyCrudRepo englishVocabularyCrudRepo, EnglishVocabularyMapper englishVocabularyMapper) {
        this.englishVocabularyCrudRepo = englishVocabularyCrudRepo;
        this.englishVocabularyMapper = englishVocabularyMapper;
    }

    @Override
    public VocabularyTopicDto getVocabularyTopic(Long id, String targetLanguage) {
        return englishVocabularyCrudRepo.findById(id)
                .map(englishVocabularyMapper::mapToDto)
                .orElseThrow(() -> new ApplicationException("Vocabulary topic with id '" + id + "' not found for target language: '" + targetLanguage + "'"));
    }

    @Override
    public Page<VocabularyTopicDto> getAllVocabularyTopics(Pageable pageable) {
        return englishVocabularyCrudRepo.findAll(pageable)
                .map(englishVocabularyMapper::mapToDto);
    }

    @Override
    public Page<VocabularyTopicDto> getVocabularyTopicsBySearch(String search, Pageable pageable) {
        return englishVocabularyCrudRepo.findVocabularyTopicsBySearch(search, pageable)
                .map(englishVocabularyMapper::mapToDto);
    }
}
