package org.bot.openAI.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Choice(
        @JsonProperty("message") Message message
) {}
