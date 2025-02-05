package com.app.api.addProduct;

import com.app.api.file.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public Product addProduct(@RequestParam("menu") String menu,
                              @RequestParam("price") int price,
                              @RequestParam(value = "stock", required = false, defaultValue = "0") int stock,
                              @RequestPart(value = "img", required = false) MultipartFile imageFile) {
        String img = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            img = fileUploadService.uploadFile(imageFile);
        }

        Product product = new Product();
        product.setMenu(menu);
        product.setPrice(price);
        product.setStock(stock);
        product.setImg(img);

        return productService.addProduct(product);
    }



    // 상품 수정 (파일 업로드 포함)
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public Product updateProduct(@PathVariable Long id,
                                 @RequestParam("menu") String menu,
                                 @RequestParam("price") int price,
                                 @RequestParam(value = "stock", required = false, defaultValue = "0") int stock,
                                 @RequestPart(value = "img", required = false) MultipartFile imageFile) {

        String img = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            img = fileUploadService.uploadFile(imageFile);
        }

        return productService.updateProduct(id, menu, price, stock, img);
    }


    // 상품 삭제
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }


}
