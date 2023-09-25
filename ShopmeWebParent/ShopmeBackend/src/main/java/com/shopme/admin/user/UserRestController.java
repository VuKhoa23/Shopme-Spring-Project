package com.shopme.admin.user;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Enumeration;
import java.util.Map;

@RestController
public class UserRestController {
    @Autowired
    UserService userService;

    @PostMapping("users/check-email")
    public String isDuplicateEmail(@RequestParam Map<String,String> allParams){
        String email = allParams.get("email");
        System.out.println(allParams.entrySet());
        return userService.isUniqueEmail(email) ? "OK" : "Duplicated";
    }
}
