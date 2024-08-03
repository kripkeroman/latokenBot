package org.bot.openAI;

import org.bot.openAI.api.OpenAIClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfiguration
{
    @Bean
    public OpenAIClient openAIClient(
            @Value("${openai.url}") String url,
            @Value("${openai.latokenToken}") String openAIToken,
            RestTemplateBuilder restTemplateBuilder
    )
    {
        return new OpenAIClient(url, openAIToken, restTemplateBuilder.build());
    }
}
