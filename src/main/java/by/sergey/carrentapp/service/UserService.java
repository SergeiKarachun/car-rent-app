package by.sergey.carrentapp.service;

import by.sergey.carrentapp.domain.dto.filterdto.UserFilter;
import by.sergey.carrentapp.domain.dto.user.*;
import by.sergey.carrentapp.domain.entity.User;
import by.sergey.carrentapp.domain.model.Role;
import by.sergey.carrentapp.mapper.user.UserCreateMapper;
import by.sergey.carrentapp.mapper.user.UserResponseMapper;
import by.sergey.carrentapp.mapper.user.UserUpdateMapper;
import by.sergey.carrentapp.repository.UserRepository;
import by.sergey.carrentapp.service.exception.CarBadRequestException;
import by.sergey.carrentapp.service.exception.ExceptionMessageUtil;
import by.sergey.carrentapp.service.exception.NotFoundException;
import by.sergey.carrentapp.service.exception.UserBadRequestException;
import by.sergey.carrentapp.utils.PageableUtils;
import by.sergey.carrentapp.utils.predicate.UserPredicateBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserCreateMapper userCreateMapper;
    private final UserResponseMapper userResponseMapper;
    private final UserUpdateMapper userUpdateMapper;
    private final UserPredicateBuilder userPredicateBuilder;

    @Transactional
    public Optional<UserResponseDto> create(UserCreateRequestDto userCreateRequestDto) {
        checkEmailIsUnique(userCreateRequestDto.getEmail());
        checkUsernameIsUnique(userCreateRequestDto.getUsername());

        return Optional.of(userCreateMapper.mapToEntity(userCreateRequestDto))
                .map(userRepository::save)
                .map(userResponseMapper::mapToDto);
    }

    @Transactional
    public Optional<UserResponseDto> login(LoginRequestDto loginRequestDto) {
        return userRepository.findByEmailAndPassword(loginRequestDto.getEmail(), loginRequestDto.getPassword())
                .map(userResponseMapper::mapToDto);
    }

    @Transactional
    public Optional<UserResponseDto> update(Long id, UserUpdateRequestDto userUpdateRequestDto) {
        User existingUser = getByIdOrElseThrow(id);
        if (!existingUser.getEmail().equals(userUpdateRequestDto.getEmail())) {
            checkEmailIsUnique(userUpdateRequestDto.getEmail());
        }
        if (!existingUser.getUsername().equals(userUpdateRequestDto.getUsername())) {
            checkUsernameIsUnique(userUpdateRequestDto.getUsername());
        }

        return Optional.of(userUpdateMapper.mapToEntity(userUpdateRequestDto, existingUser))
                .map(userRepository::save)
                .map(userResponseMapper::mapToDto);
    }

    public Optional<UserResponseDto> getById(Long id) {
        return Optional.of(getByIdOrElseThrow(id))
                .map(userResponseMapper::mapToDto);
    }

    @Transactional
    public Optional<UserResponseDto> changePassword(Long id, UserChangePasswordRequestDto userChangePasswordRequestDto) {
        User existingUser = getByIdOrElseThrow(id);

        if (isExistByEmailAndPassword(existingUser.getEmail(), userChangePasswordRequestDto.getOldPassword())) {
            existingUser.setPassword(userChangePasswordRequestDto.getNewPassword());
        }

        return Optional.of(userRepository.save(existingUser))
                .map(userResponseMapper::mapToDto);
    }

    @Transactional
    public Optional<UserResponseDto> changeRole(Long id, Role role) {
        User existingUser = getByIdOrElseThrow(id);
        Optional.of(role).ifPresent(existingUser::setRole);

        return Optional.of(userRepository.save(existingUser))
                .map(userResponseMapper::mapToDto);
    }

    public Page<UserResponseDto> getAll(UserFilter userFilter, Integer page, Integer pageSize) {
        return userFilter.getExpiredLicense() == null || !userFilter.getExpiredLicense()
                ? userRepository.findAll(userPredicateBuilder.build(userFilter), PageableUtils.getSortedPageable(page, pageSize, Sort.Direction.ASC, "id")).map(userResponseMapper::mapToDto)
                : userRepository.findAllWithExpiredDriverLicense(LocalDate.now(), PageableUtils.unSortedPageable(page, pageSize)).map(userResponseMapper::mapToDto);

    }


    public List<UserResponseDto> getAllWithoutPage() {
        return userRepository.findAll().stream()
                .map(userResponseMapper::mapToDto)
                .toList();
    }

    @Transactional
    public boolean deleteById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public User getByIdOrElseThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageUtil.getNotFoundMessage("User", "id", id)));
    }

    public void checkUsernameIsUnique(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new UserBadRequestException(String.format(ExceptionMessageUtil.getAlreadyExistsMessage("User", "username", username)));
        }
    }

    public void checkEmailIsUnique(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new CarBadRequestException(ExceptionMessageUtil.getAlreadyExistsMessage("User", "email", email));
        }
    }

    private boolean isExistByEmailAndPassword(String email, String password) {
        return userRepository.existsByEmailAndPassword(email, password);
    }


}
