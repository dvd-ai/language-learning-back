package com.example.languagelearning.common;

import java.util.Locale;

public class LanguageUtil {

    private LanguageUtil() {
    }

    public static String normalizeLocale(Locale locale) {
        return locale.getDisplayLanguage(Locale.ENGLISH).toLowerCase();
    }
}
