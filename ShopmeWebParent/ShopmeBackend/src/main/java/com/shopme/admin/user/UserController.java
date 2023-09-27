package com.shopme.admin.user;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
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
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String listAll(Model model){
        List<User> users = userService.listAll();
        model.addAttribute("users", users);

        return "users";
    }

    @GetMapping("/users/new")
    public String newUser(Model model){
        User user = new User();
        user.setEnabled(true);
        List<Role> allRoles = userService.listRoles();
        model.addAttribute("user", user);
        model.addAttribute("allRoles", allRoles);
        model.addAttribute("pageTitle", "Create a User");
        return "user-form";
    }

    @PostMapping("/users/save")
    public String handleFormSubmission(@ModelAttribute User user, RedirectAttributes redirectAttributes,
                                       @RequestParam(name = "imageInput")MultipartFile multipartFile) throws IOException {
        if(user.getId() == null){
            redirectAttributes.addFlashAttribute("message", "User added successfully");
        }
        else{
            redirectAttributes.addFlashAttribute("message", "User updated successfully");
        }

        System.out.println("-------The PHOTOS: " + user.getPhotos());

        // recommand to use the StringUtils to clean the file name
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        if(!fileName.isEmpty()) {
            user.setPhotos(fileName);
            User savedUser = userService.save(user);
            String uploadDirectory =  "user-photos/" + savedUser.getId();
            FileUploadUtil.cleanDir(uploadDirectory);
            FileUploadUtil.saveFile(uploadDirectory, fileName, multipartFile);
        }
        // user doesnt upload any file
        else{
            if(user.getPhotos().isEmpty()){
                user.setPhotos(null);
            }
            userService.save(user);
        }
        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}")
    public String showEditForm(@PathVariable(name="id") Integer id, RedirectAttributes redirectAttributes,
                               Model model){
        try {
            User user = userService.get(id);
            model.addAttribute("user", user);
            model.addAttribute("pageTitle", "Edit User with ID: " + id);
            model.addAttribute("allRoles", userService.listRoles());
            return "user-form";
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes){
        try {
            userService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Deleted user with ID: " + id);
            return "redirect:/users";
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/users/{id}/enabled/{enabled}")
    public String enabledStatusHandler(@PathVariable("id") Integer id, @PathVariable("enabled") boolean enabled
                                        , RedirectAttributes redirectAttributes){
        userService.updateUserEnabledStatus(id, enabled);
        redirectAttributes.addFlashAttribute("message", "User with ID " + id
                                                                                        + (enabled ? " is enabled" : " is disabled"));
        return "redirect:/users";
    }
}
