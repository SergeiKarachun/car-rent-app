package by.sergey.carrentapp.mapper.user;

import by.sergey.carrentapp.domain.dto.user.UserUpdateRequestDto;
import by.sergey.carrentapp.domain.entity.User;
import by.sergey.carrentapp.mapper.UpdateMapper;
import org.springframework.stereotype.Component;

@Component
public class UserUpdateMapper implements UpdateMapper<UserUpdateRequestDto, User> {

    @Override
    public User mapToEntity(UserUpdateRequestDto dto, User entity) {
        merge(dto, entity);
        return entity;
    }

    @Override
    public void merge(UserUpdateRequestDto dto, User entity) {
        entity.setName(dto.username());
        entity.setEmail(dto.email());
        entity.setRole(dto.role());
    }
}
