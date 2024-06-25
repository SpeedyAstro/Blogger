package in.astro.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostForm {
    private Long id;
    @NotBlank(message = "Title is required")
//    @Size(min = 3, message = "Title must be at least 3 characters long")
    private String title;
    @NotBlank(message = "Content is required")
    private String content;
    private Integer categoryId;
    private Long userId;
    private MultipartFile file;
    private String imageUrl;
}
