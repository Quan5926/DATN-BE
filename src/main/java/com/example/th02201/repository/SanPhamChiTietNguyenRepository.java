package com.example.th02201.repository;
import com.example.th02201.model.ChiTietSanPham;
import com.example.th02201.model.ChiTietSanPhamNguyen;
import com.example.th02201.respone.ChiTietSanPhamRespone;
import com.example.th02201.respone.SanPhamRespone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface SanPhamChiTietNguyenRepository extends JpaRepository<ChiTietSanPhamNguyen, Integer> {
    //hien thi
    @Query("""
    SELECT
    new com.example.th02201.respone.ChiTietSanPhamRespone
    (d.id,d.sanPham.tenSanPham,d.sanPham.maSanPham,d.giaBan,d.soLuongTonKho)
    FROM ChiTietSanPham d
    """)
    List<ChiTietSanPhamRespone> layDanhSachctsp();

    ///phantrang
    @Query("""
    SELECT
    new com.example.th02201.respone.ChiTietSanPhamRespone
    (d.id,d.sanPham.tenSanPham,d.sanPham.maSanPham,d.giaBan,d.soLuongTonKho)
    FROM ChiTietSanPham d
    """)
    Page<ChiTietSanPhamRespone> phanTranhDanhSachctsp(Pageable pageable);
//    //custom lai delete
//    @Modifying//bat buoc khi thuc hien truy van update hoac delete
//    @Transactional
//    @Query("DELETE FROM Sach d WHERE d.maSach=?1")
//    void deleteDiemBangMa(String ma);
//
//
//    //detail
//    @Query("""
//    SELECT
//    new com.example.de7.respone.SachRespone
//    (d.maSach,d.tenSach,d.tacGia.tenTacGia,d.ngayXuatBan,d.theLoai,d.gia)
//    FROM Sach d
//    WHERE d.id= ?1
//    """)
//    SachRespone detail(Integer id);
//    //Search
//    @Query("""
//    SELECT
//    new com.example.de7.respone.SachRespone
//    (d.maSach,d.tenSach,d.tacGia.tenTacGia,d.ngayXuatBan,d.theLoai,d.gia)
//    FROM Sach d
//    WHERE d.tenSach=?1
//    """)
//    List<SachRespone> Search(String ten);
//}
}
