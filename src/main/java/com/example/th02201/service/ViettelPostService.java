package com.example.th02201.service;

import com.example.th02201.dto.ViettelPostDTO;
import com.example.th02201.model.PhuongXa;
import com.example.th02201.model.QuanHuyen;
import com.example.th02201.model.TinhThanh;
import com.example.th02201.repository.PhuongXaRepository;
import com.example.th02201.repository.QuanHuyenRepository;
import com.example.th02201.repository.TinhThanhRepository;
import com.example.th02201.util.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Service để tích hợp và gọi các API của Viettel Post.
 */
@Service
public class ViettelPostService {

    private static final Logger logger = LoggerFactory.getLogger(ViettelPostService.class);

    @Value("${viettelpost.api.username}")
    private String username;

    @Value("${viettelpost.api.password}")
    private String password;

    @Value("${viettelpost.login.url}")
    private String loginUrl;

    private final WebClient webClient;
    private final Map<String, String> tokenCache = new ConcurrentHashMap<>();
    private final Map<String, Instant> tokenExpiry = new ConcurrentHashMap<>();

    private final TinhThanhRepository tinhThanhRepository;
    private final QuanHuyenRepository quanHuyenRepository;
    private final PhuongXaRepository phuongXaRepository;

    public ViettelPostService(WebClient.Builder webClientBuilder, TinhThanhRepository tinhThanhRepository, QuanHuyenRepository quanHuyenRepository, PhuongXaRepository phuongXaRepository) {
        this.webClient = webClientBuilder.baseUrl("https://partner.viettelpost.vn/v2").build();
        this.tinhThanhRepository = tinhThanhRepository;
        this.quanHuyenRepository = quanHuyenRepository;
        this.phuongXaRepository = phuongXaRepository;
    }

    /**
     * Lấy token từ Viettel Post và lưu vào cache.
     * @return Mono<String> - Token xác thực
     */
    private Mono<String> getAuthToken() {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            logger.error("Username or password not found in application.properties. Please check your configuration.");
            return Mono.error(new ApiException("Username and password properties are not configured correctly."));
        }

        String token = tokenCache.get("token");
        Instant expiry = tokenExpiry.get("token");

        if (token != null && expiry != null && Instant.now().isBefore(expiry.minusSeconds(300))) {
            return Mono.just(token);
        }

        logger.info("Token Viettel Post hết hạn hoặc không tồn tại. Đang lấy token mới...");
        logger.debug("Attempting to get token with username: {} and password: {}", username, password);

        return webClient.post()
                .uri("/user/Login")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(Map.of("USERNAME", username, "PASSWORD", password)))
                .retrieve()
                .bodyToMono(ViettelPostDTO.ViettelPostLoginResponse.class)
                .map(response -> {
                    if (response.isError() || response.getData() == null || response.getData().getToken() == null) {
                        logger.error("Lỗi đăng nhập: Tên đăng nhập hoặc mật khẩu không đúng.");
                        throw new ApiException("Failed to get auth token from Viettel Post API: " + response.getMessage());
                    }
                    String newToken = response.getData().getToken();
                    tokenCache.put("token", newToken);
                    tokenExpiry.put("token", Instant.now().plusSeconds(1800));
                    logger.info("Đã lấy token mới thành công.");
                    return newToken;
                })
                .doOnError(e -> logger.error("Lỗi khi lấy token Viettel Post: " + e.getMessage()));
    }

    /**
     * Lấy danh sách tỉnh/thành.
     * @return Mono<List<ViettelPostDTO.Province>> - Danh sách tỉnh/thành
     */
    public Mono<List<ViettelPostDTO.Province>> getProvinces() {
        return getAuthToken().flatMap(token ->
                webClient.get()
                        .uri("/categories/listProvinceById?provinceId=-1")
                        .header("token", token)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<ViettelPostDTO.ViettelPostResponse<List<ViettelPostDTO.Province>>>() {})
                        .map(response -> {
                            if (response.isError()) {
                                throw new ApiException("Failed to get provinces from Viettel Post API: " + response.getMessage());
                            }
                            return response.getData();
                        })
                        .doOnError(e -> logger.error("Lỗi khi lấy danh sách tỉnh/thành: " + e.getMessage()))
        );
    }

    /**
     * Lấy danh sách quận/huyện theo tỉnh.
     * @param provinceId ID của tỉnh/thành
     * @return Mono<List<ViettelPostDTO.District>> - Danh sách quận/huyện
     */
    public Mono<List<ViettelPostDTO.District>> getDistricts(Integer provinceId) {
        return getAuthToken().flatMap(token ->
                webClient.get()
                        .uri("/categories/listDistrict?provinceId=" + provinceId)
                        .header("token", token)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<ViettelPostDTO.ViettelPostResponse<List<ViettelPostDTO.District>>>() {})
                        .map(response -> {
                            if (response.isError()) {
                                throw new ApiException("Failed to get districts from Viettel Post API: " + response.getMessage());
                            }
                            return response.getData();
                        })
                        .doOnError(e -> logger.error("Lỗi khi lấy danh sách quận/huyện: " + e.getMessage()))
        );
    }

    /**
     * Lấy danh sách phường/xã theo huyện.
     * @param districtId ID của quận/huyện
     * @return Mono<List<ViettelPostDTO.Ward>> - Danh sách phường/xã
     */
    public Mono<List<ViettelPostDTO.Ward>> getWards(Integer districtId) {
        return getAuthToken().flatMap(token ->
                webClient.get()
                        .uri("/categories/listWards?districtId=" + districtId)
                        .header("token", token)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<ViettelPostDTO.ViettelPostResponse<List<ViettelPostDTO.Ward>>>() {})
                        .map(response -> {
                            if (response.isError()) {
                                throw new ApiException("Failed to get wards from Viettel Post API: " + response.getMessage());
                            }
                            return response.getData();
                        })
                        .doOnError(e -> logger.error("Lỗi khi lấy danh sách phường/xã: " + e.getMessage()))
        );
    }

    /**
     * Đồng bộ hóa dữ liệu vị trí từ Viettel Post vào cơ sở dữ liệu.
     * Cải thiện để chạy song song.
     * @return Mono<Void>
     */
    public Mono<Void> syncLocations() {
        logger.info("Bắt đầu quá trình đồng bộ hóa dữ liệu vị trí từ Viettel Post.");
        return getProvinces()
                .flatMapMany(Flux::fromIterable)
                .parallel() // Bắt đầu luồng xử lý song song
                .runOn(Schedulers.boundedElastic()) // Chạy trên pool thread phù hợp cho các tác vụ blocking I/O
                .flatMap(provinceDTO ->
                        Mono.fromCallable(() -> {
                            TinhThanh tinhThanh = new TinhThanh();
                            tinhThanh.setTenTinhThanh(provinceDTO.getProvinceName());
                            tinhThanh.setMaVung(provinceDTO.getProvinceCode());
                            return tinhThanhRepository.save(tinhThanh);
                        }).flatMapMany(savedTinhThanh -> getDistricts(provinceDTO.getProvinceId())
                                .flatMapMany(Flux::fromIterable)
                                .flatMap(districtDTO -> Mono.fromCallable(() -> {
                                    QuanHuyen quanHuyen = new QuanHuyen();
                                    quanHuyen.setTinhThanh(savedTinhThanh);
                                    quanHuyen.setTenQuanHuyen(districtDTO.getDistrictName());
                                    quanHuyen.setMaHuyen(districtDTO.getDistrictCode());
                                    return quanHuyenRepository.save(quanHuyen);
                                }).flatMap(savedQuanHuyen -> getWards(districtDTO.getDistrictId())
                                        .flatMap(wards -> {
                                            List<PhuongXa> phuongXas = wards.stream()
                                                    .map(wardDTO -> {
                                                        PhuongXa phuongXa = new PhuongXa();
                                                        phuongXa.setQuanHuyen(savedQuanHuyen);
                                                        phuongXa.setTenPhuongXa(wardDTO.getWardsName());
                                                        phuongXa.setMaXa(wardDTO.getWardsCode());
                                                        return phuongXa;
                                                    }).collect(Collectors.toList());
                                            phuongXaRepository.saveAll(phuongXas);
                                            return Mono.empty();
                                        })
                                        .doOnError(e -> logger.error("Lỗi khi đồng bộ hóa phường/xã cho huyện {}: {}", districtDTO.getDistrictName(), e.getMessage()))
                                ))
                                .doOnError(e -> logger.error("Lỗi khi đồng bộ hóa quận/huyện cho tỉnh {}: {}", provinceDTO.getProvinceName(), e.getMessage()))
                        ))
                .sequential() // Trở lại luồng tuần tự để kết thúc
                .then()
                .doOnSuccess(v -> logger.info("Đồng bộ hóa dữ liệu vị trí hoàn tất."))
                .doOnError(e -> logger.error("Quá trình đồng bộ hóa bị lỗi tổng thể: {}", e.getMessage()));
    }
}
