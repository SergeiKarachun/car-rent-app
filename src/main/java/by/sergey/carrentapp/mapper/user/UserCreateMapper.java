package by.sergey.carrentapp.mapper.user;

import by.sergey.carrentapp.domain.dto.user.UserCreateRequestDto;
import by.sergey.carrentapp.domain.entity.User;
import by.sergey.carrentapp.mapper.CreateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCreateMapper implements CreateMapper<UserCreateRequestDto, User> {
    private final UserDetailsFromUserCreateMapper userDetailsCreateMapper;
    private final DriverLicenseFromUserCreateMapper driverLicenseCreateMapper;


    public User mapToEntity(UserCreateRequestDto requestDto) {
        var driverLicense = driverLicenseCreateMapper.mapToEntity(requestDto);
        var userDetails = userDetailsCreateMapper.mapToEntity(requestDto);
        var user = User.builder()
                .name(requestDto.getUsername())
                .email(requestDto.getEmail())
                .password(requestDto.getPassword())
                .build();
        userDetails.setUser(user);
        userDetails.setDriverLicense(driverLicense);
        return user;
    }
}
