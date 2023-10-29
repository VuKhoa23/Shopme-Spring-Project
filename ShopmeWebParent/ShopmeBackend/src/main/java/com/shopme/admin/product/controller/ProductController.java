package com.shopme.admin.product.controller;


import com.shopme.admin.product.ProductService;
import com.shopme.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @GetMapping("/products")
    public String listAll(Model model){
        List<Product> productList = productService.listAll();
        model.addAttribute("products", productList);
        return "products/products";
    }
}
