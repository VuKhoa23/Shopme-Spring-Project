package com.shopme.admin.brand.controller;

import com.shopme.admin.brand.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BrandRestController {
    @Autowired
    BrandService brandService;

    @PostMapping("/brands/check-unique")
    public String checkUniqueBrand(@RequestParam(name = "id", required = false) Integer id,
                                   @RequestParam(name = "name") String name) {
        return brandService.checkUnique(id, name);
    }
}

