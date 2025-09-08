package com.example.th02201.controller;

import com.example.th02201.util.ReferencedException;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@RequestMapping(value = "/api/thuongHieus", produces = MediaType.APPLICATION_JSON_VALUE)
public class ThuongHieuController {

    private final ThuongHieuService thuongHieuService;

    public ThuongHieuController(final ThuongHieuService thuongHieuService) {
        this.thuongHieuService = thuongHieuService;
    }

    @GetMapping
    public ResponseEntity<List<ThuongHieuDTO>> getAllThuongHieus() {
        return ResponseEntity.ok(thuongHieuService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ThuongHieuDTO> getThuongHieu(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(thuongHieuService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createThuongHieu(
            @RequestBody @Valid final ThuongHieuDTO thuongHieuDTO) {
        final Long createdId = thuongHieuService.create(thuongHieuDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateThuongHieu(@PathVariable(name = "id") final Long id,
                                                 @RequestBody @Valid final ThuongHieuDTO thuongHieuDTO) {
        thuongHieuService.update(id, thuongHieuDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteThuongHieu(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = thuongHieuService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        thuongHieuService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
