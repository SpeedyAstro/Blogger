package in.astro.controller;

import in.astro.helper.SessionHelper;
import in.astro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


//    add logged in user



//    dashboard
    @GetMapping("/dashboard")
    public String dashboard(){
        return "user/dashboard";
    }

//    profile
    @GetMapping("/profile")
    public String profile(Authentication authentication){
        String username = SessionHelper.getEmailOfLoggedInUser(authentication);
        return "user/profile";
    }
}
