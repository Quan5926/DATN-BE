package com.example.th02201.service;

import com.example.th02201.dto.PhieuGiamGiaDTO;
import com.example.th02201.dto.PhieuGiamGiaResponseDTO;
import com.example.th02201.model.*;
import com.example.th02201.repository.*;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PhieuGiamGiaService {
    @Autowired
    private PhieuGiamGiaRepository phieuGiamGiaRepository;
    @Autowired
    private NhanVienRepository nhanVienRepository;
    @Autowired
    private MaGiamGiaRepository maGiamGiaRepository;
    @Autowired
    private KhachHangRepository khachHangRepository;
    @Autowired
    private TrangThaiRepository trangThaiRepository;

    @Transactional(readOnly = true)
    public Page<PhieuGiamGiaResponseDTO> getAll(Pageable pageable) {
        capNhatTrangThaiTungPhieuGiamGia();
        Page<PhieuGiamGia> page = phieuGiamGiaRepository.findAll(pageable);
        return page.map(this::toResponse);
    }

    // ==================== SEARCH ====================
    @Transactional(readOnly = true)
    public Page<PhieuGiamGiaResponseDTO> searchPhieuGiamGia(
            String tenPhieuGiamGia,
            String loaiGiamGia,
            String loaiApDung,
            LocalDateTime ngayBatDau,
            LocalDateTime ngayKetThuc,
            Pageable pageable) {

        Specification<PhieuGiamGia> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (tenPhieuGiamGia != null && !tenPhieuGiamGia.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("tenPhieuGiamGia")),
                        "%" + tenPhieuGiamGia.toLowerCase() + "%"));
            }
            if (loaiGiamGia != null && !loaiGiamGia.isEmpty()) {
                predicates.add(cb.equal(root.get("loaiGiamGia"), loaiGiamGia));
            }
            if (loaiApDung != null && !loaiApDung.isEmpty()) {
                predicates.add(cb.equal(root.get("loaiApDung"), loaiApDung));
            }
            if (ngayBatDau != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("ngayBatDau"), ngayBatDau));
            }
            if (ngayKetThuc != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("ngayKetThuc"), ngayKetThuc));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<PhieuGiamGia> page = phieuGiamGiaRepository.findAll(spec, pageable);
        List<PhieuGiamGiaResponseDTO> responses = page.getContent().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(responses, pageable, page.getTotalElements());
    }

    // ==================== GET ====================
    @Transactional(readOnly = true)
    public PhieuGiamGiaResponseDTO getPhieuGiamGiaById(UUID id) {
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Không tìm thấy phiếu giảm giá với id: " + id));
        return toResponse(phieuGiamGia);
    }

    // PhieuGiamGiaService.java
    @Transactional(readOnly = true)
    public PhieuGiamGiaResponseDTO getPhieuGiamGiaByMa(String maPhieuGiamGia) {
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findByMaPhieuGiamGia(maPhieuGiamGia)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Không tìm thấy phiếu giảm giá với mã: " + maPhieuGiamGia));
        return toResponse(phieuGiamGia);
    }

    // ==================== CREATE ====================
    @Transactional
    public PhieuGiamGiaResponseDTO createPhieuGiamGia(PhieuGiamGiaDTO request) {
        if (request.getMaPhieuGiamGia() == null || request.getMaPhieuGiamGia().isBlank()) {
            request.setMaPhieuGiamGia(generateUniqueVoucherCode());
        }

        validateRequest(request, null);

        PhieuGiamGia phieuGiamGia = new PhieuGiamGia();
        mapRequestToEntity(request, phieuGiamGia);

        NhanVien nhanVien = nhanVienRepository.findById(1)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Không tìm thấy nhân viên tạo mặc định (ID=1)."));
        phieuGiamGia.setNhanVienTao(nhanVien);
        phieuGiamGia.setNgayTao(LocalDateTime.now());

        // Gọi phương thức tự động cập nhật trạng thái trước khi lưu
        tuDongCapNhatTrangThai(phieuGiamGia);

        PhieuGiamGia saved = phieuGiamGiaRepository.save(phieuGiamGia);

        handleCouponCodesCreation(saved, request.getCustomerIds());

        return toResponse(saved);
    }

    // ==================== UPDATE ====================
    @Transactional
    public PhieuGiamGiaResponseDTO updatePhieuGiamGia(UUID id, PhieuGiamGiaDTO request) {
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Không tìm thấy phiếu giảm giá với id: " + id));

        if (request.getMaPhieuGiamGia() == null || request.getMaPhieuGiamGia().isBlank()) {
            request.setMaPhieuGiamGia(generateUniqueVoucherCode());
        }

        validateRequest(request, id);

        mapRequestToEntity(request, phieuGiamGia);

        // Gọi phương thức tự động cập nhật trạng thái trước khi lưu
        tuDongCapNhatTrangThai(phieuGiamGia);

        phieuGiamGia.setNhanVienTao(phieuGiamGia.getNhanVienTao());
        phieuGiamGia.setNgayTao(phieuGiamGia.getNgayTao());

        phieuGiamGia.setNgayCapNhat(LocalDateTime.now());

        PhieuGiamGia updated = phieuGiamGiaRepository.save(phieuGiamGia);
        return toResponse(updated);
    }

    // ==================== DELETE ====================
    @Transactional
    public void deletePhieuGiamGia(UUID id) {
        PhieuGiamGia existing = phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Không tìm thấy phiếu giảm giá với id: " + id));
        maGiamGiaRepository.deleteByPhieuGiamGiaId(id);
        phieuGiamGiaRepository.delete(existing);
    }

    // ==================== HELPER ====================
    private void validateRequest(PhieuGiamGiaDTO req, UUID idUpdate) {
        if (req.getNgayBatDau() == null || req.getNgayKetThuc() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ngày bắt đầu và ngày kết thúc không được để trống");
        }
        if (req.getNgayKetThuc().isBefore(req.getNgayBatDau())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ngày kết thúc không thể trước ngày bắt đầu");
        }
        Optional<PhieuGiamGia> byMa = phieuGiamGiaRepository.findByMaPhieuGiamGia(req.getMaPhieuGiamGia());
        if (byMa.isPresent() && (idUpdate == null || !byMa.get().getId().equals(idUpdate))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Mã phiếu giảm giá đã tồn tại");
        }
        Optional<PhieuGiamGia> byTen = phieuGiamGiaRepository.findByTenPhieuGiamGia(req.getTenPhieuGiamGia());
        if (byTen.isPresent() && (idUpdate == null || !byTen.get().getId().equals(idUpdate))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tên phiếu giảm giá đã tồn tại");
        }
    }

    private String generateUniqueVoucherCode() {
        String prefix = "PGG";
        String unique;
        do {
            unique = prefix + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        } while (phieuGiamGiaRepository.findByMaPhieuGiamGia(unique).isPresent());
        return unique;
    }

    public void mapRequestToEntity(PhieuGiamGiaDTO req, PhieuGiamGia entity) {
        entity.setMaPhieuGiamGia(req.getMaPhieuGiamGia());
        entity.setTenPhieuGiamGia(req.getTenPhieuGiamGia());

        if (req.getLoaiApDung() != null) {
            entity.setLoaiApDung(req.getLoaiApDung().name());
        }
        if (req.getLoaiGiamGia() != null) {
            entity.setLoaiGiamGia(req.getLoaiGiamGia().name());
        }

        if (req.getGiaTriGiam() != null) {
            entity.setGiaTriGiam(req.getGiaTriGiam());
        }
        if (req.getHoaDonToiThieu() != null) {
            entity.setHoaDonToiThieu(req.getHoaDonToiThieu());
        }
        if (req.getSoTienGiamToiDa() != null) {
            entity.setSoTienGiamToiDa(req.getSoTienGiamToiDa());
        }

        if (req.getNgayBatDau() != null) {
            entity.setNgayBatDau(req.getNgayBatDau());
        }
        if (req.getNgayKetThuc() != null) {
            entity.setNgayKetThuc(req.getNgayKetThuc());
        }
    }

    @Transactional
    public void capNhatTrangThaiTungPhieuGiamGia() {
        LocalDateTime now = LocalDateTime.now();
        List<PhieuGiamGia> phieuGiamGiaList = phieuGiamGiaRepository.findAll();
        for (PhieuGiamGia phieuGiamGia : phieuGiamGiaList) {
            if (phieuGiamGia.getNgayBatDau() != null && now.isBefore(phieuGiamGia.getNgayBatDau()) &&
                    !phieuGiamGia.getTrangThai().getTenTrangThai().equals("CHUA_DIEN_RA")) {
                TrangThai trangThai = trangThaiRepository.findByTenTrangThai("CHUA_DIEN_RA")
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                "Không tìm thấy trạng thái CHUA_DIEN_RA"));
                phieuGiamGia.setTrangThai(trangThai);
                phieuGiamGiaRepository.save(phieuGiamGia);
            } else if (phieuGiamGia.getNgayKetThuc() != null && now.isAfter(phieuGiamGia.getNgayKetThuc()) &&
                    !phieuGiamGia.getTrangThai().getTenTrangThai().equals("DA_KET_THUC")) {
                TrangThai trangThai = trangThaiRepository.findByTenTrangThai("DA_KET_THUC")
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                "Không tìm thấy trạng thái DA_KET_THUC"));
                phieuGiamGia.setTrangThai(trangThai);
                phieuGiamGiaRepository.save(phieuGiamGia);
            } else if (phieuGiamGia.getNgayBatDau() != null && phieuGiamGia.getNgayKetThuc() != null &&
                    (now.isAfter(phieuGiamGia.getNgayBatDau()) || now.isEqual(phieuGiamGia.getNgayBatDau())) &&
                    (now.isBefore(phieuGiamGia.getNgayKetThuc()) || now.isEqual(phieuGiamGia.getNgayKetThuc())) &&
                    !phieuGiamGia.getTrangThai().getTenTrangThai().equals("DANG_DIEN_RA")) {
                TrangThai trangThai = trangThaiRepository.findByTenTrangThai("DANG_DIEN_RA")
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                "Không tìm thấy trạng thái DANG_DIEN_RA"));
                phieuGiamGia.setTrangThai(trangThai);
                phieuGiamGiaRepository.save(phieuGiamGia);
            }
        }
    }

    public void tuDongCapNhatTrangThai(PhieuGiamGia phieuGiamGia) {
        LocalDateTime now = LocalDateTime.now();
        Integer trangThaiId;

        if (phieuGiamGia.getNgayBatDau() != null && now.isBefore(phieuGiamGia.getNgayBatDau())) {
            trangThaiId = 14;
        } else if (phieuGiamGia.getNgayKetThuc() != null && now.isAfter(phieuGiamGia.getNgayKetThuc())) {
            trangThaiId = 16;
        } else {
            trangThaiId = 15;
        }

        TrangThai trangThai = trangThaiRepository.findById(trangThaiId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Không tìm thấy ID trạng thái: " + trangThaiId));

        phieuGiamGia.setTrangThai(trangThai);
    }



    private void handleCouponCodesCreation(PhieuGiamGia phieu, List<Integer> customerIds) {
        maGiamGiaRepository.deleteByPhieuGiamGiaId(phieu.getId());

        List<MaGiamGia> maGiamGiaList = new ArrayList<>();

        if ("KH_CU_THE".equals(phieu.getLoaiApDung())) {
            if (customerIds == null || customerIds.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Phải cung cấp danh sách khách hàng khi loại áp dụng = KH_CU_THE");
            }
            for (Integer customerId : customerIds) {
                KhachHang kh = khachHangRepository.findById(customerId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Khách hàng với ID " + customerId + " không tồn tại"));

                MaGiamGia ma = new MaGiamGia();
                ma.setPhieuGiamGia(phieu);
                ma.setMaCode(generateUniqueCode(phieu.getMaPhieuGiamGia()));
                ma.setDaSuDung(false);
                ma.setKhachHangDuocCap(kh);
                ma.setNgayTao(LocalDateTime.now());
                maGiamGiaList.add(ma);
            }
        } else if ("TOAN_BO".equals(phieu.getLoaiApDung())) {
            MaGiamGia ma = new MaGiamGia();
            ma.setPhieuGiamGia(phieu);
            ma.setMaCode(generateUniqueCode(phieu.getMaPhieuGiamGia()));
            ma.setDaSuDung(false);
            ma.setKhachHangDuocCap(null);
            ma.setNgayTao(LocalDateTime.now());
            maGiamGiaList.add(ma);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Loại áp dụng không hợp lệ.");
        }

        if (!maGiamGiaList.isEmpty()) {
            maGiamGiaRepository.saveAll(maGiamGiaList);
        }
    }

    private String generateUniqueCode(String baseCode) {
        String code = (baseCode == null || baseCode.isBlank()) ? "PGG" : baseCode;
        String unique;
        do {
            unique = code + "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        } while (maGiamGiaRepository.findByMaCode(unique).isPresent());
        return unique;
    }



    private PhieuGiamGiaResponseDTO toResponse(PhieuGiamGia e) {
        PhieuGiamGiaResponseDTO r = new PhieuGiamGiaResponseDTO();
        r.setId(e.getId());
        r.setMaPhieuGiamGia(e.getMaPhieuGiamGia());
        r.setTenPhieuGiamGia(e.getTenPhieuGiamGia());
        r.setLoaiGiamGia(e.getLoaiGiamGia());
        r.setGiaTriGiam(e.getGiaTriGiam());
        r.setHoaDonToiThieu(e.getHoaDonToiThieu());
        r.setSoTienGiamToiDa(e.getSoTienGiamToiDa());
        r.setNgayBatDau(e.getNgayBatDau());
        r.setNgayKetThuc(e.getNgayKetThuc());
        r.setLoaiApDung(e.getLoaiApDung());
        r.setNgayTao(e.getNgayTao());
        r.setNgayCapNhat(e.getNgayCapNhat());

        if (e.getTrangThai() != null) {
            r.setTenTrangThai(e.getTrangThai().getTenTrangThai());
        }

        if (e.getNhanVienTao() != null) {
            r.setIdNhanVienTao(e.getNhanVienTao().getId());
            r.setTenNhanVienTao(e.getNhanVienTao().getTenNhanVien());
        }
        return r;
    }
}