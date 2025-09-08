package com.example.th02201.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.th02201.dto.AnhSanPhamDTO;
import com.example.th02201.service.AnhSanPhamService;
import com.example.th02201.util.NotFoundException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/anhSanPhams")
@CrossOrigin(origins = "http://localhost:3000") // Đảm bảo CORS được cấu hình đúng
public class AnhSanPhamController {

    private final AnhSanPhamService anhSanPhamService;
    private final ObjectMapper objectMapper;

    public AnhSanPhamController(final AnhSanPhamService anhSanPhamService, ObjectMapper objectMapper) {
        this.anhSanPhamService = anhSanPhamService;
        this.objectMapper = objectMapper;
    }

    // Endpoint để lấy tất cả ảnh của một chi tiết sản phẩm
    // Phương thức này sẽ thay thế cho getAllAnhSanPhams nếu bạn muốn lấy ảnh theo chi tiết sản phẩm
    @GetMapping("/by-chi-tiet-san-pham/{chiTietSpId}")
    public ResponseEntity<List<AnhSanPhamDTO>> getImagesByChiTietSanPhamId(@PathVariable UUID chiTietSpId) {
        return ResponseEntity.ok(anhSanPhamService.getImagesByChiTietSanPhamId(chiTietSpId));
    }

    // Endpoint để tải ảnh lên (MultipartFile)
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "201")
    public ResponseEntity<?> uploadImage(
            @RequestPart("file") MultipartFile file,
            @RequestPart(value = "data", required = false) String anhSanPhamDtoJson) {
        try {
            AnhSanPhamDTO anhSanPhamDTO = new AnhSanPhamDTO();
            if (anhSanPhamDtoJson != null && !anhSanPhamDtoJson.isEmpty()) {
                anhSanPhamDTO = objectMapper.readValue(anhSanPhamDtoJson, AnhSanPhamDTO.class);
            }

            // Đặt laAnhDaiDien mặc định là false nếu không được chỉ định
            if (anhSanPhamDTO.getLaAnhDaiDien() == null) {
                anhSanPhamDTO.setLaAnhDaiDien(false);
            }

            if (anhSanPhamDTO.getChiTietSpId() == null) {
                return ResponseEntity.badRequest().body("ID chi tiết sản phẩm không được để trống.");
            }

            // Gọi phương thức uploadImage từ service
            final AnhSanPhamDTO createdAnhSanPham = anhSanPhamService.uploadImage(file, anhSanPhamDTO.getChiTietSpId(), anhSanPhamDTO.getLaAnhDaiDien());
            return new ResponseEntity<>(createdAnhSanPham, HttpStatus.CREATED);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dữ liệu không hợp lệ: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IOException occurred during image upload: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi xử lý file: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi không mong muốn khi tải ảnh lên: " + e.getMessage());
        }
    }

    // Endpoint để thêm ảnh từ URL
    @PostMapping(value = "/add-url", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "201")
    public ResponseEntity<?> addImageFromUrl(@RequestBody @Valid AnhSanPhamDTO anhSanPhamDTO) {
        try {
            if (anhSanPhamDTO.getUrlAnh() == null || anhSanPhamDTO.getUrlAnh().trim().isEmpty()) {
                throw new IllegalArgumentException("URL ảnh không được để trống.");
            }
            if (anhSanPhamDTO.getLaAnhDaiDien() == null) {
                anhSanPhamDTO.setLaAnhDaiDien(false);
            }
            // Gọi phương thức addImageFromUrl từ service
            final AnhSanPhamDTO createdAnhSanPham = anhSanPhamService.addImageFromUrl(anhSanPhamDTO);
            return new ResponseEntity<>(createdAnhSanPham, HttpStatus.CREATED);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dữ liệu không hợp lệ: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi không mong muốn khi thêm ảnh từ URL: " + e.getMessage());
        }
    }

    // Endpoint để cập nhật ảnh (chủ yếu là trạng thái laAnhDaiDien)
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateImage(@PathVariable Long id, @RequestBody @Valid AnhSanPhamDTO anhSanPhamDTO) { // Thay đổi Long id
        try {
            // Gọi phương thức updateImage từ service
            AnhSanPhamDTO updatedImage = anhSanPhamService.updateImage(id, anhSanPhamDTO);
            return ResponseEntity.ok(updatedImage);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dữ liệu không hợp lệ: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi không mong muốn khi cập nhật ảnh: " + e.getMessage());
        }
    }

    // Endpoint để xóa ảnh
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "204")
    public ResponseEntity<?> deleteImage(@PathVariable Long id) { // Thay đổi Long id
        try {
            // Gọi phương thức deleteImage từ service
            anhSanPhamService.deleteImage(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi không mong muốn khi xóa ảnh: " + e.getMessage());
        }
    }

    // Các exception handler hiện có
    // @ExceptionHandler(ReferencedException.class) // Đã bỏ qua vì không có trong AnhSanPhamService hiện tại
    // public ResponseEntity<String> handleReferencedException(ReferencedException ex) {
    //     return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    // }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException ex) {
        System.err.println("IOException occurred: " + ex.getMessage());
        return new ResponseEntity<>("Lỗi xử lý dữ liệu hoặc file: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        ex.printStackTrace();
        return new ResponseEntity<>("Đã xảy ra lỗi không mong muốn: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}