package com.example.languagelearning.vocabulary.by_text.common;

import com.example.languagelearning.common.Language;
import com.example.languagelearning.openai.OpenAiService;
import com.example.languagelearning.vocabulary.by_text.common.dto.VocabularyByTextRequestDto;
import com.example.languagelearning.vocabulary.keyword.common.dto.VocabularyTopicDto;

import java.util.List;

public interface VocabularyByTextService extends Language {

    List<? extends VocabularyTopicDto> getVocabularyByText(VocabularyByTextRequestDto requestDto, OpenAiService openAiService);
}
