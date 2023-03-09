package by.sergey.carrentapp.mapper.userdetails;

import by.sergey.carrentapp.domain.dto.userdetails.UserDetailsResponseDto;
import by.sergey.carrentapp.domain.entity.UserDetails;
import by.sergey.carrentapp.mapper.ResponseMapper;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsResponseMapper implements ResponseMapper<UserDetails, UserDetailsResponseDto> {
    @Override
    public UserDetailsResponseDto mapToDto(UserDetails userDetails) {
        return UserDetailsResponseDto.builder()
                .id(userDetails.getId())
                .userId(userDetails.getUser().getId())
                .name(userDetails.getName())
                .surname(userDetails.getSurname())
                .address(userDetails.getUserContact().getAddress())
                .phone(userDetails.getUserContact().getPhone())
                .birthday(userDetails.getBirthday())
                .registrationAt(userDetails.getRegistrationDate())
                .build();
    }
}
