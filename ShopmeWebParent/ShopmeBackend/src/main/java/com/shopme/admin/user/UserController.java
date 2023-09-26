package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
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
    public String handleFormSubmission(@ModelAttribute User user, RedirectAttributes redirectAttributes){
        if(user.getId() == null){
            redirectAttributes.addFlashAttribute("message", "User added successfully");
        }
        else{
            redirectAttributes.addFlashAttribute("message", "User updated successfully");
        }
        userService.save(user);
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
}
