package com.example.languagelearning.vocabulary.keyword.german;

import com.example.languagelearning.common.GermanLanguage;
import com.example.languagelearning.openai.OpenAiService;
import com.example.languagelearning.vocabulary.common.VocabularyTopicDto;
import com.example.languagelearning.vocabulary.common.german.GermanVocabularyMapper;
import com.example.languagelearning.vocabulary.keyword.common.VocabularyKeywordService;
import com.example.languagelearning.vocabulary.keyword.common.dto.Subtopic1NestingLevelBlockContainer;
import com.example.languagelearning.vocabulary.keyword.common.prompt.VocabularyKeywordPromptParameters;
import com.example.languagelearning.vocabulary.keyword.common.prompt.VocabularySubtopic1LevelPromptProcessor;
import com.example.languagelearning.vocabulary.keyword.german.dto.GermanVocabularyTopic;
import com.example.languagelearning.vocabulary.keyword.german.dto.GermanVocabularyTopicDto;
import com.example.languagelearning.vocabulary.keyword.german.entity.GermanVocabularyTopicEntity;
import com.example.languagelearning.vocabulary.keyword.german.repo.GermanVocabularyTopicEntityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.example.languagelearning.util.CompletableFutureUtil.extractValuesFromCompletableFutures;
import static com.example.languagelearning.vocabulary.common.german.GermanVocabularyTopicPostProcessor.performCleanup;


@Service
public class GermanVocabularyKeywordService extends GermanLanguage implements VocabularyKeywordService {

    private final ObjectMapper objectMapper;
    private final GermanVocabularyTopicEntityService vocabularyTopicEntityService;
    private final GermanVocabularyKeywordCfTopicUtil cfUtil;
    private final VocabularySubtopic1LevelPromptProcessor subtopic1LevelPromptProcessor;
    private final GermanVocabularyMapper vocabularyMapper;

    public GermanVocabularyKeywordService(ObjectMapper objectMapper, GermanVocabularyTopicEntityService vocabularyTopicEntityService, GermanVocabularyKeywordCfTopicUtil cfUtil, VocabularySubtopic1LevelPromptProcessor subtopic1LevelPromptProcessor, GermanVocabularyMapper vocabularyMapper) {
        this.objectMapper = objectMapper;
        this.vocabularyTopicEntityService = vocabularyTopicEntityService;
        this.cfUtil = cfUtil;
        this.subtopic1LevelPromptProcessor = subtopic1LevelPromptProcessor;
        this.vocabularyMapper = vocabularyMapper;
    }

    @Override
    public List<? extends VocabularyTopicDto> processByKeyword(String keyword, OpenAiService openAiService, String translationLanguage) throws JsonProcessingException {
        List<GermanVocabularyTopicDto> vocabularyTopics = getExistingVocabularyTopics(keyword, translationLanguage);
        if (!vocabularyTopics.isEmpty())
            return vocabularyTopics;

        var subtopicBlockEntries = getSubtopic1NestingLevelBlockContainer(keyword, getLanguage(), openAiService);

        List<CompletableFuture<GermanVocabularyTopic>> topicsCompletableFutures = new ArrayList<>();

        for (var subtopicBlock : subtopicBlockEntries.entries()) {
            for (var level1topicName : subtopicBlock.getSubtopic1LevelNames()) {
                var promptParameters = new VocabularyKeywordPromptParameters(keyword, subtopicBlock.getSubtopic0LevelName(), level1topicName, translationLanguage);
                topicsCompletableFutures.add(cfUtil.getCompleteTopicCompletableFuture(openAiService, promptParameters));
            }
        }

        List<GermanVocabularyTopic> germanVocabularyTopics = extractValuesFromCompletableFutures(topicsCompletableFutures);
        performCleanup(germanVocabularyTopics);
        List<GermanVocabularyTopicEntity> entitiesToSave = vocabularyMapper.mapToEntities(germanVocabularyTopics, translationLanguage);
        List<GermanVocabularyTopicEntity> resultEntities = vocabularyTopicEntityService.addTopicEntities(entitiesToSave);
        return vocabularyMapper.mapToDtos(resultEntities);
    }

    @Override
    public void updateVocabularyTopic(VocabularyTopicDto vocabularyTopicDto) {
        var germanVocabularyTopicDto = (GermanVocabularyTopicDto) vocabularyTopicDto;
        var germanVocabularyTopicEntity = vocabularyMapper.mapToEntity(germanVocabularyTopicDto);

        vocabularyTopicEntityService.updateVocabularyTopicEntity(germanVocabularyTopicEntity);
    }

    private Subtopic1NestingLevelBlockContainer getSubtopic1NestingLevelBlockContainer(String keyword, String targetLanguage, OpenAiService openAiService) throws JsonProcessingException {
        return objectMapper.readValue(
                openAiService.customCall(subtopic1LevelPromptProcessor.getSubtopic1LevelNames(keyword, targetLanguage),
                        OpenAiChatOptions.builder()
                                .withModel("gpt-4o")
                                .build()
                ),
                Subtopic1NestingLevelBlockContainer.class
        );
    }

    private List<GermanVocabularyTopicDto> getExistingVocabularyTopics(String keyword, String translationLanguage) {
        List<GermanVocabularyTopicEntity> entityTopics = vocabularyTopicEntityService.findTopicsByKeywordAndTranslationLanguage(keyword, translationLanguage);
        return vocabularyMapper.mapToDtos(entityTopics);
    }
}
