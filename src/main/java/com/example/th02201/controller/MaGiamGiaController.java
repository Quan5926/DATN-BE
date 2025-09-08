package com.example.th02201.controller;

import com.example.th02201.dto.VoucherValidationResponse;
import com.example.th02201.model.MaGiamGia;
import com.example.th02201.service.MaGiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/ma-giam-gia")
public class MaGiamGiaController {
    @Autowired
    private MaGiamGiaService maGiamGiaService;

    // Lấy tất cả mã giảm giá có phân trang
    @GetMapping
    public ResponseEntity<Page<MaGiamGia>> getAllMaGiamGia(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "maCode") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
        // Pageable pageable = (Pageable) PageRequest.of(page, size, sort); // Không cần ép kiểu Pageable tường minh như này nữa
        Pageable pageable = PageRequest.of(page, size, sort); // PageRequest.of() đã trả về org.springframework.data.domain.Pageable

        Page<MaGiamGia> maGiamGias = maGiamGiaService.getAllMaGiamGia(pageable); // Truyền tham số pageable
        return ResponseEntity.ok(maGiamGias);
    }

    // Lấy mã giảm giá theo ID
    @GetMapping("/{id}")
    public ResponseEntity<MaGiamGia> getMaGiamGiaById(@PathVariable Integer id) {
        return maGiamGiaService.getMaGiamGiaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Lấy mã giảm giá theo mã code
    @GetMapping("/ma/{maCode}")
    public ResponseEntity<MaGiamGia> getMaGiamGiaByMaCode(@PathVariable String maCode) {
        return maGiamGiaService.getMaGiamGiaByMaCode(maCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Tạo mã giảm giá mới
    @PostMapping
    public ResponseEntity<MaGiamGia> createMaGiamGia(@RequestBody MaGiamGia maGiamGia) {
        try {
            MaGiamGia createdMaGiamGia = maGiamGiaService.createMaGiamGia(maGiamGia);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdMaGiamGia);
        } catch (IllegalArgumentException e) {
            // Log lỗi e.getMessage() để debug chi tiết hơn
            return ResponseEntity.badRequest().body(null); // Trả về 400 Bad Request
        } catch (RuntimeException e) {
            // Log lỗi e.getMessage() để debug chi tiết hơn
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Trả về 500 Internal Server Error
        }
    }

    // Cập nhật mã giảm giá
    @PutMapping("/{id}")
    public ResponseEntity<MaGiamGia> updateMaGiamGia(@PathVariable Integer id, @RequestBody MaGiamGia maGiamGiaDetails) {
        try {
            MaGiamGia updatedMaGiamGia = maGiamGiaService.updateMaGiamGia(id, maGiamGiaDetails);
            return ResponseEntity.ok(updatedMaGiamGia);
        } catch (RuntimeException e) {
            // Log lỗi e.getMessage() để debug chi tiết hơn
            return ResponseEntity.notFound().build(); // Không tìm thấy mã giảm giá để cập nhật
        }
    }

    // Xóa mã giảm giá
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaGiamGia(@PathVariable Integer id) {
        try {
            maGiamGiaService.deleteMaGiamGia(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            // Log lỗi e.getMessage() để debug chi tiết hơn
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint để đánh dấu mã giảm giá đã sử dụng
    @PostMapping("/mark-used/{maCode}")
    public ResponseEntity<MaGiamGia> markMaGiamGiaAsUsed(@PathVariable String maCode) {
        try {
            MaGiamGia updatedMa = maGiamGiaService.markMaGiamGiaAsUsed(maCode);
            return ResponseEntity.ok(updatedMa);

        } catch (IllegalArgumentException e) {
            // Log lỗi e.getMessage() để debug chi tiết hơn
            return ResponseEntity.badRequest().body(null); // Lỗi nghiệp vụ: đã sử dụng, hết hạn
        } catch (RuntimeException e) {
            // Log lỗi e.getMessage() để debug chi tiết hơn
            return ResponseEntity.notFound().build(); // Lỗi không tìm thấy mã
        }
    }

    @GetMapping("/validate/{maCode}")
    public ResponseEntity<VoucherValidationResponse> validateVoucher(
            @PathVariable String maCode,
            @RequestParam BigDecimal total,
            @RequestParam(required = false) Integer idKhachHang) {
        try {
            VoucherValidationResponse response = maGiamGiaService.validateVoucher(maCode, total, idKhachHang);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            // Xử lý các lỗi nghiệp vụ và trả về mã trạng thái phù hợp
            throw e;
        }
    }

}
