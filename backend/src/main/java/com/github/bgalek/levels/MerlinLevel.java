package com.github.bgalek.levels;

import com.azure.ai.openai.models.ChatCompletionsOptions;

public interface MerlinLevel {

    int getOrder();

    String getSecret();

    ChatCompletionsOptions prompt(String prompt);

    default boolean outputFilter(String output) {
        return false;
    }

    default boolean inputFilter(String input) {
        return false;
    }

    default String inputFilterResponse() {
        return "I have detected a manipulation attempt. Your question was blocked.";
    }

    default String outputFilterResponse() {
        return "Sorry I can't tell you that.";
    }

    default String getModel() {
        return "hackmerlin-gpt35";
    }

    String getLevelFinishedResponse();
}
