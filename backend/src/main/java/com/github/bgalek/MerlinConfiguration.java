package com.github.bgalek;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.github.bgalek.levels.MerlinLevel;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.URI;
import java.util.List;
import java.util.Set;

import static com.github.bgalek.MerlinConfiguration.MerlinAzureConfigurationProperties;
import static com.github.bgalek.MerlinConfiguration.MerlinPasswords;

@Configuration
@EnableConfigurationProperties({MerlinAzureConfigurationProperties.class, MerlinPasswords.class})
class MerlinConfiguration {

    @Bean
    OpenAIClient openAiClient(MerlinAzureConfigurationProperties merlinAzureConfigurationProperties) {
        return new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(merlinAzureConfigurationProperties.key))
                .endpoint(merlinAzureConfigurationProperties.url.toString())
                .buildClient();
    }

    @Bean
    MerlinService merlinService(List<MerlinLevel> levels, MerlinPasswords merlinPasswords, OpenAIClient openAiClient) {
        MerlinLevelRepository merlinLevelRepository = new MerlinLevelRepository(levels);
        return new MerlinService(openAiClient, merlinLevelRepository, merlinPasswords.passwords);
    }

    @Bean
    @Profile("default")
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry corsRegistry) {
                corsRegistry.addMapping("/api").allowedOrigins("http://localhost:3000");
            }
        };
    }

    @ConfigurationProperties(prefix = "merlin.azure")
    record MerlinAzureConfigurationProperties(String key, URI url) {
    }

    @ConfigurationProperties(prefix = "merlin.passwords")
    record MerlinPasswords(List<String> passwords) {
    }
}
