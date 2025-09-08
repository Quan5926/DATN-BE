package com.example.th02201.repository;

import com.example.th02201.dto.GioHangChiTietDTO;
import com.example.th02201.model.ChiTietSanPham;
import com.example.th02201.model.GioHang;
import com.example.th02201.model.GioHangChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface GioHangChiTietRepository extends JpaRepository<GioHangChiTiet, Long> {

    GioHangChiTiet findFirstByGioHang(GioHang gioHang);

    GioHangChiTiet findFirstByChiTietSp(ChiTietSanPham chiTietSanPham);

    Optional<GioHangChiTiet> findByGioHangAndChiTietSp(GioHang gioHang, ChiTietSanPham chiTietSanPham);

    // Sửa lại truy vấn để lấy chi tiết giỏ hàng theo maPhienGioHang của giỏ hàng
    @Query("SELECT new com.example.th02201.dto.GioHangChiTietDTO(" +
            "ghct.id, ghct.soLuong, ghct.giaBanHienTai, ghct.ngayThemVao, " +
            "gh.id, ctsp.id, " +
            "gh.maPhienGioHang, ctsp.maCtsp, " +
            "sp.id, sp.tenSanPham, sp.moTaSanPham, ctsp.giaBan, sp.urlAnhDaiDien, " +
            "kc.id, kc.tenKichCo, " +
            "kh.id, ms.maMauSac, ms.tenMauSac, " +
            "ctsp.soLuongTonKho) " +
            "FROM GioHangChiTiet ghct " +
            "JOIN ghct.gioHang gh " +
            "JOIN ghct.chiTietSp ctsp " +
            "LEFT JOIN gh.khachHang kh " +
            "LEFT JOIN ctsp.sanPham sp " +
            "LEFT JOIN ctsp.kichCo kc " +
            "LEFT JOIN ctsp.mauSac ms " +
            "WHERE gh.maPhienGioHang = :maPhienGioHang")
    List<GioHangChiTietDTO> findCartDetailsBySessionCode(@Param("maPhienGioHang") String maPhienGioHang);

    // Sửa lại truy vấn để lấy chi tiết giỏ hàng theo ID giỏ hàng
    @Query("SELECT new com.example.th02201.dto.GioHangChiTietDTO(" +
            "ghct.id, ghct.soLuong, ghct.giaBanHienTai, ghct.ngayThemVao, " +
            "gh.id, ctsp.id, " +
            "gh.maPhienGioHang, ctsp.maCtsp, " +
            "sp.id, sp.tenSanPham, sp.moTaSanPham, ctsp.giaBan, sp.urlAnhDaiDien, " +
            "kc.id, kc.tenKichCo, " +
            "kh.id, ms.maMauSac, ms.tenMauSac, " +
            "ctsp.soLuongTonKho) " +
            "FROM GioHangChiTiet ghct " +
            "JOIN ghct.gioHang gh " +
            "JOIN ghct.chiTietSp ctsp " +
            "LEFT JOIN gh.khachHang kh " +
            "LEFT JOIN ctsp.sanPham sp " +
            "LEFT JOIN ctsp.kichCo kc " +
            "LEFT JOIN ctsp.mauSac ms " +
            "WHERE gh.id = :idGioHang")
    List<GioHangChiTietDTO> findCartDetailsByGioHangId(@Param("idGioHang") UUID idGioHang);

    // Sửa lại truy vấn để lấy chi tiết giỏ hàng theo ID khách hàng
    @Query("SELECT new com.example.th02201.dto.GioHangChiTietDTO(" +
            "ghct.id, ghct.soLuong, ghct.giaBanHienTai, ghct.ngayThemVao, " +
            "gh.id, ctsp.id, " +
            "gh.maPhienGioHang, ctsp.maCtsp, " +
            "sp.id, sp.tenSanPham, sp.moTaSanPham, ctsp.giaBan, sp.urlAnhDaiDien, " +
            "kc.id, kc.tenKichCo, " +
            "kh.id, ms.maMauSac, ms.tenMauSac, " +
            "ctsp.soLuongTonKho) " +
            "FROM GioHangChiTiet ghct " +
            "JOIN ghct.gioHang gh " +
            "JOIN gh.khachHang kh " +
            "JOIN ghct.chiTietSp ctsp " +
            "LEFT JOIN ctsp.sanPham sp " +
            "LEFT JOIN ctsp.kichCo kc " +
            "LEFT JOIN ctsp.mauSac ms " +
            "WHERE kh.id = :idKhachHang")
    List<GioHangChiTietDTO> findCartDetailsByKhachHangID(@Param("idKhachHang") Integer idKhachHang);
}
