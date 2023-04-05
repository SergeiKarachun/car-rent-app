package by.sergey.carrentapp.http.controller;

import by.sergey.carrentapp.domain.UserDetailsImpl;
import by.sergey.carrentapp.domain.dto.filterdto.UserDetailsFilter;
import by.sergey.carrentapp.domain.dto.userdetails.UserDetailsCreateRequestDto;
import by.sergey.carrentapp.domain.dto.userdetails.UserDetailsResponseDto;
import by.sergey.carrentapp.domain.dto.userdetails.UserDetailsUpdateRequestDto;
import by.sergey.carrentapp.domain.model.Role;
import by.sergey.carrentapp.service.UserDetailsService;
import by.sergey.carrentapp.service.exception.NotFoundException;
import by.sergey.carrentapp.service.exception.UserDetailsBadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/user-details")
@RequiredArgsConstructor
public class UserDetailsController {

    private static final String SUCCESS_ATTRIBUTE = "success_message";
    private final UserDetailsService userDetailsService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String getAll(Model model,
                         @ModelAttribute UserDetailsFilter userDetailsFilter,
                         @RequestParam(required = false, defaultValue = "1") Integer page,
                         @RequestParam(required = false, defaultValue = "10") Integer size) {
        Page<UserDetailsResponseDto> usersDetailsPage = userDetailsService.getAll(userDetailsFilter, page - 1, size);
        model.addAttribute("usersDetailsPage", usersDetailsPage);
        model.addAttribute("filter", userDetailsFilter);
        model.addAttribute("page", page);
        model.addAttribute("size", size);

        return "layout/user/user-details";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String getById(@PathVariable("id") Long id, Model model) {
        return userDetailsService.getById(id)
                .map(user -> {
                    model.addAttribute("userDetails", user);
                    return "layout/user/user-detail";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute @Valid UserDetailsUpdateRequestDto userDetailsDto,
                         @AuthenticationPrincipal UserDetailsImpl user) {
        return userDetailsService.update(id, userDetailsDto)
                .map(userDetails -> {
                            if (user.getAuthorities().contains(Role.CLIENT)) {
                                return "redirect:/users/profile/" + user.getId();
                            }
                            return "redirect:/user-details/{id}";
                        }
                )
                .orElseThrow(() -> new UserDetailsBadRequestException(HttpStatus.BAD_REQUEST, "Can't update user details. please check input parameters."));
    }

    @GetMapping("/{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String delete(@PathVariable("id") Long id) {
        if (!userDetailsService.deleteById(id)) {
            throw new NotFoundException(String.format("User details with id %s doesn't exist", id));
        }
        return "redirect:/user-details";
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String create(@ModelAttribute @Valid UserDetailsCreateRequestDto userDetailsCreateRequestDto,
                         RedirectAttributes redirectAttributes) {
        return userDetailsService.create(userDetailsCreateRequestDto)
                .map(userDetails -> {
                    redirectAttributes.addAttribute(SUCCESS_ATTRIBUTE, "You have successfully created details.");
                    return "redirect:/user-details/" + userDetails.getId();
                })
                .orElseThrow(() -> new UserDetailsBadRequestException(HttpStatus.BAD_REQUEST, "Can't create user detail."));
    }

    @GetMapping("/by-user-id")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    public String getByUserId(@RequestParam Long id, Model model,
                              @AuthenticationPrincipal UserDetailsImpl user) {
        if (user.getAuthorities().contains(Role.ADMIN)) {
            return getContent(id, model);
        } else {
            return getContent(user.getId(), model);
        }
    }

    private String getContent(Long id, Model model) {
        return userDetailsService.getByUserId(id)
                .map(userDetails -> {
                    model.addAttribute("userDetails", userDetails);
                    return "layout/user/user-detail";
                })
                .orElseThrow(() -> new NotFoundException(String.format("User details with user id %s doesn't exist.", id)));
    }

    @GetMapping("/by-name-surname")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String getByNameAndSurname(@RequestParam(required = false) String name,
                                      @RequestParam(required = false) String surname,
                                      Model model,
                                      @ModelAttribute UserDetailsFilter userDetailsFilter,
                                      @RequestParam(required = false, defaultValue = "1") Integer page,
                                      @RequestParam(required = false, defaultValue = "10") Integer size) {
        List<UserDetailsResponseDto> userDetails = userDetailsService.getAllByNameAndSurname(name, surname);
        PageImpl<UserDetailsResponseDto> userDetailsPage = new PageImpl<>(userDetails);
        model.addAttribute("filter", userDetailsFilter);
        model.addAttribute("usersDetailsPage", userDetailsPage);
        model.addAttribute("page", page);
        model.addAttribute("size", size);

        return "layout/user/user-details";
    }

    @GetMapping("/by-registration-dates")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String getByRegistrationDates(@RequestParam(required = false, defaultValue = "2000-12-30") LocalDate from,
                                         @RequestParam(required = false, defaultValue = "2051-12-30") LocalDate to,
                                         Model model,
                                         @ModelAttribute UserDetailsFilter userDetailsFilter,
                                         @RequestParam(required = false, defaultValue = "1") Integer page,
                                         @RequestParam(required = false, defaultValue = "10") Integer size) {
        List<UserDetailsResponseDto> userDetails = userDetailsService.getAllByRegistrationDates(from, to);
        PageImpl<UserDetailsResponseDto> userDetailsPage = new PageImpl<>(userDetails);
        model.addAttribute("filter", userDetailsFilter);
        model.addAttribute("usersDetailsPage", userDetailsPage);
        model.addAttribute("page", page);
        model.addAttribute("size", size);

        return "layout/user/user-details";
    }
}



