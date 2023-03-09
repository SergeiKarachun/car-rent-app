package by.sergey.carrentapp.domain.dto.user;

public record UserChangePasswordRequestDto(String oldPassword,
                                           String newPassword) {
}
