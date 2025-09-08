package com.example.th02201.repository;

import com.example.th02201.model.*;
import com.example.th02201.respone.ChiTietSanPhamRespone;
import jakarta.transaction.Transactional;
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
public interface ChiTietSanPhamRepository extends JpaRepository<ChiTietSanPham, UUID> {

    // ========================== DTO hiển thị ==========================
    @Query("""
        SELECT new com.example.th02201.respone.ChiTietSanPhamRespone
        (d.id, d.sanPham.tenSanPham, d.sanPham.maSanPham, d.giaBan, d.soLuongTonKho)
        FROM ChiTietSanPham d
        """)
    List<ChiTietSanPhamRespone> layDanhSachctsp();

    @Query("""
        SELECT new com.example.th02201.respone.ChiTietSanPhamRespone
        (d.id, d.sanPham.tenSanPham, d.sanPham.maSanPham, d.giaBan, d.soLuongTonKho)
        FROM ChiTietSanPham d
        """)
    Page<ChiTietSanPhamRespone> phanTrangDanhSachctsp(Pageable pageable);

    // ========================== Truy vấn chi tiết ==========================
    ChiTietSanPham findFirstByChatLieu(ChatLieu chatLieu);
    ChiTietSanPham findFirstByMauSac(MauSac mauSac);
    ChiTietSanPham findFirstByKichCo(KichCo kichCo);
    ChiTietSanPham findFirstBySanPham(SanPham sanPham);
    ChiTietSanPham findFirstByTrangThaiRieng(TrangThai trangThai);

    @Query("SELECT ctsp FROM ChiTietSanPham ctsp " +
            "JOIN FETCH ctsp.sanPham sp " +
            "JOIN FETCH ctsp.chatLieu cl " +
            "JOIN FETCH ctsp.mauSac ms " +
            "JOIN FETCH ctsp.kichCo kc " +
            "JOIN FETCH ctsp.trangThaiRieng ttr " +
            "LEFT JOIN FETCH ctsp.anhSanPhams asp " +
            "JOIN FETCH sp.thuongHieu th " +
            "JOIN FETCH sp.danhMuc dm ")
    List<ChiTietSanPham> findAllWithDetails();

    @Query("SELECT ctsp FROM ChiTietSanPham ctsp " +
            "JOIN FETCH ctsp.sanPham sp " +
            "JOIN FETCH ctsp.chatLieu cl " +
            "JOIN FETCH ctsp.mauSac ms " +
            "JOIN FETCH ctsp.kichCo kc " +
            "JOIN FETCH ctsp.trangThaiRieng ttr " +
            "LEFT JOIN FETCH ctsp.anhSanPhams asp " +
            "JOIN FETCH sp.thuongHieu th " +
            "JOIN FETCH sp.danhMuc dm " +
            "WHERE ctsp.sanPham.id = :sanPhamId")
    List<ChiTietSanPham> findBySanPhamId(@Param("sanPhamId") Long sanPhamId);

    @Query("SELECT ctsp FROM ChiTietSanPham ctsp " +
            "JOIN FETCH ctsp.sanPham sp " +
            "JOIN FETCH ctsp.chatLieu cl " +
            "JOIN FETCH ctsp.mauSac ms " +
            "JOIN FETCH ctsp.kichCo kc " +
            "JOIN FETCH ctsp.trangThaiRieng ttr " +
            "LEFT JOIN FETCH ctsp.anhSanPhams asp " +
            "JOIN FETCH sp.thuongHieu th " +
            "JOIN FETCH sp.danhMuc dm " +
            "WHERE (sp.tenSanPham LIKE %:keyword% OR ctsp.maCtsp LIKE %:keyword%)")
    List<ChiTietSanPham> findByKeyword(@Param("keyword") String keyword);

    @Query("SELECT ctsp FROM ChiTietSanPham ctsp " +
            "JOIN FETCH ctsp.sanPham sp " +
            "JOIN FETCH ctsp.chatLieu cl " +
            "JOIN FETCH ctsp.mauSac ms " +
            "JOIN FETCH ctsp.kichCo kc " +
            "JOIN FETCH ctsp.trangThaiRieng ttr " +
            "LEFT JOIN FETCH ctsp.anhSanPhams asp " +
            "JOIN FETCH sp.thuongHieu th " +
            "JOIN FETCH sp.danhMuc dm " +
            "WHERE ctsp.chatLieu.id = :chatLieuId")
    List<ChiTietSanPham> findByChatLieuId(@Param("chatLieuId") Long chatLieuId);

    @Query("SELECT ctsp FROM ChiTietSanPham ctsp " +
            "JOIN FETCH ctsp.sanPham sp " +
            "JOIN FETCH ctsp.chatLieu cl " +
            "JOIN FETCH ctsp.mauSac ms " +
            "JOIN FETCH ctsp.kichCo kc " +
            "JOIN FETCH ctsp.trangThaiRieng ttr " +
            "LEFT JOIN FETCH ctsp.anhSanPhams asp " +
            "JOIN FETCH sp.thuongHieu th " +
            "JOIN FETCH sp.danhMuc dm " +
            "WHERE ctsp.mauSac.id = :mauSacId")
    List<ChiTietSanPham> findByMauSacId(@Param("mauSacId") Long mauSacId);

    @Query("SELECT ctsp FROM ChiTietSanPham ctsp " +
            "JOIN FETCH ctsp.sanPham sp " +
            "JOIN FETCH ctsp.chatLieu cl " +
            "JOIN FETCH ctsp.mauSac ms " +
            "JOIN FETCH ctsp.kichCo kc " +
            "JOIN FETCH ctsp.trangThaiRieng ttr " +
            "LEFT JOIN FETCH ctsp.anhSanPhams asp " +
            "JOIN FETCH sp.thuongHieu th " +
            "JOIN FETCH sp.danhMuc dm " +
            "WHERE ctsp.kichCo.id = :kichCoId")
    List<ChiTietSanPham> findByKichCoId(@Param("kichCoId") Long kichCoId);

    @Query("SELECT ctsp FROM ChiTietSanPham ctsp " +
            "JOIN FETCH ctsp.sanPham sp " +
            "JOIN FETCH sp.trangThai ts " +
            "JOIN FETCH ctsp.chatLieu cl " +
            "JOIN FETCH ctsp.mauSac ms " +
            "JOIN FETCH ctsp.kichCo kc " +
            "JOIN FETCH ctsp.trangThaiRieng ttr " +
            "LEFT JOIN FETCH ctsp.anhSanPhams asp " +
            "JOIN FETCH sp.thuongHieu th " +
            "JOIN FETCH sp.danhMuc dm " +
            "WHERE (:sanPhamId IS NULL OR sp.id = :sanPhamId) " +
            "AND (:thuongHieuId IS NULL OR sp.thuongHieu.id = :thuongHieuId) " +
            "AND (:danhMucId IS NULL OR sp.danhMuc.id = :danhMucId) " +
            "AND (:chatLieuId IS NULL OR ctsp.chatLieu.id = :chatLieuId) " +
            "AND (:mauSacId IS NULL OR ctsp.mauSac.id = :mauSacId) " +
            "AND (:kichCoId IS NULL OR ctsp.kichCo.id = :kichCoId) " +
            "AND (:idTrangThaiRieng IS NULL OR ctsp.trangThaiRieng.id = :idTrangThaiRieng) " +
            "AND (:keyword IS NULL OR sp.tenSanPham LIKE %:keyword% OR ctsp.maCtsp LIKE %:keyword%)")
    List<ChiTietSanPham> findByFilters(
            @Param("sanPhamId") Long sanPhamId,
            @Param("thuongHieuId") Long thuongHieuId,
            @Param("danhMucId") Long danhMucId,
            @Param("chatLieuId") Long chatLieuId,
            @Param("mauSacId") Long mauSacId,
            @Param("kichCoId") Long kichCoId,
            @Param("idTrangThaiRieng") Long idTrangThaiRieng,
            @Param("keyword") String keyword);

    @Modifying
    @Transactional
    @Query("UPDATE ChiTietSanPham c SET c.trangThaiRieng = :trangThaiRieng WHERE c.id = :id")
    void updateTrangThaiSanPhamRiengById(@Param("id") UUID id, @Param("trangThaiRieng") TrangThai trangThaiRieng);

    @Query("SELECT ctsp FROM ChiTietSanPham ctsp " +
            "JOIN FETCH ctsp.sanPham sp " +
            "JOIN FETCH ctsp.chatLieu cl " +
            "JOIN FETCH ctsp.mauSac ms " +
            "JOIN FETCH ctsp.kichCo kc " +
            "JOIN FETCH ctsp.trangThaiRieng ttr " +
            "LEFT JOIN FETCH ctsp.anhSanPhams asp " +
            "JOIN FETCH sp.thuongHieu th " +
            "JOIN FETCH sp.danhMuc dm " +
            "WHERE ctsp.sanPham = :sanPham")
    List<ChiTietSanPham> findBySanPham(@Param("sanPham") SanPham sanPham);

    @Query("SELECT ctsp FROM ChiTietSanPham ctsp " +
            "JOIN FETCH ctsp.sanPham sp " +
            "JOIN FETCH ctsp.chatLieu cl " +
            "JOIN FETCH ctsp.mauSac ms " +
            "JOIN FETCH ctsp.kichCo kc " +
            "JOIN FETCH ctsp.trangThaiRieng ttr " +
            "LEFT JOIN FETCH ctsp.anhSanPhams asp " +
            "JOIN FETCH sp.thuongHieu th " +
            "JOIN FETCH sp.danhMuc dm " +
            "WHERE ctsp.id = :id")
    Optional<ChiTietSanPham> findByIdWithDetailsAndImages(@Param("id") UUID id);

    @Query("SELECT ctsp FROM ChiTietSanPham ctsp " +
            "JOIN FETCH ctsp.sanPham sp " +
            "JOIN FETCH ctsp.chatLieu cl " +
            "JOIN FETCH ctsp.mauSac ms " +
            "JOIN FETCH ctsp.kichCo kc " +
            "WHERE sp.danhMuc.id = :danhMucId")
    List<ChiTietSanPham> findBySanPhamDanhMucId(@Param("danhMucId") Long danhMucId);

    @Query("SELECT ctsp FROM ChiTietSanPham ctsp " +
            "JOIN FETCH ctsp.sanPham sp " +
            "JOIN FETCH ctsp.chatLieu cl " +
            "JOIN FETCH ctsp.mauSac ms " +
            "JOIN FETCH ctsp.kichCo kc " +
            "WHERE sp.thuongHieu.id = :thuongHieuId")
    List<ChiTietSanPham> findBySanPhamThuongHieuId(@Param("thuongHieuId") Long thuongHieuId);

    ChiTietSanPham findByMaCtsp(String maCtsp);

    @Query("SELECT ctsp FROM ChiTietSanPham ctsp WHERE ctsp.chatLieu.id = :chatLieuId")
    List<ChiTietSanPham> findAllByChatLieuId(Long chatLieuId);



    @Query("""
    SELECT new com.example.th02201.respone.ChiTietSanPhamRespone(
        ctsp.id,
        sp.maSanPham,
        sp.tenSanPham,
        ctsp.giaBan,
        ctsp.soLuongTonKho,
        ms.tenMauSac,
        kt.tenKichCo
    )
    FROM ChiTietSanPham ctsp
    JOIN ctsp.sanPham sp
    JOIN ctsp.mauSac ms
    JOIN ctsp.kichCo kt
""")
    Page<ChiTietSanPhamRespone> phanTranhDanhSachctsp(Pageable pageable);

}
