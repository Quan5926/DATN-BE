package com.example.th02201.service;

import com.example.th02201.repository.*;
import com.example.th02201.model.*;
import com.example.th02201.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HoaDonService {

    private static final Integer TRANG_THAI_HOA_DON_DA_GIAO = 10;
    private static final Integer TRANG_THAI_HOA_DON_DA_HUY = 11;
    private static final Integer TRANG_THAI_HOA_DON_HOAN_TRA = 12;
    private static final String TRANG_THAI_THANH_TOAN_THANH_CONG = "thanh_cong";

    @Autowired
    private HoaDonRepository hoaDonRepository;

    // Nếu KhachHangRepository này được dùng để thao tác với bảng khach_hangg
// và bạn không còn cần nó, bạn có thể cân nhắc xóa hoặc thay thế bằng KhachHangQRepository
    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private TrangThaiRepository trangThaiRepository;

    @Autowired
    private ChiTietThanhToanRepository chiTietThanhToanRepository;

    @Autowired
    private LichSuHoaDonRepository lichSuHoaDonRepository;

    @Autowired
    private ChiTietHoaDonRepository chiTietHoaDonRepository;

    @Autowired
    private SanPhamTraLaiRepository sanPhamTraLaiRepository;

    @Autowired
    private SanPhamChiTietQRepository sanPhamChiTietRepository;

    @Autowired
    private PhuongThucThanhToanRepository phuongThucThanhToanRepository;

    @Autowired
    private PaymentService paymentService;

    private BigDecimal calculateTongTienThanhToan(HoaDon hoaDon) {
        BigDecimal productsTotal = hoaDon.getChiTietHoaDon().stream()
                .map(cthd -> cthd.getThanhTien() != null ? cthd.getThanhTien() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal totalReturnAmount = hoaDon.getSanPhamTraLai().stream()
                .map(sptl -> sptl.getSoTienHoan() != null ? sptl.getSoTienHoan() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal discountAmount = BigDecimal.ZERO;
        if (hoaDon.getMaGiamGia() != null && hoaDon.getMaGiamGia().getPhieuGiamGia() != null) {
            PhieuGiamGia phieuGiamGia = hoaDon.getMaGiamGia().getPhieuGiamGia();
            if (phieuGiamGia.getLoaiGiamGia() != null) {
                if (phieuGiamGia.getLoaiGiamGia().equals("PHAN_TRAM")) {
                    BigDecimal percentage = phieuGiamGia.getGiaTriGiam().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    discountAmount = productsTotal.multiply(percentage).setScale(2, RoundingMode.HALF_UP);
                    if (phieuGiamGia.getSoTienGiamToiDa() != null && discountAmount.compareTo(phieuGiamGia.getSoTienGiamToiDa()) > 0) {
                        discountAmount = phieuGiamGia.getSoTienGiamToiDa();
                    }
                } else if (phieuGiamGia.getLoaiGiamGia().equals("SO_TIEN")) {
                    discountAmount = phieuGiamGia.getGiaTriGiam() != null ? phieuGiamGia.getGiaTriGiam() : BigDecimal.ZERO;
                }
            }
        }

        BigDecimal phiShip = hoaDon.getPhiVanChuyen() != null ? hoaDon.getPhiVanChuyen() : BigDecimal.ZERO;

        BigDecimal tongTienThanhToan = productsTotal
                .subtract(totalReturnAmount)
                .subtract(discountAmount)
                .add(phiShip)
                .setScale(2, RoundingMode.HALF_UP);

        System.out.println("DEBUG: Tính toán tongTienThanhToan cho hóa đơn " + hoaDon.getId() + ": " +
                "productsTotal=" + productsTotal +
                ", totalReturnAmount=" + totalReturnAmount +
                ", discountAmount=" + discountAmount +
                ", phiShip=" + phiShip +
                ", tongTienThanhToan=" + tongTienThanhToan);

        return tongTienThanhToan.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : tongTienThanhToan;
    }

    private HoaDonDetailDTO convertToDto(HoaDon hoaDon) {
        if (hoaDon == null) {
            System.err.println("Hóa đơn null khi chuyển đổi sang DTO");
            return null;
        }

        System.out.println("DEBUG: convertToDto - HoaDon ID: " + hoaDon.getId() + ", ChiTietHoaDon size: " + hoaDon.getChiTietHoaDon().size());

        // Tính tongTienSanPham từ chiTietHoaDon
        BigDecimal tongTienSanPham = hoaDon.getChiTietHoaDon().stream()
                .map(cthd -> cthd.getThanhTien() != null ? cthd.getThanhTien() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal tongTienThanhToan = calculateTongTienThanhToan(hoaDon);

        KhachHangDTO khachHangDTO = null;
        if (hoaDon.getKhachHang() != null) {
            String email = null;
            TaiKhoan taiKhoanOfKhachHang = hoaDon.getKhachHang().getTaiKhoan();
            if (taiKhoanOfKhachHang != null) {
                email = taiKhoanOfKhachHang.getEmail();
            }
            khachHangDTO = new KhachHangDTO(
                    hoaDon.getKhachHang().getId(),
                    hoaDon.getKhachHang().getTenKhachHang(),
                    hoaDon.getKhachHang().getSoDienThoai(),
                    email,
                    hoaDon.getKhachHang().getNgaySinh()
            );
        }

        TrangThaiDTO trangThaiDTO = null;
        if (hoaDon.getTrangThai() != null) {
            trangThaiDTO = new TrangThaiDTO(
                    hoaDon.getTrangThai().getId(),
                    hoaDon.getTrangThai().getTenTrangThai()
            );
        }

        List<LichSuThanhToanDTO> lichSuThanhToanDTOS = hoaDon.getLichSuThanhToan().stream()
                .map(p -> new LichSuThanhToanDTO(
                        p.getId(),
                        p.getThoiGianThanhToan(),
                        p.getSoTienThanhToan() != null ? p.getSoTienThanhToan() : BigDecimal.ZERO,
                        p.getPhuongThucThanhToan() != null ? p.getPhuongThucThanhToan().getTenPhuongThuc() : "N/A",
                        p.getGhiChuThanhToan() != null ? p.getGhiChuThanhToan() : "",
                        p.getTrangThaiThanhToan() != null ? p.getTrangThaiThanhToan() : "N/A"))
                .collect(Collectors.toList());

        List<LichSuTrangThaiDTO> lichSuTrangThaiDTOS = hoaDon.getLichSuHoaDon().stream()
                .map(s -> new LichSuTrangThaiDTO(
                        s.getId(),
                        s.getHanhDong(),
                        s.getMoTaHanhDong(),
                        s.getThoiGian()))
                .collect(Collectors.toList());

        List<ChiTietHoaDonDTO> chiTietHoaDonDTOS = hoaDon.getChiTietHoaDon().stream()
                .map(cthd -> {
                    try {
                        ChiTietSanPham spct = cthd.getChiTietSanPham();
                        if (spct == null || spct.getSanPham() == null) {
                            System.err.println("ChiTietSanPham hoặc SanPham null cho ChiTietHoaDon ID: " + cthd.getId());
                            return null;
                        }
                        String tenSanPham = spct.getSanPham().getTenSanPham();
                        String chatLieuTen = (spct.getChatLieu() != null) ? spct.getChatLieu().getTenChatLieu() : "N/A";
                        String sizeTen = (spct.getKichCo() != null) ? spct.getKichCo().getTenKichCo() : "N/A";
                        String mauSacTen = (spct.getMauSac() != null) ? spct.getMauSac().getTenMauSac() : "N/A";
                        return new ChiTietHoaDonDTO(
                                cthd.getId(),
                                Math.toIntExact(spct.getSanPham().getId()),
                                tenSanPham,
                                cthd.getSoLuong(),
                                cthd.getDonGia() != null ? cthd.getDonGia() : BigDecimal.ZERO,
                                cthd.getThanhTien() != null ? cthd.getThanhTien() : BigDecimal.ZERO,
                                chatLieuTen,
                                sizeTen,
                                mauSacTen
                        );
                    } catch (Exception e) {
                        System.err.println("Lỗi khi chuyển đổi ChiTietHoaDon ID: " + cthd.getId() + ": " + e.getMessage());
                        return null;
                    }
                })
                .filter(dto -> dto != null)
                .collect(Collectors.toList());

        List<SanPhamTraLaiDTO> sanPhamTraLaiDTOS = hoaDon.getSanPhamTraLai().stream()
                .map(sptl -> {
                    try {
                        HoaDonChiTiet cthdGoc = sptl.getChiTietHoaDon();
                        if (cthdGoc == null) {
                            System.err.println("ChiTietHoaDon null cho SanPhamTraLai ID: " + sptl.getId());
                            return null;
                        }
                        ChiTietSanPham spctGoc = cthdGoc.getChiTietSanPham();
                        if (spctGoc == null || spctGoc.getSanPham() == null) {
                            System.err.println("ChiTietSanPham hoặc SanPham null cho SanPhamTraLai ID: " + sptl.getId());
                            return null;
                        }
                        String tenSanPham = spctGoc.getSanPham().getTenSanPham();
                        String chatLieuGocTen = (spctGoc.getChatLieu() != null) ? spctGoc.getChatLieu().getTenChatLieu() : "N/A";
                        String sizeGocTen = (spctGoc.getKichCo() != null) ? spctGoc.getKichCo().getTenKichCo() : "N/A";
                        String mauSacGocTen = (spctGoc.getMauSac() != null) ? spctGoc.getMauSac().getTenMauSac() : "N/A";
                        BigDecimal soTienHoan = sptl.getSoTienHoan() != null ? sptl.getSoTienHoan() :
                                cthdGoc.getDonGia() != null ?
                                        cthdGoc.getDonGia().multiply(BigDecimal.valueOf(sptl.getSoLuongTra())).setScale(2, RoundingMode.HALF_UP) :
                                        BigDecimal.ZERO;
                        return new SanPhamTraLaiDTO(
                                sptl.getId(),
                                cthdGoc.getId(),
                                tenSanPham,
                                chatLieuGocTen,
                                sizeGocTen,
                                mauSacGocTen,
                                sptl.getSoLuongTra(),
                                cthdGoc.getDonGia() != null ? cthdGoc.getDonGia() : BigDecimal.ZERO,
                                soTienHoan,
                                sptl.getLyDoTra() != null ? sptl.getLyDoTra() : "Không có lý do",
                                sptl.getNgayTra() != null ? sptl.getNgayTra() : LocalDateTime.now()
                        );
                    } catch (Exception e) {
                        System.err.println("Lỗi khi chuyển đổi SanPhamTraLai ID: " + sptl.getId() + ": " + e.getMessage());
                        return null;
                    }
                })
                .filter(dto -> dto != null)
                .collect(Collectors.toList());

        BigDecimal giaTriGiam = BigDecimal.ZERO;
        if (hoaDon.getMaGiamGia() != null && hoaDon.getMaGiamGia().getPhieuGiamGia() != null) {
            BigDecimal rawGiaTriGiam = hoaDon.getMaGiamGia().getPhieuGiamGia().getGiaTriGiam() != null
                    ? hoaDon.getMaGiamGia().getPhieuGiamGia().getGiaTriGiam()
                    : BigDecimal.ZERO;
            if (hoaDon.getMaGiamGia().getPhieuGiamGia().getLoaiGiamGia().equals("PHAN_TRAM")) {
                giaTriGiam = rawGiaTriGiam;
            } else {
                giaTriGiam = rawGiaTriGiam;
            }
        }

        HoaDonDetailDTO dto = new HoaDonDetailDTO(
                hoaDon.getId(),
                hoaDon.getMaHoaDon(),
                khachHangDTO,
                hoaDon.getMaVanDon(),
                hoaDon.getNgayTao(),
                tongTienSanPham, // Map tongTienSanPham
                tongTienThanhToan,
                hoaDon.getPhiVanChuyen(),
                giaTriGiam,
                trangThaiDTO,
                lichSuThanhToanDTOS,
                chiTietHoaDonDTOS,
                lichSuTrangThaiDTOS,
                sanPhamTraLaiDTOS
        );

        System.out.println("DEBUG: tongTienSanPham = " + tongTienSanPham + " for HoaDon ID: " + hoaDon.getId());
        return dto;
    }

    private HoaDonDTO convertToSimpleDto(HoaDon hoaDon) {
        if (hoaDon == null) {
            System.err.println("Hóa đơn null khi chuyển đổi sang DTO");
            return null;
        }

        BigDecimal giaTriGiam = BigDecimal.ZERO;
        if (hoaDon.getMaGiamGia() != null && hoaDon.getMaGiamGia().getPhieuGiamGia() != null) {
            BigDecimal rawGiaTriGiam = hoaDon.getMaGiamGia().getPhieuGiamGia().getGiaTriGiam() != null
                    ? hoaDon.getMaGiamGia().getPhieuGiamGia().getGiaTriGiam()
                    : BigDecimal.ZERO;
            if (hoaDon.getMaGiamGia().getPhieuGiamGia().getLoaiGiamGia().equals("PHAN_TRAM")) {
                giaTriGiam = rawGiaTriGiam;
            } else {
                giaTriGiam = rawGiaTriGiam;
            }
        }

        String loaiThanhToan = hoaDon.getLichSuThanhToan().stream()
                .findFirst()
                .map(p -> p.getPhuongThucThanhToan() != null ? p.getPhuongThucThanhToan().getTenPhuongThuc() : "N/A")
                .orElse("N/A");

        BigDecimal soTienThanhToanFinal = hoaDon.getLichSuThanhToan().stream()
                .map(p -> p.getSoTienThanhToan() != null ? p.getSoTienThanhToan() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new HoaDonDTO(
                hoaDon.getId(),
                hoaDon.getMaHoaDon(),
                hoaDon.getNgayTao(),
                calculateTongTienThanhToan(hoaDon),
                hoaDon.getPhiVanChuyen(),
                giaTriGiam,
                hoaDon.getNgayThanhToan(),
                loaiThanhToan,
                soTienThanhToanFinal,
                hoaDon.getKhachHang() != null ? hoaDon.getKhachHang().getId() : null,
                hoaDon.getKhachHang() != null ? hoaDon.getKhachHang().getTenKhachHang() : null,
                hoaDon.getKhachHang() != null ? hoaDon.getKhachHang().getSoDienThoai() : null,
                hoaDon.getTrangThai() != null ? hoaDon.getTrangThai().getId() : null,
                hoaDon.getTrangThai() != null ? hoaDon.getTrangThai().getTenTrangThai() : null
        );
    }

    @Transactional(readOnly = true)
    public List<HoaDonDetailDTO> getAllInvoices() {
        return hoaDonRepository.findAllWithDetails().stream()
                .map(this::convertToDto)
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<HoaDonDetailDTO> getInvoiceById(UUID id) {
        return hoaDonRepository.findByIdWithDetails(id)
                .map(this::convertToDto);
    }

    @Transactional
    public Optional<HoaDonDetailDTO> updateInvoiceStatus(UUID invoiceId, Integer newStatusId, String ghiChu) {
        Optional<HoaDon> hoaDonOptional = hoaDonRepository.findByIdWithDetails(invoiceId);
        if (hoaDonOptional.isEmpty()) {
            System.err.println("Không tìm thấy hóa đơn với ID: " + invoiceId);
            return Optional.empty();
        }

        HoaDon hoaDon = hoaDonOptional.get();
        Optional<TrangThai> trangThaiOptional = trangThaiRepository.findById(newStatusId);
        if (trangThaiOptional.isEmpty()) {
            throw new IllegalArgumentException("ID trạng thái không hợp lệ: " + newStatusId);
        }

        hoaDon.setTrangThai(trangThaiOptional.get());
        hoaDon.setTongTienThanhToan(calculateTongTienThanhToan(hoaDon));
        HoaDon savedHoaDon = hoaDonRepository.save(hoaDon);

        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        lichSuHoaDon.setHoaDon(savedHoaDon);
        lichSuHoaDon.setHanhDong("Cập nhật trạng thái");
        lichSuHoaDon.setMoTaHanhDong(ghiChu != null ? ghiChu : "Cập nhật trạng thái hóa đơn");
        lichSuHoaDon.setThoiGian(LocalDateTime.now());
        lichSuHoaDonRepository.save(lichSuHoaDon);

        return getInvoiceById(savedHoaDon.getId());
    }

    @Transactional
    public Optional<HoaDonDetailDTO> cancelInvoice(UUID invoiceId, String ghiChu) {
        Optional<HoaDon> hoaDonOptional = hoaDonRepository.findByIdWithDetails(invoiceId);
        if (hoaDonOptional.isEmpty()) {
            System.err.println("Không tìm thấy hóa đơn với ID: " + invoiceId);
            return Optional.empty();
        }

        HoaDon hoaDon = hoaDonOptional.get();
        hoaDon.setPhiVanChuyen(BigDecimal.ZERO);
        hoaDon.setTongTienThanhToan(calculateTongTienThanhToan(hoaDon));
        hoaDonRepository.save(hoaDon);

        return updateInvoiceStatus(invoiceId, TRANG_THAI_HOA_DON_DA_HUY, ghiChu);
    }

    @Transactional
    public Optional<HoaDonDetailDTO> addPayment(UUID invoiceId, BigDecimal amount, Integer paymentMethodId) {
        System.out.println("addPayment called with invoiceId: " + invoiceId + ", amount: " + amount + ", paymentMethodId: " + paymentMethodId);

        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO();
        paymentRequestDTO.setSoTienThanhToan(amount);
        paymentRequestDTO.setIdPhuongThucThanhToan(paymentMethodId);
        paymentRequestDTO.setTrangThaiThanhToan(TRANG_THAI_THANH_TOAN_THANH_CONG);
        paymentRequestDTO.setGhiChuThanhToan("Thanh toán hóa đơn " + invoiceId);

        ChiTietThanhToan payment = paymentService.addPayment(invoiceId, paymentRequestDTO);

        Optional<HoaDon> hoaDonOptional = hoaDonRepository.findByIdWithDetails(invoiceId);
        if (hoaDonOptional.isEmpty()) {
            System.err.println("Không tìm thấy hóa đơn với ID: " + invoiceId);
            return Optional.empty();
        }

        HoaDon hoaDon = hoaDonOptional.get();
        hoaDon.setTongTienThanhToan(calculateTongTienThanhToan(hoaDon));

        // Cập nhật trạng thái hóa đơn nếu đã thanh toán đủ
        BigDecimal tongTienDaThanhToan = chiTietThanhToanRepository.findByHoaDonId(invoiceId)
                .stream()
                .map(ChiTietThanhToan::getSoTienThanhToan)
                .filter(t -> t != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (tongTienDaThanhToan.compareTo(hoaDon.getTongTienThanhToan()) >= 0) {
            Optional<TrangThai> paidStatus = trangThaiRepository.findById(3); // Giả sử 3 là trạng thái "đã thanh toán"
            if (paidStatus.isPresent()) {
                hoaDon.setTrangThai(paidStatus.get());
                LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
                lichSuHoaDon.setHoaDon(hoaDon);
                lichSuHoaDon.setHanhDong("Thanh toán hoàn tất");
                lichSuHoaDon.setMoTaHanhDong("Hóa đơn đã được thanh toán đầy đủ.");
                lichSuHoaDon.setThoiGian(LocalDateTime.now());
                lichSuHoaDonRepository.save(lichSuHoaDon);
            }
        }

        hoaDonRepository.save(hoaDon);
        return getInvoiceById(hoaDon.getId());
    }

    @Transactional
    public Optional<HoaDonDetailDTO> refundInvoice(UUID invoiceId, BigDecimal refundAmount, String refundNote) {
        Optional<HoaDon> hoaDonOptional = hoaDonRepository.findByIdWithDetails(invoiceId);
        if (hoaDonOptional.isEmpty()) {
            System.err.println("Không tìm thấy hóa đơn với ID: " + invoiceId);
            return Optional.empty();
        }

        HoaDon hoaDon = hoaDonOptional.get();
        ChiTietThanhToan refundPayment = new ChiTietThanhToan();
        refundPayment.setHoaDon(hoaDon);
        refundPayment.setSoTienThanhToan(refundAmount.negate().setScale(2, RoundingMode.HALF_UP));
        refundPayment.setGhiChuThanhToan("Hoàn tiền: " + (refundNote != null ? refundNote : ""));
        refundPayment.setThoiGianThanhToan(LocalDateTime.now());
        refundPayment.setTrangThaiThanhToan(TRANG_THAI_THANH_TOAN_THANH_CONG);
        chiTietThanhToanRepository.save(refundPayment);

        if (hoaDon.getTrangThai().getId() != TRANG_THAI_HOA_DON_HOAN_TRA) {
            Optional<TrangThai> partialRefundStatus = trangThaiRepository.findById(TRANG_THAI_HOA_DON_HOAN_TRA);
            if (partialRefundStatus.isPresent()) {
                hoaDon.setTrangThai(partialRefundStatus.get());
                LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
                lichSuHoaDon.setHoaDon(hoaDon);
                lichSuHoaDon.setHanhDong("Hoàn tiền");
                lichSuHoaDon.setMoTaHanhDong("Hoàn tiền một phần: " + (refundNote != null ? refundNote : ""));
                lichSuHoaDon.setThoiGian(LocalDateTime.now());
                lichSuHoaDonRepository.save(lichSuHoaDon);
            }
        }

        hoaDon.setTongTienThanhToan(calculateTongTienThanhToan(hoaDon));
        hoaDonRepository.save(hoaDon);

        return getInvoiceById(hoaDon.getId());
    }

    @Transactional
    public Optional<HoaDonDetailDTO> returnProducts(UUID invoiceId, List<SanPhamTraLaiDTO> returnedItemsDto, String returnReason) {
        Optional<HoaDon> hoaDonOptional = hoaDonRepository.findByIdWithDetails(invoiceId);
        if (hoaDonOptional.isEmpty()) {
            System.err.println("Không tìm thấy hóa đơn với ID: " + invoiceId);
            return Optional.empty();
        }

        HoaDon hoaDon = hoaDonOptional.get();
        for (SanPhamTraLaiDTO returnedItemDto : returnedItemsDto) {
            Optional<HoaDonChiTiet> cthdOptional = chiTietHoaDonRepository.findById(returnedItemDto.getChiTietHoaDonId());
            if (cthdOptional.isEmpty() || !cthdOptional.get().getHoaDon().getId().equals(invoiceId)) {
                throw new IllegalArgumentException("Sản phẩm trả lại không hợp lệ hoặc không thuộc hóa đơn này: " + returnedItemDto.getChiTietHoaDonId());
            }

            HoaDonChiTiet cthd = cthdOptional.get();
            List<SanPhamTraLai> existingReturns = sanPhamTraLaiRepository.findByChiTietHoaDonId(cthd.getId());
            int totalReturnedQuantity = existingReturns.stream()
                    .mapToInt(SanPhamTraLai::getSoLuongTra)
                    .sum();
            int remainingQuantity = cthd.getSoLuong() - totalReturnedQuantity;
            if (returnedItemDto.getSoLuongTra() <= 0 || returnedItemDto.getSoLuongTra() > remainingQuantity) {
                throw new IllegalArgumentException("Số lượng trả lại không hợp lệ: " + returnedItemDto.getSoLuongTra() +
                        ", còn lại: " + remainingQuantity + " cho ChiTietHoaDon ID: " + cthd.getId());
            }

            SanPhamTraLai newReturn = new SanPhamTraLai();
            newReturn.setHoaDon(hoaDon);
            newReturn.setChiTietHoaDon(cthd);
            newReturn.setSoLuongTra(returnedItemDto.getSoLuongTra());
            newReturn.setLyDoTra(returnReason != null ? returnReason : "Không có lý do");
            newReturn.setNgayTra(LocalDateTime.now());
            newReturn.setSoTienHoan(cthd.getDonGia() != null ?
                    cthd.getDonGia().multiply(BigDecimal.valueOf(returnedItemDto.getSoLuongTra())).setScale(2, RoundingMode.HALF_UP) :
                    BigDecimal.ZERO);
            sanPhamTraLaiRepository.save(newReturn);

            ChiTietSanPham spct = cthd.getChiTietSanPham();
            if (spct != null) {
                spct.setSoLuongTonKho(spct.getSoLuongTonKho() + returnedItemDto.getSoLuongTra());
                sanPhamChiTietRepository.save(spct);
            } else {
                System.err.println("ChiTietSanPham null cho ChiTietHoaDon ID: " + cthd.getId());
            }

            System.out.println("Lưu SanPhamTraLai: ID=" + newReturn.getId() +
                    ", ChiTietHoaDonId=" + cthd.getId() +
                    ", SoLuongTra=" + newReturn.getSoLuongTra() +
                    ", SoTienHoan=" + newReturn.getSoTienHoan());
        }

        hoaDon.setTongTienThanhToan(calculateTongTienThanhToan(hoaDon));

        long totalOriginalQuantity = hoaDon.getChiTietHoaDon().stream()
                .mapToLong(HoaDonChiTiet::getSoLuong)
                .sum();
        long totalReturnedQuantity = hoaDon.getSanPhamTraLai().stream()
                .mapToLong(SanPhamTraLai::getSoLuongTra)
                .sum();

        if (totalReturnedQuantity >= totalOriginalQuantity) {
            hoaDon.setPhiVanChuyen(BigDecimal.ZERO);
            hoaDon.setTongTienThanhToan(calculateTongTienThanhToan(hoaDon));
            hoaDonRepository.save(hoaDon);
            updateInvoiceStatus(invoiceId, TRANG_THAI_HOA_DON_DA_HUY, "Tất cả sản phẩm đã được trả lại, đơn hàng được hủy.");
        } else if (totalReturnedQuantity > 0 && hoaDon.getTrangThai().getId() != TRANG_THAI_HOA_DON_HOAN_TRA) {
            updateInvoiceStatus(invoiceId, TRANG_THAI_HOA_DON_HOAN_TRA, "Hoàn một phần sản phẩm.");
        }

        hoaDonRepository.save(hoaDon);
        return getInvoiceById(hoaDon.getId());
    }

    @Transactional
    public boolean deleteInvoice(UUID id) {
        if (hoaDonRepository.existsById(id)) {
            hoaDonRepository.deleteById(id);
            return true;
        }
        return false;
    }



}