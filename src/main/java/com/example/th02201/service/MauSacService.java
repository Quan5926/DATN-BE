package com.example.th02201.service;

import org.springframework.stereotype.Service;
import com.example.th02201.repository.*;
import com.example.th02201.dto.*;
import com.example.th02201.model.*;
import com.example.th02201.util.NotFoundException;
import com.example.th02201.util.ReferencedException;
import com.example.th02201.util.ReferencedWarning;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MauSacService {

    private final MauSacRepository mauSacRepository;
    private final ChiTietSanPhamRepository chiTietSanPhamRepository; // Thêm repository này

    public MauSacService(final MauSacRepository mauSacRepository,
                         final ChiTietSanPhamRepository chiTietSanPhamRepository) {
        this.mauSacRepository = mauSacRepository;
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
    }

    // Phương thức tạo màu sắc mới
    public Long create(MauSacDTO mauSacDTO) {
        if (mauSacRepository.existsByMaMauSac(mauSacDTO.getMaMauSac())) {
            throw new IllegalArgumentException("Mã màu sắc '" + mauSacDTO.getMaMauSac() + "' đã tồn tại.");
        }

        MauSac mauSac = new MauSac();
        mapToEntity(mauSacDTO, mauSac);
        mauSac.setNgayTao(LocalDateTime.now());
        mauSac.setNgayCapNhat(LocalDateTime.now());

        return mauSacRepository.save(mauSac).getId();
    }

    // Phương thức lấy tất cả màu sắc (để hiển thị danh sách)
    public List<MauSacDTO> findAll() {
        return mauSacRepository.findAllByOrderByTenMauSacAsc()
                .stream()
                .map(mauSac -> mapToDTO(mauSac, new MauSacDTO()))
                .collect(Collectors.toList());
    }

    public MauSacDTO findById(Long id) {
        return mauSacRepository.findById(id)
                .map(mauSac -> mapToDTO(mauSac, new MauSacDTO()))
                .orElse(null);
    }

    public void update(Long id, MauSacDTO mauSacDTO) {
        MauSac existingMauSac = mauSacRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Màu sắc với ID " + id + " không tồn tại."));

        if (!existingMauSac.getMaMauSac().equals(mauSacDTO.getMaMauSac()) &&
                mauSacRepository.existsByMaMauSac(mauSacDTO.getMaMauSac())) {
            throw new IllegalArgumentException("Mã màu sắc '" + mauSacDTO.getMaMauSac() + "' đã tồn tại.");
        }

        mapToEntity(mauSacDTO, existingMauSac);
        existingMauSac.setNgayCapNhat(LocalDateTime.now());
        mauSacRepository.save(existingMauSac);
    }

    public void delete(final Long id) {
        // Kiểm tra ràng buộc khóa ngoại trước khi xóa
        if (getReferencedWarning(id) != null) {
            throw new ReferencedException();
        }
        mauSacRepository.deleteById(id);
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Optional<MauSac> mauSacOptional = mauSacRepository.findById(id);
        if (mauSacOptional.isEmpty()) {
            throw new NotFoundException();
        }
        final MauSac mauSac = mauSacOptional.get();

        final ChiTietSanPham mauSacChiTietSanPham = chiTietSanPhamRepository.findFirstByMauSac(mauSac);
        if (mauSacChiTietSanPham != null) {
            referencedWarning.addWarning("Màu sắc này đang được sử dụng bởi sản phẩm và không thể xóa.", 1);
            return referencedWarning;
        }
        return null;
    }

    private MauSacDTO mapToDTO(final MauSac mauSac, final MauSacDTO mauSacDTO) {
        mauSacDTO.setId(mauSac.getId());
        mauSacDTO.setTenMauSac(mauSac.getTenMauSac());
        mauSacDTO.setMaMauSac(mauSac.getMaMauSac());
        mauSacDTO.setHex(mauSac.getHex());
        mauSacDTO.setNgayTao(mauSac.getNgayTao());
        mauSacDTO.setNgayCapNhat(mauSac.getNgayCapNhat());

        // Tính toán và thiết lập soLuongSanPham
        Integer totalQuantity = mauSacRepository.sumSoLuongSanPhamByMauSacId(mauSac.getId());
        mauSacDTO.setSoLuongSanPham(totalQuantity != null ? totalQuantity : 0);

        return mauSacDTO;
    }

    private MauSac mapToEntity(final MauSacDTO mauSacDTO, final MauSac mauSac) {
        mauSac.setTenMauSac(mauSacDTO.getTenMauSac());
        mauSac.setMaMauSac(mauSacDTO.getMaMauSac());
        mauSac.setHex(mauSacDTO.getHex());
        return mauSac;
    }
}