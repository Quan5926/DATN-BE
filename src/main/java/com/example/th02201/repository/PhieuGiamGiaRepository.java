package com.example.th02201.repository;

import com.example.th02201.model.PhieuGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PhieuGiamGiaRepository extends JpaRepository<PhieuGiamGia, UUID>, JpaSpecificationExecutor<PhieuGiamGia> {
    Optional<PhieuGiamGia> findByMaPhieuGiamGia(String maPhieuGiamGia);
    Optional<PhieuGiamGia> findByTenPhieuGiamGia(String tenPhieuGiamGia);
}
