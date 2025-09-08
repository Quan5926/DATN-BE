package com.example.th02201.service;

import com.example.th02201.dto.*;
import com.example.th02201.model.*;
import com.example.th02201.repository.*;
import com.example.th02201.util.NotFoundException;
import com.example.th02201.util.ReferencedWarning;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChiTietSanPhamService {

    private final ChiTietSanPhamRepository chiTietSanPhamRepository;
    private final SanPhamRepository sanPhamRepository;
    private final ChatLieuRepository chatLieuRepository;
    private final MauSacRepository mauSacRepository;
    private final KichCoRepository kichCoRepository;
    private final TrangThaiRepository trangThaiRepository;

    public ChiTietSanPhamService(ChiTietSanPhamRepository chiTietSanPhamRepository,
                                 SanPhamRepository sanPhamRepository,
                                 ChatLieuRepository chatLieuRepository,
                                 MauSacRepository mauSacRepository,
                                 KichCoRepository kichCoRepository,
                                 TrangThaiRepository trangThaiRepository) {
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
        this.sanPhamRepository = sanPhamRepository;
        this.chatLieuRepository = chatLieuRepository;
        this.mauSacRepository = mauSacRepository;
        this.kichCoRepository = kichCoRepository;
        this.trangThaiRepository = trangThaiRepository;
    }

    @Transactional
    public List<ChiTietSanPhamDTO> findAll() {
        return chiTietSanPhamRepository.findAllWithDetails().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ChiTietSanPhamDTO> findBySanPhamId(Long sanPhamId) {
        return chiTietSanPhamRepository.findBySanPhamId(sanPhamId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ChiTietSanPhamDTO> findByKeyword(String keyword) {
        return chiTietSanPhamRepository.findByKeyword(keyword).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ChiTietSanPhamDTO> findByChatLieuId(Long chatLieuId) {
        return chiTietSanPhamRepository.findByChatLieuId(chatLieuId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ChiTietSanPhamDTO> findByMauSacId(Long mauSacId) {
        return chiTietSanPhamRepository.findByMauSacId(mauSacId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ChiTietSanPhamDTO> findByKichCoId(Long kichCoId) {
        return chiTietSanPhamRepository.findByKichCoId(kichCoId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ChiTietSanPhamDTO> findByFilters(Long sanPhamId, Long thuongHieuId, Long danhMucId, Long chatLieuId, Long mauSacId, Long kichCoId, String keyword) {
        return chiTietSanPhamRepository.findByFilters(sanPhamId, thuongHieuId, danhMucId, chatLieuId, mauSacId, kichCoId, null, keyword).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    @Transactional
    public List<ChiTietSanPhamDTO> findByDanhMucId(Long danhMucId) {
        return chiTietSanPhamRepository.findBySanPhamDanhMucId(danhMucId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ChiTietSanPhamDTO> findByThuongHieuId(Long thuongHieuId) {
        return chiTietSanPhamRepository.findBySanPhamThuongHieuId(thuongHieuId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ChiTietSanPhamDTO get(UUID id) {
        return chiTietSanPhamRepository.findByIdWithDetailsAndImages(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new NotFoundException("Chi tiết sản phẩm không tồn tại"));
    }

    @Transactional
    public UUID create(final ChiTietSanPhamDTO chiTietSanPhamDTO) {
        final ChiTietSanPham chiTietSanPham = new ChiTietSanPham();
        mapToEntity(chiTietSanPhamDTO, chiTietSanPham);
        ChiTietSanPham savedChiTietSanPham = chiTietSanPhamRepository.save(chiTietSanPham);
        updateSanPhamStatus(savedChiTietSanPham.getSanPham());
        return savedChiTietSanPham.getId();
    }

    @Transactional
    public void updateTrangThaiSanPhamRieng(UUID id, Integer idTrangThaiRieng) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        TrangThai trangThai = trangThaiRepository.findById(idTrangThaiRieng)
                .orElseThrow(() -> new NotFoundException("Trạng thái không tồn tại với ID: " + idTrangThaiRieng));

        ChiTietSanPham chiTietSanPham = chiTietSanPhamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Chi tiết sản phẩm không tồn tại"));

        if ("het_hang".equals(trangThai.getTenTrangThai()) && chiTietSanPham.getSoLuongTonKho() > 0) {
            throw new IllegalArgumentException("Không thể đặt trạng thái 'het_hang' khi số lượng tồn kho lớn hơn 0");
        }
        chiTietSanPham.setTrangThaiRieng(trangThai);
        chiTietSanPham.setNgayCapNhat(LocalDateTime.now());
        chiTietSanPhamRepository.save(chiTietSanPham);
        updateSanPhamStatus(chiTietSanPham.getSanPham());
    }

    @Transactional
    public void toggleStatus(UUID id, boolean active) {
        ChiTietSanPham chiTietSanPham = chiTietSanPhamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Chi tiết sản phẩm không tồn tại"));

        TrangThai newStatus;
        if (chiTietSanPham.getSoLuongTonKho() == 0 && active) {
            throw new IllegalArgumentException("Không thể đặt trạng thái 'dang_kinh_doanh' khi số lượng tồn kho bằng 0");
        } else if (chiTietSanPham.getSoLuongTonKho() == 0) {
            newStatus = trangThaiRepository.findByTenTrangThai("het_hang")
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy trạng thái 'het_hang'"));
        } else {
            newStatus = active ? trangThaiRepository.findByTenTrangThai("dang_kinh_doanh")
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy trạng thái 'dang_kinh_doanh'"))
                    : trangThaiRepository.findByTenTrangThai("ngung_kinh_doanh")
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy trạng thái 'ngung_kinh_doanh'"));
        }
        chiTietSanPham.setTrangThaiRieng(newStatus);
        chiTietSanPham.setNgayCapNhat(LocalDateTime.now());
        chiTietSanPhamRepository.save(chiTietSanPham);
        updateSanPhamStatus(chiTietSanPham.getSanPham());
    }

    @Transactional
    public void update(final UUID id, final ChiTietSanPhamDTO chiTietSanPhamDTO) {
        final ChiTietSanPham chiTietSanPham = chiTietSanPhamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Chi tiết sản phẩm không tồn tại"));
        mapToEntity(chiTietSanPhamDTO, chiTietSanPham);
        chiTietSanPhamRepository.save(chiTietSanPham);
        updateSanPhamStatus(chiTietSanPham.getSanPham());
    }

    @Transactional
    public void delete(final UUID id) {
        ChiTietSanPham chiTietSanPham = chiTietSanPhamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Chi tiết sản phẩm không tồn tại"));
        TrangThai ngungKinhDoanhStatus = trangThaiRepository.findByTenTrangThai("ngung_kinh_doanh")
                .orElseThrow(() -> new NotFoundException("Không tìm thấy trạng thái 'ngung_kinh_doanh'"));
        chiTietSanPham.setTrangThaiRieng(ngungKinhDoanhStatus);
        chiTietSanPham.setNgayCapNhat(LocalDateTime.now());
        chiTietSanPhamRepository.save(chiTietSanPham);
        updateSanPhamStatus(chiTietSanPham.getSanPham());
    }

    private void updateSanPhamStatus(SanPham sanPham) {
        long totalQuantity = chiTietSanPhamRepository.findBySanPham(sanPham).stream()
                .filter(ctsp -> !"ngung_kinh_doanh".equals(ctsp.getTrangThaiRieng().getTenTrangThai()))
                .mapToLong(ChiTietSanPham::getSoLuongTonKho)
                .sum();

        String currentSanPhamStatusName = sanPham.getTrangThai().getTenTrangThai();
        String newSanPhamStatusName;

        if (totalQuantity == 0) {
            newSanPhamStatusName = "het_hang";
        } else if ("het_hang".equals(currentSanPhamStatusName) && totalQuantity > 0) {
            newSanPhamStatusName = "dang_kinh_doanh";
        } else {
            newSanPhamStatusName = currentSanPhamStatusName;
        }

        if (!"ngung_kinh_doanh".equals(currentSanPhamStatusName) && !newSanPhamStatusName.equals(currentSanPhamStatusName)) {
            TrangThai updatedStatus = trangThaiRepository.findByTenTrangThai(newSanPhamStatusName)
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy trạng thái: " + newSanPhamStatusName));
            sanPham.setTrangThai(updatedStatus);
            sanPham.setNgayCapNhat(LocalDateTime.now());
            sanPhamRepository.save(sanPham);
        } else if ("ngung_kinh_doanh".equals(currentSanPhamStatusName)) {
            sanPham.setNgayCapNhat(LocalDateTime.now());
            sanPhamRepository.save(sanPham);
        } else {
            sanPham.setNgayCapNhat(LocalDateTime.now());
            sanPhamRepository.save(sanPham);
        }
    }


    private ChiTietSanPhamDTO mapToDto(final ChiTietSanPham chiTietSanPham) {
        ChiTietSanPhamDTO dto = ChiTietSanPhamDTO.builder()
                .id(chiTietSanPham.getId())
                .soLuongTonKho(chiTietSanPham.getSoLuongTonKho())
                .moTaChiTiet(chiTietSanPham.getMoTaChiTiet())
                .giaNhap(chiTietSanPham.getGiaNhap())
                .giaBan(chiTietSanPham.getGiaBan())
                .maCtsp(chiTietSanPham.getMaCtsp())
                .ngayNhap(chiTietSanPham.getNgayNhap())
                .idTrangThaiRieng(chiTietSanPham.getTrangThaiRieng() == null ? null : chiTietSanPham.getTrangThaiRieng().getId())
                .tenTrangThaiRieng(chiTietSanPham.getTrangThaiRieng() == null ? null : chiTietSanPham.getTrangThaiRieng().getTenTrangThai())
                .ngayTao(chiTietSanPham.getNgayTao())
                .ngayCapNhat(chiTietSanPham.getNgayCapNhat())
                .chatLieu(chiTietSanPham.getChatLieu() == null ? null : chiTietSanPham.getChatLieu().getId())
                .mauSac(chiTietSanPham.getMauSac() == null ? null : chiTietSanPham.getMauSac().getId())
                .kichCo(chiTietSanPham.getKichCo() == null ? null : chiTietSanPham.getKichCo().getId())
                .sanPham(chiTietSanPham.getSanPham() == null ? null : chiTietSanPham.getSanPham().getId())
                .build();

        if (chiTietSanPham.getSanPham() != null) {
            dto.setTenSanPham(chiTietSanPham.getSanPham().getTenSanPham());
            if (chiTietSanPham.getSanPham().getThuongHieu() != null) {
                dto.setTenThuongHieu(chiTietSanPham.getSanPham().getThuongHieu().getTenThuongHieu());
                dto.setThuongHieu(chiTietSanPham.getSanPham().getThuongHieu().getId());
            }
            if (chiTietSanPham.getSanPham().getDanhMuc() != null) {
                dto.setTenDanhMuc(chiTietSanPham.getSanPham().getDanhMuc().getTenDanhMuc());
                dto.setDanhMuc(chiTietSanPham.getSanPham().getDanhMuc().getId());
            }
        }
        if (chiTietSanPham.getChatLieu() != null) {
            dto.setTenChatLieu(chiTietSanPham.getChatLieu().getTenChatLieu());
        }
        if (chiTietSanPham.getMauSac() != null) {
            dto.setTenMauSac(chiTietSanPham.getMauSac().getTenMauSac());
        }
        if (chiTietSanPham.getKichCo() != null) {
            dto.setTenKichCo(chiTietSanPham.getKichCo().getTenKichCo());
        }

        // Xử lý danh sách ảnh và URL ảnh đại diện
        if (chiTietSanPham.getAnhSanPhams() != null && !chiTietSanPham.getAnhSanPhams().isEmpty()) {
            List<AnhSanPhamDTO> images = chiTietSanPham.getAnhSanPhams().stream()
                    .map(img -> AnhSanPhamDTO.builder()
                            .id(img.getId())
                            .urlAnh(img.getUrlAnh())
                            .laAnhDaiDien(img.getLaAnhDaiDien())
                            .chiTietSpId(img.getChiTietSp().getId())
                            .ngayTao(img.getNgayTao())
                            .ngayCapNhat(img.getNgayCapNhat())
                            .build())
                    .collect(Collectors.toList());
            dto.setImages(images);

            // Tìm ảnh đại diện và đặt URL
            chiTietSanPham.getAnhSanPhams().stream()
                    .filter(AnhSanPham::getLaAnhDaiDien)
                    .findFirst()
                    .ifPresent(repImage -> dto.setUrlAnhDaiDien(repImage.getUrlAnh()));

            // Nếu không có ảnh nào được đánh dấu là đại diện, lấy ảnh đầu tiên làm mặc định
            if (dto.getUrlAnhDaiDien() == null && !images.isEmpty()) {
                dto.setUrlAnhDaiDien(images.get(0).getUrlAnh());
            }

        } else {
            // Nếu không có ảnh nào, đặt danh sách ảnh rỗng và URL đại diện là null
            dto.setImages(List.of());
            dto.setUrlAnhDaiDien(null);
        }

        return dto;
    }

    private ChiTietSanPham mapToEntity(final ChiTietSanPhamDTO chiTietSanPhamDTO,
                                       final ChiTietSanPham chiTietSanPham) {
        chiTietSanPham.setSoLuongTonKho(chiTietSanPhamDTO.getSoLuongTonKho());
        chiTietSanPham.setMoTaChiTiet(chiTietSanPhamDTO.getMoTaChiTiet());
        chiTietSanPham.setGiaNhap(chiTietSanPhamDTO.getGiaNhap());
        chiTietSanPham.setGiaBan(chiTietSanPhamDTO.getGiaBan());
        chiTietSanPham.setMaCtsp(chiTietSanPhamDTO.getMaCtsp());
        if (chiTietSanPhamDTO.getNgayNhap() != null) {
            chiTietSanPham.setNgayNhap(chiTietSanPhamDTO.getNgayNhap());
        } else if (chiTietSanPham.getNgayNhap() == null) {
            chiTietSanPham.setNgayNhap(LocalDateTime.now());
        }

        TrangThai trangThaiRieng = trangThaiRepository.findById(chiTietSanPhamDTO.getIdTrangThaiRieng())
                .orElseThrow(() -> new NotFoundException("Trạng thái riêng không tồn tại với ID: " + chiTietSanPhamDTO.getIdTrangThaiRieng()));
        chiTietSanPham.setTrangThaiRieng(trangThaiRieng);

        if ("het_hang".equals(trangThaiRieng.getTenTrangThai()) && chiTietSanPhamDTO.getSoLuongTonKho() > 0) {
            throw new IllegalArgumentException("Không thể đặt trạng thái 'het_hang' khi số lượng tồn kho lớn hơn 0");
        }

        final SanPham sanPham = chiTietSanPhamDTO.getSanPham() == null ? null : sanPhamRepository.findById(chiTietSanPhamDTO.getSanPham())
                .orElseThrow(() -> new NotFoundException("Sản phẩm không tồn tại"));
        chiTietSanPham.setSanPham(sanPham);
        final ChatLieu chatLieu = chiTietSanPhamDTO.getChatLieu() == null ? null : chatLieuRepository.findById(chiTietSanPhamDTO.getChatLieu())
                .orElseThrow(() -> new NotFoundException("Chất liệu không tồn tại"));
        chiTietSanPham.setChatLieu(chatLieu);
        final MauSac mauSac = chiTietSanPhamDTO.getMauSac() == null ? null : mauSacRepository.findById(chiTietSanPhamDTO.getMauSac())
                .orElseThrow(() -> new NotFoundException("Màu sắc không tồn tại"));
        chiTietSanPham.setMauSac(mauSac);
        final KichCo kichCo = chiTietSanPhamDTO.getKichCo() == null ? null : kichCoRepository.findById(chiTietSanPhamDTO.getKichCo())
                .orElseThrow(() -> new NotFoundException("Kích cỡ không tồn tại"));
        chiTietSanPham.setKichCo(kichCo);

        if (chiTietSanPham.getId() == null) {
            chiTietSanPham.setNgayTao(LocalDateTime.now());
        }
        chiTietSanPham.setNgayCapNhat(LocalDateTime.now());

        return chiTietSanPham;
    }

    public ReferencedWarning getReferencedWarning(UUID id) {
        ReferencedWarning referencedWarning = new ReferencedWarning();
        ChiTietSanPham chiTietSanPham = chiTietSanPhamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Chi tiết sản phẩm không tồn tại"));
        if (chiTietSanPham.getAnhSanPhams() != null && !chiTietSanPham.getAnhSanPhams().isEmpty()) {
            referencedWarning.addWarning("Ảnh sản phẩm", chiTietSanPham.getAnhSanPhams().size());
        }
        return referencedWarning.hasWarnings() ? referencedWarning : null;
    }
}
