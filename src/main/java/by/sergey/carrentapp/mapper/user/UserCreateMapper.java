package by.sergey.carrentapp.mapper.user;

import by.sergey.carrentapp.domain.dto.user.UserCreateRequestDto;
import by.sergey.carrentapp.domain.entity.User;
import by.sergey.carrentapp.mapper.CreateMapper;
import org.springframework.stereotype.Component;

@Component
public class UserCreateMapper implements CreateMapper<UserCreateRequestDto, User> {
    @Override
    public User mapToEntity(UserCreateRequestDto requestDto) {
        return null;
    }
}
