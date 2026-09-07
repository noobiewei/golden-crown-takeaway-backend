package com.goldencrown.takeaway_backend.assistant;

import com.anthropic.client.AnthropicClient;
import com.anthropic.models.messages.ContentBlock;
import com.anthropic.models.messages.Message;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.Model;
import org.springframework.stereotype.Service;

@Service
public class TranslationService {

    private final AnthropicClient anthropicClient;

    public TranslationService(AnthropicClient anthropicClient) {
        this.anthropicClient = anthropicClient;
    }

    // Used for one-off customer notes and special instructions, which — unlike
    // dish names — aren't a fixed catalog and can't be hand-translated ahead
    // of time. Returns null on any failure so a receipt can still print in
    // English rather than fail outright.
    public String translateToChinese(String englishText) {
        if (englishText == null || englishText.isBlank()) {
            return null;
        }

        try {
            MessageCreateParams params = MessageCreateParams.builder()
                    .model(Model.CLAUDE_HAIKU_4_5)
                    .maxTokens(200)
                    .system("""
                            Translate the following English food-order note into natural, concise \
                            Simplified Chinese for a takeaway kitchen ticket. Respond with ONLY the \
                            translation — no explanation, no quotes, no pinyin.
                            """)
                    .addUserMessage(englishText)
                    .build();

            Message message = anthropicClient.messages().create(params);

            return message.content().stream()
                    .filter(ContentBlock::isText)
                    .findFirst()
                    .map(block -> block.asText().text().trim())
                    .orElse(null);
        } catch (Exception e) {
            System.out.println("WARNING: translation failed for \"" + englishText + "\": " + e.getMessage());
            return null;
        }
    }
}
