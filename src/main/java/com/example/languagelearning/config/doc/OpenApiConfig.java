package com.example.languagelearning.config.doc;

import org.springframework.core.io.ClassPathResource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.examples.Example;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() throws IOException {
        return new OpenAPI()
                .components(new Components()
                        .addExamples("EnglishVocabularyTopicExample", new Example().value(readFile("openapi/components/examples/EnglishVocabularyTopic.json")))
                        .addExamples("GermanVocabularyTopicExample", new Example().value(readFile("openapi/components/examples/GermanVocabularyTopic.json"))))
                .info(new Info().title("Learning Language API Documentation").version("v1"));
    }

    private String readFile(String path) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(path);
        byte[] binaryData = Files.readAllBytes(classPathResource.getFile().toPath());
        return new String(binaryData, StandardCharsets.UTF_8);
    }
}
