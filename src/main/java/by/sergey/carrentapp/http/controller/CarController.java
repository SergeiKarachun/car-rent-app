package by.sergey.carrentapp.http.controller;

import by.sergey.carrentapp.domain.dto.car.CarCreateRequestDto;
import by.sergey.carrentapp.domain.dto.car.CarResponseDto;
import by.sergey.carrentapp.domain.dto.car.CarUpdateRequestDto;
import by.sergey.carrentapp.domain.dto.filterdto.CarFilter;
import by.sergey.carrentapp.domain.model.Color;
import by.sergey.carrentapp.domain.model.EngineType;
import by.sergey.carrentapp.domain.model.Transmission;
import by.sergey.carrentapp.service.BrandService;
import by.sergey.carrentapp.service.CarService;
import by.sergey.carrentapp.service.CategoryService;
import by.sergey.carrentapp.service.ModelService;
import by.sergey.carrentapp.service.exception.CarBadRequestException;
import by.sergey.carrentapp.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {

    private static final String SUCCESS_ATTRIBUTE = "success_message";

    private final CarService carService;
    private final ModelService modelService;
    private final CategoryService categoryService;
    private final BrandService brandService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    public String getAll(Model model,
                         @ModelAttribute CarFilter carFilter,
                         @RequestParam(required = false, defaultValue = "1") Integer page,
                         @RequestParam(required = false, defaultValue = "9") Integer size) {
        Page<CarResponseDto> carPage = carService.getAll(carFilter, page - 1, size);
        int totalPages = carPage.getTotalPages();
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("carPage", carPage);
        model.addAttribute("brands", brandService.getAll());
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("transmissions", Transmission.values());
        model.addAttribute("engines", EngineType.values());
        model.addAttribute("carFilter", carFilter);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("colors", Color.values());
        model.addAttribute("transmissions", Transmission.values());
        model.addAttribute("engines", EngineType.values());

        return "layout/car/cars";
    }

    @GetMapping("/with-accidents")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String findAllWithAccidents(Model model,
                                       @ModelAttribute CarFilter carFilter,
                                       @RequestParam(required = false, defaultValue = "1") Integer page,
                                       @RequestParam(required = false, defaultValue = "9") Integer size) {
        Page<CarResponseDto> carPage = carService.getAllWithAccidents(page - 1, size);
        int totalPagesWith = carPage.getTotalPages();
        model.addAttribute("totalPagesWith", totalPagesWith);
        model.addAttribute("carPage", carPage);
        model.addAttribute("page", page);
        model.addAttribute("size", size);

        return "layout/car/cars";
    }

    @GetMapping("/without-accidents")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String findAllWithoutAccidents(Model model,
                                          @ModelAttribute CarFilter carFilter,
                                          @RequestParam(required = false, defaultValue = "1") Integer page,
                                          @RequestParam(required = false, defaultValue = "9") Integer size) {
        Page<CarResponseDto> cars = carService.getAllWithoutAccidents(page - 1, size);
        int totalPagesWithout = cars.getTotalPages();
        model.addAttribute("totalPagesWithout", totalPagesWithout);
        model.addAttribute("carPage", cars);
        model.addAttribute("page", page);
        model.addAttribute("size", size);

        return "layout/car/cars";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") Long id, Model model) {
        return carService.getById(id)
                .map(car -> {
                    model.addAttribute("car", car);
                    model.addAttribute("models", modelService.getAll());
                    model.addAttribute("categories", categoryService.getAll());
                    model.addAttribute("booleans", List.of(Boolean.TRUE, Boolean.FALSE));
                    model.addAttribute("colors", Color.values());
                    return "layout/car/car";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/by-number")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String getByNumber(@RequestParam("number") String number, Model model) {
        return carService.getByCarNumber(number)
                .map(car -> {
                    model.addAttribute("car", car);
                    model.addAttribute("models", modelService.getAll());
                    model.addAttribute("categories", categoryService.getAll());
                    model.addAttribute("booleans", List.of(Boolean.TRUE, Boolean.FALSE));
                    model.addAttribute("colors", Color.values());
                    return "layout/car/car";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/car-create")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String createView(Model model, @ModelAttribute CarCreateRequestDto carDto) {
        model.addAttribute("car", carDto);
        model.addAttribute("brands", brandService.getAll());
        model.addAttribute("models", modelService.getAll());
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("colors", Color.values());
        model.addAttribute("booleans", List.of(Boolean.TRUE, Boolean.FALSE));

        return "layout/car/car-create";
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String createCar(@ModelAttribute @Valid CarCreateRequestDto carDto,
                            RedirectAttributes redirectAttributes) {
        return carService.create(carDto)
                .map(car -> {
                    redirectAttributes.addAttribute(SUCCESS_ATTRIBUTE, "New car created successfully");
                    return "redirect:/cars/" + car.getId();
                })
                .orElseThrow(() -> new CarBadRequestException(HttpStatus.BAD_REQUEST, "Can't create new car, please check input parameters"));
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute @Valid CarUpdateRequestDto carDto) {
        return carService.update(id, carDto)
                .map(car -> "redirect:/cars/{id}")
                .orElseThrow(() -> new CarBadRequestException(HttpStatus.BAD_REQUEST, "Can't update car, please check input parameters"));
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String delete(@PathVariable("id") Long id) {
        if (!carService.deleteById(id)) {
            throw new NotFoundException(String.format("Car with id %s doesn't exist.", id));
        }
        return "redirect:/cars";
    }
}
