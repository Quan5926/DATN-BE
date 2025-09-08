package com.example.th02201.repository;

import com.example.th02201.model.KichCo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KichCoRepository extends JpaRepository<KichCo, Long> {
    // Chọn toàn bộ entity KichCo, không chỉ tên
    @Query("SELECT kc FROM KichCo kc ORDER BY kc.tenKichCo ASC")
    List<KichCo> findAllOrderedByName();

    boolean existsByMaKichCo(String maKichCo);

    Optional<KichCo> findByMaKichCo(String maKichCo);

    // Truy vấn để tính tổng số lượng của ChiTietSanPham liên kết với một KichCo
    // Giả định ChiTietSanPham có trường 'soLuongTonKho' và mối quan hệ 'kichCo'
    @Query("SELECT COALESCE(SUM(ctsp.soLuongTonKho), 0) FROM ChiTietSanPham ctsp WHERE ctsp.kichCo.id = :kichCoId")
    Integer sumSoLuongSanPhamByKichCoId(@Param("kichCoId") Long kichCoId);
}
