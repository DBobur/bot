package uz.app.service.service;

import uz.app.service.model.Category;
import uz.app.service.repository.CategoryRepository;

import java.util.List;

public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

    public void updateCategory(Category category) {
        if (!categoryRepository.existsById(category.getId())) {
            throw new RuntimeException("Category not found with id: " + category.getId());
        }
        categoryRepository.update(category);
    }

    public void deleteCategory(long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    public Category getCategoryById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getCategoriesByUserId(long userId) {
        return categoryRepository.findByUserId(userId);
    }
}