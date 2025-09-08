package com.example.th02201.service;

import com.example.th02201.dto.AnhSanPhamDTO;
import com.example.th02201.model.AnhSanPham;
import com.example.th02201.model.ChiTietSanPham;
import com.example.th02201.repository.AnhSanPhamRepository;
import com.example.th02201.repository.ChiTietSanPhamRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AnhSanPhamService {

    private final AnhSanPhamRepository anhSanPhamRepository;
    private final ChiTietSanPhamRepository chiTietSanPhamRepository;

    private final String UPLOAD_DIR = "uploads/images/";

    public AnhSanPhamService(AnhSanPhamRepository anhSanPhamRepository, ChiTietSanPhamRepository chiTietSanPhamRepository) {
        this.anhSanPhamRepository = anhSanPhamRepository;
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory!", e);
        }
    }

    // Lấy tất cả ảnh theo chi tiết sản phẩm
    public List<AnhSanPhamDTO> getImagesByChiTietSanPhamId(UUID chiTietSpId) {
        return anhSanPhamRepository.findByChiTietSp_Id(chiTietSpId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Upload ảnh từ file
    @Transactional
    public AnhSanPhamDTO uploadImage(MultipartFile file, UUID chiTietSpId, Boolean laAnhDaiDien) throws IOException {
        ChiTietSanPham chiTietSanPham = chiTietSanPhamRepository.findById(chiTietSpId)
                .orElseThrow(() -> new EntityNotFoundException("Chi tiết sản phẩm không tồn tại với ID: " + chiTietSpId));

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName).normalize();
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        String imageUrl = "/images/" + fileName;

        laAnhDaiDien = handleLaAnhDaiDien(chiTietSpId, laAnhDaiDien);

        AnhSanPham anhSanPham = AnhSanPham.builder()
                .urlAnh(imageUrl)
                .laAnhDaiDien(laAnhDaiDien)
                .chiTietSp(chiTietSanPham)
                .ngayTao(LocalDateTime.now())
                .ngayCapNhat(LocalDateTime.now())
                .build();

        return mapToDTO(anhSanPhamRepository.save(anhSanPham));
    }

    // Thêm ảnh từ URL
    @Transactional
    public AnhSanPhamDTO addImageFromUrl(AnhSanPhamDTO dto) {
        ChiTietSanPham chiTietSanPham = chiTietSanPhamRepository.findById(dto.getChiTietSpId())
                .orElseThrow(() -> new EntityNotFoundException("Chi tiết sản phẩm không tồn tại với ID: " + dto.getChiTietSpId()));

        dto.setLaAnhDaiDien(handleLaAnhDaiDien(dto.getChiTietSpId(), dto.getLaAnhDaiDien()));

        AnhSanPham anhSanPham = AnhSanPham.builder()
                .urlAnh(dto.getUrlAnh())
                .laAnhDaiDien(dto.getLaAnhDaiDien())
                .chiTietSp(chiTietSanPham)
                .ngayTao(LocalDateTime.now())
                .ngayCapNhat(LocalDateTime.now())
                .build();

        return mapToDTO(anhSanPhamRepository.save(anhSanPham));
    }

    // Cập nhật ảnh
    @Transactional
    public AnhSanPhamDTO updateImage(Long id, AnhSanPhamDTO dto) {
        AnhSanPham existingImage = anhSanPhamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ảnh sản phẩm không tồn tại với ID: " + id));

        ChiTietSanPham chiTietSanPham = chiTietSanPhamRepository.findById(dto.getChiTietSpId())
                .orElseThrow(() -> new EntityNotFoundException("Chi tiết sản phẩm không tồn tại với ID: " + dto.getChiTietSpId()));

        if (dto.getLaAnhDaiDien()) {
            anhSanPhamRepository.resetLaAnhDaiDienForChiTietSanPham(chiTietSanPham.getId());
        }

        existingImage.setUrlAnh(dto.getUrlAnh());
        existingImage.setLaAnhDaiDien(dto.getLaAnhDaiDien());
        existingImage.setChiTietSp(chiTietSanPham);
        existingImage.setNgayCapNhat(LocalDateTime.now());

        return mapToDTO(anhSanPhamRepository.save(existingImage));
    }

    // Xóa ảnh
    @Transactional
    public void deleteImage(Long id) {
        AnhSanPham anhSanPham = anhSanPhamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ảnh sản phẩm không tồn tại với ID: " + id));

        if (anhSanPham.getUrlAnh().startsWith("/images/")) {
            try {
                Path filePath = Paths.get(UPLOAD_DIR).resolve(
                        anhSanPham.getUrlAnh().substring("/images/".length())).normalize();
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                System.err.println("Could not delete image file: " + anhSanPham.getUrlAnh() + " - " + e.getMessage());
            }
        }

        anhSanPhamRepository.delete(anhSanPham);

        if (anhSanPham.getLaAnhDaiDien()) {
            List<AnhSanPham> remainingImages = anhSanPhamRepository.findByChiTietSp_Id(anhSanPham.getChiTietSp().getId());
            if (!remainingImages.isEmpty()) {
                AnhSanPham firstRemaining = remainingImages.get(0);
                firstRemaining.setLaAnhDaiDien(true);
                anhSanPhamRepository.save(firstRemaining);
            }
        }
    }

    // Helper xử lý laAnhDaiDien
    private boolean handleLaAnhDaiDien(UUID chiTietSpId, Boolean laAnhDaiDien) {
        if (Boolean.TRUE.equals(laAnhDaiDien)) {
            anhSanPhamRepository.resetLaAnhDaiDienForChiTietSanPham(chiTietSpId);
            return true;
        }
        return anhSanPhamRepository.findByChiTietSp_IdAndLaAnhDaiDienIsTrue(chiTietSpId).isPresent()
                ? laAnhDaiDien
                : true; // nếu chưa có ảnh nào đại diện thì ép ảnh mới thành đại diện
    }

    // Mapper
    private AnhSanPhamDTO mapToDTO(AnhSanPham anhSanPham) {
        return AnhSanPhamDTO.builder()
                .id(anhSanPham.getId())
                .urlAnh(anhSanPham.getUrlAnh())
                .laAnhDaiDien(anhSanPham.getLaAnhDaiDien())
                .chiTietSpId(anhSanPham.getChiTietSp().getId())
                .ngayTao(anhSanPham.getNgayTao())
                .ngayCapNhat(anhSanPham.getNgayCapNhat())
                .build();
    }
}
