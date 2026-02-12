package com.rashad.TourPlanner.controller;

import com.rashad.TourPlanner.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@Tag(name = "Chat Controller", description = "AI Chat Controller using Groq AI")
public class ChatController {

    @Autowired
    private ChatService chatService;


    @Operation(summary = "Chat Service")
    @PostMapping("/chat")
    public ResponseEntity<?> chat(@RequestBody Map<String, String> payload) throws IOException, InterruptedException {
        String query = payload.get("message");
        String response = chatService.getChat(query);
        return ResponseEntity.ok(Map.of("reply", response));
    }

}
