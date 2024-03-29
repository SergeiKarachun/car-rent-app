package by.sergey.carrentapp.http.controller;

import by.sergey.carrentapp.domain.dto.category.CategoryCreateUpdateRequestDto;
import by.sergey.carrentapp.domain.dto.filterdto.CategoryFilter;
import by.sergey.carrentapp.service.CategoryService;
import by.sergey.carrentapp.service.exception.CategoryBadRequestException;
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
import java.math.BigDecimal;

@RequestMapping("/categories")
@RequiredArgsConstructor
@Controller
public class CategoryController {

    private static final String SUCCESS_ATTRIBUTE = "success_message";

    private final CategoryService categoryService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    public String getAll(Model model, @ModelAttribute CategoryFilter categoryFilter) {
        model.addAttribute("categoryFilter", categoryFilter);
        model.addAttribute("categories", categoryService.getAll());
        return "layout/category/categories";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    public String getById(@PathVariable("id") Long id, Model model) {
        return categoryService.getById(id)
                .map(categoryResponseDto -> {
                    model.addAttribute("category", categoryResponseDto);
                    return "layout/category/category";
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute @Valid CategoryCreateUpdateRequestDto categoryDto) {
        return categoryService.update(id, categoryDto)
                .map(category -> "redirect:/categories/{id}")
                .orElseThrow(() -> new CategoryBadRequestException(HttpStatus.BAD_REQUEST, "can not update model, please check input parameters."));
    }

    @GetMapping("/category-create")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String createView(Model model, @ModelAttribute CategoryCreateUpdateRequestDto categoryDto) {
        model.addAttribute("category", categoryDto);
        return "layout/category/category-create";
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String createCategory(@ModelAttribute @Valid CategoryCreateUpdateRequestDto categoryDto,
                                 RedirectAttributes redirectAttributes) {
        return categoryService.create(categoryDto)
                .map(category -> {
                    redirectAttributes.addAttribute(SUCCESS_ATTRIBUTE, "You successfully create new category");
                    return "redirect:/categories/" + category.getId();
                })
                .orElseThrow(() -> new CategoryBadRequestException(HttpStatus.BAD_REQUEST, "Can't create new category, please check input parameters"));
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String delete(@PathVariable("id") Long id) {
        if (!categoryService.deleteById(id)) {
            throw new NotFoundException(String.format("Category with id %s doesn't exist.", id));
        }
        return "redirect:/categories";
    }

    @GetMapping("/by-name")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    public String findByName(@RequestParam String name, Model model) {
        return categoryService.findByName(name)
                .map(category -> {
                    model.addAttribute("category", category);
                    return "redirect:/categories/" + category.getId();
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/by-price-less")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    public String findByPriceLess(BigDecimal priceLs, Model model) {
        model.addAttribute("priceLs", priceLs);
        model.addAttribute("categories", categoryService.getAllByPriceLess(priceLs));
        return "layout/category/categories";
    }

    @GetMapping("/by-price-greater")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    public String findByPriceGreater(BigDecimal priceGr, Model model) {
        model.addAttribute("priceGr", priceGr);
        model.addAttribute("categories", categoryService.getAllByPriceGreater(priceGr));
        return "layout/category/categories";
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    public String findAllByFilter(Model model, CategoryFilter categoryFilter) {
        model.addAttribute("categories", categoryService.getAllByPrice(categoryFilter));
        return "layout/category/categories";
    }
}
