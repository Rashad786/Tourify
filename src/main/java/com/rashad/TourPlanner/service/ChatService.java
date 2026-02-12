package com.rashad.TourPlanner.service;

import org.springframework.beans.factory.annotation.Value;
import com.jayway.jsonpath.JsonPath;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class ChatService {

    @Value("${ai.apiKey}")
    private String apiKey;

    private final String BASE_URL = "https://api.groq.com/openai/v1";

    public String getChat(String query) throws IOException, InterruptedException {
        String model = "openai/gpt-oss-20b";
        String requestBody = "{ \"model\": \"" + model + "\", \"input\": \"" + query + "\" }";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/responses"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            return "Groq API error: " + response.body();
        }

        List<String> assistantTexts = JsonPath.read(
                response.body(),
                "$.output[?(@.role=='assistant')].content[0].text"
        );

        return assistantTexts.isEmpty()
                ? "No response from assistant"
                : assistantTexts.get(0);
    }
}