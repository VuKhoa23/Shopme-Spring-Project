package com.shopme.admin.product.controller;

import com.shopme.admin.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {
    @Autowired
    ProductService productService;

    @PostMapping("/products/check-unique")
    public String checkUniqueCategory(@RequestParam(name="id", required = false) Integer id,
                                      @RequestParam(name="name") String name,
                                      @RequestParam(name="alias") String alias){
        System.out.println(id);
        System.out.println(name);
        return productService.checkUnique(id, name, alias);
    }
}
