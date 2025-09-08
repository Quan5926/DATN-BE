package com.example.th02201.service;

import com.example.th02201.dto.PaymentRequestDTO;
import com.example.th02201.model.ChiTietThanhToan;
import com.example.th02201.model.HoaDon;
import com.example.th02201.model.PhuongThucThanhToan;
import com.example.th02201.repository.ChiTietThanhToanRepository;
import com.example.th02201.repository.HoaDonRepository;
import com.example.th02201.repository.PhuongThucThanhToanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private PhuongThucThanhToanRepository phuongThucThanhToanRepository;

    @Autowired
    private ChiTietThanhToanRepository chiTietThanhToanRepository;

    // Danh sách trạng thái hợp lệ dưới dạng chuỗi
    private static final String[] VALID_TRANG_THAI_THANH_TOAN = {"thanh_cong", "that_bai", "cho_xu_ly"};

    @Transactional
    public ChiTietThanhToan addPayment(UUID invoiceId, PaymentRequestDTO paymentRequestDTO) {
        System.out.println("Received invoiceId: " + invoiceId);
        System.out.println("Received PaymentRequestDTO: " + paymentRequestDTO);

        Optional<HoaDon> hoaDonOptional = hoaDonRepository.findByIdWithDetails(invoiceId);
        if (hoaDonOptional.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + invoiceId);
        }

        HoaDon hoaDon = hoaDonOptional.get();
        BigDecimal soTienThanhToan = paymentRequestDTO.getSoTienThanhToan();
        System.out.println("soTienThanhToan: " + soTienThanhToan);
        if (soTienThanhToan == null || soTienThanhToan.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Số tiền thanh toán không hợp lệ: " + soTienThanhToan);
        }

        // Kiểm tra tổng tiền còn lại
        BigDecimal tongTienDaThanhToan = chiTietThanhToanRepository.findByHoaDonId(invoiceId)
                .stream()
                .map(ChiTietThanhToan::getSoTienThanhToan)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal tongTienConLai = hoaDon.getTongTienThanhToan().subtract(tongTienDaThanhToan);
        if (soTienThanhToan.compareTo(tongTienConLai) > 0) {
            throw new IllegalArgumentException("Số tiền thanh toán vượt quá số tiền còn lại: " + tongTienConLai);
        }

        Optional<PhuongThucThanhToan> phuongThucOptional = phuongThucThanhToanRepository.findById(paymentRequestDTO.getIdPhuongThucThanhToan());
        if (phuongThucOptional.isEmpty()) {
            throw new IllegalArgumentException("Phương thức thanh toán không hợp lệ: " + paymentRequestDTO.getIdPhuongThucThanhToan());
        }

        // Lấy và chuẩn hóa trạng thái thanh toán từ DTO
        String trangThaiThanhToan = normalizeTrangThaiThanhToan(paymentRequestDTO.getTrangThaiThanhToan());

        if (trangThaiThanhToan == null) {
            throw new IllegalArgumentException("Trạng thái thanh toán không hợp lệ: " + paymentRequestDTO.getTrangThaiThanhToan());
        }

        ChiTietThanhToan payment = new ChiTietThanhToan();
        payment.setHoaDon(hoaDon);
        payment.setPhuongThucThanhToan(phuongThucOptional.get());
        payment.setSoTienThanhToan(soTienThanhToan.setScale(2, RoundingMode.HALF_UP));
        payment.setGhiChuThanhToan(paymentRequestDTO.getGhiChuThanhToan());
        payment.setThoiGianThanhToan(LocalDateTime.now());

        // Chỉ lưu chuỗi trạng thái đã được chuẩn hóa vào trường trangThaiThanhToan
        payment.setTrangThaiThanhToan(trangThaiThanhToan);

        String maGiaoDich = paymentRequestDTO.getMaGiaoDichNganHang();
        if (maGiaoDich == null || maGiaoDich.isEmpty()) {
            maGiaoDich = phuongThucOptional.get().getId() == 2
                    ? "TXN_BANK_" + UUID.randomUUID().toString()
                    : "TXN_CASH_" + UUID.randomUUID().toString();
        }
        payment.setMaGiaoDichNganHang(maGiaoDich);
        payment.setNganHangThanhToan(phuongThucOptional.get().getId() == 2 ? paymentRequestDTO.getNganHangThanhToan() : null);
        payment.setNgayCapNhat(LocalDateTime.now());

        ChiTietThanhToan savedPayment = chiTietThanhToanRepository.save(payment);
        System.out.println("Saved payment: " + savedPayment.getId() + ", soTienThanhToan: " + savedPayment.getSoTienThanhToan());
        return savedPayment;
    }

    /**
     * Chuẩn hóa trạng thái thanh toán từ chuỗi hoặc ID chuỗi.
     * @param input Chuỗi trạng thái từ frontend.
     * @return Chuỗi trạng thái hợp lệ hoặc null nếu không hợp lệ.
     */
    private String normalizeTrangThaiThanhToan(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }
        String normalizedInput = input.trim().toLowerCase();

        // Trường hợp 1: Input là chuỗi trạng thái hợp lệ
        if (Arrays.asList(VALID_TRANG_THAI_THANH_TOAN).contains(normalizedInput)) {
            return normalizedInput;
        }

        // Trường hợp 2: Input là ID trạng thái dưới dạng chuỗi
        try {
            int id = Integer.parseInt(normalizedInput);
            switch (id) {
                case 17: return "thanh_cong";
                case 18: return "that_bai";
                case 8: return "cho_xu_ly";
                default: return null;
            }
        } catch (NumberFormatException e) {
            // Không phải là số, trả về null
            return null;
        }
    }
}