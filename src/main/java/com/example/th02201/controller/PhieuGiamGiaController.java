package com.example.th02201.controller;

import com.example.th02201.dto.PhieuGiamGiaDTO;
import com.example.th02201.dto.PhieuGiamGiaResponseDTO;
import com.example.th02201.service.PhieuGiamGiaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/phieu-giam-gia")
public class PhieuGiamGiaController {

    @Autowired
    private PhieuGiamGiaService phieuGiamGiaService;

    // ==================== GET ALL ====================
    @GetMapping
    public ResponseEntity<Page<PhieuGiamGiaResponseDTO>> getAll(Pageable pageable) {
        Page<PhieuGiamGiaResponseDTO> result = phieuGiamGiaService.getAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PhieuGiamGiaResponseDTO>> searchPhieuGiamGia(
            @RequestParam(required = false) String tenPhieuGiamGia,
            @RequestParam(required = false) String loaiGiamGia,
            @RequestParam(required = false) String loaiApDung,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ngayBatDau,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ngayKetThuc,
            Pageable pageable) {
        Page<PhieuGiamGiaResponseDTO> result = phieuGiamGiaService.searchPhieuGiamGia(
                tenPhieuGiamGia, loaiGiamGia, loaiApDung, ngayBatDau, ngayKetThuc, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhieuGiamGiaResponseDTO> getPhieuGiamGiaById(@PathVariable UUID id) {
        try {
            PhieuGiamGiaResponseDTO responseDTO = phieuGiamGiaService.getPhieuGiamGiaById(id);
            return ResponseEntity.ok(responseDTO);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi server khi tải phiếu giảm giá.", e);
        }
    }


    @GetMapping("/code/{maPhieuGiamGia}")
    public ResponseEntity<PhieuGiamGiaResponseDTO> getPhieuGiamGiaByMa(@PathVariable String maPhieuGiamGia) {
        try {
            PhieuGiamGiaResponseDTO responseDTO = phieuGiamGiaService.getPhieuGiamGiaByMa(maPhieuGiamGia);
            return ResponseEntity.ok(responseDTO);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi server khi tải phiếu giảm giá.", e);
        }
    }



    @PostMapping("/create")
    public ResponseEntity<PhieuGiamGiaResponseDTO> createPhieuGiamGia(@Valid @RequestBody PhieuGiamGiaDTO request) {
        try {
            PhieuGiamGiaResponseDTO created = phieuGiamGiaService.createPhieuGiamGia(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PhieuGiamGiaResponseDTO> updatePhieuGiamGia(@PathVariable UUID id, @Valid @RequestBody PhieuGiamGiaDTO request) {
        try {
            PhieuGiamGiaResponseDTO updated = phieuGiamGiaService.updatePhieuGiamGia(id, request);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhieuGiamGia(@PathVariable UUID id) {
        try {
            phieuGiamGiaService.deletePhieuGiamGia(id);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi server khi xóa phiếu giảm giá.", e);
        }
    }
}