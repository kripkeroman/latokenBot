package org.bot.openAI.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ChatComplitionResponse(
        @JsonProperty("choices") List<Choice> choices
) {}
