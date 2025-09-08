package com.example.th02201.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    // Điều chỉnh RequestMapping để chỉ chuyển tiếp các đường dẫn không có phần mở rộng tệp
    // Điều này cho phép Spring Boot tự động xử lý các tài nguyên tĩnh (như .jpg, .css, .js)
    // và chỉ chuyển tiếp các đường dẫn SPA (Single Page Application) về index.html
    @GetMapping(value = {"/", "/{path:[^\\.]*}", "/{path:.*}/{path2:[^\\.]*}","/{path:^(?!api$).*$}"})
    public String forward() {
        return "forward:/index.html";
    }
}
