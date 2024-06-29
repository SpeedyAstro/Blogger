package in.astro.service.impl;

import in.astro.entity.Category;
import in.astro.entity.Post;
import in.astro.entity.User;
import in.astro.model.PostForm;
import in.astro.payload.PostResponse;
import in.astro.repository.CategoryRepository;
import in.astro.repository.PostRepository;
import in.astro.repository.UserRepository;
import in.astro.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class PostServiceImpl implements IPostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;
    @Override
    public void savePost(PostForm postForm,String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        Post post = Post.builder()
                .title(postForm.getTitle())
                .content(postForm.getContent())
                .category(categoryRepository.findById(postForm.getCategoryId()).orElse(null))
                .image(postForm.getImageUrl())
                .user(user)
                .postAdded(Date.from(Instant.now()))
                .build();
        postRepository.save(post);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public PostResponse getAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        System.out.println("************"+pageDetails);
        Page<Post> postPage = postRepository.findAll(pageDetails);
        List<Post> posts = postPage.getContent();
        return  PostResponse.builder()
                .content(posts)
                .pageSize(postPage.getSize())
                .totalPages(postPage.getTotalPages())
                .totalElements(postPage.getTotalElements())
                .lastPage(postPage.isLast())
                .pageNumber(postPage.getNumber())
                .build();
    }

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public void updatePost(PostForm postForm) {
        Post post = postRepository.findById(postForm.getId()).orElse(null);
        if (post == null) return;
        post.setTitle(postForm.getTitle());
        post.setContent(postForm.getContent());
        post.setCategory(categoryRepository.findById(postForm.getCategoryId()).orElse(null));
        post.setUser(userRepository.findById(postForm.getUserId()).orElse(null));
        postRepository.save(post);
    }

    @Override
    public List<Post> getPostsByCategory(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) return null;
        return postRepository.findAllByCategory(category);
    }

    @Override
    public List<Post> getPostsByUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return null;
        return postRepository.findAllByUser(user);
    }


}
