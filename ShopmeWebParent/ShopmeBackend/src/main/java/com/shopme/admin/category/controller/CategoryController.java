package com.shopme.admin.category.controller;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.category.CategoryNotFoundException;
import com.shopme.admin.category.CategoryPageInfo;
import com.shopme.admin.category.CategoryService;
import com.shopme.admin.user.UserService;
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
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/categories")
    public String listAll(Model model,
                          @RequestParam(name = "sortOrder", required = false) String sortOrder) {
        return listByPage(1, model, sortOrder);
    }

    @GetMapping("/categories/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum,
                             Model model,
                             @RequestParam(name = "sortOrder", required = false) String sortOrder) {
        // list categories in hierachical form
        if (sortOrder == null || sortOrder.isEmpty()) {
            sortOrder = "asc";
        }

        CategoryPageInfo info = new CategoryPageInfo();

        if (sortOrder.equals("asc")) {
            model.addAttribute("reverseOrder", "desc");
        } else {
            model.addAttribute("reverseOrder", "asc");

        }

        long start = (pageNum - 1) * CategoryService.ROOT_CATEGORIES_PER_PAGE + 1;
        long end = start + CategoryService.ROOT_CATEGORIES_PER_PAGE - 1;

        if(end > info.getTotalElements()){
            end = info.getTotalElements();
        }
        model.addAttribute("categories", categoryService.listByPage(info, pageNum, sortOrder));
        model.addAttribute("totalPages", info.getTotalPages());
        model.addAttribute("totalItems", info.getTotalElements());
        model.addAttribute("currentPageNum", pageNum);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("sortField", "name");
        return "categories/categories";

    }

    @GetMapping("/categories/new")
    public String showForm(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("pageTitle", "New category");

        List<Category> categoryList = categoryService.listCategoryByTreeInForm();
        model.addAttribute("categories", categoryList);

        return "categories/categories-form";
    }

    @GetMapping("/categories/edit/{id}")
    public String showForm(Model model, @PathVariable("id") Integer id,
                           RedirectAttributes redirectAttributes) {
        try {
            Category category = categoryService.findById(id);
            model.addAttribute("pageTitle", "New category");
            List<Category> categoryList = categoryService.listCategoryByTreeInForm();

            model.addAttribute("category", category);
            model.addAttribute("categories", categoryList);
            return "categories/categories-form";
        } catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/categories";
        }
    }

    @PostMapping("/categories/save")
    public String save(@ModelAttribute("category") Category category,
                       @RequestParam("imageInput") MultipartFile image,
                       RedirectAttributes redirectAttributes) throws IOException {
        if (category.getId() == null) {
            redirectAttributes.addFlashAttribute("message", "Category added successfully");
        } else {
            redirectAttributes.addFlashAttribute("message", "Category updated successfully");
        }
        String fileName = StringUtils.cleanPath(image.getOriginalFilename());
        if (!fileName.isEmpty()) {
            category.setImage(fileName);
            Category saved = categoryService.save(category);
            String uploadDir = "../categories-images/" + saved.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, image);
        } else {
            categoryService.save(category);
        }
        return "redirect:/categories";
    }

    @GetMapping("/categories/{id}/enabled/{enabled}")
    public String setEnabled(Model model,
                             @PathVariable("id") Integer id,
                             @PathVariable("enabled") boolean enabled,
                             RedirectAttributes redirectAttributes) {
        try {
            Category category = categoryService.findById(id);
            category.setEnabled(enabled);
            categoryService.save(category);
            redirectAttributes.addFlashAttribute("message", (enabled ? "Enabled" : "Disabled") + " category with " +
                    "id: " + id);
            return "redirect:/categories";

        } catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/categories";
        }
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(Model model,
                                 @PathVariable("id") Integer id,
                                 RedirectAttributes redirectAttributes) {
        try {
            Category category = categoryService.findById(id);
            categoryService.save(category);
            categoryService.remove(category);
            redirectAttributes.addFlashAttribute("message", "Deleted category with " +
                    "id: " + id);
            return "redirect:/categories";

        } catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/categories";
        }
    }
}
