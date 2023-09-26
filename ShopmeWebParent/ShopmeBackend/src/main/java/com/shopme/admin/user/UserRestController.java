package com.shopme.admin.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class UserRestController {
    @Autowired
    UserService userService;

    @PostMapping("users/check-email")
    public String isUniqueEmailHandler(@RequestParam(name="email") String email, @RequestParam(name="id", required=false) Integer id){
        return userService.isUniqueEmail(id, email) ? "OK" : "Duplicated";
    }
}
