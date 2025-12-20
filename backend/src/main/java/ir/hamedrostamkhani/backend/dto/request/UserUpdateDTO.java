package ir.hamedrostamkhani.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class UserUpdateDTO {
        @Size(min = 3, max = 50)
        private String name;

        @Email
        private String email;

        @Size(min = 8, max = 60)
        private String password;

        private MultipartFile avatar;

        @Pattern(regexp = "^(user|admin)$", message = "Role must be 'user' or 'admin'")
        private String role;

        Boolean active;
}
