package com.shopme.admin.brand.controller;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.brand.BrandNotFoundException;
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class BrandController {
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/brands")
    public String listAll(Model model) {

        model.addAttribute("brands", brandService.listAll());
        return "brands/brands";
    }

    @PostMapping("/brands/save")
    public String save(@ModelAttribute("brand") Brand brand,
                       @RequestParam("imageInput") MultipartFile image,
                       RedirectAttributes redirectAttributes) throws IOException {
        if (brand.getId() == null) {
            redirectAttributes.addFlashAttribute("message", "Brand added successfully");
        } else {
            redirectAttributes.addFlashAttribute("message", "Brand updated successfully");
        }
        String fileName = StringUtils.cleanPath(image.getOriginalFilename());
        if (!fileName.isEmpty()) {
            brand.setLogo(fileName);
            Brand saved = brandService.save(brand);
            String uploadDir = "../brands-logos/" + saved.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, image);
        } else {
            brandService.save(brand);
        }
        return "redirect:/brands";
    }

    @GetMapping("/brands/new")
    public String showForm(Model model) {
        model.addAttribute("brand", new Brand());
        model.addAttribute("pageTitle", "New brand");
        List<Category> categoryList = categoryService.listCategoryByTreeInForm("--");
        model.addAttribute("categories", categoryList);
        return "brands/brands-form";
    }

    @GetMapping("/brands/edit/{id}")
    public String showEditForm(Model model, @PathVariable("id") Integer id,
                           RedirectAttributes redirectAttributes) {
        try {
            Brand brand = brandService.findById(id);
            model.addAttribute("pageTitle", "New Brand");

            model.addAttribute("brand", brand);
            List<Category> categoryList = categoryService.listCategoryByTreeInForm("--");
            model.addAttribute("categories", categoryList);
            return "brands/brands-form";
        } catch (BrandNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/brands";
        }
    }

    @GetMapping("/brands/delete/{id}")
    public String deleteBrand(Model model,
                                 @PathVariable("id") Integer id,
                                 RedirectAttributes redirectAttributes) {
        try {
            Brand brand = brandService.findById(id);
            brandService.remove(brand);
            redirectAttributes.addFlashAttribute("message", "Deleted brand with " +
                    "id: " + id);
            return "redirect:/brands";

        } catch (BrandNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/brands";
        }
    }
}
