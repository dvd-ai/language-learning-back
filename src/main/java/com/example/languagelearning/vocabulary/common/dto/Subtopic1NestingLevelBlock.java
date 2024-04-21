package com.example.languagelearning.vocabulary.common.dto;

import java.util.List;

public class Subtopic1NestingLevelBlock {
    private String subtopic0LevelName;
    private List<String> subtopic1LevelNames;


    public Subtopic1NestingLevelBlock(String subtopic0LevelName, List<String> subtopic1LevelNames) {
        this.subtopic0LevelName = subtopic0LevelName;
        this.subtopic1LevelNames = subtopic1LevelNames;
    }

    public String getSubtopic0LevelName() {
        return subtopic0LevelName;
    }

    public void setSubtopic0LevelName(String subtopic0LevelName) {
        this.subtopic0LevelName = subtopic0LevelName;
    }

    public List<String> getSubtopic1LevelNames() {
        return subtopic1LevelNames;
    }

    public void setSubtopic1LevelNames(List<String> subtopic1LevelNames) {
        this.subtopic1LevelNames = subtopic1LevelNames;
    }
}
