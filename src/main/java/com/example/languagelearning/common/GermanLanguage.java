package com.example.languagelearning.common;

import java.util.Locale;

import static com.example.languagelearning.common.LanguageUtil.normalizeLocale;

public class GermanLanguage implements Language {
    @Override
    public String getLanguage() {
        return normalizeLocale(Locale.GERMAN);
    }
}
