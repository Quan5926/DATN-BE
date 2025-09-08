package com.example.th02201.controller;

import com.example.th02201.dto.*;
import com.example.th02201.service.*;
import com.example.th02201.util.ReferencedWarning;
import com.example.th02201.util.NotFoundException;
import com.example.th02201.util.ReferencedException;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/san-phams", produces = MediaType.APPLICATION_JSON_VALUE)
public class SanPhamController {

    private final SanPhamService sanPhamService;

    public SanPhamController(SanPhamService sanPhamService) {
        this.sanPhamService = sanPhamService;
    }

    @GetMapping
    public ResponseEntity<Page<SanPhamDTO>> getAllSanPhams(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(sanPhamService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SanPhamDTO> getSanPham(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(sanPhamService.get(id));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<SanPhamDTO>> searchSanPhams(@RequestParam String keyword, @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(sanPhamService.search(keyword, pageable));
    }

    // Endpoint để cập nhật trạng thái sản phẩm
    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, String>> updateSanPhamStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusUpdate) {
        try {
            String status = statusUpdate.get("trangThai");
            if (status == null || (!status.equals("dang_kinh_doanh") && !status.equals("ngung_kinh_doanh") && !status.equals("het_hang"))) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Trạng thái không hợp lệ");
                errorResponse.put("details", "Trạng thái phải là 'dang_kinh_doanh', 'ngung_kinh_doanh', hoặc 'het_hang'");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            sanPhamService.updateStatus(id, status);
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("message", "Cập nhật trạng thái sản phẩm thành công");
            return ResponseEntity.ok(successResponse);
        } catch (NotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Sản phẩm không tồn tại");
            errorResponse.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Lỗi server khi cập nhật trạng thái");
            errorResponse.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Map<String, String>> deleteSanPham(@PathVariable(name = "id") Long id) {
        try {
            sanPhamService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Sản phẩm không tồn tại");
            errorResponse.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            System.out.println("Error deleting product ID: " + id + ", Message: " + e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Lỗi server");
            errorResponse.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{id}/referenced-warning")
    public ResponseEntity<ReferencedWarning> getReferencedWarning(@PathVariable Long id) {
        ReferencedWarning warning = sanPhamService.getReferencedWarning(id);
        if (warning != null && warning.hasWarnings()) {
            return ResponseEntity.ok(warning);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-thuong-hieu")
    public ResponseEntity<Page<SanPhamDTO>> getSanPhamsByThuongHieu(
            @RequestParam(name = "thuongHieuIds") List<Long> thuongHieuIds,
            @PageableDefault(page = 0, size = 10, sort = "tenSanPham") Pageable pageable) {
        Page<SanPhamDTO> products = sanPhamService.findByThuongHieuIds(thuongHieuIds, pageable);
        return ResponseEntity.ok(products);
    }

    // ***************************************************************
    // PHƯƠNG THỨC MỚI: Endpoint để thêm mới sản phẩm và các chi tiết của nó
    // ***************************************************************
    @PostMapping
    @ApiResponse(responseCode = "201", description = "Sản phẩm và các chi tiết được tạo thành công")
    @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
    @ApiResponse(responseCode = "500", description = "Lỗi server")
    public ResponseEntity<?> createNewProduct(@Valid @RequestBody SanPhamDTO sanPhamDto) {
        try {
            System.out.println("Received SanPhamDTO for creation: " + sanPhamDto);
            SanPhamDTO createdProduct = sanPhamService.createProductWithDetails(sanPhamDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (NotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Lỗi dữ liệu tham chiếu");
            errorResponse.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (DataIntegrityViolationException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Lỗi dữ liệu trùng lặp");
            errorResponse.put("details", "Mã sản phẩm hoặc tên sản phẩm đã tồn tại. Vui lòng kiểm tra lại.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Lỗi dữ liệu không hợp lệ");
            errorResponse.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            System.err.println("Error creating product: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Lỗi server khi thêm sản phẩm");
            errorResponse.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // ***************************************************************
    // PHƯƠNG THỨC MỚI: Endpoint để cập nhật sản phẩm theo ID
    // ***************************************************************
    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "Cập nhật sản phẩm thành công")
    @ApiResponse(responseCode = "404", description = "Không tìm thấy sản phẩm")
    @ApiResponse(responseCode = "400", description = "Lỗi validation hoặc dữ liệu không hợp lệ")
    @ApiResponse(responseCode = "500", description = "Lỗi server")
    public ResponseEntity<?> updateSanPham(@PathVariable("id") Long id, @Valid @RequestBody SanPhamUpdateDTO sanPhamUpdateDto) {
        try {
            // Đặt ID vào DTO trước khi gọi service
            sanPhamUpdateDto.setId(id);
            SanPhamDTO updatedSanPham = sanPhamService.update(sanPhamUpdateDto);
            return ResponseEntity.ok(updatedSanPham);
        } catch (NotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Lỗi dữ liệu không tồn tại");
            errorResponse.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Lỗi dữ liệu không hợp lệ");
            errorResponse.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (DataIntegrityViolationException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Lỗi dữ liệu");
            errorResponse.put("details", "Tên sản phẩm hoặc Mã sản phẩm có thể đã tồn tại.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Lỗi server khi cập nhật sản phẩm");
            errorResponse.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Xử lý chung các ngoại lệ liên quan đến validation của Spring
    // Đã thay đổi cách xử lý MethodArgumentNotValidException để fix lỗi
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "Lỗi validation dữ liệu");
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        errorResponse.put("details", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // Các exception handler hiện có
    @ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class})
    public ResponseEntity<String> handleDataIntegrityViolation(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Lỗi dữ liệu: " + ex.getMessage());
    }
}