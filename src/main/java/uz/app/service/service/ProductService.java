package uz.app.service.service;

import uz.app.service.model.Product;
import uz.app.service.repository.ProductRepository;

import java.util.List;

public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // 🔹 Yangi mahsulot qo‘shish
    public void addProduct(Product product) {
        productRepository.save(product);
    }

    // 🔹 Mahsulotni yangilash
    public void updateProduct(Product product) {
        if (productRepository.findById(product.getId()).isEmpty()) {
            throw new RuntimeException("Product not found with id: " + product.getId());
        }
        productRepository.update(product);
    }

    // 🔹 Mahsulotni o‘chirish
    public void deleteProduct(long id) {
        if (productRepository.findById(id).isEmpty()) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    // 🔹 Barcha mahsulotlarni olish
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 🔹 ID orqali mahsulotni olish
    public Product getProductById(long id) {
        return productRepository.findById(id).orElse(null);
    }

    // 🔹 Muallif (user) bo‘yicha mahsulotlar
    public List<Product> getProductsByUserId(long userId) {
        return productRepository.findAll().stream()
                .filter(p -> p.getUserId() == userId)
                .toList();
    }

    // 🔹 Category bo‘yicha mahsulotlar
    public List<Product> getProductsByCategoryId(long categoryId) {
        return productRepository.findAll().stream()
                .filter(p -> p.getCategoryId() == categoryId)
                .toList();
    }
}