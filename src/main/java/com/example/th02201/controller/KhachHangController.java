package com.example.th02201.controller;

import com.example.th02201.model.KhachHang;
import com.example.th02201.model.TaiKhoan;
import com.example.th02201.repository.KhachHangRepository;
import com.example.th02201.repository.TaiKhoanRepository;
import com.example.th02201.request.KhachHangRequest;
import com.example.th02201.respone.KhachHangRespone;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/khach-hang")
public class KhachHangController {

    @Autowired
    private KhachHangRepository khachHangRepository;
    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @GetMapping("/hien-thi")
    public List<KhachHangRespone> getAll() {
        return khachHangRepository.layDanhSachDRepone();
    }

    // Sửa endpoint phân trang
    @GetMapping("/phan-trang")
    public Page<KhachHangRespone> phanTrang(@RequestParam(value = "pageNo", required = false, defaultValue = "0") Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        return khachHangRepository.phanTranhDanhSach(pageable);
    }

    @PostMapping("/add")
    public String addKhachHang(@RequestBody KhachHangRequest khachHangRequest) {
        KhachHang khachHang = new KhachHang();
        BeanUtils.copyProperties(khachHangRequest, khachHang);

        if (khachHangRequest.getTaiKhoanID() != null) {
            Optional<TaiKhoan> optionalTaiKhoan = taiKhoanRepository.findById(khachHangRequest.getTaiKhoanID());
            if (optionalTaiKhoan.isPresent()) {
                khachHang.setTaiKhoan(optionalTaiKhoan.get());
            } else {
                return "Tài khoản với ID " + khachHangRequest.getTaiKhoanID() + " không tồn tại!";
            }
        } else {
            khachHang.setTaiKhoan(null);
        }

        khachHangRepository.save(khachHang);
        return "Thêm khách hàng thành công!";
    }

    @PutMapping("/update/{id}")
    public String updateKhachHang(@RequestBody KhachHangRequest dienThoaiRequest, @PathVariable("id") Integer id) {
        KhachHang khachHang = khachHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + id));

        khachHang.setTenKhachHang(dienThoaiRequest.getTenKhachHang());
        khachHang.setSoDienThoai(dienThoaiRequest.getSoDienThoai());
        khachHang.setGioiTinh(dienThoaiRequest.getGioiTinh());
        khachHang.setNgaySinh(dienThoaiRequest.getNgaySinh());
        khachHang.setNgayTao(dienThoaiRequest.getNgayTao());
        khachHang.setNgayCapNhat(dienThoaiRequest.getNgayCapNhat());

        if (dienThoaiRequest.getTaiKhoanID() != null) {
            TaiKhoan taiKhoan = taiKhoanRepository.findById(dienThoaiRequest.getTaiKhoanID())
                    .orElseThrow(() -> new RuntimeException("ID tài khoản không tồn tại!"));
            khachHang.setTaiKhoan(taiKhoan);
        } else {
            khachHang.setTaiKhoan(null);
        }

        khachHangRepository.save(khachHang);
        return "Cập nhật khách hàng thành công!";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteD(@PathVariable("id") String ma) {
        khachHangRepository.deleteDMa(ma);
        return "Xóa khách hàng thành công!";
    }

    @GetMapping("/detail/{id}")
    public KhachHangRespone detalD(@PathVariable("id") Integer id) {
        return khachHangRepository.detail(id);
    }

    // Sửa endpoint search
    @GetMapping("/search/{ma}")
    public List<KhachHangRespone> Search(@PathVariable("ma") String ma) {
        return khachHangRepository.Search(ma);
    }
}