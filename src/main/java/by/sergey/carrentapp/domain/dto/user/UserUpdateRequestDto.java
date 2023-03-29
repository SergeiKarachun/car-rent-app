package by.sergey.carrentapp.domain.dto.user;

import by.sergey.carrentapp.annotation.EnumNamePattern;
import by.sergey.carrentapp.domain.model.Role;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
public class UserUpdateRequestDto {
    @NotBlank(message = "Username is required")
    @Size(min = 2, message = "Username should have at least 2 characters")
    String username;
    @NotBlank(message = "Email is required")
    @Email
    String email;
    @EnumNamePattern(regexp = "CLIENT|ADMIN")
    Role role;
}
