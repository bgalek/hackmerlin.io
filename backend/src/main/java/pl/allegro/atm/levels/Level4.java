package pl.allegro.atm.levels;

import org.springframework.stereotype.Component;

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
    public String getSecret() {
        return "PUFFIN";
    }

    @Override
    public boolean inputFilter(String input) {
        String inputNormalized = input.toLowerCase();
        return inputNormalized.contains("password") || inputNormalized.contains("secret") || inputNormalized.contains("word") || inputNormalized.contains("magic") || inputNormalized.contains("pass");
    }
}
