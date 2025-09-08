package com.example.th02201.repository;


import com.example.th02201.model.ThuongHieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThuongHieuRepository extends JpaRepository<ThuongHieu, Long> {
    // Tìm thương hiệu theo mã thương hiệu
    Optional<ThuongHieu> findByMaThuongHieu(String maThuongHieu);

    // Thêm phương thức này để lấy danh sách thương hiệu đã sắp xếp theo tên
    @Query("SELECT th FROM ThuongHieu th ORDER BY th.tenThuongHieu ASC")
    List<ThuongHieu> findAllOrderedByName();

    // Truy vấn để tính tổng số lượng của ChiTietSanPham liên kết với một Thương hiệu
    // Giả định ChiTietSanPham có trường 'soLuong' và mối quan hệ 'sanPham',
    // và SanPham có mối quan hệ 'thuongHieu'.
    @Query("SELECT COALESCE(SUM(ctsp.soLuongTonKho), 0) FROM ChiTietSanPham ctsp JOIN ctsp.sanPham sp WHERE sp.thuongHieu.id = :thuongHieuId")
    Integer sumSoLuongSanPhamByThuongHieuId(@Param("thuongHieuId") Long thuongHieuId);
}

