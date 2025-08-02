package com.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/chat")
public class ChatController {
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/send")
    public ResponseEntity<?> handleMessage(@RequestBody Map<String, String> request) {
        try {
            System.out.println("Received message: " + request.get("message"));
            
            // Better payload construction
            Map<String, String> payloadMap = new HashMap<>();
            payloadMap.put("sender", "user");
            payloadMap.put("message", request.get("message"));
            String payload = objectMapper.writeValueAsString(payloadMap);
            
            System.out.println("Forwarding to Rasa: " + payload);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(payload, headers);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> rasaResponse = restTemplate.postForEntity(
                    "http://localhost:5005/webhooks/rest/webhook",
                    entity,
                    String.class
            );
            
            System.out.println("Rasa response status: " + rasaResponse.getStatusCode());
            System.out.println("Rasa response body: " + rasaResponse.getBody());

            List<Map<String, String>> result = objectMapper.readValue(
                rasaResponse.getBody(), 
                objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class)
            );
            
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }
}