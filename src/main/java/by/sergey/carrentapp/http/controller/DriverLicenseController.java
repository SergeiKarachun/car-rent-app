package by.sergey.carrentapp.http.controller;

import by.sergey.carrentapp.domain.UserDetailsImpl;
import by.sergey.carrentapp.domain.dto.driverlicense.DriverLicenseCreateRequestDto;
import by.sergey.carrentapp.domain.dto.driverlicense.DriverLicenseResponseDto;
import by.sergey.carrentapp.domain.dto.driverlicense.DriverLicenseUpdateRequestDto;
import by.sergey.carrentapp.domain.model.Role;
import by.sergey.carrentapp.service.DriverLicenseService;
import by.sergey.carrentapp.service.exception.DriverLicenseBadRequestException;
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
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/driver-licenses")
public class DriverLicenseController {

    private static final String SUCCESS_ATTRIBUTE = "success_message";
    private final DriverLicenseService driverLicenseService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String getAll(Model model,
                         @RequestParam(required = false, defaultValue = "1") Integer page,
                         @RequestParam(required = false, defaultValue = "10") Integer size) {

        Page<DriverLicenseResponseDto> driverLicensePage = driverLicenseService.getAll(page - 1, size);
        model.addAttribute("driverLicensePage", driverLicensePage);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        return "layout/user/driver-licenses";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String getById(@PathVariable("id") Long id, Model model) {
        return driverLicenseService.getById(id)
                .map(dl -> {
                    model.addAttribute("driverLicense", dl);
                    return "layout/user/driver-license";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute @Valid DriverLicenseUpdateRequestDto driverLicenseUpdateRequestDto,
                         @AuthenticationPrincipal UserDetailsImpl user) {
        return driverLicenseService.update(id, driverLicenseUpdateRequestDto)
                .map(dl -> {
                            if (user.getAuthorities().contains(Role.CLIENT)) {
                                return "redirect:/users/profile/" + user.getId();
                            }
                            return "redirect:/driver-licenses/{id}";
                        }
                )
                .orElseThrow(() -> new DriverLicenseBadRequestException(HttpStatus.BAD_REQUEST, "Can't update driver license. Please check input parameters."));
    }

    @GetMapping("/{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String deleteById(@PathVariable("id") Long id) {
        if (!driverLicenseService.deleteById(id)) {
            throw new NotFoundException(String.format("Driver license with id %s doesn't exist.", id));
        }
        return "redirect:/driver-licenses";
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String create(@ModelAttribute @Valid DriverLicenseCreateRequestDto driverLicenseCreateRequestDto,
                         RedirectAttributes redirectAttributes) {
        return driverLicenseService.create(driverLicenseCreateRequestDto)
                .map(dl -> {
                    redirectAttributes.addAttribute(SUCCESS_ATTRIBUTE, "You have successfully created driver license.");
                    return "redirect:/driver-license" + dl.getId();
                })
                .orElseThrow(() -> new DriverLicenseBadRequestException(HttpStatus.BAD_REQUEST, "Can't create driver license"));
    }

    @GetMapping("/by-user-id")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    public String getByUserId(@RequestParam("userId") Long userId, Model model,
                              @AuthenticationPrincipal UserDetailsImpl user) {
        if (user.getAuthorities().contains(Role.ADMIN)) {
            return getContent(userId, model);
        } else {
            return getContent(user.getId(), model);
        }
    }

    private String getContent(Long userId, Model model) {
        return driverLicenseService.getByUserId(userId)
                .map(driverLicense -> {
                    model.addAttribute("driverLicense", driverLicense);
                    return "layout/user/driver-license";
                })
                .orElseThrow(() -> new NotFoundException(String.format("Driver license with user id %s doesn't exist", userId)));
    }

    @GetMapping("/by-number")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String getByNumber(@RequestParam String number, Model model) {
        return driverLicenseService.getByNumber(number)
                .map(driverLicense -> {
                    model.addAttribute("driverLicense", driverLicense);
                    return "layout/user/driver-license";
                })
                .orElseThrow(() -> new NotFoundException(String.format("Driver license with number %s doesn't exist", number)));
    }

    @GetMapping("/all-expired")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String getAllExpired(Model model,
                                @RequestParam(required = false, defaultValue = "1") Integer page,
                                @RequestParam(required = false, defaultValue = "10") Integer size) {
        List<DriverLicenseResponseDto> allExpired = driverLicenseService.getAllExpiredDriverLicenses();
        PageImpl<DriverLicenseResponseDto> driverLicensePage = new PageImpl<>(allExpired);
        model.addAttribute("driverLicensePage", driverLicensePage);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        return "layout/user/driver-licenses";
    }


}
