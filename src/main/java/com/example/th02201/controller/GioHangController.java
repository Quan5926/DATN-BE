package com.example.th02201.controller;

import com.example.th02201.dto.GioHangDTO;
import com.example.th02201.service.GioHangService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/gioHangs", produces = MediaType.APPLICATION_JSON_VALUE)
public class GioHangController {

    private final GioHangService gioHangService;

    public GioHangController(final GioHangService gioHangService) {
        this.gioHangService = gioHangService;
    }

    @GetMapping
    public ResponseEntity<List<GioHangDTO>> getAllGioHangs() {
        return ResponseEntity.ok(gioHangService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GioHangDTO> getGioHang(@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(gioHangService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createGioHang(@RequestBody @Valid final GioHangDTO gioHangDTO) {
        final UUID createdId = gioHangService.create(gioHangDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    // Endpoint mới để tạo cả giỏ hàng và chi tiết
    @PostMapping("/full")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createFullCart(@RequestBody @Valid final GioHangDTO gioHangDTO) {
        final UUID createdId = gioHangService.createFullCart(gioHangDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateGioHang(@PathVariable(name = "id") final UUID id,
                                              @RequestBody @Valid final GioHangDTO gioHangDTO) {
        gioHangService.update(id, gioHangDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteGioHang(@PathVariable(name = "id") final UUID id) {
        // Logic check ReferencedWarning đã được xử lý trong service
        // Nên chỉ cần gọi service và trả về response
        gioHangService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
