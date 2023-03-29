package by.sergey.carrentapp.http.controller;

import by.sergey.carrentapp.domain.dto.accident.AccidentCreateRequestDto;
import by.sergey.carrentapp.domain.dto.accident.AccidentResponseDto;
import by.sergey.carrentapp.domain.dto.accident.AccidentUpdateRequestDto;
import by.sergey.carrentapp.service.AccidentService;
import by.sergey.carrentapp.service.OrderService;
import by.sergey.carrentapp.service.exception.AccidentBadRequestException;
import by.sergey.carrentapp.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/accidents")
public class AccidentController {

    private static final String SUCCESS_ATTRIBUTE = "success_message";
    private static final String ERROR_ATTRIBUTE = "error_message";
    private final AccidentService accidentService;
    private final OrderService orderService;

    @GetMapping
    public String getAll(@RequestParam(required = false, defaultValue = "1") Integer page,
                         @RequestParam(required = false, defaultValue = "10") Integer size,
                         Model model) {
        model.addAttribute("accidentsPage", accidentService.getAll(page - 1, size));
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        return "layout/accident/accidents";
    }

    @GetMapping("/by-car")
    public String getAllByCarNumber(@RequestParam(required = false, defaultValue = "1") Integer page,
                                    @RequestParam(required = false, defaultValue = "10") Integer size,
                                    Model model,
                                    @RequestParam(required = false) @Nullable String carNumber) {
        List<AccidentResponseDto> accidents = accidentService.getAllByCarNumber(carNumber);
        PageImpl<AccidentResponseDto> accidentsPage = new PageImpl<>(accidents);

        model.addAttribute("accidentsPage", accidentsPage);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("carNumber", carNumber);
        return "layout/accident/accidents";
    }

    @GetMapping("/by-order")
    public String getAllByOrder(@RequestParam(required = false, defaultValue = "1") Integer page,
                                @RequestParam(required = false, defaultValue = "10") Integer size,
                                Model model,
                                @RequestParam(required = false) @Nullable Long orderId) {
        List<AccidentResponseDto> accidents = accidentService.getAllByOrderId(orderId);
        PageImpl<AccidentResponseDto> accidentsPage = new PageImpl<>(accidents);

        model.addAttribute("accidentsPage", accidentsPage);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("orderId", orderId);
        return "layout/accident/accidents";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") Long id, Model model) {
        return accidentService.getById(id)
                .map(accident -> {
                    model.addAttribute("accident", accident);
                    return "layout/accident/accident";
                })
                .orElseThrow(() -> new NotFoundException(String.format("Accident with od %s doesn't exist.", id)));
    }

    @PostMapping("/{id}/update")
    public String update(@ModelAttribute @Valid AccidentUpdateRequestDto accidentUpdateRequestDto,
                         @PathVariable("id") Long id) {
        return accidentService.update(id, accidentUpdateRequestDto)
                .map(accident -> "redirect:/accidents/{id}")
                .orElseThrow(() -> new AccidentBadRequestException(HttpStatus.NOT_FOUND, "Can't update accident, please check input parameters."));
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        if (!accidentService.deleteById(id)) {
            throw new AccidentBadRequestException(HttpStatus.NOT_FOUND, "Accident with id %s doesn't exist.");
        }
        return "redirect:/accidents";
    }

    @GetMapping("/accident-create")
    public String createAccident(@ModelAttribute AccidentCreateRequestDto accidentCreateRequestDto,
                                 Model model) {
        model.addAttribute("accident", accidentCreateRequestDto);
        model.addAttribute("orders", orderService.getAll());
        return "layout/accident/accident-create";
    }

    @PostMapping
    public String create(@ModelAttribute @Valid AccidentCreateRequestDto accidentCreateRequestDto,
                         RedirectAttributes redirectAttributes) {
        return accidentService.create(accidentCreateRequestDto)
                .map(accident -> {
                    redirectAttributes.addAttribute(SUCCESS_ATTRIBUTE, "New accident has been created.");
                    return "redirect:/accidents/" + accident.getId();
                })
                .orElseThrow(() -> new AccidentBadRequestException(HttpStatus.BAD_REQUEST, "Can't create new accident, please check input parameters."));
    }


}
