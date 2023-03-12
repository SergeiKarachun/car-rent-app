package by.sergey.carrentapp.domain.dto.user;

import lombok.Value;

@Value
public class UserChangePasswordRequestDto {
    String oldPassword;
    String newPassword;
}
