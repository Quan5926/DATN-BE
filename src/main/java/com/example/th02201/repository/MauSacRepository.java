package com.example.th02201.repository;


import com.example.th02201.model.MauSac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MauSacRepository extends JpaRepository<MauSac, Long> {

    boolean existsByMaMauSac(String maMauSac);

    List<MauSac> findAllByOrderByTenMauSacAsc();

    Optional<MauSac> findByMaMauSac(String maMauSac);

    // Thêm phương thức truy vấn để tính tổng số lượng sản phẩm chi tiết
    // Giả định ChiTietSanPham có trường 'soLuongTonKho' và mối quan hệ 'mauSac'
    @Query("SELECT COALESCE(SUM(ctsp.soLuongTonKho), 0) FROM ChiTietSanPham ctsp WHERE ctsp.mauSac.id = :mauSacId")
    Integer sumSoLuongSanPhamByMauSacId(@Param("mauSacId") Long mauSacId);
}
