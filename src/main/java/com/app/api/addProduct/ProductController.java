package com.app.api.addProduct;

import com.app.api.file.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import lombok.ToString;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000")


public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileUploadService fileUploadService;

    // 전체 상품 조회
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // 상품 추가 (파일 업로드 포함)
    @PostMapping(consumes = {"multipart/form-data"})
    public Product addProduct(
            @RequestParam("cafe") String cafe,                   // <-- 추가
            @RequestParam("menu") String menu,
            @RequestParam("price") int price,
            @RequestParam(value = "stock", required = false, defaultValue = "0") int stock,
            @RequestPart(value = "img", required = false) MultipartFile imageFile) {

        // 파일 업로드 로직
        String img = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            img = fileUploadService.uploadFile(imageFile);
        }

        // 엔티티 생성 및 값 설정
        Product product = new Product();
        product.setCafe(cafe);         // <-- 추가
        product.setMenu(menu);
        product.setPrice(price);
        product.setStock(stock);
        product.setImg(img);

        return productService.addProduct(product);
    }



    // 상품 수정 (파일 업로드 포함)
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public Product updateProduct(
            @PathVariable Long id,
            @RequestParam("cafe") String cafe,                   // <-- 추가
            @RequestParam("menu") String menu,
            @RequestParam("price") int price,
            @RequestParam(value = "stock", required = false, defaultValue = "0") int stock,
            @RequestPart(value = "img", required = false) MultipartFile imageFile) {

        // 파일 업로드 로직
        String img = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            img = fileUploadService.uploadFile(imageFile);
        }

        // Service로 넘겨서 DB 업데이트
        return productService.updateProduct(id, cafe, menu, price, stock, img);
    }


    // 상품 삭제
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @Autowired
    private ProductRepository productRepository;
    @PostMapping("/voice-process")
    public ResponseEntity<?> processVoiceText(@RequestBody Map<String, String> payload) {
        // 1) 파라미터 추출
        System.out.println(payload);
        String recognizedText = payload.get("text");   // 사용자가 말한 음성 인식 결과
        String brand = payload.get("brand");          // sessionStorage의 matchedBrand

        // 2) 유효성 검사
        if (recognizedText == null || recognizedText.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "전송된 텍스트가 비어 있습니다."));
        }
        if (brand == null || brand.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "brand(카페)가 비어 있습니다."));
        }

        // 3) DB에서 brand(=카페명)에 해당하는 메뉴 목록 조회
        List<Product> productList = productRepository.findByCafe(brand);
        System.out.println(productList);
        if (productList.isEmpty()) {
            // 브랜드에 해당하는 메뉴가 없으면 안내
            return ResponseEntity.ok(Map.of("message", "해당 브랜드에 메뉴가 없습니다.",
                    "brand", brand,
                    "gpt_answer", ""));
        }

        // 4) 메뉴 이름들만 추출 (예: List<String>)
        List<String> menuNames = productList.stream()
                .map(Product::getMenu)
                .collect(Collectors.toList());
        System.out.println(menuNames);
        // 5) FastAPI로 넘길 requestBody 구성
        //    예: {"text": recognizedText, "brand": brand, "menus": ["메뉴1", "메뉴2", ...]}
        Map<String, Object> fastApiRequest = new HashMap<>();
        fastApiRequest.put("text", recognizedText);
        fastApiRequest.put("brand", brand);
        fastApiRequest.put("menus", menuNames);

        // 6) FastAPI 엔드포인트로 POST (restTemplate 사용)
        String fastapiUrl = "http://127.0.0.1:8000/fastapi/voice-process"; // 실제 FastAPI 주소/포트
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<Map> fastapiResponse = restTemplate.postForEntity(
                    fastapiUrl,
                    fastApiRequest,
                    Map.class
            );

            if (!fastapiResponse.getStatusCode().is2xxSuccessful() || fastapiResponse.getBody() == null) {
                return ResponseEntity.status(500).body(Map.of("error", "FastAPI 응답 오류"));
            }

            // 7) FastAPI 결과를 받아서 그대로 반환(또는 가공)
            return ResponseEntity.ok(fastapiResponse.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "FastAPI 호출 오류: " + e.getMessage()));
        }
    }
    @GetMapping("/getmenu")
    public ResponseEntity<?> getMenuByBrand(@RequestParam("brand") String brand) {
        // 파라미터 유효성 검사
        if (brand == null || brand.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "brand(카페명)이 비어 있습니다."));
        }

        // DB에서 해당 브랜드의 상품 조회 (정확한 일치)
        List<Product> productList = productRepository.findByCafe(brand);
        if (productList.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                    "message", "해당 브랜드에 메뉴가 없습니다.",
                    "brand", brand,
                    "menus", List.of()
            ));
        }

        // 필요한 정보만 추출하여 리스트로 변환
        List<Map<String, Object>> menuDetails = productList.stream()
                .map(product -> {
                    Map<String, Object> menuMap = new HashMap<>();
                    menuMap.put("menu", product.getMenu());
                    menuMap.put("img", product.getImg());
                    menuMap.put("price", product.getPrice());
                    menuMap.put("stock", product.getStock());
                    return menuMap;
                })
                .collect(Collectors.toList());

        // 결과 반환
        return ResponseEntity.ok(Map.of(
                "brand", brand,
                "menus", menuDetails
        ));
    }
}
