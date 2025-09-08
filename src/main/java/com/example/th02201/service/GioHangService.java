// File: GioHangService.java
package com.example.th02201.service;

import com.example.th02201.dto.GioHangChiTietDTO;
import com.example.th02201.dto.GioHangDTO;
import com.example.th02201.model.*;
import com.example.th02201.repository.*;
import com.example.th02201.util.NotFoundException;
import com.example.th02201.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class GioHangService {

    private final GioHangRepository gioHangRepository;
    private final KhachHangRepository khachHangRepository;
    private final GioHangChiTietRepository gioHangChiTietRepository;
    // Thêm repository cho ChiTietSanPham để lấy giá bán
    private final ChiTietSanPhamRepository chiTietSanPhamRepository;
    private final TrangThaiRepository trangThaiRepository;

    public GioHangService(final GioHangRepository gioHangRepository,
                          final KhachHangRepository khachHangRepository,
                          final GioHangChiTietRepository gioHangChiTietRepository,
                          final ChiTietSanPhamRepository chiTietSanPhamRepository,
                          final TrangThaiRepository trangThaiRepository) {
        this.gioHangRepository = gioHangRepository;
        this.khachHangRepository = khachHangRepository;
        this.gioHangChiTietRepository = gioHangChiTietRepository;
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
        this.trangThaiRepository = trangThaiRepository;
    }

    public List<GioHangDTO> findAll() {
        final List<GioHang> gioHangs = gioHangRepository.findAll(Sort.by("id"));
        return gioHangs.stream()
                .map(this::mapToDto) // Corrected to use method reference
                .collect(Collectors.toList());
    }

    public GioHangDTO get(final UUID id) {
        return gioHangRepository.findById(id)
                .map(this::mapToDto) // Corrected to use method reference
                .orElseThrow(NotFoundException::new);
    }

    // Tạo mới một giỏ hàng
    public UUID create(final GioHangDTO gioHangDTO) {
        final GioHang gioHang = new GioHang();
        mapToEntity(gioHangDTO, gioHang);
        return gioHangRepository.save(gioHang).getId();
    }

    // Updated createFullCart method
    @Transactional
    public UUID createFullCart(final GioHangDTO gioHangDTO) {
        // Find existing cart by maPhienGioHang or create a new one
        final Optional<GioHang> existingGioHang = gioHangRepository.findByMaPhienGioHang(gioHangDTO.getMaPhienGioHang());
        final GioHang gioHang = existingGioHang.orElseGet(() -> {
            final GioHang newGioHang = new GioHang();
            mapToEntity(gioHangDTO, newGioHang);
            newGioHang.setNgayTao(LocalDateTime.now());
            newGioHang.setNgayCapNhat(LocalDateTime.now());
            // Set default status if not provided, for example, "Active"
            final TrangThai activeStatus = trangThaiRepository.findById(1).orElseThrow(() -> new NotFoundException("Active Status not found"));
            newGioHang.setTrangThai(activeStatus);
            return gioHangRepository.save(newGioHang);
        });

        // Loop through each cart detail from DTO
        for (final GioHangChiTietDTO gioHangChiTietDTO : gioHangDTO.getGioHangChiTiets()) {
            // Find ChiTietSanPham by its maCtsp. This is the crucial step.
            final ChiTietSanPham chiTietSp = chiTietSanPhamRepository.findByMaCtsp(gioHangChiTietDTO.getMaCtsp());
            if (chiTietSp == null) {
                throw new NotFoundException("ChiTietSanPham with maCtsp " + gioHangChiTietDTO.getMaCtsp() + " not found");
            }

            // Check if cart detail already exists for this product in this cart
            Optional<GioHangChiTiet> existingGioHangChiTiet = gioHangChiTietRepository.findByGioHangAndChiTietSp(gioHang, chiTietSp);

            if (existingGioHangChiTiet.isPresent()) {
                // Update existing cart detail
                GioHangChiTiet detail = existingGioHangChiTiet.get();
                detail.setSoLuong(detail.getSoLuong() + gioHangChiTietDTO.getSoLuong());
                detail.setGiaBanHienTai(chiTietSp.getGiaBan()); // Update price just in case
                gioHangChiTietRepository.save(detail);
            } else {
                // Create a new cart detail
                final GioHangChiTiet gioHangChiTiet = new GioHangChiTiet();
                gioHangChiTiet.setGioHang(gioHang);
                gioHangChiTiet.setChiTietSp(chiTietSp);
                gioHangChiTiet.setSoLuong(gioHangChiTietDTO.getSoLuong());
                gioHangChiTiet.setGiaBanHienTai(chiTietSp.getGiaBan());
                gioHangChiTiet.setNgayThemVao(LocalDateTime.now());
                gioHangChiTietRepository.save(gioHangChiTiet);
            }
        }

        // Return the cart ID
        return gioHang.getId();
    }

    public void update(final UUID id, final GioHangDTO gioHangDTO) {
        final GioHang gioHang = gioHangRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(gioHangDTO, gioHang);
        gioHangRepository.save(gioHang);
    }

    // Logic xóa đã được cập nhật để đảm bảo xóa sạch các chi tiết giỏ hàng
    @Transactional
    public void delete(final UUID id) {
        final GioHang gioHang = gioHangRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        // Xóa tất cả các GioHangChiTiet liên quan đến giỏ hàng này
        gioHangChiTietRepository.deleteAll(gioHang.getGioHangChiTiets());

        // Sau đó xóa chính giỏ hàng
        gioHangRepository.delete(gioHang);
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final GioHang gioHang = gioHangRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final GioHangChiTiet gioHangGioHangChiTiet = gioHangChiTietRepository.findFirstByGioHang(gioHang);
        if (gioHangGioHangChiTiet != null) {
            referencedWarning.setKey("gioHang.gioHangChiTiet.gioHang.referenced");
            referencedWarning.addParam(gioHangGioHangChiTiet.getId());
            return referencedWarning;
        }
        return null;
    }

    private GioHangDTO mapToDto(final GioHang gioHang) {
        final GioHangDTO gioHangDTO = new GioHangDTO();
        gioHangDTO.setId(gioHang.getId());
        gioHangDTO.setMaPhienGioHang(gioHang.getMaPhienGioHang());
        gioHangDTO.setNgayTao(gioHang.getNgayTao());
        gioHangDTO.setNgayCapNhat(gioHang.getNgayCapNhat());
        gioHangDTO.setIdTrangThai(gioHang.getTrangThai() == null ? null : gioHang.getTrangThai().getId());
        gioHangDTO.setIdKhachHang(gioHang.getKhachHang() == null ? null : gioHang.getKhachHang().getId().intValue());
        return gioHangDTO;
    }

    private GioHang mapToEntity(final GioHangDTO gioHangDTO, final GioHang gioHang) {
        gioHang.setMaPhienGioHang(gioHangDTO.getMaPhienGioHang());
        final TrangThai trangThai = gioHangDTO.getIdTrangThai() == null ? null : trangThaiRepository.findById(gioHangDTO.getIdTrangThai())
                .orElseThrow(NotFoundException::new);
        gioHang.setTrangThai(trangThai);
        final KhachHang khachHang = gioHangDTO.getIdKhachHang() == null ? null : khachHangRepository.findById(gioHangDTO.getIdKhachHang())
                .orElseThrow(() -> new NotFoundException("KhachHang not found"));
        gioHang.setKhachHang(khachHang);
        return gioHang;
    }
}
