package by.sergey.carrentapp.domain.dto.user;

import by.sergey.carrentapp.domain.model.Role;


public record UserUpdateRequestDto(String username,
                                   String email,
                                   Role role) {
}
