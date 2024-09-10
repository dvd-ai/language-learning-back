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
    public String getLanguage() {
        return normalizeLocale(Locale.ENGLISH);
    }

    @Override
    public List<? extends VocabularyTopicDto> processByKeyword(String keyword, OpenAiService openAiService, String translationLanguage) throws JsonProcessingException {
        List<EnglishVocabularyTopicDto> vocabularyTopics = getExistingVocabularyTopics(keyword, translationLanguage);
        if (!vocabularyTopics.isEmpty()) {
            return vocabularyTopics.stream().sorted(new VocabularyTopicComparator()).toList();
        }


        var subtopicBlockEntries = getSubtopic1NestingLevelBlockContainer(keyword, getLanguage(), openAiService);

        List<CompletableFuture<EnglishVocabularyTopic>> topicsCompletableFutures = new ArrayList<>();

        for (var subtopicBlock : subtopicBlockEntries.entries()) {
            for (var level1topicName : subtopicBlock.getSubtopic1LevelNames()) {
                var promptParameters = new VocabularyKeywordPromptParameters(keyword, subtopicBlock.getSubtopic0LevelName(), level1topicName, translationLanguage);
                topicsCompletableFutures.add(getCompleteTopicCompletableFuture(openAiService, promptParameters));
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

    @Override
    public List<? extends VocabularyTopicDto> getVocabularyByText(VocabularyByTextRequestDto requestDto, OpenAiService openAiService) {
        Map<Integer, String> sortedParagraphs = breakTextIntoSortedParagraphs(requestDto.textContent());
        List<CompletableFuture<EnglishVocabularyTopic>> topicsCompletableFutures = new ArrayList<>();

        for (Map.Entry<Integer, String> mapEntry : sortedParagraphs.entrySet()) {
            VocabularyByTextPromptParameters promptParameters = new VocabularyByTextPromptParameters(requestDto, mapEntry.getValue(), mapEntry.getKey());
            if (mapEntry.getValue().length() < 900) {
                topicsCompletableFutures.add(getCompleteTopicByTextCompletableFuture(openAiService, promptParameters));
            } else {
                topicsCompletableFutures.add(getCollectedCfVocabularyTopic(openAiService, promptParameters));
            }
        }

        List<EnglishVocabularyTopic> vocabularyTopics = new ArrayList<>(extractValuesFromCompletableFutures(topicsCompletableFutures));
        performCleanup(vocabularyTopics);
        List<EnglishVocabularyTopicEntity> entities = vocabularyTopicEntityService.addTopicEntities(vocabularyMapper.mapToEntities(vocabularyTopics, requestDto.translationLanguage()));
        return vocabularyMapper.mapToDtos(entities);
    }

    @Async
    private CompletableFuture<EnglishVocabularyTopic> getCollectedCfVocabularyTopic(OpenAiService openAiService, VocabularyByTextPromptParameters promptParameters) {

        List<String> sentencesParts = breakTextIntoSentencesParts(promptParameters.text());
        List<CompletableFuture<EnglishVocabularyTopic>> topicPartsCompletableFutures = new ArrayList<>();

        for (String sentencesPart : sentencesParts) {
            topicPartsCompletableFutures.add(
                    getCompleteTopicByTextCompletableFuture(
                            openAiService,
                            new VocabularyByTextPromptParameters(
                                    promptParameters.requestDto(), sentencesPart, promptParameters.textNumber()
                            )
                    )
            );
        }

        List<EnglishVocabularyTopic> vocabularyTopicParts = extractValuesFromCompletableFutures(topicPartsCompletableFutures);
        var resultTopic = getAccumulatedVocabularyTopic(vocabularyTopicParts, promptParameters);
        return CompletableFuture.completedFuture(resultTopic);
    }

    private EnglishVocabularyTopic getAccumulatedVocabularyTopic(List<EnglishVocabularyTopic> vocabularyTopicParts, VocabularyByTextPromptParameters promptParameters) {
        EnglishVocabularyTopic resultTopic = new EnglishVocabularyTopic(
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                promptParameters.requestDto().textTopicLabel()
                        .concat(".P")
                        .concat(promptParameters.textNumber().toString())
        );

        for (EnglishVocabularyTopic vt : vocabularyTopicParts) {
            resultTopic.getVerbs().addAll(vt.getVerbs());
            resultTopic.getNouns().addAll(vt.getNouns());
            resultTopic.getAdjectives().addAll(vt.getAdjectives());
            resultTopic.getCollocations().addAll(vt.getCollocations());
            resultTopic.getIdioms().addAll(vt.getIdioms());
            resultTopic.getPrepositionalVerbs().addAll(vt.getPrepositionalVerbs());
            resultTopic.getPhrasalVerbs().addAll(vt.getPhrasalVerbs());
        }

        return resultTopic;
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
