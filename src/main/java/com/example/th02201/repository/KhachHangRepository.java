package com.example.th02201.repository;
import com.example.th02201.model.KhachHang;
import com.example.th02201.respone.KhachHangRespone;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {
    //hien thi
    @Query("""
    SELECT
    new com.example.th02201.respone.KhachHangRespone
    (d.id,d.tenKhachHang,d.soDienThoai,d.gioiTinh,d.ngaySinh,d.maKhachHang,d.ngayTao,d.ngayCapNhat)
    FROM KhachHang d
    """)
    List<KhachHangRespone> layDanhSachDRepone();

    //    ///phantrang
    @Query("""
    SELECT
    new com.example.th02201.respone.KhachHangRespone
    (d.id,d.tenKhachHang,d.soDienThoai,d.gioiTinh,d.ngaySinh,d.maKhachHang,d.ngayTao,d.ngayCapNhat)
    FROM KhachHang d
    """)
    Page<KhachHangRespone> phanTranhDanhSach(Pageable pageable);
    //    //custom lai delete
    @Modifying//bat buoc khi thuc hien truy van update hoac delete
    @Transactional
    @Query("DELETE FROM KhachHang d WHERE d.maKhachHang=?1")
    void deleteDMa(String ma);
    //
//
//    //detail
    @Query("""
    SELECT
    new com.example.th02201.respone.KhachHangRespone
    (d.id,d.tenKhachHang,d.soDienThoai,d.gioiTinh,d.ngaySinh,d.maKhachHang,d.ngayTao,d.ngayCapNhat)
    FROM KhachHang d
    WHERE d.id= ?1
    """)
    KhachHangRespone detail(Integer id);
    //    //Search
    @Query("""
    SELECT
    new com.example.th02201.respone.KhachHangRespone
    (d.id,d.tenKhachHang,d.soDienThoai,d.gioiTinh,d.ngaySinh,d.maKhachHang,d.ngayTao,d.ngayCapNhat)
    FROM KhachHang d
    WHERE d.maKhachHang=?1
    """)
    List<KhachHangRespone> Search(String ma);
/////////////////////////////////////////////////////

    Optional<KhachHang> findBySoDienThoai(String soDienThoai);
    Optional<KhachHang> findByTenKhachHang(String tenKhachHang); // Hữu ích để tìm "Khách lẻ"

}
