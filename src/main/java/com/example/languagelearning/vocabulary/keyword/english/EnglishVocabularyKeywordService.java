package com.example.languagelearning.vocabulary.keyword.english;

import com.example.languagelearning.error.ApplicationException;
import com.example.languagelearning.openai.OpenAiService;
import com.example.languagelearning.vocabulary.keyword.common.VocabularyKeywordService;
import com.example.languagelearning.vocabulary.keyword.common.dto.Subtopic1NestingLevelBlockContainer;
import com.example.languagelearning.vocabulary.by_text.common.dto.VocabularyByTextRequestDto;
import com.example.languagelearning.vocabulary.keyword.common.dto.VocabularyTopicComparator;
import com.example.languagelearning.vocabulary.keyword.common.dto.VocabularyTopicDto;
import com.example.languagelearning.vocabulary.keyword.common.prompt.VocabularyByTextPromptParameters;
import com.example.languagelearning.vocabulary.keyword.common.prompt.VocabularyKeywordPromptParameters;
import com.example.languagelearning.vocabulary.keyword.common.prompt.VocabularySubtopic1LevelPromptProcessor;
import com.example.languagelearning.vocabulary.keyword.english.dto.EnglishVocabularyTopic;
import com.example.languagelearning.vocabulary.keyword.english.dto.EnglishVocabularyTopicDto;
import com.example.languagelearning.vocabulary.keyword.english.dto.container.*;
import com.example.languagelearning.vocabulary.keyword.english.entity.EnglishVocabularyTopicEntity;
import com.example.languagelearning.vocabulary.keyword.english.prompt.EnglishVocabularyByTextPromptProcessor;
import com.example.languagelearning.vocabulary.keyword.english.prompt.EnglishVocabularyPromptProcessor;
import com.example.languagelearning.vocabulary.keyword.english.repo.EnglishVocabularyTopicEntityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.example.languagelearning.common.LanguageUtil.normalizeLocale;
import static com.example.languagelearning.util.CompletableFutureUtil.extractValuesFromCompletableFutures;
import static com.example.languagelearning.util.CompletableFutureUtil.tryToExtractSingleCompletedFutureElement;
import static com.example.languagelearning.util.TextUtil.breakTextIntoSentencesParts;
import static com.example.languagelearning.util.TextUtil.breakTextIntoSortedParagraphs;
import static com.example.languagelearning.vocabulary.keyword.common.util.VocabularyKeywordUtil.getSpeechPartJson;
import static com.example.languagelearning.vocabulary.keyword.english.EnglishVocabularyTopicPostProcessor.performCleanup;

@Service
public class EnglishVocabularyKeywordService extends EnglishLanguage implements VocabularyKeywordService {

    private final ObjectMapper objectMapper;
    private final EnglishVocabularyTopicEntityService vocabularyTopicEntityService;

    private final EnglishVocabularyMapper vocabularyMapper;
    private final VocabularySubtopic1LevelPromptProcessor subtopic1LevelPromptProcessor;

    public EnglishVocabularyKeywordService(ObjectMapper objectMapper, EnglishVocabularyTopicEntityService vocabularyTopicEntityService, EnglishVocabularyMapper vocabularyMapper, VocabularySubtopic1LevelPromptProcessor subtopic1LevelPromptProcessor) {
        this.objectMapper = objectMapper;
        this.vocabularyTopicEntityService = vocabularyTopicEntityService;
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
