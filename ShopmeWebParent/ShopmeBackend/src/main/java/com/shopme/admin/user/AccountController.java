package com.shopme.admin.user;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

@Controller
public class AccountController {
    @Autowired
    private UserService userService;

    @GetMapping("/account")
    public String viewAccountDetails(@AuthenticationPrincipal ShopmeUserDetails loggedUser,
                                     Model model) {
        String email = loggedUser.getUsername();
        User user = userService.getUserByEmail(email);
        model.addAttribute("user", user);

        return "account-form";
    }

    @PostMapping("/account/update")
    public String updateAccountDetail(@ModelAttribute User user,
                                      RedirectAttributes redirectAttributes,
                                      @RequestParam(name = "imageInput") MultipartFile multipartFile,
                                      @AuthenticationPrincipal ShopmeUserDetails loggedUser) throws IOException {

        // recommand to use the StringUtils to clean the file name
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        if (!fileName.isEmpty()) {
            user.setPhotos(fileName);
            User savedUser = userService.updateAccount(user);
            String uploadDirectory = "user-photos/" + savedUser.getId();
            FileUploadUtil.cleanDir(uploadDirectory);
            FileUploadUtil.saveFile(uploadDirectory, fileName, multipartFile);
        }
        // user doesnt upload any file
        else {
            // if user never upload a photo
            if (user.getPhotos().isEmpty()) {
                user.setPhotos(null);
            }
            userService.updateAccount(user);
        }
        // display the affected user for more convinient user expirience
        redirectAttributes.addFlashAttribute("message", "Account details updated !");
        // update the user authentication principal so the principal user full name on the header navigation bar will change
        loggedUser.setFirstName(user.getFirstName());
        loggedUser.setLastName(user.getLastName());
        return "redirect:/account";
    }
}
