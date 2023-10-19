package com.shopme.admin.brand.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BrandController {
    @Autowired
    private BrandService brandService;
    @GetMapping("/brands")
    public String listAll(Model model){

        model.addAttribute("brands", brandService.listAll());
        return "brands/brands";
    }
}
