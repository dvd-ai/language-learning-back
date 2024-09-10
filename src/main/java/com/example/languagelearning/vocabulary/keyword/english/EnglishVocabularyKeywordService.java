package com.example.languagelearning.vocabulary.keyword.english;

import com.example.languagelearning.common.EnglishLanguage;
import com.example.languagelearning.openai.OpenAiService;
import com.example.languagelearning.vocabulary.common.VocabularyTopicDto;
import com.example.languagelearning.vocabulary.common.english.EnglishVocabularyMapper;
import com.example.languagelearning.vocabulary.keyword.common.VocabularyKeywordService;
import com.example.languagelearning.vocabulary.keyword.common.dto.Subtopic1NestingLevelBlockContainer;
import com.example.languagelearning.vocabulary.keyword.common.prompt.VocabularyKeywordPromptParameters;
import com.example.languagelearning.vocabulary.keyword.common.prompt.VocabularySubtopic1LevelPromptProcessor;
import com.example.languagelearning.vocabulary.keyword.english.dto.EnglishVocabularyTopic;
import com.example.languagelearning.vocabulary.keyword.english.dto.EnglishVocabularyTopicDto;
import com.example.languagelearning.vocabulary.keyword.english.entity.EnglishVocabularyTopicEntity;
import com.example.languagelearning.vocabulary.keyword.english.repo.EnglishVocabularyTopicEntityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.example.languagelearning.util.CompletableFutureUtil.extractValuesFromCompletableFutures;
import static com.example.languagelearning.vocabulary.common.english.EnglishVocabularyTopicPostProcessor.performCleanup;


@Service
public class EnglishVocabularyKeywordService extends EnglishLanguage implements VocabularyKeywordService {

    private final ObjectMapper objectMapper;
    private final EnglishVocabularyTopicEntityService vocabularyTopicEntityService;
    private final EnglishVocabularyKeywordCfTopicUtil cfUtil;

    private final EnglishVocabularyMapper vocabularyMapper;
    private final VocabularySubtopic1LevelPromptProcessor subtopic1LevelPromptProcessor;

    public EnglishVocabularyKeywordService(ObjectMapper objectMapper, EnglishVocabularyTopicEntityService vocabularyTopicEntityService, EnglishVocabularyKeywordCfTopicUtil cfUtil, EnglishVocabularyMapper vocabularyMapper, VocabularySubtopic1LevelPromptProcessor subtopic1LevelPromptProcessor) {
        this.objectMapper = objectMapper;
        this.vocabularyTopicEntityService = vocabularyTopicEntityService;
        this.cfUtil = cfUtil;
        this.vocabularyMapper = vocabularyMapper;
        this.subtopic1LevelPromptProcessor = subtopic1LevelPromptProcessor;
    }

    @Override
    public List<? extends VocabularyTopicDto> processByKeyword(String keyword, OpenAiService openAiService, String translationLanguage) throws JsonProcessingException {
        List<EnglishVocabularyTopicDto> vocabularyTopics = getExistingVocabularyTopics(keyword, translationLanguage);

        if (!vocabularyTopics.isEmpty()) {
            return vocabularyTopics;
        }

        var subtopicBlockEntries = getSubtopic1NestingLevelBlockContainer(keyword, getLanguage(), openAiService);
        List<CompletableFuture<EnglishVocabularyTopic>> topicsCompletableFutures = new ArrayList<>();

        for (var subtopicBlock : subtopicBlockEntries.entries()) {
            for (var level1topicName : subtopicBlock.getSubtopic1LevelNames()) {
                var promptParameters = new VocabularyKeywordPromptParameters(keyword, subtopicBlock.getSubtopic0LevelName(), level1topicName, translationLanguage);
                topicsCompletableFutures.add(cfUtil.getCompleteTopicCompletableFuture(openAiService, promptParameters));
            }
        }

        List<EnglishVocabularyTopic> englishVocabularyTopics = extractValuesFromCompletableFutures(topicsCompletableFutures);
        performCleanup(englishVocabularyTopics);
        List<EnglishVocabularyTopicEntity> entitiesToSave = vocabularyMapper.mapToEntities(englishVocabularyTopics, translationLanguage);
        List<EnglishVocabularyTopicEntity> resultEntities = vocabularyTopicEntityService.addTopicEntities(entitiesToSave);
        return vocabularyMapper.mapToDtos(resultEntities);
    }

    @Override
    public void updateVocabularyTopic(VocabularyTopicDto vocabularyTopicDto) {
        var englishVocabularyTopicDto = (EnglishVocabularyTopicDto) vocabularyTopicDto;
        var englishVocabularyTopicEntity = vocabularyMapper.mapToEntity(englishVocabularyTopicDto);
        vocabularyTopicEntityService.updateVocabularyTopicEntity(englishVocabularyTopicEntity);
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

    private List<EnglishVocabularyTopicDto> getExistingVocabularyTopics(String keyword, String translationLanguage) {
        List<EnglishVocabularyTopicEntity> entityTopics = vocabularyTopicEntityService.findTopicsByKeywordAndTranslationLanguage(keyword, translationLanguage);
        return vocabularyMapper.mapToDtos(entityTopics);
    }
}
