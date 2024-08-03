package org.bot.openAI.api.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ChatComplitionRequest(
        String model,
        List<Message> messages
) {}
