package com.example.languagelearning.vocabulary.keyword.common.controller;

import com.example.languagelearning.error.ClientException;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

import static com.example.languagelearning.openai.gpt.GptSupportedLanguages.isLanguageSupported;

@Component
public class VocabularyKeywordValidator {
    public void validate(Locale translationLanguage) {
        checkTranslationLanguage(translationLanguage);
    }


    private void checkTranslationLanguage(Locale translationLanguage) {
        if (!isLanguageSupported(translationLanguage)) {
            throw new ClientException(String.format("the translationLanguage %s is not supported by GPT", translationLanguage));
        }
    }
}
