package in.astro.controller;

import in.astro.config.AppConstants;
import in.astro.entity.Comment;
import in.astro.entity.Post;
import in.astro.entity.User;
import in.astro.helper.Message;
import in.astro.helper.MessageType;
import in.astro.helper.SessionHelper;
import in.astro.model.UserForm;
import in.astro.payload.PostResponse;
import in.astro.repository.CommentRepository;
import in.astro.repository.PostRepository;
import in.astro.repository.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;


    @RequestMapping("/home")
    public String home(
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_BY_DATE, required = false) String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder,
            Model model){
        System.out.println("Page Number: "+pageNumber);
        PostResponse response = postService.getAllPosts(pageNumber, pageSize, sortBy, sortOrder);
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("response", response);
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
        if (authentication != null) {
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
    public String createComment(@PathVariable Long id,@ModelAttribute Comment comment, Authentication authentication) {
        Post post = postService.getPostById(id);
        if (post != null){
            System.out.println("post found");
            String username = SessionHelper.getEmailOfLoggedInUser(authentication);
            if (username != null){
                User user = userService.getUserByEmail(username);
                if (user != null){
                    Comment newComment = new Comment();
                    newComment.setContent(comment.getContent());
                    newComment.setPost(post);
                    newComment.setUser(user);
                    System.out.println(comment.getContent());
                    commentRepository.save(newComment);
                }
            }
        }
        return "redirect:/post/"+id;
    }
}
