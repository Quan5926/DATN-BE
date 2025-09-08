//package com.example.th02201.controller;
//
//import com.example.th02201.repository.DienThoaiRepository;
//import com.example.th02201.request.DienThoaiRequest;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.web.bind.annotation.*;
//
//
//import java.util.List;
//
//@RestController
//public class DienThoaiController {
//    @Autowired
//    private DienThoaiRepository dienThoaiRepository;
//    @Autowired
//    private HangDienThoaiRepository hangDienThoaiRepository;
//    @GetMapping("/api/d/hien-thi")
//    public List<DienThoaiRespone> getAll() {
//        return dienThoaiRepository.layDanhSachDRepone();
//
//    }
//
//    //phan trang
//    @GetMapping("api/d/phan-trang")
//    public List<DienThoaiRespone> phanTrang(@RequestParam(value = "pageNo",
//            required = false, defaultValue = "0") Integer pageNo) {
//
//        Pageable pageable = PageRequest.of(pageNo, 5);
//        return dienThoaiRepository.phanTranhDanhSach(pageable).getContent();
//    }
//
//
//    @PostMapping("/api/d/add")
//    public String addD(@RequestBody DienThoaiRequest dienThoaiRequest) {
//        DienThoai dt = new DienThoai();
//        BeanUtils.copyProperties(dienThoaiRequest, dt);
//        HangDienThoai sv = hangDienThoaiRepository.findById(dienThoaiRequest.getTenHangDienThoaiID()).get();//khoaphu
//        dt.setHangDienThoai(sv);
//        dienThoaiRepository.save(dt);
//        return "Them dt thanh cong";
//
//    }
////    ///update
//    @PutMapping("/api/d/update/{id}")
//    public String updateD(@RequestBody DienThoaiRequest dienThoaiRequest,
//                                     @PathVariable("id")Integer id) {
//        DienThoai dt = new DienThoai();
//        BeanUtils.copyProperties(dienThoaiRequest, dt);
//        HangDienThoai sv = hangDienThoaiRepository.findById(dienThoaiRequest.getTenHangDienThoaiID()).get();//khoaphu
//        dt.setHangDienThoai(sv);
//        dt.setId(id);
//        dienThoaiRepository.save(dt);
//        return "Update dt thanh cong";
//
//    }
//    //delete
//    @DeleteMapping("/api/d/delete/{id}")
//    public String deleteD(@PathVariable("id")String ma){
//        dienThoaiRepository.deleteDMa(ma);
//        return "Xoa dt thang cong!";
//
//    }
//    //detail
//    @GetMapping("/api/d/detail/{id}")
//    public DienThoaiRespone detalD(@PathVariable("id")Integer id){
//        return dienThoaiRepository.detail(id);
//
//    }
//    //Search
//    @GetMapping("/api/d/search/{ten}")
//    public List<DienThoaiRespone> Search(@PathVariable("ten")  String ten){
//        return dienThoaiRepository.Search(ten);
//
//    }
//}
//
