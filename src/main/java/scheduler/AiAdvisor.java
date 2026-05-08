package scheduler;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.anthropic.models.messages.MessageCreateParams;

import java.util.stream.Collectors;

public class AiAdvisor {
    private static final String MODEL = "claude-opus-4-7";
    private static final long MAX_TOKENS = 1024L;

    public static String getSuggestions(Schedule schedule) {
        String apiKey = System.getenv("ANTHROPIC_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            return "[AI] Set the ANTHROPIC_API_KEY environment variable to enable suggestions.";
        }

        try {
            AnthropicClient client = AnthropicOkHttpClient.builder()
                .apiKey(apiKey)
                .build();

            MessageCreateParams params = MessageCreateParams.builder()
                .model(MODEL)
                .maxTokens(MAX_TOKENS)
                .addUserMessage(buildPrompt(schedule.toPromptString()))
                .build();

            return client.messages().create(params).content().stream()
                .flatMap(block -> block.text().stream())
                .map(textBlock -> textBlock.text())
                .collect(Collectors.joining("\n"))
                .strip();

        } catch (Exception e) {
            return "[AI] Error: " + e.getMessage();
        }
    }

    private static String buildPrompt(String scheduleText) {
        return "You are a scheduling assistant. Here is a task schedule:\n\n" + scheduleText +
            "\n\nProvide 3-5 concise suggestions to improve this schedule. " +
            "Focus on overloaded days, related tasks that should be grouped, " +
            "and lower-priority tasks that could be deferred. Be specific and brief.";
    }
}
