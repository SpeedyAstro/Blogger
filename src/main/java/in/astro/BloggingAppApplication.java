package in.astro;

import com.cloudinary.Cloudinary;
import in.astro.entity.Category;
import in.astro.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootApplication
//@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
public class BloggingAppApplication implements CommandLineRunner {

	@Autowired
	private CategoryRepository categoryRepository;

	public static void main(String[] args) {
		SpringApplication.run(BloggingAppApplication.class, args);

	}

	@Bean
	public Cloudinary cloudinary() {
		Map<String, String> env = new HashMap<>();
		env.put("cloud_name", "dvqaeehte");
		env.put("api_key", "617982441979326");
		env.put("api_secret", "zh46RJEuNBiVy4by1F6NJpT9D20");
		env.put("secure", "true");
		return new Cloudinary(env);
	}

	@Bean
	public DispatcherServlet dispatcherServlet() {
		DispatcherServlet dispatcherServlet = new DispatcherServlet();
//		set true for handling 404 error, setThrowExceptionIfNoHandlerFound is deprecated
		dispatcherServlet.setDetectAllHandlerExceptionResolvers(true);
		return dispatcherServlet;
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		List<String> categories = List.of("Technology", "Health", "Science", "Sports", "Food", "Travel", "Fashion", "Education", "Music", "Movies");
		Integer id = 1;
		Optional<Category> id1 = categoryRepository.findById(id);
		if (id1.isPresent()) {
			return;
		}
		if (id1.isEmpty()) {
			for (String category : categories) {
				Category newCategory = new Category();
				newCategory.setCategory_id(id);
				newCategory.setCategory_title(category);
				categoryRepository.save(newCategory);
				id++;
			}
		}
	}
}
