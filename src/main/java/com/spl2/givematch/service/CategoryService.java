package com.spl2.givematch.service;

import com.spl2.givematch.dao.CategoryDAO;
import com.spl2.givematch.model.Category;

import java.util.List;

public class CategoryService {

    private final CategoryDAO categoryDAO = new CategoryDAO();

    public List<Category> getAllCategories() {
        return categoryDAO.findAll();
    }

    public Category createCategory(String name, String description) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Category name cannot be empty.");
        }
        Category category = new Category();
        category.setName(name.trim());
        category.setDescription(description);
        return categoryDAO.insert(category);
    }
}
