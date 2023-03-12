package by.sergey.carrentapp.mapper.userdetails;

import by.sergey.carrentapp.domain.dto.userdetails.UserDetailsCreateRequestDto;
import by.sergey.carrentapp.domain.entity.User;
import by.sergey.carrentapp.domain.entity.UserContact;
import by.sergey.carrentapp.domain.entity.UserDetails;
import by.sergey.carrentapp.mapper.CreateMapper;
import by.sergey.carrentapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDetailsCreateMapper implements CreateMapper<UserDetailsCreateRequestDto, UserDetails> {

    private final UserRepository userRepository;

    @Override
    public UserDetails mapToEntity(UserDetailsCreateRequestDto requestDto) {
        UserDetails userDetails = UserDetails.builder()
                .name(requestDto.getName())
                .surname(requestDto.getSurname())
                .userContact(UserContact.builder()
                        .address(requestDto.getAddress())
                        .phone(requestDto.getPhone())
                        .build())
                .birthday(requestDto.getBirthday())
                .build();

        var user = getUser(requestDto.getUserId());
        user.ifPresent(userDetails::setUser);

        return userDetails;
    }

    private Optional<User> getUser(Long userId) {
        return Optional.ofNullable(userId).
                flatMap(userRepository::findById);
    }
}
