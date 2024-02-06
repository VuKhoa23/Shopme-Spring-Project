package com.shopme.admin.product.controller;


import com.shopme.admin.brand.BrandService;
import com.shopme.admin.product.ProductNotFoundException;
import com.shopme.admin.product.ProductService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;
    @GetMapping("/products")
    public String listAll(Model model){
        List<Product> productList = productService.listAll();
        model.addAttribute("products", productList);
        return "products/products";
    }

    @GetMapping("/products/new")
    public String newProduct(Model model){
        List<Brand> brands = brandService.listAllProjection();
        Product product = new Product();
        product.setEnabled(true);
        product.setInStock(true);

        model.addAttribute("product", product);
        model.addAttribute("brands", brands);
        model.addAttribute("pageTitle", "New Product");

        return "products/products-form";
    }

    @PostMapping("/products/save")
    public String save(@ModelAttribute("product") Product product, RedirectAttributes ra){
        productService.save(product);
        ra.addFlashAttribute("message", "Product has been saved successfully");
        return "redirect:/products";
    }

    @GetMapping("/products/{id}/enabled/{enabled}")
    public String setEnabled(Model model,
                             @PathVariable("id") Integer id,
                             @PathVariable("enabled") boolean enabled,
                             RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.findById(id);
            product.setEnabled(enabled);
            productService.save(product);
            redirectAttributes.addFlashAttribute("message", (enabled ? "Enabled" : "Disabled") + " product with " +
                    "id: " + id);
            return "redirect:/products";

        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/products";
        }
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(Model model,
                              @PathVariable("id") Integer id,
                              RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.findById(id);
            productService.remove(product);
            redirectAttributes.addFlashAttribute("message", "Deleted product with " +
                    "id: " + id);
            return "redirect:/products";

        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/products";
        }
    }
}
