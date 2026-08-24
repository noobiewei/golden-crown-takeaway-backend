package com.goldencrown.takeaway_backend;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnthropicConfig {

    @Bean
    public AnthropicClient anthropicClient() {
        String apiKey = System.getenv("ANTHROPIC_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            System.out.println("WARNING: ANTHROPIC_API_KEY is not set — the menu assistant will fail.");
            apiKey = "missing-key";
        }
        return AnthropicOkHttpClient.builder().apiKey(apiKey).build();
    }
}
