package com.example.languagelearning.common;

import java.util.Locale;

public class LanguageUtil {
    public static boolean equalLanguages(Locale language1, Locale language2) {
        return language1.getDisplayLanguage(Locale.ENGLISH)
                .equalsIgnoreCase(language2.getDisplayLanguage(Locale.ENGLISH));
    }
}
