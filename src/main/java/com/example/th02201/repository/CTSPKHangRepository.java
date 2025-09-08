package com.example.th02201.repository;

import com.example.th02201.model.ChiTietSanPham;
import com.example.th02201.model.KhachHang;
import com.example.th02201.model.KhachHangCTSP;
import com.example.th02201.respone.CTSPKHangRespone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CTSPKHangRepository extends JpaRepository<KhachHangCTSP, Integer> {
    //hien thi
    @Query("""
            SELECT
            new com.example.th02201.respone.CTSPKHangRespone
            (d.id, d.maKH, d.tenKH, d.soDT, d.ngayT)
            FROM KhachHangCTSP d
            """)
    List<CTSPKHangRespone> layDanhSachCTSPHKhang();

    //Search
    @Query("""
            SELECT
            new com.example.th02201.respone.CTSPKHangRespone
                    (d.id, d.maKH, d.tenKH, d.soDT, d.ngayT)
                    FROM KhachHangCTSP d
            WHERE d.maKH=?1
            """)
    List<CTSPKHangRespone> SearchKHCTSP(String ma);
}

