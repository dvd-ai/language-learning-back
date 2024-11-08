package com.example.languagelearning.vocabulary.keyword.english.dto;

import com.example.languagelearning.vocabulary.keyword.common.dto.VocabularyTopic;

import java.util.List;

public class EnglishVocabularyTopic extends VocabularyTopic {

    private String vocabularyName;
    private List<EnglishVerb> verbs;
    private List<EnglishNoun> nouns;
    private List<EnglishAdjective> adjectives;
    private List<EnglishCollocation> collocations;
    private List<EnglishIdiom> idioms;
    private List<EnglishPrepositionalVerb> prepositionalVerbs;
    private List<EnglishPhrasalVerb> phrasalVerbs;


    public EnglishVocabularyTopic(List<EnglishVerb> verbs, List<EnglishNoun> nouns,
                                  List<EnglishAdjective> adjectives, List<EnglishCollocation> collocations,
                                  List<EnglishIdiom> idioms, List<EnglishPrepositionalVerb> prepositionalVerbs,
                                  List<EnglishPhrasalVerb> phrasalVerbs, String vocabularyName) {
        this.verbs = verbs;
        this.nouns = nouns;
        this.adjectives = adjectives;
        this.collocations = collocations;
        this.idioms = idioms;
        this.prepositionalVerbs = prepositionalVerbs;
        this.phrasalVerbs = phrasalVerbs;
        this.vocabularyName = vocabularyName;
    }

    public List<EnglishVerb> getVerbs() {
        return verbs;
    }

    public void setVerbs(List<EnglishVerb> verbs) {
        this.verbs = verbs;
    }

    public List<EnglishNoun> getNouns() {
        return nouns;
    }

    public void setNouns(List<EnglishNoun> nouns) {
        this.nouns = nouns;
    }

    public List<EnglishAdjective> getAdjectives() {
        return adjectives;
    }

    public void setAdjectives(List<EnglishAdjective> adjectives) {
        this.adjectives = adjectives;
    }

    public List<EnglishCollocation> getCollocations() {
        return collocations;
    }

    public void setCollocations(List<EnglishCollocation> collocations) {
        this.collocations = collocations;
    }

    public List<EnglishIdiom> getIdioms() {
        return idioms;
    }

    public void setIdioms(List<EnglishIdiom> idioms) {
        this.idioms = idioms;
    }

    public List<EnglishPrepositionalVerb> getPrepositionalVerbs() {
        return prepositionalVerbs;
    }

    public void setPrepositionalVerbs(List<EnglishPrepositionalVerb> prepositionalVerbs) {
        this.prepositionalVerbs = prepositionalVerbs;
    }

    public List<EnglishPhrasalVerb> getPhrasalVerbs() {
        return phrasalVerbs;
    }

    public void setPhrasalVerbs(List<EnglishPhrasalVerb> phrasalVerbs) {
        this.phrasalVerbs = phrasalVerbs;
    }

    public String getVocabularyName() {
        return vocabularyName;
    }

    public void setVocabularyName(String vocabularyName) {
        this.vocabularyName = vocabularyName;
    }
}
