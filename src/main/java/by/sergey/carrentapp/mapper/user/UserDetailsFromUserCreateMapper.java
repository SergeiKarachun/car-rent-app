package by.sergey.carrentapp.mapper.user;

import by.sergey.carrentapp.domain.dto.user.UserCreateRequestDto;
import by.sergey.carrentapp.domain.entity.UserContact;
import by.sergey.carrentapp.domain.entity.UserDetails;
import by.sergey.carrentapp.mapper.CreateMapper;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsFromUserCreateMapper implements CreateMapper<UserCreateRequestDto, UserDetails> {
    @Override
    public UserDetails mapToEntity(UserCreateRequestDto requestDto) {
        return UserDetails.builder()
                .name(requestDto.getName())
                .surname(requestDto.getSurname())
                .userContact(UserContact.builder()
                        .address(requestDto.getAddress())
                        .phone(requestDto.getPhone())
                        .build())
                .birthday(requestDto.getBirthday())
                .build();
    }
}
