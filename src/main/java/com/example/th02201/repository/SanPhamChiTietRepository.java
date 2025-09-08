package com.example.th02201.repository;

import com.example.th02201.model.ChiTietSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SanPhamChiTietRepository extends JpaRepository<ChiTietSanPham, UUID> {

    /**
     * Lấy danh sách sản phẩm tồn kho thấp (dưới 10 đơn vị).
     * Trả về thông tin tên sản phẩm, số lượng tồn kho, màu sắc và kích cỡ.
     *
     * @return Danh sách mảng Object, với Object[0] là tenSanPham, Object[1] là soLuongTon,
     * Object[2] là tenMauSac, Object[3] là tenKichCo, sắp xếp theo số lượng tồn kho tăng dần.
     */
    @Query("SELECT sp.tenSanPham, spct.soLuongTonKho, ms.tenMauSac, s.tenKichCo " + // ✨ Đã sửa: tenSize thành tenKichCo
            "FROM ChiTietSanPham spct " +
            "JOIN spct.sanPham sp " +
            "LEFT JOIN spct.mauSac ms " +
            "LEFT JOIN spct.kichCo s " +
            "WHERE spct.soLuongTonKho < 10 ORDER BY spct.soLuongTonKho ASC")
    List<Object[]> getLowStockProducts();

    /**
     * Lấy thống kê kho hàng, bao gồm tổng số sản phẩm khác nhau và tổng số lượng tồn kho.
     *
     * @return Danh sách mảng Object, với Object[0] là số lượng sản phẩm khác nhau (COUNT DISTINCT sanPham.id),
     * Object[1] là tổng số lượng tồn kho (SUM soLuongTon).
     */
    @Query("SELECT COUNT(DISTINCT spct.sanPham.id), SUM(spct.soLuongTonKho) FROM ChiTietSanPham spct")
    List<Object[]> getInventoryStats();
}
