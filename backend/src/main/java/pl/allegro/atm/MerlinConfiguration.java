package pl.allegro.atm;

import com.theokanning.openai.service.OpenAiService;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.allegro.atm.levels.MerlinLevel;

import java.time.Duration;
import java.util.List;

@Configuration
@EnableConfigurationProperties(MerlinConfiguration.MerlinConfigurationProperties.class)
class MerlinConfiguration {

    @Bean
    OpenAiService openApiService(MerlinConfigurationProperties merlinConfigurationProperties) {
        return new OpenAiService(merlinConfigurationProperties.openAiApiKey, Duration.ofSeconds(25));
    }

    @Bean
    MerlinService merlinService(List<MerlinLevel> levels, OpenAiService openAiService) {
        MerlinLevelRepository merlinLevelRepository = new MerlinLevelRepository(levels);
        return new MerlinService(openAiService, merlinLevelRepository);
    }

    @Bean
    @Profile("default")
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NotNull CorsRegistry corsRegistry) {
                corsRegistry.addMapping("/api").allowedOrigins("http://localhost:3000");
            }
        };
    }

    @ConfigurationProperties(prefix = "merlin")
    record MerlinConfigurationProperties(String openAiApiKey) {
    }
}
