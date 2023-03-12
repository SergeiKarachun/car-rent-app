package by.sergey.carrentapp.domain.dto.user;

import lombok.Value;

@Value
public class LoginRequestDto {
    String email;
    String password;
}
