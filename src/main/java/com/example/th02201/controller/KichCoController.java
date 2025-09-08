package com.example.th02201.controller;

import com.example.th02201.util.ReferencedException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.th02201.repository.*;
import com.example.th02201.service.*;
import com.example.th02201.dto.*;
import com.example.th02201.model.*;
import com.example.th02201.util.NotFoundException;
import com.example.th02201.util.ReferencedWarning;

import java.util.List;

@RestController
@RequestMapping(value = "/api/kichCos", produces = MediaType.APPLICATION_JSON_VALUE)
public class KichCoController {

    private final KichCoService kichCoService;

    public KichCoController(final KichCoService kichCoService) {
        this.kichCoService = kichCoService;
    }

    @GetMapping
    public ResponseEntity<List<KichCoDTO>> getAllKichCos() {
        return ResponseEntity.ok(kichCoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<KichCoDTO> getKichCo(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(kichCoService.get(id));
    }
    @PostMapping
    public ResponseEntity<Long> createKichCo(@RequestBody @Valid final KichCoDTO kichCoDTO) {
        final Long createdId = kichCoService.create(kichCoDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateKichCo(@PathVariable(name = "id") final Long id,
                                             @RequestBody @Valid final KichCoDTO kichCoDTO) {
        kichCoService.update(id, kichCoDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKichCo(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = kichCoService.getReferencedWarning(id);
        if (referencedWarning != null && referencedWarning.hasWarnings()) { // Kiểm tra null trước khi gọi hasWarnings()
            throw new ReferencedException(referencedWarning);
        }
        kichCoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ReferencedException.class)
    public ResponseEntity<String> handleReferencedException(ReferencedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
