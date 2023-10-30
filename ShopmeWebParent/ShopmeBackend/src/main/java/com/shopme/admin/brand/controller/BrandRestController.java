package com.shopme.admin.brand.controller;

import com.shopme.admin.brand.BrandNotFoundException;
import com.shopme.admin.brand.BrandService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
public class BrandRestController {
    @Autowired
    BrandService brandService;

    @PostMapping("/brands/check-unique")
    public String checkUniqueBrand(@RequestParam(name = "id", required = false) Integer id,
                                   @RequestParam(name = "name") String name) {
        return brandService.checkUnique(id, name);
    }

    @GetMapping("/brands/{id}/categories")
    public List<CategoryDTO> getCategoriesFromBrand(@PathVariable(name = "id") Integer id) throws BrandNotFoundRestException {
        try {
            Brand brand = brandService.findById(id);
            Set<Category> categories = brand.getCategories();

            List<CategoryDTO> dtoList = new ArrayList<>();
            categories.forEach(category -> {
                dtoList.add(new CategoryDTO(category.getId(), category.getName()));
            });
            return dtoList;
        } catch (BrandNotFoundException e) {
            throw new BrandNotFoundRestException();
        }
    }
}

