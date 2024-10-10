package com.example.languagelearning.vocabulary.crud.german;

import com.example.languagelearning.common.GermanLanguage;
import com.example.languagelearning.error.ApplicationException;
import com.example.languagelearning.vocabulary.common.VocabularyTopicDto;
import com.example.languagelearning.vocabulary.common.german.GermanVocabularyMapper;
import com.example.languagelearning.vocabulary.crud.common.VocabularyCrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GermanVocabularyCrudService extends GermanLanguage implements VocabularyCrudService {

    private final GermanVocabularyCrudRepo germanVocabularyCrudRepo;
    private final GermanVocabularyMapper germanVocabularyMapper;


    public GermanVocabularyCrudService(GermanVocabularyCrudRepo germanVocabularyCrudRepo, GermanVocabularyMapper germanVocabularyMapper) {
        this.germanVocabularyCrudRepo = germanVocabularyCrudRepo;
        this.germanVocabularyMapper = germanVocabularyMapper;
    }

    @Override
    public VocabularyTopicDto getVocabularyTopic(Long id, String targetLanguage) {
        return germanVocabularyCrudRepo.findById(id)
                .map(germanVocabularyMapper::mapToDto)
                .orElseThrow(() -> new ApplicationException("Vocabulary topic with id '" + id + "' not found for target language: '" + targetLanguage + "'"));
    }

    @Override
    public Page<VocabularyTopicDto> getAllVocabularyTopics(Pageable pageable) {
        return germanVocabularyCrudRepo.findAll(pageable)
                .map(germanVocabularyMapper::mapToDto);
    }

    @Override
    public Page<VocabularyTopicDto> getVocabularyTopicsBySearch(String search, Pageable pageable) {
        return germanVocabularyCrudRepo.findVocabularyTopicsBySearch(search, pageable)
                .map(germanVocabularyMapper::mapToDto);
    }
}
