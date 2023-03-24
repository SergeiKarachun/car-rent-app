package by.sergey.carrentapp.http.controller;

import by.sergey.carrentapp.domain.dto.user.LoginRequestDto;
import by.sergey.carrentapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "/layout/user/login";
    }

    @PostMapping("/login")
    public String login(Model model, @ModelAttribute("login") LoginRequestDto loginDto) {
        return userService.login(loginDto)
                .map(user -> {
                    return "redirect:/cars";
                })
                .orElse("redirect:/login");

    }
}
