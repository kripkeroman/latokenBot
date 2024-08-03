package org.bot.openAI.api;

import lombok.AllArgsConstructor;
import org.bot.openAI.api.dto.ChatComplitionRequest;
import org.bot.openAI.api.dto.ChatComplitionResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
public class OpenAIClient
{
    private final String url;
    private final String token;
    private final RestTemplate restTemplate;

    public ChatComplitionResponse createChatComplition(ChatComplitionRequest request)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Content-Type", "application/json");

        HttpEntity<ChatComplitionRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<ChatComplitionResponse> responseEntity = restTemplate.exchange(
                url, HttpMethod.POST, entity, ChatComplitionResponse.class
        );
        return responseEntity.getBody();
    }
}
