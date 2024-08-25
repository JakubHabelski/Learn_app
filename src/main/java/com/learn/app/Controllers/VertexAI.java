package com.learn.app.Controllers;


import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class VertexAI {

    public static String textInput(
            String textPrompt) throws IOException {
        String projectId = "project-jh-425111";
        String location = "europe-west4";
        String modelName = "gemini-1.5-flash-001";

        // Initialize client that will be used to send requests. This client only needs
        // to be created once, and can be reused for multiple requests.
        try (com.google.cloud.vertexai.VertexAI vertexAI = new com.google.cloud.vertexai.VertexAI(projectId, location)) {
            GenerativeModel model = new GenerativeModel(modelName, vertexAI);

            GenerateContentResponse response = model.generateContent(textPrompt);
            return ResponseHandler.getText(response);
        }
    }

}
