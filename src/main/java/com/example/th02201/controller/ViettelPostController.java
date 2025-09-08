package com.example.th02201.controller;

import com.example.th02201.dto.ViettelPostDTO;
import com.example.th02201.service.ViettelPostService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Controller chuyên trách xử lý các request liên quan đến API Viettel Post.
 * Tách biệt logic này ra khỏi DiaChiKhachHangController để tuân thủ SRP.
 */
@RestController
@RequestMapping(value = "/api/viettel-post", produces = MediaType.APPLICATION_JSON_VALUE)
public class ViettelPostController {

    private final ViettelPostService viettelPostService;

    public ViettelPostController(final ViettelPostService viettelPostService) {
        this.viettelPostService = viettelPostService;
    }

    /**
     * Lấy danh sách tỉnh/thành phố từ Viettel Post.
     * @return Mono<ResponseEntity<List<ViettelPostDTO.Province>>>
     */
    @GetMapping("/provinces")
    @ApiResponse(responseCode = "200", description = "Lấy danh sách tỉnh thành công")
    @ApiResponse(responseCode = "404", description = "Không tìm thấy dữ liệu")
    public Mono<ResponseEntity<List<ViettelPostDTO.Province>>> getProvinces() {
        return viettelPostService.getProvinces()
                .map(provinces -> ResponseEntity.ok(provinces))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Lấy danh sách quận/huyện theo ID của tỉnh.
     * @param provinceId ID của tỉnh/thành phố
     * @return Mono<ResponseEntity<List<ViettelPostDTO.District>>>
     */
    @GetMapping("/districts/{provinceId}")
    @ApiResponse(responseCode = "200", description = "Lấy danh sách quận huyện thành công")
    @ApiResponse(responseCode = "404", description = "Không tìm thấy dữ liệu")
    public Mono<ResponseEntity<List<ViettelPostDTO.District>>> getDistricts(@PathVariable Integer provinceId) {
        return viettelPostService.getDistricts(provinceId)
                .map(districts -> ResponseEntity.ok(districts))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Lấy danh sách phường/xã theo ID của huyện.
     * @param districtId ID của quận/huyện
     * @return Mono<ResponseEntity<List<ViettelPostDTO.Ward>>>
     */
    @GetMapping("/wards/{districtId}")
    @ApiResponse(responseCode = "200", description = "Lấy danh sách phường xã thành công")
    @ApiResponse(responseCode = "404", description = "Không tìm thấy dữ liệu")
    public Mono<ResponseEntity<List<ViettelPostDTO.Ward>>> getWards(@PathVariable Integer districtId) {
        return viettelPostService.getWards(districtId)
                .map(wards -> ResponseEntity.ok(wards))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint để kích hoạt quá trình đồng bộ hóa dữ liệu vị trí.
     * @return Mono<ResponseEntity<String>> - Thông báo kết quả
     */
    @PostMapping("/sync-locations")
    @ApiResponse(responseCode = "200", description = "Đồng bộ hóa thành công")
    @ApiResponse(responseCode = "500", description = "Lỗi trong quá trình đồng bộ hóa")
    public Mono<ResponseEntity<String>> syncLocations() {
        return viettelPostService.syncLocations()
                .then(Mono.just(ResponseEntity.ok("Đồng bộ hóa dữ liệu vị trí thành công.")))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Lỗi trong quá trình đồng bộ hóa: " + e.getMessage())));
    }
}
