package com.goldencrown.takeaway_backend.assistant;

import com.anthropic.client.AnthropicClient;
import com.anthropic.models.messages.ContentBlock;
import com.anthropic.models.messages.Message;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.Model;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goldencrown.takeaway_backend.menu.MenuItem;
import com.goldencrown.takeaway_backend.menu.MenuItemRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/assistant")
public class MenuAssistantController {

    private static final String FALLBACK_REPLY =
            "Sorry, I couldn't come up with a suggestion just now — feel free to browse the menu below instead.";

    private final AnthropicClient anthropicClient;
    private final MenuItemRepository menuItemRepository;

    // A separate ObjectMapper instance deliberately: this one parses Claude's
    // reply text and must be the classic Jackson 2.x that anthropic-java-core
    // itself uses, not Spring's injected Jackson 3.x bean (incompatible types).
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MenuAssistantController(AnthropicClient anthropicClient, MenuItemRepository menuItemRepository) {
        this.anthropicClient = anthropicClient;
        this.menuItemRepository = menuItemRepository;
    }

    @PostMapping("/recommend")
    public RecommendResponse recommend(@RequestBody RecommendRequest request) {
        List<MenuItem> availableItems = menuItemRepository.findByAvailableTrue();
        Map<Long, MenuItem> itemsById = availableItems.stream()
                .collect(Collectors.toMap(MenuItem::getId, item -> item));

        String menuContext = availableItems.stream()
                .map(item -> "id=%d | %s | £%s | %s%s".formatted(
                        item.getId(), item.getName(), item.getPrice(), item.getDescription(), dietaryTags(item)))
                .collect(Collectors.joining("\n"));

        MessageCreateParams params = MessageCreateParams.builder()
                .model(Model.CLAUDE_HAIKU_4_5)
                .maxTokens(1024)
                .system("""
                        You are the ordering assistant for Golden Crown Takeaway, a Chinese takeaway \
                        in North Watford. A customer will describe what they're in the mood for. \
                        Recommend 2-4 dishes from the menu below that best match — never invent a \
                        dish that isn't listed. If nothing fits well, return an empty list and say so.

                        Menu:
                        %s

                        Respond with ONLY a JSON object in this exact shape, no other text, no markdown:
                        {"reply": "<one short friendly sentence>", "menuItemIds": [<id>, <id>]}
                        """.formatted(menuContext))
                .addUserMessage(request.message())
                .build();

        Message message = anthropicClient.messages().create(params);

        String replyText = message.content().stream()
                .filter(ContentBlock::isText)
                .findFirst()
                .map(block -> block.asText().text())
                .orElse(null);

        if (replyText == null) {
            return new RecommendResponse(FALLBACK_REPLY, List.of());
        }

        try {
            AiRecommendation recommendation = objectMapper.readValue(stripMarkdownFence(replyText), AiRecommendation.class);
            List<MenuItem> recommendedItems = recommendation.menuItemIds().stream()
                    .map(itemsById::get)
                    .filter(Objects::nonNull)
                    .toList();
            return new RecommendResponse(recommendation.reply(), recommendedItems);
        } catch (Exception e) {
            // Claude's reply didn't parse as the expected JSON shape — degrade
            // gracefully rather than 500 the customer-facing feature over it.
            System.out.println("WARNING: could not parse assistant reply as JSON: " + replyText);
            return new RecommendResponse(FALLBACK_REPLY, List.of());
        }
    }

    // Claude sometimes wraps JSON in a markdown code fence despite being told
    // not to — strip it rather than failing to parse over a formatting habit.
    private String stripMarkdownFence(String text) {
        String trimmed = text.trim();
        if (trimmed.startsWith("```")) {
            trimmed = trimmed.substring(trimmed.indexOf('\n') + 1);
            int closingFence = trimmed.lastIndexOf("```");
            if (closingFence != -1) {
                trimmed = trimmed.substring(0, closingFence);
            }
        }
        return trimmed.trim();
    }

    private String dietaryTags(MenuItem item) {
        StringBuilder tags = new StringBuilder();
        if (item.isVegetarian()) tags.append(" [vegetarian]");
        if (item.isSpicy()) tags.append(" [spicy]");
        if (item.isContainsNuts()) tags.append(" [contains nuts]");
        return tags.toString();
    }

    private record AiRecommendation(String reply, List<Long> menuItemIds) {}
}
