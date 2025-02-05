package com.app.api.addBasic;

import com.app.api.file.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/basic-menu")
@CrossOrigin(origins = "http://localhost:3000")
public class BasicMenuController {

    @Autowired
    private BasicMenuRepository basicMenuRepository;

    @Autowired
    private FileUploadService fileUploadService;

    // CSV 파일 업로드 및 메뉴 데이터 삽입
    @PostMapping("/upload")
    public String uploadMenuCsv(@RequestParam("file") MultipartFile file) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
            List<BasicMenu> menuList = reader.lines()
                    .skip(1) // 첫 번째 줄 (헤더) 건너뛰기
                    .map(line -> {
                        String[] data = line.split(",");
                        String cafe = data[0].trim();
                        String menu = data[1].trim();
                        double price = Double.parseDouble(data[2].trim());
                        int stock = Integer.parseInt(data[3].trim());

                        // 이미지 S3 업로드 처리 (예제에서는 URL이 제공된 경우 그대로 사용)
                        String img = data.length > 4 && !data[4].trim().isEmpty() ? fileUploadService.uploadFile(null) : null;

                        return new BasicMenu(cafe, menu, img, price, stock);
                    })
                    .collect(Collectors.toList());

            basicMenuRepository.saveAll(menuList);
            return "✅ 메뉴 업로드 완료!";
        } catch (Exception e) {
            return "❌ CSV 업로드 실패: " + e.getMessage();
        }
    }
    // 특정 카페의 메뉴 조회 API
    @GetMapping("/cafe/{cafeName}")
    public List<BasicMenu> getMenusByCafe(@PathVariable String cafeName) {
        return basicMenuRepository.findByCafe(cafeName);
    }

}
