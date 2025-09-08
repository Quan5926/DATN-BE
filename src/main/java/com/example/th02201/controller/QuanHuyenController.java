package com.example.th02201.controller;

import com.example.th02201.dto.QuanHuyenDTO;
import com.example.th02201.service.QuanHuyenService;
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
@RequestMapping(value = "/api/quanHuyens", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuanHuyenController {

    private final QuanHuyenService quanHuyenService;

    public QuanHuyenController(final QuanHuyenService quanHuyenService) {
        this.quanHuyenService = quanHuyenService;
    }

    @GetMapping
    public ResponseEntity<List<QuanHuyenDTO>> getAllQuanHuyens() {
        return ResponseEntity.ok(quanHuyenService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuanHuyenDTO> getQuanHuyen(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(quanHuyenService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createQuanHuyen(
            @RequestBody @Valid final QuanHuyenDTO quanHuyenDTO) {
        final Integer createdId = quanHuyenService.create(quanHuyenDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateQuanHuyen(@PathVariable(name = "id") final Integer id,
                                                @RequestBody @Valid final QuanHuyenDTO quanHuyenDTO) {
        quanHuyenService.update(id, quanHuyenDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteQuanHuyen(@PathVariable(name = "id") final Integer id) {
        final ReferencedWarning referencedWarning = quanHuyenService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        quanHuyenService.delete(id);
        return ResponseEntity.noContent().build();
    }
}