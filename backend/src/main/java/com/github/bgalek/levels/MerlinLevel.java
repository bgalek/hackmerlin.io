package com.github.bgalek.levels;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;

public interface MerlinLevel {

    int getOrder();

    String getSecret();

    ChatCompletionRequest prompt(String prompt);

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

    String getLevelFinishedResponse();
}
