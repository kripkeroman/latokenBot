package org.bot.openAI;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import org.bot.openAI.api.OpenAIClient;
import org.bot.openAI.api.dto.ChatComplitionRequest;
import org.bot.openAI.api.dto.Message;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@AllArgsConstructor
public class ChatGptService
{
    private final OpenAIClient openAIClient;

    @Nonnull
    public String getResponseChatForUser(
            Long userId,
            String userTextInput)
    {
        var request = ChatComplitionRequest.builder()
                .model("gpt-4o-mini")
                .messages(List.of(
                        Message.builder()
                                .content(userTextInput)
                                .role("user")
                                .build()
                ))
                .build();
        var response = openAIClient.createChatComplition(request);
        return response.choices().get(0).message().content();
    }
}
