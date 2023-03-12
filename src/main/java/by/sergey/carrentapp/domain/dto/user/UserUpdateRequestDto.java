package by.sergey.carrentapp.domain.dto.user;

import by.sergey.carrentapp.domain.model.Role;
import lombok.Value;

@Value
public class UserUpdateRequestDto {
    String username;
    String email;
    Role role;
}
