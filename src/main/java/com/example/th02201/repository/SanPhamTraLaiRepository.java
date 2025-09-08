package com.example.th02201.repository;

import com.example.th02201.model.SanPhamTraLai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SanPhamTraLaiRepository extends JpaRepository<SanPhamTraLai, Long> {
    List<SanPhamTraLai> findByChiTietHoaDonId(Integer chiTietHoaDonId); // Đã đúng
}