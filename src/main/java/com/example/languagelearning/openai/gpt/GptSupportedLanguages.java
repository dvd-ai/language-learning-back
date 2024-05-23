package com.example.languagelearning.openai.gpt;

import java.util.List;
import java.util.Locale;

public enum GptSupportedLanguages {

    ALBANIAN("sq"),
    AMHARIC("am"),
    ARABIC("ar"),
    ARMENIAN("hy"),
    BENGALI("bn"),
    BOSNIAN("bs"),
    BULGARIAN("bg"),
    BURMESE("my"),
    CATALAN("ca"),
    CHINESE("zh"),
    CROATIAN("hr"),
    CZECH("cs"),
    DANISH("da"),
    DUTCH("nl"),
    ESTONIAN("et"),
    FINNISH("fi"),
    FRENCH("fr"),
    GEORGIAN("ka"),
    GERMAN("de"),
    GREEK("el"),
    GUJARATI("gu"),
    HINDI("hi"),
    HUNGARIAN("hu"),
    ICELANDIC("is"),
    INDONESIAN("id"),
    ITALIAN("it"),
    JAPANESE("ja"),
    KANNADA("kn"),
    KAZAKH("kk"),
    KOREAN("ko"),
    LATVIAN("lv"),
    LITHUANIAN("lt"),
    MACEDONIAN("mk"),
    MALAY("ms"),
    MALAYALAM("ml"),
    MARATHI("mr"),
    MONGOLIAN("mn"),
    NORWEGIAN("no"),
    PERSIAN("fa"),
    POLISH("pl"),
    PORTUGUESE("pt"),
    PUNJABI("pa"),
    ROMANIAN("ro"),
    RUSSIAN("ru"),
    SERBIAN("sr"),
    SLOVAK("sk"),
    SLOVENIAN("sl"),
    SOMALI("so"),
    SPANISH("es"),
    SWAHILI("sw"),
    SWEDISH("sv"),
    TAGALOG("tl"),

    ENGLISH("en"),
    TAMIL("ta"),
    TELUGU("te"),
    THAI("th"),
    TURKISH("tr"),
    UKRAINIAN("uk"),
    URDU("ur"),
    VIETNAMESE("vi");

    private final String englishLanguageName;

    GptSupportedLanguages(String languageCode) {
        this.englishLanguageName = new Locale(languageCode).getDisplayLanguage(Locale.ENGLISH).toLowerCase();
    }

    public static boolean isLanguageSupported(Locale languageLocale) {
        String language = languageLocale.getDisplayLanguage(Locale.ENGLISH).toLowerCase();
        List<GptSupportedLanguages> gptSupportedLanguages = List.of(GptSupportedLanguages.values());

        return gptSupportedLanguages.stream()
                .map(GptSupportedLanguages::getLanguage)
                .anyMatch(e -> e.equals(language));
    }

    public String getLanguage() {
        return englishLanguageName;
    }
}
