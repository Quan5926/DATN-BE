package com.example.th02201.repository;
import com.example.th02201.model.SanPham;
import com.example.th02201.model.SanPhamNguyen;
import com.example.th02201.respone.SanPhamRespone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface SanPhamNguyenRepository extends JpaRepository<SanPhamNguyen, Integer> {
    //hien thi san pham
    @Query("""

            SELECT new com.example.th02201.respone.SanPhamRespone(
    d.id,
    d.tenSanPham,
    d.maSanPham,
    d.moTaSanPham,
    MIN(ctsp.giaBan),
    SUM(ctsp.soLuongTonKho)
)
FROM SanPham d
LEFT JOIN ChiTietSanPham ctsp ON ctsp.sanPham.id = d.id
GROUP BY d.id, d.tenSanPham, d.maSanPham, d.moTaSanPham
""")
    List<SanPhamRespone> layDanhSachSanPham();
    }

