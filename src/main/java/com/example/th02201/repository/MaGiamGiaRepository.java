package com.example.th02201.repository;

import com.example.th02201.model.MaGiamGia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MaGiamGiaRepository extends JpaRepository<MaGiamGia, Integer> {
    Optional<MaGiamGia> findByMaCode(String maCode);
    List<MaGiamGia> findByPhieuGiamGiaId(UUID phieuGiamGiaId);
    List<MaGiamGia> findByPhieuGiamGiaIdAndDaSuDungFalse(UUID phieuGiamGiaId);

    Page<MaGiamGia> findAll(Pageable pageable);

    // Thêm phương thức xóa tất cả mã giảm giá theo ID phiếu
    @Modifying
    @Query("DELETE FROM MaGiamGia m WHERE m.phieuGiamGia.id = :phieuGiamGiaId")
    void deleteByPhieuGiamGiaId(@Param("phieuGiamGiaId") UUID phieuGiamGiaId);
}
