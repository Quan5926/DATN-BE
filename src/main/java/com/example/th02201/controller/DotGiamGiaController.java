package com.example.th02201.controller;

import com.example.th02201.model.DotGiamGia;
import com.example.th02201.repository.DotGiamGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/dot-giam-gia")
public class DotGiamGiaController {
    @Autowired
    private DotGiamGiaRepository dotGiamGiaRepo;

    @GetMapping
    public List<DotGiamGia> getAllDotGiamGia() {
        return dotGiamGiaRepo.findAll();
    }
    @GetMapping("/{id}") // <-- THÊM API NÀY
    public Optional<DotGiamGia> getDotGiamGiaById(@PathVariable Integer id) {
        return dotGiamGiaRepo.findById(id);
    }
    // 2. Chức năng Thêm mới (Create)
    @PostMapping
    public DotGiamGia createDotGiamGia(@RequestBody DotGiamGia dotGiamGia) {
        return dotGiamGiaRepo.save(dotGiamGia);
    }

    // 3. Chức năng Sửa (Update)
    @PutMapping("/{id}")
    public DotGiamGia updateDotGiamGia(@PathVariable Integer id, @RequestBody DotGiamGia dotGiamGiaDetails) {
        Optional<DotGiamGia> optionalGiamGia = dotGiamGiaRepo.findById(id);

        if (optionalGiamGia.isPresent()) {
            DotGiamGia giamGia = optionalGiamGia.get();
            giamGia.setTenDotGiamGia(dotGiamGiaDetails.getTenDotGiamGia());
            giamGia.setGiaTri(dotGiamGiaDetails.getGiaTri());
            giamGia.setThoiGianBatDau(dotGiamGiaDetails.getThoiGianBatDau());
            giamGia.setThoiGianKetThuc(dotGiamGiaDetails.getThoiGianKetThuc());
            giamGia.setTrangThai(dotGiamGiaDetails.getTrangThai());
            return dotGiamGiaRepo.save(giamGia);
        } else {
            // Xử lý khi không tìm thấy bản ghi (ví dụ: trả về null hoặc ném ngoại lệ)
            return null;
        }
    }

    // 4. Chức năng Xóa (Delete)
    @DeleteMapping("/{id}")
    public void deleteDotGiamGia(@PathVariable Integer id) {
        dotGiamGiaRepo.deleteById(id);
    }
}
