package by.sergey.carrentapp.http.controller;

import by.sergey.carrentapp.domain.dto.filterdto.UserFilter;
import by.sergey.carrentapp.domain.dto.user.UserChangePasswordRequestDto;
import by.sergey.carrentapp.domain.dto.user.UserCreateRequestDto;
import by.sergey.carrentapp.domain.dto.user.UserResponseDto;
import by.sergey.carrentapp.domain.dto.user.UserUpdateRequestDto;
import by.sergey.carrentapp.domain.model.Role;
import by.sergey.carrentapp.service.UserService;
import by.sergey.carrentapp.service.exception.NotFoundException;
import by.sergey.carrentapp.service.exception.UserBadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private static final String SUCCESS_ATTRIBUTE = "success_message";
    private final UserService userService;

    @GetMapping()
    public String getAllUsers(Model model,
                              @ModelAttribute UserFilter userFilter,
                              @RequestParam(required = false, defaultValue = "1") Integer page,
                              @RequestParam(required = false, defaultValue = "10") Integer size) {
        Page<UserResponseDto> usersPage = userService.getAll(userFilter, page - 1, size);
        model.addAttribute("usersPage", usersPage);
        model.addAttribute("totalPages", usersPage.getTotalPages());
        model.addAttribute("filter", userFilter);
        model.addAttribute("roles", Role.values());
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("booleans", List.of(Boolean.FALSE, Boolean.TRUE));
        return "layout/user/users";
    }

    @GetMapping("/{id}")
    public String getById (@PathVariable("id") Long id, Model model) {
        return userService.getById(id)
                .map(user -> {
                    model.addAttribute("user", user);
                    return "layout/user/user";
                })
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s does not exist.", id)));
    }

    @GetMapping("/profile/{id}")
    public String getProfileById (@PathVariable("id") Long id, Model model) {
        return userService.getById(id)
                .map(user -> {
                    model.addAttribute("user", user);
                    return "layout/user/profile";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute @Valid UserUpdateRequestDto userDto) {
        return userService.update(id, userDto)
                .map(user -> "redirect:/users/{id}")
                .orElseThrow(() -> new UserBadRequestException(HttpStatus.BAD_REQUEST, "Can't update user, please check input parameters"));
    }

    @GetMapping("/sign-up")
    public String createView(Model model,
                             @ModelAttribute UserCreateRequestDto userCreateRequestDto) {
        model.addAttribute("user", userCreateRequestDto);
        return "layout/user/sign-up";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute @Valid UserCreateRequestDto userCreateRequestDto, BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userCreateRequestDto", bindingResult);
            redirectAttributes.addFlashAttribute("userCreateRequestDto", userCreateRequestDto);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            System.out.println(bindingResult);
            return "redirect:/users/sign-up";
        }

        return userService.create(userCreateRequestDto)
                .map(user -> {
                    redirectAttributes.addAttribute(SUCCESS_ATTRIBUTE, "New user created successfully");
                    //return "redirect:/login/";
                    return "redirect:/users/" + user.getId();
                })
                .orElseThrow(() -> new UserBadRequestException(HttpStatus.BAD_REQUEST, "Can't create new user"));
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        if (!userService.deleteById(id)) {
            throw new NotFoundException(String.format("User with id %s doesn't exist.", id));
        }
        return "redirect:/users";
    }

    @GetMapping("/change-password")
    public String changePasswordForm(Model model,
                                     @ModelAttribute @Valid UserChangePasswordRequestDto userChangePasswordDto) {
        model.addAttribute("change_password", userChangePasswordDto);
        return "layout/user/change-password";
    }

    @PostMapping("{id}/change-password")
    public String changePassword(@PathVariable("id") Long id,
                                 @ModelAttribute @Valid UserChangePasswordRequestDto userChangePasswordDto) {

        return userService.changePassword(id, userChangePasswordDto)
                .map(result -> "redirect:/users/{id}")
                .orElseThrow(() -> new UserBadRequestException("Password has not been changed. Please check if old password is correct"));
    }

    @PostMapping("/{id}/change-role")
    public String changeRole(@PathVariable("id") Long id,
                             @PathParam(value = "role") Role role) {
        return userService.changeRole(id, role)
                .map(result -> "redirect:/users")
                .orElseThrow(() -> new UserBadRequestException("Role have not been changed"));
    }

}
