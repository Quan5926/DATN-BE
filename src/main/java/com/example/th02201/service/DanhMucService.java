package com.example.th02201.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.th02201.repository.*;
import com.example.th02201.dto.*;
import com.example.th02201.model.*;
import com.example.th02201.util.NotFoundException;
import com.example.th02201.util.ReferencedWarning;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DanhMucService {

    private final DanhMucRepository danhMucRepository;
    private final SanPhamRepository sanPhamRepository; // Assuming this repository exists and is correctly typed

    public DanhMucService(DanhMucRepository danhMucRepository, SanPhamRepository sanPhamRepository) {
        this.danhMucRepository = danhMucRepository;
        this.sanPhamRepository = sanPhamRepository;
    }

    public List<DanhMucDTO> findAll() {
        // Đã sửa đổi để sử dụng phương thức findAllOrderedByName() từ repository
        // để lấy danh sách danh mục đã sắp xếp theo tên, phục vụ cho combobox
        List<DanhMuc> danhMucs = danhMucRepository.findAllOrderedByName();
        return danhMucs.stream()
                .map(danhMuc -> mapToDTO(danhMuc, new DanhMucDTO()))
                .toList();
    }

    public DanhMucDTO get(Long id) {
        return danhMucRepository.findById(id)
                .map(danhMuc -> mapToDTO(danhMuc, new DanhMucDTO()))
                .orElseThrow(() -> new NotFoundException("Danh mục không tồn tại"));
    }

    @Transactional
    public Long create(DanhMucDTO danhMucDTO) {
        DanhMuc danhMuc = new DanhMuc();
        mapToEntity(danhMucDTO, danhMuc);
        return danhMucRepository.save(danhMuc).getId();
    }

    @Transactional
    public void update(Long id, DanhMucDTO danhMucDTO) {
        DanhMuc danhMuc = danhMucRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Danh mục không tồn tại"));
        mapToEntity(danhMucDTO, danhMuc);
        danhMuc.setNgayCapNhat(LocalDateTime.now());
        danhMucRepository.save(danhMuc);
    }

    @Transactional
    public void delete(Long id) {
        danhMucRepository.deleteById(id);
    }

    private DanhMucDTO mapToDTO(DanhMuc danhMuc, DanhMucDTO danhMucDTO) {
        danhMucDTO.setId(danhMuc.getId());
        danhMucDTO.setTenDanhMuc(danhMuc.getTenDanhMuc());
        danhMucDTO.setMaDanhMuc(danhMuc.getMaDanhMuc());

        // Tính toán và thiết lập soLuongSanPham
        Integer totalQuantity = danhMucRepository.sumSoLuongSanPhamByDanhMucId(danhMuc.getId());
        danhMucDTO.setSoLuongSanPham(totalQuantity != null ? totalQuantity : 0);
        danhMucDTO.setNgayTao(danhMuc.getNgayTao());
        danhMucDTO.setNgayCapNhat(danhMuc.getNgayCapNhat());


        return danhMucDTO;
    }

    private DanhMuc mapToEntity(DanhMucDTO danhMucDTO, DanhMuc danhMuc) {
        danhMuc.setTenDanhMuc(danhMucDTO.getTenDanhMuc());
        danhMuc.setMaDanhMuc(danhMucDTO.getMaDanhMuc());
        // Không ánh xạ soLuongSanPham từ DTO sang Entity
        return danhMuc;
    }

    public ReferencedWarning getReferencedWarning(Long id) {
        ReferencedWarning referencedWarning = new ReferencedWarning();
        Optional<DanhMuc> danhMucOptional = danhMucRepository.findById(id);
        if (danhMucOptional.isEmpty()) {
            throw new NotFoundException("Danh mục không tồn tại");
        }
        DanhMuc danhMuc = danhMucOptional.get();

        SanPham sanPham = sanPhamRepository.findFirstByDanhMuc(danhMuc);
        if (sanPham != null) {
            referencedWarning.setKey("danhMuc.sanPham.danhMuc.referenced");
            referencedWarning.addParam(sanPham.getId());
            return referencedWarning;
        }
        return null;
    }
}
