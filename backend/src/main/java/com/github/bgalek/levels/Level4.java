package com.github.bgalek.levels;

import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Input filter that does not allow using specific words
 */
@Component
class Level4 extends Level3 {
    @Override
    public int getOrder() {
        return 4;
    }

    @Override
    public boolean inputFilter(String input) {
        String inputNormalized = input.toLowerCase(Locale.ROOT);
        return inputNormalized.contains("password") || inputNormalized.contains("secret") || inputNormalized.contains("word") || inputNormalized.contains("magic") || inputNormalized.contains("pass");
    }

    @Override
    public String getLevelFinishedResponse() {
        return "This time Merlin API had an input filter that would block any prompts containing words like password, secret etc.";
    }
}
