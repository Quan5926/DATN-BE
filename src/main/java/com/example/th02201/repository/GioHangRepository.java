package com.example.th02201.repository;


import com.example.th02201.model.GioHang;
import com.example.th02201.model.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GioHangRepository extends JpaRepository<GioHang, UUID> {
    Optional<GioHang> findByMaPhienGioHang(String maPhienGioHang);

    GioHang findFirstByKhachHang(KhachHang khachHang);

}