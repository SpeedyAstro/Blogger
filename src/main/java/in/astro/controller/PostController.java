package in.astro.controller;

import in.astro.helper.Message;
import in.astro.helper.MessageType;
import in.astro.helper.SessionHelper;
import in.astro.model.PostForm;
import in.astro.service.IFileUploadService;
import in.astro.service.IPostService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;


@Controller
@RequestMapping("/user/post")
public class PostController {

    @Autowired
    private IPostService postService;

    @Autowired
    private IFileUploadService fileUploadService;

    @GetMapping("/create")
    public String addPost(Model model){
        PostForm post = new PostForm();
        model.addAttribute("post", post);
        return "user/add_post";
    }

    @PostMapping("/create")
    public String createPost(@Valid @ModelAttribute("post") PostForm post, BindingResult bindingResult, Authentication authentication, HttpSession session){
        if (bindingResult.hasErrors()){
            return "user/add_post";
        }
        String email = SessionHelper.getEmailOfLoggedInUser(authentication);
        Map<?, ?> map = fileUploadService.uploadImage(post.getFile());
        post.setImageUrl(map.get("url").toString());
        postService.savePost(post,email);
        session.setAttribute("message", Message.builder().content("Post created successfully").type(MessageType.GREEN).build());
        return "redirect:/home";
    }




}
