package com.example.th02201.controller;
import com.example.th02201.repository.*;
import com.example.th02201.respone.CTSPKHangRespone;
import com.example.th02201.respone.ChiTietSanPhamRespone;
import com.example.th02201.respone.KhachHangRespone;
import com.example.th02201.respone.SanPhamRespone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
public class ChiTietSanPhamNguyenController {
    @Autowired
    private SanPhamChiTietNguyenRepository chiTietSanPhamRepository;
    @Autowired
    private SanPhamNguyenRepository sanPhamRepository;
    @Autowired
    private CTSPKHangRepository ctspkHangRepository;

    @GetMapping("/api/c-t-s-p/hien-thi")
    public List<ChiTietSanPhamRespone> layDanhSachctsp() {
        return chiTietSanPhamRepository.layDanhSachctsp();
    }

    //hien thi san pham
    @GetMapping("/api/s-p/hien-thi")
    public List<SanPhamRespone> getSP() {
        return sanPhamRepository.layDanhSachSanPham();
    }

    //    //phan trang
    @GetMapping("api/ctsp/phan-trang")
    public List<ChiTietSanPhamRespone> phanTrang(@RequestParam(value = "pageNo",
            required = false, defaultValue = "0") Integer pageNo) {

        Pageable pageable = PageRequest.of(pageNo, 5);
        return chiTietSanPhamRepository.phanTranhDanhSachctsp(pageable).getContent();
    }

    @GetMapping("/api/ctspkhang/hien-thi")
    public List<CTSPKHangRespone> getCTSPHKhang() {
        return ctspkHangRepository.layDanhSachCTSPHKhang();

    }

    //    //Search
    @GetMapping("/api/ctspkhang/search/{ma}")
    public List<CTSPKHangRespone> Search(@PathVariable("ma") String ma) {
        return ctspkHangRepository.SearchKHCTSP(ma);

    }
}