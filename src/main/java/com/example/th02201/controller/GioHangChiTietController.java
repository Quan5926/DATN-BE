package com.example.th02201.controller;

import com.example.th02201.dto.GioHangChiTietDTO;
import com.example.th02201.service.GioHangChiTietService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/gioHangChiTiets", produces = MediaType.APPLICATION_JSON_VALUE)
public class GioHangChiTietController {

    private final GioHangChiTietService gioHangChiTietService;

    public GioHangChiTietController(final GioHangChiTietService gioHangChiTietService) {
        this.gioHangChiTietService = gioHangChiTietService;
    }

    @GetMapping
    public ResponseEntity<List<GioHangChiTietDTO>> getAllGioHangChiTiets() {
        return ResponseEntity.ok(gioHangChiTietService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GioHangChiTietDTO> getGioHangChiTiet(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(gioHangChiTietService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createGioHangChiTiet(@RequestBody @Valid final GioHangChiTietDTO gioHangChiTietDTO) {
        final Long createdId = gioHangChiTietService.create(gioHangChiTietDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateGioHangChiTiet(@PathVariable(name = "id") final Long id,
                                                     @RequestBody @Valid final GioHangChiTietDTO gioHangChiTietDTO) {
        gioHangChiTietService.update(id, gioHangChiTietDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteGioHangChiTiet(@PathVariable(name = "id") final Long id) {
        gioHangChiTietService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-gio-hang/{idGioHang}")
    public ResponseEntity<List<GioHangChiTietDTO>> getCartDetailsByGioHangId(@PathVariable("idGioHang") final UUID idGioHang) {
        List<GioHangChiTietDTO> cartDetails = gioHangChiTietService.getCartDetailsByGioHangId(idGioHang);
        return ResponseEntity.ok(cartDetails);
    }

    @GetMapping("/by-ma-phien-gio-hang/{maPhienGioHang}")
    public ResponseEntity<List<GioHangChiTietDTO>> getCartDetailsBySessionCode(@PathVariable("maPhienGioHang") final String maPhienGioHang) {
        List<GioHangChiTietDTO> cartDetails = gioHangChiTietService.getCartDetailsBySessionCode(maPhienGioHang);
        return ResponseEntity.ok(cartDetails);
    }

    // Endpoint mới để lấy chi tiết giỏ hàng theo ID khách hàng
    @GetMapping("/by-khach-hang/{idKhachHang}")
    public ResponseEntity<List<GioHangChiTietDTO>> getCartDetailsByKhachHangId(@PathVariable("idKhachHang") final Integer idKhachHang) {
        List<GioHangChiTietDTO> cartDetails = gioHangChiTietService.getCartDetailsByKhachHangId(idKhachHang);
        return ResponseEntity.ok(cartDetails);
    }
}
