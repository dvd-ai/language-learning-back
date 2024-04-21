package com.example.languagelearning.openai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class OpenAiService {

    private final OpenAiChatClient chatClient;
    Logger logger = LoggerFactory.getLogger(OpenAiService.class);

    public OpenAiService(OpenAiChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Async
    public CompletableFuture<String> defaultAsyncCall(String prompt) {
        String res = chatClient.call(prompt);
        logger.info("Async chat completion output: \n" + res);
        return CompletableFuture.completedFuture(res);
    }

    public String defaultCall(String prompt) {
        String res = chatClient.call(prompt);
        logger.info("Sync chat completion output: \n" + res);
        return res;
    }

    public String customCall(String prompt, ChatOptions openAiChatOptions) {
        String res = chatClient.call(new Prompt(prompt, openAiChatOptions))
                .getResult()
                .getOutput()
                .getContent();
        logger.info("Sync custom chat completion output: \n" + res);
        return res;
    }

}
