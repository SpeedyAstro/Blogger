package in.astro.controller;

import in.astro.helper.SessionHelper;
import in.astro.repository.CategoryRepository;
import in.astro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class RootController {
    @Autowired
    private UserService userService;

    @Autowired
    private CategoryRepository categoryRepository;

    @ModelAttribute
    public void addLoggedInUser(Model model, Authentication authentication){
        if (authentication == null) return;
        String username = SessionHelper.getEmailOfLoggedInUser(authentication);
        var user = userService.getUserByEmail(username);
        model.addAttribute("loggedInUser",user);
    }

    @ModelAttribute
    public void addCategories(Model model){
        var categories = categoryRepository.findAll();
        model.addAttribute("categories",categories);
    }


}
