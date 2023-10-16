package com.shopme.admin.category.controller;

import com.shopme.admin.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryRestController {
    @Autowired
    CategoryService categoryService;

    @PostMapping("/categories/check-unique")
    public String checkUniqueCategory(@RequestParam(name="id", required = false) Integer id,
                                      @RequestParam(name="name") String name,
                                      @RequestParam(name="alias") String alias){
        System.out.println(id);
        System.out.println(name);
        return categoryService.checkUnique(id, name, alias);
    }
}
