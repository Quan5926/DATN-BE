package com.example.th02201.controller;


import com.example.th02201.dto.TinhThanhDTO;
import com.example.th02201.service.TinhThanhService;
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
@RequestMapping(value = "/api/tinhThanhs", produces = MediaType.APPLICATION_JSON_VALUE)
public class TinhThanhController {

    private final TinhThanhService tinhThanhService;

    public TinhThanhController(final TinhThanhService tinhThanhService) {
        this.tinhThanhService = tinhThanhService;
    }

    @GetMapping
    public ResponseEntity<List<TinhThanhDTO>> getAllTinhThanhs() {
        return ResponseEntity.ok(tinhThanhService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TinhThanhDTO> getTinhThanh(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(tinhThanhService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createTinhThanh(
            @RequestBody @Valid final TinhThanhDTO tinhThanhDTO) {
        final Integer createdId = tinhThanhService.create(tinhThanhDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateTinhThanh(@PathVariable(name = "id") final Integer id,
                                                @RequestBody @Valid final TinhThanhDTO tinhThanhDTO) {
        tinhThanhService.update(id, tinhThanhDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteTinhThanh(@PathVariable(name = "id") final Integer id) {
        final ReferencedWarning referencedWarning = tinhThanhService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        tinhThanhService.delete(id);
        return ResponseEntity.noContent().build();
    }
}