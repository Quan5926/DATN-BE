package com.example.th02201.repository;
import com.example.th02201.model.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime; // ✨ Đổi từ OffsetDateTime sang LocalDateTime
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChiTietHoaDonRepository extends JpaRepository<HoaDonChiTiet, Integer>{
    /**
     * Lấy top sản phẩm bán chạy nhất theo số lượng và doanh thu trong khoảng thời gian.
     * Kết quả bao gồm tên sản phẩm, tổng số lượng bán, và tổng doanh thu từ các hóa đơn hoàn thành (id_trang_thai = 21).
     *
     * @param fromDateTime Thời điểm bắt đầu của khoảng thời gian (bao gồm).
     * @param toDateTime Thời điểm kết thúc của khoảng thời gian (bao gồm).
     * @return Danh sách mảng Object, với Object[0] là tenSanPham, Object[1] là tổng số lượng, Object[2] là tổng doanh thu,
     * sắp xếp theo tổng số lượng giảm dần.
     */
    @Query("SELECT sp.tenSanPham, SUM(cthd.soLuong), SUM(cthd.thanhTien) FROM HoaDonChiTiet cthd " +
            "JOIN cthd.hoaDon hd " +
            "JOIN cthd.chiTietSanPham spct " +
            "JOIN spct.sanPham sp " +
            "WHERE hd.ngayTao BETWEEN :fromDateTime AND :toDateTime AND hd.trangThai.id = 21 " +
            "GROUP BY sp.tenSanPham ORDER BY SUM(cthd.soLuong) DESC")
    List<Object[]> getTopSellingProducts(@Param("fromDateTime") LocalDateTime fromDateTime, @Param("toDateTime") LocalDateTime toDateTime);

    /**
     * Lấy doanh thu theo từng sản phẩm trong khoảng thời gian.
     * Kết quả bao gồm tên sản phẩm và tổng doanh thu từ các hóa đơn hoàn thành (id_trang_thai = 21).
     *
     * @param fromDateTime Thời điểm bắt đầu của khoảng thời gian (bao gồm).
     * @param toDateTime Thời điểm kết thúc của khoảng thời gian (bao gồm).
     * @return Danh sách mảng Object, với Object[0] là tenSanPham, Object[1] là tổng doanh thu,
     * sắp xếp theo tổng doanh thu giảm dần.
     */
    @Query("SELECT sp.tenSanPham, SUM(cthd.thanhTien) FROM HoaDonChiTiet cthd " +
            "JOIN cthd.hoaDon hd " +
            "JOIN cthd.chiTietSanPham spct " +
            "JOIN spct.sanPham sp " +
            "WHERE hd.ngayTao BETWEEN :fromDateTime AND :toDateTime AND hd.trangThai.id = 21 " +
            "GROUP BY sp.tenSanPham ORDER BY SUM(cthd.thanhTien) DESC")
    List<Object[]> getProductRevenue(@Param("fromDateTime") LocalDateTime fromDateTime, @Param("toDateTime") LocalDateTime toDateTime);

    Optional<HoaDonChiTiet> findById(Integer chiTietHoaDonId);
}
