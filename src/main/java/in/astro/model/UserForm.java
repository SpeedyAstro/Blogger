package in.astro.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor@NoArgsConstructor@Builder
public class UserForm {
    private Long userId;
    @Email(message = "Please enter a valid email")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Min 6 Characters is required")
    private String password;
    @NotBlank
    @Size(min = 3, message = "First Name should have at least 3 characters")
    private String firstName;
    private String lastName;
    private String about;
    private String companyName;
    @Size(min = 8, max = 12, message = "Invalid Phone Number")
    private String phoneNumber;
    private String profilePic;
}
