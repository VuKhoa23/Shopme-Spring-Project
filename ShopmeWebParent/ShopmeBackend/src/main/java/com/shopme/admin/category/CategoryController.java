package com.shopme.admin.category;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.Category;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @GetMapping("/categories")
    public String listAll(Model model){
        model.addAttribute("categories", categoryService.listAll());
        return "categories/categories";
    }

    @GetMapping("/categories/new")
    public String showForm(Model model){

        model.addAttribute("category", new Category());
        model.addAttribute("pageTitle", "New category");

        List<Category> categoryList = categoryService.listCategoryByTree();
        model.addAttribute("categories", categoryList);

        return "categories/categories-form";
    }

    @PostMapping("/categories/save")
    public String save(@ModelAttribute("category")Category category,
                       @RequestParam("imageInput")MultipartFile image,
                       RedirectAttributes redirectAttributes) throws IOException {
        String fileName = StringUtils.cleanPath(image.getOriginalFilename());
        category.setImage(fileName);
        Category saved = categoryService.save(category);
        // create the directory in the same level with front end and back end so both can use this dir
        String uploadDir = "../categories-images/" + saved.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, image);
        redirectAttributes.addFlashAttribute("message", "Category saved");
        return "redirect:/categories";
    }
}
