package com.github.bgalek.levels;

import org.springframework.stereotype.Component;

import java.util.stream.Stream;

/**
 * Output filter that ignores other than lowercase letters
 */
@Component
class Level5 extends Level4 {
    @Override
    public int getOrder() {
        return 5;
    }

    @Override
    public boolean outputFilter(String output, String secret) {
        String normalized = output.toLowerCase().replaceAll("[^a-z]+", "");
        return Stream.of(secret.toLowerCase(), new StringBuffer(secret).reverse().toString().toLowerCase()).anyMatch(normalized::contains);
    }

    @Override
    public String getLevelFinishedResponse() {
        return "This time Merlin API had a bit more complex output filter, that checks reversing string and ignores sentence case.";
    }
}
