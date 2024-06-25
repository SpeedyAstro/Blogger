package in.astro.controller;

import in.astro.entity.Comment;
import in.astro.entity.Post;
import in.astro.entity.User;
import in.astro.helper.Message;
import in.astro.helper.MessageType;
import in.astro.helper.SessionHelper;
import in.astro.model.UserForm;
import in.astro.repository.CommentRepository;
import in.astro.service.IPostService;
import in.astro.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class PageController {
    @Autowired
    private UserService userService;

    @Autowired
    private IPostService postService;

    @Autowired
    private CommentRepository commentRepository;


    @RequestMapping("/home")
    public String home(Model model){
        List<Post> allPosts = postService.getAllPosts();
        model.addAttribute("posts", allPosts);
        System.out.println("home page");
        return "home";
    }

    @RequestMapping("/about")
    public String about(Model model){
        model.addAttribute("title", "About - Blogging App");
        return "about";
    }

    @RequestMapping("/contact")
    public String contact(Model model){
        model.addAttribute("title", "Contact - Blogging App");
        return "contact";
    }

    @GetMapping("/register")
    public String register(Model model){
        UserForm userForm = new UserForm();
        model.addAttribute("user",userForm);
        return "register";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/")
    public String displayAllPost(){
        return "redirect:/home";
    }

    @PostMapping("/register")
    public String registration(@Valid @ModelAttribute("user") UserForm user, BindingResult result, HttpSession session){
        if (result.hasErrors()){
            return "register";
        }
        User user1 = userService.saveUser(user);
        if (user1 != null){
            Message message = Message.builder().content("User registered successfully").type(MessageType.GREEN).build();
            session.setAttribute("message", message);
            return "redirect:/login";
        }else {
            Message message = Message.builder().content("User already exists").type(MessageType.RED).build();
            session.setAttribute("message", message);
            return "redirect:/register";
        }
    }

    @GetMapping("/post/{id}")
    public String getPost(@PathVariable Long id, Model model, Authentication authentication){
        Comment comment = new Comment();
        String authUsername = "anonymousUser";
        if (authUsername != null) {
            authUsername = SessionHelper.getEmailOfLoggedInUser(authentication);
        }
        Post post = postService.getPostById(id);
        if (post == null){
            return "error/404";
        }
        model.addAttribute("post", post);
        model.addAttribute("comment", comment);
        if (authUsername.equals(post.getUser().getUsername())) {
            model.addAttribute("isOwner", true);
        }
        return "post";
    }

    @PostMapping("/comment/{id}")
    public String createComment(@PathVariable Long id,@ModelAttribute Comment comment, Model model, Authentication authentication) {
        System.out.println("comment: "+comment.getContent());
        System.out.println(id);
        Post post = postService.getPostById(id);
        if (post != null){
            System.out.println("post found");
            String username = SessionHelper.getEmailOfLoggedInUser(authentication);
            if (username != null){
                System.out.println(username);
                User user = userService.getUserByEmail(username);
                if (user != null){
                    System.out.println("user found");
                    comment.setUser(user);
                    comment.setPost(post);
                    commentRepository.save(comment);
                    System.out.println("comment saved");
                }
            }
        }
        return "redirect:/post/"+id;
    }
}
