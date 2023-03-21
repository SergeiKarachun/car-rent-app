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
import by.sergey.carrentapp.utils.PageableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String getAll(Model model,
                         @ModelAttribute CarFilter carFilter,
                         @RequestParam(required = false, defaultValue = "1") Integer page,
                         @RequestParam(required = false, defaultValue = "10") Integer size) {
        Page<CarResponseDto> carPage = carService.getAll(carFilter, page - 1, size);
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
    public String findAllWithAccidents(Model model,
                                       @ModelAttribute CarFilter carFilter,
                                       @RequestParam(required = false, defaultValue = "1") Integer page,
                                       @RequestParam(required = false, defaultValue = "10") Integer size) {

        model.addAttribute("page", page);
        model.addAttribute("size", size);


        return "layout/car/cars";
    }

    @GetMapping("/without-accidents")
    public String findAllWithoutAccidents(Model model,
                                          @ModelAttribute CarFilter carFilter,
                                          @RequestParam(required = false, defaultValue = "1") Integer page,
                                          @RequestParam(required = false, defaultValue = "10") Integer size) {
        var cars = carService.getAllWithoutAccidents();
        var carPage = new PageImpl<>(cars);
        model.addAttribute("carPage", carPage);
        model.addAttribute("brands", brandService.getAll());
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("transmissions", Transmission.values());
        model.addAttribute("engines", EngineType.values());
        model.addAttribute("carFilter", carFilter);
        model.addAttribute("colors", Color.values());
        model.addAttribute("transmissions", Transmission.values());
        model.addAttribute("engines", EngineType.values());

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
    public String createCar(@ModelAttribute CarCreateRequestDto carDto,
                            RedirectAttributes redirectAttributes) {
        return carService.create(carDto)
                .map(car -> {
                    redirectAttributes.addAttribute(SUCCESS_ATTRIBUTE, "New car created successfully");
                    return "redirect:/cars/" + car.getId();
                })
                .orElseThrow(() -> new CarBadRequestException(HttpStatus.BAD_REQUEST, "Can't create new car, please check input parameters"));
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute CarUpdateRequestDto carDto) {
        return carService.update(id, carDto)
                .map(car -> "redirect:/cars/{id}")
                .orElseThrow(() -> new CarBadRequestException(HttpStatus.BAD_REQUEST, "Can't update car, please check input parameters"));
    }


    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        if (!carService.deleteById(id)) {
            throw new NotFoundException(String.format("Car with id %s doesn't exist."));
        }
        return "redirect:/cars";
    }


}
