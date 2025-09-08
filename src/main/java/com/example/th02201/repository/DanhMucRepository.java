package com.example.th02201.repository;

import com.example.th02201.model.DanhMuc;
import com.example.th02201.model.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DanhMucRepository extends JpaRepository<DanhMuc, Long> {

    DanhMuc findFirstBySanPhams(SanPham sanPham);
    // Tìm danh mục theo mã danh mục
    Optional<DanhMuc> findByMaDanhMuc(String maDanhMuc);


    // Thêm phương thức này để lấy danh mục cho combobox
    @Query("SELECT d FROM DanhMuc d ORDER BY d.tenDanhMuc ASC")
    List<DanhMuc> findAllOrderedByName();

    // Truy vấn để tính tổng số lượng của ChiTietSanPham liên kết với một DanhMuc
    // Giả định ChiTietSanPham có trường 'soLuongTonKho' và mối quan hệ 'sanPham',
    // và SanPham có mối quan hệ 'danhMuc'.
    @Query("SELECT COALESCE(SUM(ctsp.soLuongTonKho), 0) FROM ChiTietSanPham ctsp JOIN ctsp.sanPham sp WHERE sp.danhMuc.id = :danhMucId")
    Integer sumSoLuongSanPhamByDanhMucId(@Param("danhMucId") Long danhMucId);
}
