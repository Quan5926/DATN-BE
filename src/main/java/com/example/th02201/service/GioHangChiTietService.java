package com.example.th02201.service;

import com.example.th02201.dto.GioHangChiTietDTO;
import com.example.th02201.model.ChiTietSanPham;
import com.example.th02201.model.GioHang;
import com.example.th02201.model.GioHangChiTiet;
import com.example.th02201.repository.ChiTietSanPhamRepository;
import com.example.th02201.repository.GioHangChiTietRepository;
import com.example.th02201.repository.GioHangRepository;
import com.example.th02201.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GioHangChiTietService {

    private final GioHangChiTietRepository gioHangChiTietRepository;
    private final GioHangRepository gioHangRepository;
    private final ChiTietSanPhamRepository chiTietSanPhamRepository;

    public GioHangChiTietService(final GioHangChiTietRepository gioHangChiTietRepository,
                                 final GioHangRepository gioHangRepository,
                                 final ChiTietSanPhamRepository chiTietSanPhamRepository) {
        this.gioHangChiTietRepository = gioHangChiTietRepository;
        this.gioHangRepository = gioHangRepository;
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
    }

    // Lấy tất cả các mục chi tiết giỏ hàng, sắp xếp theo ID giảm dần
    public List<GioHangChiTietDTO> findAll() {
        final List<GioHangChiTiet> gioHangChiTiets = gioHangChiTietRepository.findAll(Sort.by("id").descending());
        return gioHangChiTiets.stream()
                .map(this::mapToDto) // Corrected to use method reference
                .collect(Collectors.toList());
    }

    // Lấy một mục chi tiết giỏ hàng theo ID
    public GioHangChiTietDTO get(final Long id) {
        return gioHangChiTietRepository.findById(id)
                .map(this::mapToDto) // Corrected to use method reference
                .orElseThrow(NotFoundException::new);
    }

    // Tạo mới một mục chi tiết giỏ hàng
    public Long create(final GioHangChiTietDTO gioHangChiTietDTO) {
        final GioHangChiTiet gioHangChiTiet = new GioHangChiTiet();
        mapToEntity(gioHangChiTietDTO, gioHangChiTiet);
        return gioHangChiTietRepository.save(gioHangChiTiet).getId();
    }

    // Cập nhật một mục chi tiết giỏ hàng
    public void update(final Long id, final GioHangChiTietDTO gioHangChiTietDTO) {
        final GioHangChiTiet gioHangChiTiet = gioHangChiTietRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(gioHangChiTietDTO, gioHangChiTiet);
        gioHangChiTietRepository.save(gioHangChiTiet);
    }

    // Xóa một mục chi tiết giỏ hàng
    public void delete(final Long id) {
        gioHangChiTietRepository.deleteById(id);
    }

    // Phương thức tìm chi tiết giỏ hàng bằng ID giỏ hàng
    @Transactional(readOnly = true)
    public List<GioHangChiTietDTO> getCartDetailsByGioHangId(final UUID idGioHang) {
        // Cần cập nhật repository để sử dụng truy vấn mới
        return gioHangChiTietRepository.findCartDetailsByGioHangId(idGioHang);
    }

    // Phương thức tìm chi tiết giỏ hàng bằng mã phiên giỏ hàng
    @Transactional(readOnly = true)
    public List<GioHangChiTietDTO> getCartDetailsBySessionCode(final String maPhienGioHang) {
        // Cần cập nhật repository để sử dụng truy vấn mới
        return gioHangChiTietRepository.findCartDetailsBySessionCode(maPhienGioHang);
    }

    // Phương thức tìm chi tiết giỏ hàng bằng idKhachHang
    @Transactional(readOnly = true)
    public List<GioHangChiTietDTO> getCartDetailsByKhachHangId(final Integer idKhachHang) {
        // Cần cập nhật repository để sử dụng truy vấn mới
        return gioHangChiTietRepository.findCartDetailsByKhachHangID(idKhachHang);
    }

    // Method to map DTO to Entity
    // Corrected to handle cases where related entities might not be found
    private GioHangChiTiet mapToEntity(final GioHangChiTietDTO gioHangChiTietDTO, final GioHangChiTiet gioHangChiTiet) {
        gioHangChiTiet.setSoLuong(gioHangChiTietDTO.getSoLuong());
        gioHangChiTiet.setGiaBanHienTai(gioHangChiTietDTO.getGiaBanHienTai());
        gioHangChiTiet.setNgayThemVao(LocalDateTime.now());

        // Find and set GioHang entity
        final GioHang gioHang = gioHangChiTietDTO.getIdGioHang() == null ? null : gioHangRepository.findById(gioHangChiTietDTO.getIdGioHang())
                .orElseThrow(() -> new NotFoundException("GioHang not found"));
        gioHangChiTiet.setGioHang(gioHang);

        // Find and set ChiTietSanPham entity, handle case where it's not found
        final ChiTietSanPham chiTietSanPham = gioHangChiTietDTO.getIdChiTietSp() == null ? null : chiTietSanPhamRepository.findById(gioHangChiTietDTO.getIdChiTietSp())
                .orElseThrow(() -> new NotFoundException("ChiTietSanPham not found"));
        gioHangChiTiet.setChiTietSp(chiTietSanPham);

        return gioHangChiTiet;
    }

    // Method to map Entity to DTO
    public GioHangChiTietDTO mapToDto(final GioHangChiTiet gioHangChiTiet) {
        final GioHangChiTietDTO gioHangChiTietDTO = GioHangChiTietDTO.builder()
                .id(gioHangChiTiet.getId())
                .soLuong(gioHangChiTiet.getSoLuong())
                .giaBanHienTai(gioHangChiTiet.getGiaBanHienTai())
                .ngayThemVao(gioHangChiTiet.getNgayThemVao())
                .idGioHang(gioHangChiTiet.getGioHang() == null ? null : gioHangChiTiet.getGioHang().getId())
                .idChiTietSp(gioHangChiTiet.getChiTietSp() == null ? null : gioHangChiTiet.getChiTietSp().getId())
                .build();

        // Populate extra fields for convenience
        if (gioHangChiTiet.getGioHang() != null) {
            gioHangChiTietDTO.setMaPhienGioHang(gioHangChiTiet.getGioHang().getMaPhienGioHang());
            if (gioHangChiTiet.getGioHang().getKhachHang() != null) {
                gioHangChiTietDTO.setIdKhachHang(gioHangChiTiet.getGioHang().getKhachHang().getId().intValue());
            }
        }

        if (gioHangChiTiet.getChiTietSp() != null) {
            gioHangChiTietDTO.setMaCtsp(gioHangChiTiet.getChiTietSp().getMaCtsp());
            gioHangChiTietDTO.setGiaBanHienTai(gioHangChiTiet.getChiTietSp().getGiaBan());
            if (gioHangChiTiet.getChiTietSp().getSanPham() != null) {
                gioHangChiTietDTO.setSanPham(gioHangChiTiet.getChiTietSp().getSanPham().getId());
                gioHangChiTietDTO.setTenSanPham(gioHangChiTiet.getChiTietSp().getSanPham().getTenSanPham());
                gioHangChiTietDTO.setMoTaSanPham(gioHangChiTiet.getChiTietSp().getSanPham().getMoTaSanPham());
                gioHangChiTietDTO.setUrlAnhDaiDien(gioHangChiTiet.getChiTietSp().getSanPham().getUrlAnhDaiDien());
            }

            if (gioHangChiTiet.getChiTietSp().getKichCo() != null) {
                gioHangChiTietDTO.setKichCo(gioHangChiTiet.getChiTietSp().getKichCo().getId());
                gioHangChiTietDTO.setTenKichCo(gioHangChiTiet.getChiTietSp().getKichCo().getTenKichCo());
            }
            if (gioHangChiTiet.getChiTietSp().getMauSac() != null) {
                gioHangChiTietDTO.setMaMauSac(gioHangChiTiet.getChiTietSp().getMauSac().getMaMauSac());
                gioHangChiTietDTO.setTenMauSac(gioHangChiTiet.getChiTietSp().getMauSac().getTenMauSac());
            }
        }

        return gioHangChiTietDTO;
    }
}
