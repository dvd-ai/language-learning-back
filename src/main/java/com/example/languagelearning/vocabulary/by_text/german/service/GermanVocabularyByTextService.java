package com.example.languagelearning.vocabulary.by_text.german.service;

import com.example.languagelearning.common.GermanLanguage;
import com.example.languagelearning.openai.OpenAiService;
import com.example.languagelearning.vocabulary.by_text.common.VocabularyByTextPromptParameters;
import com.example.languagelearning.vocabulary.by_text.common.VocabularyByTextService;
import com.example.languagelearning.vocabulary.by_text.common.VocabularyTopicByTextCollectorUtil;
import com.example.languagelearning.vocabulary.by_text.common.dto.VocabularyByTextRequestDto;
import com.example.languagelearning.vocabulary.common.VocabularyTopicDto;
import com.example.languagelearning.vocabulary.common.german.GermanVocabularyMapper;
import com.example.languagelearning.vocabulary.keyword.german.dto.GermanVocabularyTopic;
import com.example.languagelearning.vocabulary.keyword.german.entity.GermanVocabularyTopicEntity;
import com.example.languagelearning.vocabulary.keyword.german.repo.GermanVocabularyTopicEntityService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.example.languagelearning.util.CompletableFutureUtil.extractValuesFromCompletableFutures;
import static com.example.languagelearning.util.TextUtil.breakTextIntoSortedParagraphs;
import static com.example.languagelearning.vocabulary.by_text.common.UserTextConstants.APPROPRIATE_TEXT_LENGTH_FOR_AI;
import static com.example.languagelearning.vocabulary.common.german.GermanVocabularyTopicPostProcessor.performCleanup;


@Service
public class GermanVocabularyByTextService extends GermanLanguage implements VocabularyByTextService {

    private final GermanVocabularyMapper vocabularyMapper;
    private final GermanVocabularyByTextServiceCfTopicUtil germanCfUtil;
    private final GermanVocabularyTopicEntityService vocabularyTopicEntityService;
    private final VocabularyTopicByTextCollectorUtil collectorUtil;

    public GermanVocabularyByTextService(GermanVocabularyMapper vocabularyMapper, GermanVocabularyByTextServiceCfTopicUtil germanCfUtil,
                                         GermanVocabularyTopicEntityService vocabularyTopicEntityService, VocabularyTopicByTextCollectorUtil collectorUtil) {
        this.vocabularyMapper = vocabularyMapper;
        this.germanCfUtil = germanCfUtil;
        this.vocabularyTopicEntityService = vocabularyTopicEntityService;
        this.collectorUtil = collectorUtil;
    }

    @Override
    public List<? extends VocabularyTopicDto> getVocabularyByText(VocabularyByTextRequestDto requestDto, OpenAiService openAiService) {
        Map<Integer, String> sortedParagraphs = breakTextIntoSortedParagraphs(requestDto.textContent());
        List<CompletableFuture<GermanVocabularyTopic>> topicsCompletableFutures = new ArrayList<>();

        for (Map.Entry<Integer, String> mapEntry : sortedParagraphs.entrySet()) {
            VocabularyByTextPromptParameters promptParameters = new VocabularyByTextPromptParameters(requestDto, mapEntry.getValue(), mapEntry.getKey());
            if (mapEntry.getValue().length() < APPROPRIATE_TEXT_LENGTH_FOR_AI) {
                addCompleteCfVocabularyTopic(openAiService, promptParameters, topicsCompletableFutures);
            } else {
                addCollectedCfVocabularyTopic(openAiService, promptParameters, topicsCompletableFutures);
            }
        }

        List<GermanVocabularyTopic> vocabularyTopics = new ArrayList<>(extractValuesFromCompletableFutures(topicsCompletableFutures));
        performCleanup(vocabularyTopics);
        List<GermanVocabularyTopicEntity> entitiesToSave = vocabularyMapper.mapToEntities(vocabularyTopics, requestDto.translationLanguage());
        List<GermanVocabularyTopicEntity> resultEntities = vocabularyTopicEntityService.addTopicEntities(entitiesToSave);
        return vocabularyMapper.mapToDtos(resultEntities);
    }

    private void addCollectedCfVocabularyTopic(OpenAiService openAiService, VocabularyByTextPromptParameters promptParameters, List<CompletableFuture<GermanVocabularyTopic>> topicsCompletableFutures) {
        topicsCompletableFutures.add(
                collectorUtil.getCollectedCfVocabularyTopic(
                        openAiService, promptParameters, GermanVocabularyByTextTopicAccumulator::getAccumulatedVocabularyTopic,
                        germanCfUtil::getCompleteTopicByTextCompletableFuture
                )
        );
    }

    private void addCompleteCfVocabularyTopic(OpenAiService openAiService, VocabularyByTextPromptParameters promptParameters, List<CompletableFuture<GermanVocabularyTopic>> topicsCompletableFutures) {
        topicsCompletableFutures.add(
                germanCfUtil.getCompleteTopicByTextCompletableFuture(openAiService, promptParameters)
        );
    }
}
