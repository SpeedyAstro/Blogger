package in.astro.repository;

import in.astro.entity.Category;
import in.astro.entity.Post;
import in.astro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findAllByCategory(Category category);
    List<Post> findAllByUser(User user);
}
