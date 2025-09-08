package com.example.th02201.controller;

import com.example.th02201.dto.PhuongXaDTO;
import com.example.th02201.service.PhuongXaService;
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
@RequestMapping(value = "/api/phuongXas", produces = MediaType.APPLICATION_JSON_VALUE)
public class PhuongXaController {

    private final PhuongXaService phuongXaService;

    public PhuongXaController(final PhuongXaService phuongXaService) {
        this.phuongXaService = phuongXaService;
    }

    @GetMapping
    public ResponseEntity<List<PhuongXaDTO>> getAllPhuongXas() {
        return ResponseEntity.ok(phuongXaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhuongXaDTO> getPhuongXa(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(phuongXaService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createPhuongXa(@RequestBody @Valid final PhuongXaDTO phuongXaDTO) {
        final Integer createdId = phuongXaService.create(phuongXaDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updatePhuongXa(@PathVariable(name = "id") final Integer id,
                                               @RequestBody @Valid final PhuongXaDTO phuongXaDTO) {
        phuongXaService.update(id, phuongXaDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deletePhuongXa(@PathVariable(name = "id") final Integer id) {
        final ReferencedWarning referencedWarning = phuongXaService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        phuongXaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}