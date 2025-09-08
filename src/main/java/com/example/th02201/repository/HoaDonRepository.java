package com.example.th02201.repository;

import com.example.th02201.model.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, UUID> {

    List<HoaDon> findByNgayTaoBetweenAndTrangThaiId(LocalDateTime fromDateTime, LocalDateTime toDateTime, Integer idTrangThai);

    @Query("SELECT COALESCE(SUM(hd.tongTienThanhToan), 0) FROM HoaDon hd WHERE hd.ngayTao BETWEEN :fromDateTime AND :toDateTime AND hd.trangThai.id = 21")
    BigDecimal getTotalRevenue(@Param("fromDateTime") LocalDateTime fromDateTime, @Param("toDateTime") LocalDateTime toDateTime);

    @Query("SELECT COUNT(hd.id) FROM HoaDon hd WHERE hd.ngayTao BETWEEN :fromDateTime AND :toDateTime")
    Long getOrderCount(@Param("fromDateTime") LocalDateTime fromDateTime, @Param("toDateTime") LocalDateTime toDateTime);

    @Query(value = "SELECT COUNT(DISTINCT hd.khachHang.id) FROM HoaDon hd " +
            "WHERE hd.khachHang IS NOT NULL " +
            "AND hd.ngayTao BETWEEN :fromDateTime AND :toDateTime " +
            "AND hd.khachHang.id NOT IN (SELECT subHd.khachHang.id FROM HoaDon subHd WHERE subHd.ngayTao < :fromDateTime AND subHd.khachHang IS NOT NULL)")
    Long getNewCustomers(@Param("fromDateTime") LocalDateTime fromDateTime, @Param("toDateTime") LocalDateTime toDateTime);

    @Query("SELECT t.id, COUNT(hd.id) FROM HoaDon hd JOIN hd.trangThai t WHERE hd.ngayTao BETWEEN :fromDateTime AND :toDateTime GROUP BY t.id ORDER BY t.id")
    List<Object[]> getOrderStatusData(@Param("fromDateTime") LocalDateTime fromDateTime, @Param("toDateTime") LocalDateTime toDateTime);

    @Query(value = "SELECT FORMAT(hd.ngay_tao, 'yyyy-MM-dd') AS ngay, SUM(hd.tong_tien_thanh_toan) AS doanh_thu " +
            "FROM hoa_don hd " +
            "WHERE hd.ngay_tao BETWEEN :fromDateTime AND :toDateTime AND hd.id_trang_thai = 21 " +
            "GROUP BY FORMAT(hd.ngay_tao, 'yyyy-MM-dd') " +
            "ORDER BY ngay",
            nativeQuery = true)
    List<Object[]> getRevenueData(@Param("fromDateTime") LocalDateTime fromDateTime, @Param("toDateTime") LocalDateTime toDateTime);

    @Query(value = "SELECT DATEPART(weekday, hd.ngay_tao) AS thu_trong_tuan, SUM(hd.tong_tien_thanh_toan) AS doanh_thu " +
            "FROM hoa_don hd " +
            "WHERE hd.ngay_tao BETWEEN :fromDateTime AND :toDateTime AND hd.id_trang_thai = 21 " +
            "GROUP BY DATEPART(weekday, hd.ngay_tao) " +
            "ORDER BY thu_trong_tuan",
            nativeQuery = true)
    List<Object[]> getRevenueByWeekDay(@Param("fromDateTime") LocalDateTime fromDateTime, @Param("toDateTime") LocalDateTime toDateTime);

    @Query(value = "SELECT DATEPART(hour, hd.ngay_tao) AS gio_trong_ngay, SUM(hd.tong_tien_thanh_toan) AS doanh_thu " +
            "FROM hoa_don hd " +
            "WHERE hd.ngay_tao BETWEEN :fromDateTime AND :toDateTime AND hd.id_trang_thai = 21 " +
            "GROUP BY DATEPART(hour, hd.ngay_tao) " +
            "ORDER BY gio_trong_ngay",
            nativeQuery = true)
    List<Object[]> getRevenueByHour(@Param("fromDateTime") LocalDateTime fromDateTime, @Param("toDateTime") LocalDateTime toDateTime);

    List<HoaDon> findByNgayTaoBetween(LocalDateTime fromDateTime, LocalDateTime toDateTime);

    Long countByTrangThaiIdAndNgayTaoBetween(Integer idTrangThai, LocalDateTime fromDateTime, LocalDateTime toDateTime);

    Long countByNgayTaoBetween(LocalDateTime fromDateTime, LocalDateTime toDateTime);

    @Query(value = "SELECT COALESCE(SUM(hd.tong_tien_thanh_toan), 0) FROM hoa_don hd " +
            "WHERE CAST(hd.ngay_tao AS DATE) = CAST(GETDATE() AS DATE) AND hd.id_trang_thai = 21",
            nativeQuery = true)
    BigDecimal getTotalRevenueForToday();

    @Query(value = "SELECT COALESCE(SUM(hd.tong_tien_thanh_toan), 0) FROM hoa_don hd " +
            "WHERE hd.ngay_tao BETWEEN DATEADD(wk, DATEDIFF(wk, 0, GETDATE()), 0) AND DATEADD(wk, DATEDIFF(wk, 0, GETDATE()), 6) AND hd.id_trang_thai = 21",
            nativeQuery = true)
    BigDecimal getTotalRevenueForCurrentWeek();

    @Query(value = "SELECT COALESCE(SUM(hd.tong_tien_thanh_toan), 0) FROM hoa_don hd " +
            "WHERE YEAR(hd.ngay_tao) = YEAR(GETDATE()) AND MONTH(hd.ngay_tao) = MONTH(GETDATE()) AND hd.id_trang_thai = 21",
            nativeQuery = true)
    BigDecimal getTotalRevenueForCurrentMonth();

    @Query("SELECT h FROM HoaDon h " +
            "JOIN FETCH h.trangThai " +
            "LEFT JOIN FETCH h.lichSuThanhToan lstt " +
            "LEFT JOIN FETCH lstt.phuongThucThanhToan " +
            "LEFT JOIN FETCH h.maGiamGia mgg " +
            "LEFT JOIN FETCH mgg.phieuGiamGia " +
            "LEFT JOIN FETCH h.chiTietHoaDon cthd " +
            "LEFT JOIN FETCH cthd.chiTietSanPham spct " +
            "LEFT JOIN FETCH spct.sanPham " +
            "LEFT JOIN FETCH spct.chatLieu " +
            "LEFT JOIN FETCH spct.mauSac " +
            "LEFT JOIN FETCH spct.kichCo " +
            "LEFT JOIN FETCH h.sanPhamTraLai sptl")
    List<HoaDon> findAllWithDetails();

    @Query("SELECT hd FROM HoaDon hd " +
            "JOIN FETCH hd.trangThai " +
            "LEFT JOIN FETCH hd.chiTietHoaDon cthd " +
            "LEFT JOIN FETCH cthd.chiTietSanPham spct " +
            "LEFT JOIN FETCH spct.sanPham sp " +
            "LEFT JOIN FETCH spct.chatLieu cl " +
            "LEFT JOIN FETCH spct.mauSac ms " +
            "LEFT JOIN FETCH spct.kichCo sz " +
            "LEFT JOIN FETCH hd.sanPhamTraLai sptl " +
            "LEFT JOIN FETCH sptl.chiTietHoaDon cthd_return " +
            "LEFT JOIN FETCH cthd_return.chiTietSanPham spct_return " +
            "LEFT JOIN FETCH spct_return.sanPham sp_return " +
            "LEFT JOIN FETCH spct_return.chatLieu cl_return " +
            "LEFT JOIN FETCH spct_return.mauSac ms_return " +
            "LEFT JOIN FETCH spct_return.kichCo sz_return " +
            "LEFT JOIN FETCH hd.lichSuThanhToan lstt " +
            "LEFT JOIN FETCH lstt.phuongThucThanhToan pttt " +
            "LEFT JOIN FETCH hd.lichSuHoaDon lstt_hd " +
            "LEFT JOIN FETCH hd.khachHang kh " +
            "LEFT JOIN FETCH kh.taiKhoan tk " +
            "LEFT JOIN FETCH hd.maGiamGia mgg " +
            "LEFT JOIN FETCH mgg.phieuGiamGia pgg " +
            "LEFT JOIN FETCH hd.nhanVienTao nv " +
            "LEFT JOIN FETCH hd.diaChiGiaoHang dcgh " +
            "LEFT JOIN FETCH dcgh.phuongXa px " +
            "LEFT JOIN FETCH px.quanHuyen qh " +
            "LEFT JOIN FETCH qh.tinhThanh tt_dc " +
            "WHERE hd.id = :id")
    Optional<HoaDon> findByIdWithDetails(@Param("id") UUID id);

    // --- Phương thức mới được thêm vào ---

    /**
     * Lấy danh sách khách hàng chi tiêu nhiều nhất trong khoảng thời gian đã chọn.
     * Trả về: List<[idKhachHang, tenKhachHang, soDonHang, tongTienChiTieu]>
     */
    @Query(value = "SELECT hd.khachHang.id, hd.khachHang.tenKhachHang, COUNT(hd.id), SUM(hd.tongTienThanhToan) " +
            "FROM HoaDon hd " +
            "WHERE hd.ngayTao BETWEEN :startDate AND :endDate " +
            "AND hd.trangThai.id = 21 " + // 21 là trạng thái Đã hoàn thành
            "AND hd.khachHang IS NOT NULL " +
            "GROUP BY hd.khachHang.id, hd.khachHang.tenKhachHang " +
            "ORDER BY SUM(hd.tongTienThanhToan) DESC")
    List<Object[]> getTopCustomersByTotalSpent(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * Đếm số lượng khách hàng duy nhất đã mua hàng trong khoảng thời gian.
     */
    @Query("SELECT COUNT(DISTINCT hd.khachHang.id) FROM HoaDon hd WHERE hd.ngayTao BETWEEN :startDate AND :endDate AND hd.khachHang IS NOT NULL")
    Long countDistinctCustomersInPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}