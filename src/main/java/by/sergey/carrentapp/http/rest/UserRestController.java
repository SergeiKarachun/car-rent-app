package by.sergey.carrentapp.http.rest;

import by.sergey.carrentapp.domain.dto.PageResponse;
import by.sergey.carrentapp.domain.dto.filterdto.UserFilter;
import by.sergey.carrentapp.domain.dto.user.UserChangePasswordRequestDto;
import by.sergey.carrentapp.domain.dto.user.UserCreateRequestDto;
import by.sergey.carrentapp.domain.dto.user.UserResponseDto;
import by.sergey.carrentapp.domain.dto.user.UserUpdateRequestDto;
import by.sergey.carrentapp.service.UserService;
import by.sergey.carrentapp.service.exception.NotFoundException;
import by.sergey.carrentapp.service.exception.UserBadRequestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@Tag(name = "User API", description = "User API")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping()
    @Operation(summary = "Get all users")
    public PageResponse<UserResponseDto> getAllUsers(@Parameter @RequestParam(required = false) String username,
                                                     @Parameter @RequestParam(required = false) String email,
                                                     @Parameter @RequestParam(required = false) String name,
                                                     @Parameter @RequestParam(required = false) String surname,
                                                     @Parameter @RequestParam(required = false) LocalDate birthday,
                                                     @Parameter @RequestParam(required = false) LocalDate expirationDate,
                                                     @Parameter @RequestParam(required = false) Boolean expiredLicense,
                                                     Pageable pageable) {
        UserFilter userFilter = UserFilter.builder()
                .username(username)
                .email(email)
                .name(name)
                .surname(surname)
                .birthday(birthday)
                .expirationDate(expirationDate)
                .expiredLicense(expiredLicense)
                .build();
        Page<UserResponseDto> usersPage = userService.getAll(userFilter, pageable.getPageNumber(), pageable.getPageSize());
        return PageResponse.of(usersPage);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create user")
    public UserResponseDto createUser(@Parameter(required = true) @RequestBody @Valid UserCreateRequestDto userCreateRequestDto) {

        return userService.create(userCreateRequestDto)
                .orElseThrow(() -> new UserBadRequestException("Can't create new user, please check input parametrs"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id")
    public UserResponseDto getById(@PathVariable("id") Long id) {
        return userService.getById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s does not exist.", id)));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update user")
    public UserResponseDto update(@PathVariable("id") Long id, @RequestBody @Valid UserUpdateRequestDto userDto) {
        return userService.update(id, userDto)
                .orElseThrow(() -> new UserBadRequestException(HttpStatus.BAD_REQUEST, "Can't update user, please check input parameters"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete user by id")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        return userService.deleteById(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PutMapping("{id}/change-password")
    @Operation(summary = "Change user password")
    public UserResponseDto changePassword(@PathVariable("id") Long id, @RequestBody @Valid UserChangePasswordRequestDto userChangePasswordDto) {

        return userService.changePassword(id, userChangePasswordDto)
                .orElseThrow(() -> new UserBadRequestException("Password has not been changed. Please check if old password is correct"));
    }

}
