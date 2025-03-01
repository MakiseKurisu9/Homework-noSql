package com.example.articleManagement.service;

import com.example.articleManagement.entity.Category;

import java.util.List;

public interface CategoryService {
    void add(Category category);

    List<Category> list();

    Category findById(String id);

    void update(Category category);

    void delete(String id);
}
