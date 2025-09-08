package com.example.th02201.repository;

import com.example.th02201.model.NhanVien;
import com.example.th02201.respone.NhanVienRespone;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> {
    @Query("""
        SELECT
        new com.example.th02201.respone.NhanVienRespone
        (d.id, d.tenNhanVien, d.ngaySinh, d.gioiTinh, d.soDienThoai, d.maNhanVien, d.cCCD,
        d.diaChiSoNhaTenDuong, d.diaChiPhuongXa,
        d.diaChiQuanHuyen, d.diaChiTinhThanh, d.ngayTao, d.ngayCapNhat,
        COALESCE(d.trangThai, false))
        FROM NhanVien d
        ORDER BY d.id ASC
        """)
    List<NhanVienRespone> layDanhSachDRepone();

    @Query("""
        SELECT
        new com.example.th02201.respone.NhanVienRespone
        (d.id, d.tenNhanVien, d.ngaySinh, d.gioiTinh, d.soDienThoai, d.maNhanVien, d.cCCD,
        d.diaChiSoNhaTenDuong, d.diaChiPhuongXa,
        d.diaChiQuanHuyen, d.diaChiTinhThanh, d.ngayTao, d.ngayCapNhat,
        COALESCE(d.trangThai, false))
        FROM NhanVien d
        ORDER BY d.id ASC
        """)
    Page<NhanVienRespone> phanTranhDanhSach(Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM NhanVien d WHERE d.maNhanVien = ?1")
    void deleteDMa(String ma);

    @Query("""
        SELECT
        new com.example.th02201.respone.NhanVienRespone
        (d.id, d.tenNhanVien, d.ngaySinh, d.gioiTinh, d.soDienThoai, d.maNhanVien, d.cCCD,
        d.diaChiSoNhaTenDuong, d.diaChiPhuongXa,
        d.diaChiQuanHuyen, d.diaChiTinhThanh, d.ngayTao, d.ngayCapNhat,
        COALESCE(d.trangThai, false))
        FROM NhanVien d
        WHERE d.id = ?1
        """)
    NhanVienRespone detailNV(Integer id);

    @Query("""
        SELECT
        new com.example.th02201.respone.NhanVienRespone
        (d.id, d.tenNhanVien, d.ngaySinh, d.gioiTinh, d.soDienThoai, d.maNhanVien, d.cCCD,
        d.diaChiSoNhaTenDuong, d.diaChiPhuongXa,
        d.diaChiQuanHuyen, d.diaChiTinhThanh, d.ngayTao, d.ngayCapNhat,
        COALESCE(d.trangThai, false))
        FROM NhanVien d
        WHERE d.maNhanVien = ?1
        """)
    List<NhanVienRespone> Search(String ma);

    @Query("""
        SELECT
        new com.example.th02201.respone.NhanVienRespone
        (d.id, d.tenNhanVien, d.ngaySinh, d.gioiTinh, d.soDienThoai, d.maNhanVien, d.cCCD,
        d.diaChiSoNhaTenDuong, d.diaChiPhuongXa,
        d.diaChiQuanHuyen, d.diaChiTinhThanh, d.ngayTao, d.ngayCapNhat,
        COALESCE(d.trangThai, false))
        FROM NhanVien d
        WHERE COALESCE(d.trangThai, false) = ?1
        """)
    List<NhanVienRespone> findByTrangThai(Boolean trangThai);
}