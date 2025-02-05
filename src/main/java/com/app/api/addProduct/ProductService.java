package com.app.api.addProduct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // 전체 상품 조회
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 상품 추가
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    // 상품 수정 (파일 포함)
    public Product updateProduct(Long id, String menu, int price, int stock, String img) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existingProduct.setMenu(menu);
        existingProduct.setPrice(price);
        existingProduct.setStock(stock);

        // 이미지가 업로드된 경우에만 변경
        if (img != null) {
            existingProduct.setImg(img);
        }

        return productRepository.save(existingProduct);
    }


    // 상품 삭제
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
