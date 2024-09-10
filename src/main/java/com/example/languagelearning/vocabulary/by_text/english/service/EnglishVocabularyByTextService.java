package com.example.languagelearning.vocabulary.by_text.english.service;

import com.example.languagelearning.common.EnglishLanguage;
import com.example.languagelearning.openai.OpenAiService;
import com.example.languagelearning.vocabulary.by_text.common.VocabularyByTextService;
import com.example.languagelearning.vocabulary.by_text.common.VocabularyTopicByTextCollectorUtil;
import com.example.languagelearning.vocabulary.by_text.common.dto.VocabularyByTextRequestDto;

import com.example.languagelearning.vocabulary.common.VocabularyTopicDto;
import com.example.languagelearning.vocabulary.by_text.common.VocabularyByTextPromptParameters;
import com.example.languagelearning.vocabulary.common.english.EnglishVocabularyMapper;
import com.example.languagelearning.vocabulary.keyword.english.dto.EnglishVocabularyTopic;
import com.example.languagelearning.vocabulary.keyword.english.entity.EnglishVocabularyTopicEntity;
import com.example.languagelearning.vocabulary.keyword.english.repo.EnglishVocabularyTopicEntityService;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.example.languagelearning.util.CompletableFutureUtil.extractValuesFromCompletableFutures;
import static com.example.languagelearning.util.TextUtil.breakTextIntoSortedParagraphs;

import static com.example.languagelearning.vocabulary.by_text.common.UserTextConstants.APPROPRIATE_TEXT_LENGTH_FOR_AI;
import static com.example.languagelearning.vocabulary.common.english.EnglishVocabularyTopicPostProcessor.performCleanup;

@Service
public class EnglishVocabularyByTextService extends EnglishLanguage implements VocabularyByTextService {
    
    private final EnglishVocabularyMapper vocabularyMapper;

    private final EnglishVocabularyByTextCfTopicUtil englishCfUtil;
    private final EnglishVocabularyTopicEntityService vocabularyTopicEntityService;
    private final VocabularyTopicByTextCollectorUtil collectorUtil;

    public EnglishVocabularyByTextService(EnglishVocabularyMapper vocabularyMapper, EnglishVocabularyByTextCfTopicUtil englishCfUtil, EnglishVocabularyTopicEntityService vocabularyTopicEntityService, VocabularyTopicByTextCollectorUtil collectorUtil) {
        this.englishCfUtil = englishCfUtil;
        this.vocabularyMapper = vocabularyMapper;
        this.vocabularyTopicEntityService = vocabularyTopicEntityService;
        this.collectorUtil = collectorUtil;
    }

    @Override
    public List<? extends VocabularyTopicDto> getVocabularyByText(VocabularyByTextRequestDto requestDto, OpenAiService openAiService) {
        Map<Integer, String> sortedParagraphs = breakTextIntoSortedParagraphs(requestDto.textContent());
        List<CompletableFuture<EnglishVocabularyTopic>> topicsCompletableFutures = new ArrayList<>();

        for (Map.Entry<Integer, String> mapEntry : sortedParagraphs.entrySet()) {
            VocabularyByTextPromptParameters promptParameters = new VocabularyByTextPromptParameters(requestDto, mapEntry.getValue(), mapEntry.getKey());
            if (mapEntry.getValue().length() < APPROPRIATE_TEXT_LENGTH_FOR_AI) {
                addCompleteCfVocabularyTopic(openAiService, promptParameters, topicsCompletableFutures);
            } else {
                addCollectedCfVocabularyTopic(openAiService, promptParameters, topicsCompletableFutures);
            }
        }

        List<EnglishVocabularyTopic> vocabularyTopics = new ArrayList<>(extractValuesFromCompletableFutures(topicsCompletableFutures));
        performCleanup(vocabularyTopics);
        List<EnglishVocabularyTopicEntity> entitiesToSave = vocabularyMapper.mapToEntities(vocabularyTopics, requestDto.translationLanguage());
        List<EnglishVocabularyTopicEntity> resultEntities = vocabularyTopicEntityService.addTopicEntities(entitiesToSave);
        return vocabularyMapper.mapToDtos(resultEntities);
    }

    private void addCollectedCfVocabularyTopic(OpenAiService openAiService, VocabularyByTextPromptParameters promptParameters, List<CompletableFuture<EnglishVocabularyTopic>> topicsCompletableFutures) {
        topicsCompletableFutures.add(
                collectorUtil.getCollectedCfVocabularyTopic(
                        openAiService, promptParameters, EnglishVocabularyByTextTopicAccumulator::getAccumulatedVocabularyTopic,
                        englishCfUtil::getCompleteTopicByTextCompletableFuture
                )
        );
    }

    private void addCompleteCfVocabularyTopic(OpenAiService openAiService, VocabularyByTextPromptParameters promptParameters, List<CompletableFuture<EnglishVocabularyTopic>> topicsCompletableFutures) {
        topicsCompletableFutures.add(
                englishCfUtil.getCompleteTopicByTextCompletableFuture(openAiService, promptParameters)
        );
    }
    
}
