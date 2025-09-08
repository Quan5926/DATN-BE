package com.example.th02201.controller;


import com.example.th02201.dto.ChiTietSanPhamDTO;
import com.example.th02201.service.ChiTietSanPhamService;
import com.example.th02201.util.ReferencedException;
import com.example.th02201.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/chi-tiet-san-phams", produces = MediaType.APPLICATION_JSON_VALUE)
public class ChiTietSanPhamController {

    private final ChiTietSanPhamService chiTietSanPhamService;

    public ChiTietSanPhamController(ChiTietSanPhamService chiTietSanPhamService) {
        this.chiTietSanPhamService = chiTietSanPhamService;
    }

    @GetMapping
    public ResponseEntity<List<ChiTietSanPhamDTO>> getAllChiTietSanPham() {
        return ResponseEntity.ok(chiTietSanPhamService.findAll());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ChiTietSanPhamDTO>> filterChiTietSanPham(
            @RequestParam(required = false) Long sanPhamId,
            @RequestParam(required = false) Long thuongHieuId,
            @RequestParam(required = false) Long danhMucId,
            @RequestParam(required = false) Long chatLieuId,
            @RequestParam(required = false) Long mauSacId,
            @RequestParam(required = false) Long kichCoId,
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(chiTietSanPhamService.findByFilters(sanPhamId, thuongHieuId, danhMucId, chatLieuId, mauSacId, kichCoId, keyword));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateTrangThaiSanPhamRieng(
            @PathVariable UUID id,
            @RequestBody Map<String, Integer> statusUpdate) { // Thay đổi kiểu dữ liệu của request body
        try {
            Integer idTrangThaiRieng = statusUpdate.get("idTrangThaiRieng"); // Lấy ID trạng thái
            if (idTrangThaiRieng == null) {
                return ResponseEntity.badRequest().body("ID trạng thái không được để trống.");
            }
            chiTietSanPhamService.updateTrangThaiSanPhamRieng(id, idTrangThaiRieng); // Gọi service với ID trạng thái
            return ResponseEntity.ok("Cập nhật trạng thái thành công");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Cập nhật trạng thái thất bại: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/toggle-status") // Endpoint mới để bật/tắt trạng thái
    public ResponseEntity<String> toggleChiTietSanPhamStatus(
            @PathVariable UUID id,
            @RequestBody Map<String, Boolean> status) {
        try {
            boolean active = status.get("active");
            chiTietSanPhamService.toggleStatus(id, active);
            return ResponseEntity.ok("Chuyển đổi trạng thái thành công");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Chuyển đổi trạng thái thất bại: " + e.getMessage());
        }
    }

    @GetMapping("/by-san-pham/{sanPhamId}")
    public ResponseEntity<List<ChiTietSanPhamDTO>> getVariantsBySanPhamId(@PathVariable Long sanPhamId) {
        return ResponseEntity.ok(chiTietSanPhamService.findBySanPhamId(sanPhamId));
    }
    @GetMapping("/by-chat-lieu/{chatLieuId}")
    public ResponseEntity<List<ChiTietSanPhamDTO>> getChiTietSanPhamByChatLieuId(@PathVariable Long chatLieuId) {
        // Gọi service để lấy danh sách sản phẩm theo ID chất liệu
        List<ChiTietSanPhamDTO> list = chiTietSanPhamService.findByChatLieuId(chatLieuId);
        return ResponseEntity.ok(list);
    }
    @GetMapping("/by-danh-muc/{danhMucId}")
    public ResponseEntity<List<ChiTietSanPhamDTO>> findByDanhMucId(@PathVariable Long danhMucId) {
        return ResponseEntity.ok(chiTietSanPhamService.findByDanhMucId(danhMucId));
    }

    @GetMapping("/by-thuong-hieu/{thuongHieuId}")
    public ResponseEntity<List<ChiTietSanPhamDTO>> findByThuongHieuId(@PathVariable Long thuongHieuId) {
        return ResponseEntity.ok(chiTietSanPhamService.findByThuongHieuId(thuongHieuId));
    }

    @GetMapping("/by-mau-sac/{id}")
    public ResponseEntity<List<ChiTietSanPhamDTO>> findByMauSacId(@PathVariable Long id) {
        return ResponseEntity.ok(chiTietSanPhamService.findByMauSacId(id));
    }

    @GetMapping("/by-kich-co/{id}")
    public ResponseEntity<List<ChiTietSanPhamDTO>> findByKichCoId(@PathVariable Long id) {
        return ResponseEntity.ok(chiTietSanPhamService.findByKichCoId(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChiTietSanPhamDTO> getChiTietSanPham(@PathVariable UUID id) {
        return ResponseEntity.ok(chiTietSanPhamService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createChiTietSanPham(@RequestBody @Valid ChiTietSanPhamDTO chiTietSanPhamDTO) {
        UUID createdId = chiTietSanPhamService.create(chiTietSanPhamDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateChiTietSanPham(@PathVariable UUID id,
                                                     @RequestBody @Valid ChiTietSanPhamDTO chiTietSanPhamDTO) {
        chiTietSanPhamService.update(id, chiTietSanPhamDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteChiTietSanPham(@PathVariable UUID id) {
        try {
            ReferencedWarning referencedWarning = chiTietSanPhamService.getReferencedWarning(id);
            if (referencedWarning != null) {
                throw new ReferencedException(referencedWarning);
            }
            chiTietSanPhamService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (ReferencedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}