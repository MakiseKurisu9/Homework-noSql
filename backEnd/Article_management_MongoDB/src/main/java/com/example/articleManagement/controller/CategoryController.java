package com.example.articleManagement.controller;

import com.example.articleManagement.entity.Category;
import com.example.articleManagement.entity.Result;
import com.example.articleManagement.service.CategoryService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService service;

    @PostMapping
    public Result add(@RequestBody @Validated(Category.add.class) Category category) {
        service.add(category);
        return Result.success();
    }

    @GetMapping
    public Result<List<Category>> list() {
        List<Category> cs = service.list();
        return Result.success(cs);
    }

    @GetMapping("/detail")
    public Result<Category> detail(String id) {
        Category category = service.findById(id);
        return Result.success(category);
    }

    @PutMapping
    public Result update(@RequestBody @Validated(Category.update.class) Category category){
        service.update(category);
        return Result.success();
    }

    @DeleteMapping
    public Result delete(@Validated(Category.delete.class) String id) {
        service.delete(id);
        return Result.success();
    }
}
