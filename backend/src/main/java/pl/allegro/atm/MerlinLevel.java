package pl.allegro.atm;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;

public interface MerlinLevel {

    int getOrder();

    String getSecret();

    ChatCompletionRequest prompt(String prompt);

    default boolean outputFilter(String output) {
        return true;
    }

    default boolean inputFilter(String input) {
        return true;
    }

    default String inputFilterResponse() {
        return "Input was blocked.";
    }

    default String outputFilterResponse() {
        return "Sorry I can't tell you that.";
    }

}
