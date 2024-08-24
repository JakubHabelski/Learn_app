package com.learn.app.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OpenAiService {

    private final String apiKey = "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";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getChatResponse(String message) throws Exception {
        String url = "https://api.openai.com/v1/chat/completions";

        // Create request body
        String requestBody = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"" + message + "\"}], \"max_tokens\": 100}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            return jsonResponse.get("choices").get(0).get("message").get("content").asText();
        } else {
            throw new RuntimeException("Failed to get response from OpenAI API");
        }
    }
}
