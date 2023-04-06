package by.sergey.carrentapp.http.controller;

import by.sergey.carrentapp.domain.dto.brand.BrandCreateUpdateRequestDto;
import by.sergey.carrentapp.domain.projection.BrandFullView;
import by.sergey.carrentapp.service.BrandService;
import by.sergey.carrentapp.service.exception.BrandBadRequestException;
import by.sergey.carrentapp.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandController {

    private static final String SUCCESS_ATTRIBUTE = "success_message";

    private final BrandService brandService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    public String findAllBrands(Model model) {
        model.addAttribute("brands", brandService.getAll());

        return "layout/brand/brands";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    public String findById(@PathVariable("id") Long id, Model model) {
        return brandService.getById(id)
                .map(brand -> {
                    model.addAttribute("brand", brand);
                    return "layout/brand/brand";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/brand-full-view/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String findFull(@PathVariable("id") Long id, Model model) {
        model.addAttribute("brand", brandService.getByIdFullView(id));
        return "layout/brand/brand-full-view";
    }

    @GetMapping("/by-name")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    public String findByName(@RequestParam String name, Model model) {
        return brandService.getByName(name)
                .map(brand -> {
                    model.addAttribute("brand", brand);
                    return "redirect:/brands/" + brand.getId();
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/by-names")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    public String findByNames(@RequestParam String name, Model model) {
        List<String> strings = Arrays.stream(name.split(",")).toList();
        model.addAttribute("brands", brandService.getByNames(strings));

        return "layout/brand/brands";

    }

    @GetMapping("/all-by-names")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String findAllByNames(@RequestParam String name, Model model) {
        model.addAttribute("listfullview", brandService.getByNameFullView(name));
        return "layout/brand/brands-full-view";
    }

    @GetMapping("/brands-full-view")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String findAllFull(Model model) {
        List<BrandFullView> allFullView = brandService.getAllFullView();
        model.addAttribute("listfullview", allFullView);
        return "layout/brand/brands-full-view";
    }

    @GetMapping("/brand-create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String create(Model model, @ModelAttribute BrandCreateUpdateRequestDto brand) {
        model.addAttribute("brand", brand);
        return "layout/brand/brand-create";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String create(@ModelAttribute @Valid BrandCreateUpdateRequestDto requestDto,
                         RedirectAttributes redirectAttributes) {

        return brandService.create(requestDto)
                .map(brand -> {
                    redirectAttributes.addAttribute(SUCCESS_ATTRIBUTE, "You create new brand successfully");
                    return "redirect:/brands/" + brand.getId();
                })
                .orElseThrow(() -> new BrandBadRequestException(HttpStatus.BAD_REQUEST, "Can not create brand. Please check input parameters"));
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute @Valid BrandCreateUpdateRequestDto requestDto) {
        return brandService.update(id, requestDto)
                .map(brand -> "redirect:/brands/{id}")
                .orElseThrow(() -> new BrandBadRequestException(HttpStatus.NOT_FOUND, "can not update brand. Please check input parameters."));
    }

    @PostMapping("{id}/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String delete(@PathVariable("id") Long id) {
        if (!brandService.deleteById(id)) {
            throw new NotFoundException(String.format("Brand with id %s does not exist.", id));
        }
        return "redirect:/brands";
    }
}
