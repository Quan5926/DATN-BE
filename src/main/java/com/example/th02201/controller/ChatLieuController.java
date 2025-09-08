package com.example.th02201.controller;


import com.example.th02201.dto.ChatLieuDTO;
import com.example.th02201.service.ChatLieuService;
import com.example.th02201.util.ReferencedException;
import com.example.th02201.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/chatLieus", produces = MediaType.APPLICATION_JSON_VALUE)
public class ChatLieuController {

    private final ChatLieuService chatLieuService;

    public ChatLieuController(final ChatLieuService chatLieuService) {
        this.chatLieuService = chatLieuService;
    }

    @GetMapping
    public ResponseEntity<List<ChatLieuDTO>> getAllChatLieus() {
        return ResponseEntity.ok(chatLieuService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatLieuDTO> getChatLieu(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(chatLieuService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createChatLieu(
            @RequestBody @Valid final ChatLieuDTO chatLieuDTO) {
        final Long createdId = chatLieuService.create(chatLieuDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateChatLieu(@PathVariable(name = "id") final Long id,
                                               @RequestBody @Valid final ChatLieuDTO chatLieuDTO) {
        chatLieuService.update(id, chatLieuDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteChatLieu(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = chatLieuService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        chatLieuService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
