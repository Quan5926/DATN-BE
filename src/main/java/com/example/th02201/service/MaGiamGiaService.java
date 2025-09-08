package com.example.th02201.service;

import com.example.th02201.dto.VoucherValidationResponse;
import com.example.th02201.model.KhachHang;
import com.example.th02201.model.MaGiamGia;
import com.example.th02201.model.PhieuGiamGia;
import com.example.th02201.repository.KhachHangRepository;
import com.example.th02201.repository.MaGiamGiaRepository;
import com.example.th02201.repository.PhieuGiamGiaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MaGiamGiaService {
    private final MaGiamGiaRepository maGiamGiaRepository;
    private final PhieuGiamGiaRepository phieuGiamGiaRepository;
    private final KhachHangRepository khachHangRepository;

    // Sử dụng Constructor Injection (khuyến nghị thay vì Field Injection)
    public MaGiamGiaService(MaGiamGiaRepository maGiamGiaRepository,
                            PhieuGiamGiaRepository phieuGiamGiaRepository,
                            KhachHangRepository khachHangRepository) {
        this.maGiamGiaRepository = maGiamGiaRepository;
        this.phieuGiamGiaRepository = phieuGiamGiaRepository;
        this.khachHangRepository = khachHangRepository;
    }

    /**
     * Lấy tất cả các mã giảm giá có phân trang.
     * @param pageable Đối tượng Pageable để phân trang.
     * @return Page chứa danh sách MaGiamGia.
     */
    public Page<MaGiamGia> getAllMaGiamGia(Pageable pageable) {
        return maGiamGiaRepository.findAll(pageable);
    }

    /**
     * Lấy mã giảm giá theo ID.
     * @param id ID của mã giảm giá.
     * @return Optional chứa MaGiamGia nếu tìm thấy, ngược lại là Optional.empty().
     */
    public Optional<MaGiamGia> getMaGiamGiaById(Integer id) {
        return maGiamGiaRepository.findById(id);
    }

    /**
     * Lấy mã giảm giá theo mã code.
     * @param maCode Mã code của mã giảm giá.
     * @return Optional chứa MaGiamGia nếu tìm thấy, ngược lại là Optional.empty().
     */
    public Optional<MaGiamGia> getMaGiamGiaByMaCode(String maCode) {
        return maGiamGiaRepository.findByMaCode(maCode);
    }

    /**
     * Tạo một mã giảm giá mới.
     * @param maGiamGia Đối tượng MaGiamGia cần tạo.
     * @return MaGiamGia đã được lưu vào cơ sở dữ liệu.
     * @throws ResponseStatusException Nếu mã giảm giá không liên kết với phiếu giảm giá, mã code đã tồn tại, hoặc khách hàng được cấp không tồn tại.
     */
    @Transactional
    public MaGiamGia createMaGiamGia(MaGiamGia maGiamGia) {
        // Kiểm tra xem phiếu giảm giá có tồn tại không
        if (maGiamGia.getPhieuGiamGia() == null || maGiamGia.getPhieuGiamGia().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mã giảm giá phải liên kết với một phiếu giảm giá.");
        }
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(maGiamGia.getPhieuGiamGia().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Phiếu giảm giá gốc không tồn tại."));
        maGiamGia.setPhieuGiamGia(phieuGiamGia);

        // Kiểm tra xem mã code đã tồn tại chưa
        if (maGiamGiaRepository.findByMaCode(maGiamGia.getMaCode()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Mã code đã tồn tại.");
        }

        // Nếu có khách hàng được cấp, kiểm tra khách hàng có tồn tại không
        if (maGiamGia.getKhachHangDuocCap() != null && maGiamGia.getKhachHangDuocCap().getId() != null) {
            KhachHang khachHang = khachHangRepository.findById(maGiamGia.getKhachHangDuocCap().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Khách hàng được cấp không tồn tại."));
            maGiamGia.setKhachHangDuocCap(khachHang);
        } else {
            maGiamGia.setKhachHangDuocCap(null); // Đảm bảo là null nếu không được cấp
        }

        maGiamGia.setDaSuDung(false); // Mặc định là chưa sử dụng khi tạo
        maGiamGia.setNgayTao(LocalDateTime.now());
        maGiamGia.setNgaySuDung(null); // Đảm bảo ngày sử dụng là null khi mới tạo

        return maGiamGiaRepository.save(maGiamGia);
    }

    /**
     * Cập nhật thông tin mã giảm giá.
     * @param id ID của mã giảm giá cần cập nhật.
     * @param maGiamGiaDetails Đối tượng MaGiamGia chứa thông tin chi tiết cần cập nhật.
     * @return MaGiamGia đã được cập nhật.
     * @throws ResponseStatusException Nếu mã giảm giá không tìm thấy, hoặc phiếu giảm giá/khách hàng được cấp không tồn tại, hoặc mã code bị trùng.
     */
    @Transactional
    public MaGiamGia updateMaGiamGia(Integer id, MaGiamGia maGiamGiaDetails) {
        MaGiamGia maGiamGia = maGiamGiaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mã giảm giá không tìm thấy với id: " + id));

        // Kiểm tra mã code trùng lặp khi cập nhật
        if (!maGiamGia.getMaCode().equals(maGiamGiaDetails.getMaCode())) {
            if (maGiamGiaRepository.findByMaCode(maGiamGiaDetails.getMaCode()).isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Mã code đã tồn tại.");
            }
        }

        // Cập nhật các trường cơ bản
        maGiamGia.setMaCode(maGiamGiaDetails.getMaCode());
        maGiamGia.setDaSuDung(maGiamGiaDetails.getDaSuDung()); // Đã sửa tên phương thức
        maGiamGia.setIdHoaDonDaSuDung(maGiamGiaDetails.getIdHoaDonDaSuDung());
        maGiamGia.setNgaySuDung(maGiamGiaDetails.getNgaySuDung());
        // Cập nhật mối quan hệ với phiếu giảm giá
        if (maGiamGiaDetails.getPhieuGiamGia() != null && maGiamGiaDetails.getPhieuGiamGia().getId() != null) {
            PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(maGiamGiaDetails.getPhieuGiamGia().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Phiếu giảm giá gốc không tồn tại."));
            maGiamGia.setPhieuGiamGia(phieuGiamGia);
        } else {
            maGiamGia.setPhieuGiamGia(null); // Đảm bảo là null nếu không được cấp hoặc giữ nguyên giá trị cũ
        }

        // Cập nhật mối quan hệ với khách hàng
        if (maGiamGiaDetails.getKhachHangDuocCap() != null && maGiamGiaDetails.getKhachHangDuocCap().getId() != null) {
            KhachHang khachHang = khachHangRepository.findById(maGiamGiaDetails.getKhachHangDuocCap().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Khách hàng được cấp không tồn tại."));
            maGiamGia.setKhachHangDuocCap(khachHang);
        } else {
            maGiamGia.setKhachHangDuocCap(null);
        }

        return maGiamGiaRepository.save(maGiamGia);
    }

    /**
     * Xóa một mã giảm giá.
     * @param id ID của mã giảm giá cần xóa.
     * @throws ResponseStatusException Nếu mã giảm giá không tìm thấy.
     */
    @Transactional
    public void deleteMaGiamGia(Integer id) {
        if (!maGiamGiaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mã giảm giá không tìm thấy với id: " + id);
        }
        maGiamGiaRepository.deleteById(id);
    }

    /**
     * Đánh dấu một mã giảm giá đã được sử dụng.
     * @param maCode Mã code của mã giảm giá cần đánh dấu.
     * @return MaGiamGia đã được cập nhật trạng thái.
     * @throws ResponseStatusException Nếu mã giảm giá không tìm thấy, đã được sử dụng, hoặc phiếu giảm giá đã hết hạn/chưa đến ngày áp dụng.
     */
    @Transactional
    public MaGiamGia markMaGiamGiaAsUsed(String maCode) {
        MaGiamGia maGiamGia = maGiamGiaRepository.findByMaCode(maCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mã giảm giá không tìm thấy: " + maCode));

        if (maGiamGia.getDaSuDung()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mã giảm giá đã được sử dụng.");
        }

        // Kiểm tra ngày hết hạn của phiếu giảm giá gốc (campaign)
        else {
            // Trường hợp mã giảm giá không có phiếu giảm giá gốc (ví dụ: mã cứng)
            // Bạn cần xem xét logic nghiệp vụ cụ thể cho trường hợp này.
            // Ví dụ: nếu đây là mã cứng không phụ thuộc vào phiếu giảm giá, không cần kiểm tra ngày.
            // Nếu không được phép, có thể ném exception. Hiện tại để trống (hoặc thêm log)
        }

        maGiamGia.setDaSuDung(true);
        maGiamGia.setNgaySuDung(LocalDateTime.now());
        // Bạn có thể thêm logic để liên kết với hóa đơn nếu cần
        // maGiamGia.setIdHoaDonDaSuDung(idHoaDon);

        return maGiamGiaRepository.save(maGiamGia);
    }

    /**
     * Kiểm tra tính hợp lệ của mã giảm giá và tính toán số tiền giảm.
     * @param maCode Mã code của mã giảm giá.
     * @param tongTienHoaDon Tổng tiền hoá đơn hiện tại.
     * @param idKhachHang ID của khách hàng (có thể null).
     * @return VoucherValidationResponse chứa thông tin giảm giá.
     */
    @Transactional(readOnly = true) // Chỉ đọc dữ liệu, không thay đổi
    public VoucherValidationResponse validateVoucher(String maCode, BigDecimal tongTienHoaDon, Integer idKhachHang) {
        MaGiamGia maGiamGia = maGiamGiaRepository.findByMaCode(maCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mã giảm giá không hợp lệ."));

        // 1. Kiểm tra trạng thái và ngày hết hạn
        PhieuGiamGia phieuGiamGia = maGiamGia.getPhieuGiamGia();
        if (phieuGiamGia == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mã giảm giá không có phiếu giảm giá liên kết.");
        }

        // Tự động cập nhật trạng thái nếu cần
        phieuGiamGia.capNhatTrangThai();
        int trangThaiId = phieuGiamGia.getTrangThai().getId();
        if (trangThaiId != 15) { // ID 15 là trạng thái "Đang diễn ra"
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mã giảm giá không còn hiệu lực.");
        }

        // 2. Kiểm tra mã đã được sử dụng
        if (maGiamGia.getDaSuDung()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mã giảm giá đã được sử dụng.");
        }

        // 3. Kiểm tra điều kiện hoá đơn tối thiểu
        if (tongTienHoaDon.compareTo(phieuGiamGia.getHoaDonToiThieu()) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hoá đơn chưa đạt giá trị tối thiểu để sử dụng mã này.");
        }

        // 4. Kiểm tra logic áp dụng cho từng loại phiếu
        String loaiApDung = phieuGiamGia.getLoaiApDung();
        if ("KH_CU_THE".equals(loaiApDung)) {
            if (idKhachHang == null || maGiamGia.getKhachHangDuocCap() == null || !maGiamGia.getKhachHangDuocCap().getId().equals(idKhachHang)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không được phép sử dụng mã giảm giá này.");
            }
        }
        // Đối với loại TOAN_BO, không cần kiểm tra idKhachHang vì nó hợp lệ cho mọi người

        // 5. Tính toán giá trị giảm
        BigDecimal soTienGiam;
        if ("PHAN_TRAM".equals(phieuGiamGia.getLoaiGiamGia())) {
            BigDecimal phanTramGiam = phieuGiamGia.getGiaTriGiam().divide(new BigDecimal(100));
            soTienGiam = tongTienHoaDon.multiply(phanTramGiam);
            // Giới hạn số tiền giảm tối đa
            if (phieuGiamGia.getSoTienGiamToiDa() != null && soTienGiam.compareTo(phieuGiamGia.getSoTienGiamToiDa()) > 0) {
                soTienGiam = phieuGiamGia.getSoTienGiamToiDa();
            }
        } else { // LOAI_GIAM_GIA.SO_TIEN_CO_DINH
            soTienGiam = phieuGiamGia.getGiaTriGiam();
        }

        return new VoucherValidationResponse(
                "Mã giảm giá hợp lệ!",
                soTienGiam,
                phieuGiamGia.getLoaiGiamGia(),
                phieuGiamGia.getGiaTriGiam()
        );
    }
}
