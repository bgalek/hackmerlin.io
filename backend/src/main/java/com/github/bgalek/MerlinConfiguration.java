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
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.URI;
import java.util.List;

@Configuration
@EnableConfigurationProperties({MerlinConfiguration.MerlinConfigurationProperties.class})
class MerlinConfiguration {

    @Bean
    OpenAIClient openAiClient(MerlinConfigurationProperties properties) {
        return new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(properties.azure.key))
                .endpoint(properties.azure.url.toString())
                .buildClient();
    }

    @Bean
    MerlinService merlinService(List<MerlinLevel> levels,
                                MerlinConfigurationProperties properties,
                                OpenAIClient openAiClient,
                                JdbcClient jdbcClient
    ) {
        MerlinLevelRepository merlinLevelRepository = new MerlinLevelRepository(levels);
        MerlinLeaderboardRepository merlinLeaderboardRepository = new MerlinLeaderboardRepository(jdbcClient);
        MerlinLogger merlinLogger = new MerlinLogger(jdbcClient);
        return new MerlinService(
                openAiClient,
                merlinLevelRepository,
                merlinLeaderboardRepository,
                merlinLogger,
                properties.passwords
        );
    }

    @Bean
    @Profile("default")
    WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry corsRegistry) {
                corsRegistry.addMapping("/api").allowedOrigins("http://localhost:3000");
            }
        };
    }


    @ConfigurationProperties(prefix = "merlin")
    record MerlinConfigurationProperties(MerlinAzureConfigurationProperties azure, List<String> passwords) {
        record MerlinAzureConfigurationProperties(String key, URI url) {
        }
    }
}
