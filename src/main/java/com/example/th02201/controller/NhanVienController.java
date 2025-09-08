package com.example.th02201.controller;

import com.example.th02201.model.ChucVu;
import com.example.th02201.model.NhanVien;
import com.example.th02201.model.TaiKhoan;
import com.example.th02201.repository.ChucVuRepository;
import com.example.th02201.repository.NhanVienRepository;
import com.example.th02201.repository.TaiKhoanRepository;
import com.example.th02201.request.NhanVienRequest;
import com.example.th02201.respone.NhanVienRespone;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/nhan-vien")
public class NhanVienController {
    @Autowired
    private NhanVienRepository nhanVienRepository;
    @Autowired
    private ChucVuRepository chucVuRepository;
    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @GetMapping("/hien-thi")
    public List<NhanVienRespone> getAll() {
        return nhanVienRepository.layDanhSachDRepone();
    }

    // Sửa endpoint phân trang
    @GetMapping("/phan-trang")
    public Page<NhanVienRespone> phanTrang(@RequestParam(value = "pageNo", required = false, defaultValue = "0") Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        return nhanVienRepository.phanTranhDanhSach(pageable);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addNhanVien(@RequestBody NhanVienRequest nhanVienRequest) {
        NhanVien dt = new NhanVien();
        BeanUtils.copyProperties(nhanVienRequest, dt);

        if (nhanVienRequest.getChucVuID() != null) {
            chucVuRepository.findById(nhanVienRequest.getChucVuID())
                    .ifPresentOrElse(dt::setChucVu, () -> dt.setChucVu(null));
        }

        if (nhanVienRequest.getTaiKhoanID() != null) {
            taiKhoanRepository.findById(nhanVienRequest.getTaiKhoanID())
                    .ifPresentOrElse(dt::setTaiKhoan, () -> dt.setTaiKhoan(null));
        }

        nhanVienRepository.save(dt);
        return ResponseEntity.ok("Thêm nhân viên thành công");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateD(@RequestBody NhanVienRequest nhanVienRequest, @PathVariable("id") Integer id) {
        Optional<NhanVien> existing = nhanVienRepository.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nhân viên không tồn tại");
        }

        NhanVien dt = existing.get();
        BeanUtils.copyProperties(nhanVienRequest, dt, "id", "maNhanVien");

        if (nhanVienRequest.getChucVuID() != null) {
            chucVuRepository.findById(nhanVienRequest.getChucVuID())
                    .ifPresentOrElse(dt::setChucVu, () -> dt.setChucVu(null));
        }

        if (nhanVienRequest.getTaiKhoanID() != null) {
            taiKhoanRepository.findById(nhanVienRequest.getTaiKhoanID())
                    .ifPresentOrElse(dt::setTaiKhoan, () -> dt.setTaiKhoan(null));
        }

        nhanVienRepository.save(dt);
        return ResponseEntity.ok("Cập nhật nhân viên thành công");
    }

    @DeleteMapping("/delete/{id}")
    public String deleteD(@PathVariable("id") String ma) {
        nhanVienRepository.deleteDMa(ma);
        return "Xóa nhân viên thành công!";
    }

    @GetMapping("/detail/{id}")
    public NhanVienRespone detalD(@PathVariable("id") Integer id) {
        return nhanVienRepository.detailNV(id);
    }

    @GetMapping("/search/{ma}")
    public List<NhanVienRespone> Search(@PathVariable("ma") String ma) {
        return nhanVienRepository.Search(ma);
    }

    @GetMapping("/loc-trang-thai")
    public ResponseEntity<List<NhanVienRespone>> locTheoTrangThai(@RequestParam boolean trangThai) {
        List<NhanVienRespone> ds = nhanVienRepository.findByTrangThai(trangThai);
        return ResponseEntity.ok(ds);
    }
}