package com.example.languagelearning.vocabulary.keyword.german.dto;

import com.example.languagelearning.vocabulary.keyword.common.dto.VocabularyTopic;

import java.util.List;

public class GermanVocabularyTopic extends VocabularyTopic {

    private String vocabularyName;
    private List<GermanVerb> verbs;
    private List<GermanNoun> nouns;
    private List<GermanAdjective> adjectives;
    private List<GermanCollocation> collocations;
    private List<GermanIdiom> idioms;
    private List<GermanPrepositionalVerb> prepositionalVerbs;

    public GermanVocabularyTopic() {
    }

    public GermanVocabularyTopic(String vocabularyName, List<GermanVerb> verbs, List<GermanNoun> nouns,
                                 List<GermanAdjective> adjectives, List<GermanCollocation> collocations,
                                 List<GermanIdiom> idioms, List<GermanPrepositionalVerb> prepositionalVerbs) {
        this.vocabularyName = vocabularyName;
        this.verbs = verbs;
        this.nouns = nouns;
        this.adjectives = adjectives;
        this.collocations = collocations;
        this.idioms = idioms;
        this.prepositionalVerbs = prepositionalVerbs;
    }

    public String getVocabularyName() {
        return vocabularyName;
    }

    public void setVocabularyName(String vocabularyName) {
        this.vocabularyName = vocabularyName;
    }

    public List<GermanVerb> getVerbs() {
        return verbs;
    }

    public void setVerbs(List<GermanVerb> verbs) {
        this.verbs = verbs;
    }

    public List<GermanNoun> getNouns() {
        return nouns;
    }

    public void setNouns(List<GermanNoun> nouns) {
        this.nouns = nouns;
    }

    public List<GermanAdjective> getAdjectives() {
        return adjectives;
    }

    public void setAdjectives(List<GermanAdjective> adjectives) {
        this.adjectives = adjectives;
    }

    public List<GermanCollocation> getCollocations() {
        return collocations;
    }

    public void setCollocations(List<GermanCollocation> collocations) {
        this.collocations = collocations;
    }

    public List<GermanIdiom> getIdioms() {
        return idioms;
    }

    public void setIdioms(List<GermanIdiom> idioms) {
        this.idioms = idioms;
    }

    public List<GermanPrepositionalVerb> getPrepositionalVerbs() {
        return prepositionalVerbs;
    }

    public void setPrepositionalVerbs(List<GermanPrepositionalVerb> prepositionalVerbs) {
        this.prepositionalVerbs = prepositionalVerbs;
    }
}
