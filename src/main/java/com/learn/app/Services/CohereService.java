package com.learn.app.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CohereService {

    @Value("${cohere.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateText(String prompt) throws Exception {
        String url = "https://api.cohere.ai/generate";

        // Tworzenie ciała żądania
        String requestBody = "{\"model\": \"command-xlarge-nightly\", \"prompt\": \"" + prompt + "\", \"max_tokens\": 100}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());

            // Pobranie wygenerowanego tekstu z klucza "text"
            JsonNode textNode = jsonResponse.get("text");
            if (textNode != null) {
                return textNode.asText();
            } else {
                throw new RuntimeException("Unexpected JSON structure: 'text' key is missing or empty");
            }
        } else {
            throw new RuntimeException("Failed to get response from Cohere API. Status code: " + response.getStatusCode());
        }
    }
}
