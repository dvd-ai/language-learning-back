package com.example.languagelearning.vocabulary.common.english;

import com.example.languagelearning.vocabulary.keyword.english.dto.EnglishVocabularyTopic;

import java.util.Iterator;
import java.util.List;

import static com.example.languagelearning.vocabulary.common.WordUtils.removeEmptyWordsPhrases;
import static com.example.languagelearning.vocabulary.keyword.common.util.DuplicationUtil.removeDuplicates;
import static com.example.languagelearning.vocabulary.keyword.common.util.DuplicationUtil.removeDuplicatesBetweenLists;

public class EnglishVocabularyTopicPostProcessor {

    private EnglishVocabularyTopicPostProcessor() {
    }

    public static void performCleanup(List<EnglishVocabularyTopic> vocabularyTopics) {
        vocabularyTopics.forEach(EnglishVocabularyTopicPostProcessor::removeDuplicatedWordsInsideTopic);
        vocabularyTopics.forEach(EnglishVocabularyTopicPostProcessor::removeEmptyWordsPhrasesInsideTopic);
        removeDuplicatesOutsideTheTopic(vocabularyTopics);
        removeEmptyTopics(vocabularyTopics);
    }

    private static void removeEmptyWordsPhrasesInsideTopic(EnglishVocabularyTopic englishVocabularyTopic) {
        removeEmptyWordsPhrases(englishVocabularyTopic.getVerbs());
        removeEmptyWordsPhrases(englishVocabularyTopic.getNouns());
        removeEmptyWordsPhrases(englishVocabularyTopic.getAdjectives());
        removeEmptyWordsPhrases(englishVocabularyTopic.getCollocations());
        removeEmptyWordsPhrases(englishVocabularyTopic.getPrepositionalVerbs());
        removeEmptyWordsPhrases(englishVocabularyTopic.getPhrasalVerbs());
        removeEmptyWordsPhrases(englishVocabularyTopic.getIdioms());
    }

    private static void removeDuplicatesOutsideTheTopic(List<EnglishVocabularyTopic> vocabularyTopics) {
        for (int i = 0; i < vocabularyTopics.size(); i++) {
            EnglishVocabularyTopic topic1 = vocabularyTopics.get(i);
            for (int j = i + 1; j < vocabularyTopics.size(); j++) {
                EnglishVocabularyTopic topic2 = vocabularyTopics.get(j);
                removeDuplicatesBetweenLists(topic1.getVerbs(), topic2.getVerbs());
                removeDuplicatesBetweenLists(topic1.getNouns(), topic2.getNouns());
                removeDuplicatesBetweenLists(topic1.getAdjectives(), topic2.getAdjectives());
                removeDuplicatesBetweenLists(topic1.getCollocations(), topic2.getCollocations());
                removeDuplicatesBetweenLists(topic1.getPrepositionalVerbs(), topic2.getPrepositionalVerbs());
                removeDuplicatesBetweenLists(topic1.getPhrasalVerbs(), topic2.getPhrasalVerbs());
                removeDuplicatesBetweenLists(topic1.getIdioms(), topic2.getIdioms());
            }
        }
    }

    private static void removeDuplicatedWordsInsideTopic(EnglishVocabularyTopic vocabularyTopic) {
        removeDuplicates(vocabularyTopic.getVerbs());
        removeDuplicates(vocabularyTopic.getNouns());
        removeDuplicates(vocabularyTopic.getAdjectives());
        removeDuplicates(vocabularyTopic.getCollocations());
        removeDuplicates(vocabularyTopic.getPrepositionalVerbs());
        removeDuplicates(vocabularyTopic.getPhrasalVerbs());
        removeDuplicates(vocabularyTopic.getIdioms());
    }

    private static boolean isTopicEmpty(EnglishVocabularyTopic vocabularyTopic) {
        return vocabularyTopic.getVerbs().isEmpty() && vocabularyTopic.getNouns().isEmpty()
                && vocabularyTopic.getAdjectives().isEmpty() && vocabularyTopic.getCollocations().isEmpty()
                && vocabularyTopic.getPhrasalVerbs().isEmpty() && vocabularyTopic.getPrepositionalVerbs().isEmpty()
                && vocabularyTopic.getIdioms().isEmpty();
    }

    private static void removeEmptyTopics(List<EnglishVocabularyTopic> vocabularyTopics) {
        Iterator<EnglishVocabularyTopic> iterator = vocabularyTopics.iterator();
        while (iterator.hasNext()) {
            EnglishVocabularyTopic vocabularyTopic = iterator.next();
            if (isTopicEmpty(vocabularyTopic)) {
                iterator.remove();
            }
        }
    }
}
