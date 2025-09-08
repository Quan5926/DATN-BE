package com.example.th02201.repository;


import com.example.th02201.model.AnhSanPham;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface
AnhSanPhamRepository extends JpaRepository<AnhSanPham, Long> { // Changed to Long

    // Tìm tất cả ảnh theo ID của ChiTietSanPham
    List<AnhSanPham> findByChiTietSp_Id(UUID chiTietSpId);

    // Tìm ảnh đại diện cho một ChiTietSanPham
    Optional<AnhSanPham> findByChiTietSp_IdAndLaAnhDaiDienIsTrue(UUID chiTietSpId);

    // Đặt tất cả ảnh của một chi tiết sản phẩm về không phải ảnh đại diện
    @Modifying
    @Transactional
    @Query("UPDATE AnhSanPham a SET a.laAnhDaiDien = false WHERE a.chiTietSp.id = :chiTietSpId")
    void resetLaAnhDaiDienForChiTietSanPham(@Param("chiTietSpId") UUID chiTietSpId);

    // Kiểm tra xem có ảnh nào khác với ID cụ thể là ảnh đại diện không
    boolean existsByChiTietSp_IdAndLaAnhDaiDienIsTrueAndIdNot(UUID chiTietSpId, Long currentImageId); // Changed to Long
}

