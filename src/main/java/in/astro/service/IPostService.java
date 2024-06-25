package in.astro.service;

import in.astro.entity.Post;
import in.astro.model.PostForm;

import java.util.List;

public interface IPostService {
    public void savePost(PostForm postForm,String email);
    public List<Post> getAllPosts();
    public Post getPostById(Long id);
    public void deletePost(Long id);
    public void updatePost(PostForm postForm);
    public List<Post> getPostsByCategory(Integer categoryId);
    public List<Post> getPostsByUser(Long userId);
}
