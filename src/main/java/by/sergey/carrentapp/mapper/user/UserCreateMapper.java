package by.sergey.carrentapp.mapper.user;

import by.sergey.carrentapp.domain.dto.user.UserCreateRequestDto;
import by.sergey.carrentapp.domain.entity.User;
import by.sergey.carrentapp.mapper.CreateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCreateMapper implements CreateMapper<UserCreateRequestDto, User> {
    private final UserDetailsFromUserCreateMapper userDetailsCreateMapper;
    private final DriverLicenseFromUserCreateMapper driverLicenseCreateMapper;
    private final PasswordEncoder passwordEncoder;

    public User mapToEntity(UserCreateRequestDto requestDto) {
        var driverLicense = driverLicenseCreateMapper.mapToEntity(requestDto);
        var userDetails = userDetailsCreateMapper.mapToEntity(requestDto);
        var user = User.builder()
                .username(requestDto.getUsername())
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build();
        userDetails.setUser(user);
        driverLicense.setUserDetails(userDetails);
        return user;
    }

}
