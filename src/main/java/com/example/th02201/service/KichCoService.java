package com.example.th02201.service;

import org.springframework.stereotype.Service;
import com.example.th02201.repository.*;
import com.example.th02201.dto.*;
import com.example.th02201.model.*;
import com.example.th02201.util.NotFoundException;
import com.example.th02201.util.ReferencedWarning;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class KichCoService {

    private final KichCoRepository kichCoRepository;
    private final ChiTietSanPhamRepository chiTietSanPhamRepository; // Assuming this repository exists and is correctly typed

    public KichCoService(final KichCoRepository kichCoRepository,
                         final ChiTietSanPhamRepository chiTietSanPhamRepository) {
        this.kichCoRepository = kichCoRepository;
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
    }

    public List<KichCoDTO> findAll() {
        return kichCoRepository.findAllOrderedByName()
                .stream()
                .map(kichCo -> mapToDTO(kichCo, new KichCoDTO()))
                .collect(Collectors.toList());
    }

    public KichCoDTO get(final Long id) {
        return kichCoRepository.findById(id)
                .map(kichCo -> mapToDTO(kichCo, new KichCoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final KichCoDTO kichCoDTO) {
        // 1. Kiểm tra tính duy nhất của maKichCo
        if (kichCoRepository.existsByMaKichCo(kichCoDTO.getMaKichCo())) {
            throw new IllegalArgumentException("Mã kích cỡ '" + kichCoDTO.getMaKichCo() + "' đã tồn tại.");
        }
        // 2. Tạo đối tượng KichCo và gán giá trị từ DTO
        KichCo kichCo = new KichCo();
        mapToEntity(kichCoDTO, kichCo); // Đây là nơi tenKichCo và maKichCo được gán
        kichCo.setNgayTao(java.time.LocalDateTime.now());
        kichCoRepository.save(kichCo);
        return kichCo.getId();
    }

    public void update(final Long id, final KichCoDTO kichCoDTO) {
        final KichCo kichCo = kichCoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        if (!kichCo.getMaKichCo().equals(kichCoDTO.getMaKichCo()) &&
                kichCoRepository.existsByMaKichCo(kichCoDTO.getMaKichCo())) {
            throw new IllegalArgumentException("Mã kích cỡ '" + kichCoDTO.getMaKichCo() + "' đã tồn tại.");
        }
        mapToEntity(kichCoDTO, kichCo);
        kichCo.setNgayCapNhat(java.time.LocalDateTime.now());
        kichCoRepository.save(kichCo);
    }

    public void delete(final Long id) {
        kichCoRepository.deleteById(id);
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Optional<KichCo> kichCoOptional = kichCoRepository.findById(id);
        if (kichCoOptional.isEmpty()) {
            throw new NotFoundException();
        }
        final KichCo kichCo = kichCoOptional.get();

        final ChiTietSanPham kichCoChiTietSanPham = chiTietSanPhamRepository.findFirstByKichCo(kichCo);
        if (kichCoChiTietSanPham != null) {
            referencedWarning.addWarning("Kích cỡ này đang được sử dụng bởi sản phẩm và không thể xóa.", 1);
            return referencedWarning;
        }
        return null;
    }

    private KichCoDTO mapToDTO(final KichCo kichCo, final KichCoDTO kichCoDTO) {
        kichCoDTO.setId(kichCo.getId());
        kichCoDTO.setTenKichCo(kichCo.getTenKichCo());
        kichCoDTO.setMaKichCo(kichCo.getMaKichCo());
        kichCoDTO.setNgayTao(kichCo.getNgayTao());
        kichCoDTO.setNgayCapNhat(kichCo.getNgayCapNhat());

        // Tính toán và thiết lập soLuongSanPham
        Integer totalQuantity = kichCoRepository.sumSoLuongSanPhamByKichCoId(kichCo.getId());
        kichCoDTO.setSoLuongSanPham(totalQuantity != null ? totalQuantity : 0);

        return kichCoDTO;
    }

    private KichCo mapToEntity(final KichCoDTO kichCoDTO, final KichCo kichCo) {
        kichCo.setTenKichCo(kichCoDTO.getTenKichCo());
        kichCo.setMaKichCo(kichCoDTO.getMaKichCo());
        // Không ánh xạ soLuongSanPham từ DTO sang Entity
        return kichCo;
    }
}
