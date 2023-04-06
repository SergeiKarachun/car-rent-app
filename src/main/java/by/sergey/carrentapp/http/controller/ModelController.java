package by.sergey.carrentapp.http.controller;

import by.sergey.carrentapp.domain.dto.filterdto.ModelFilter;
import by.sergey.carrentapp.domain.dto.model.ModelCreateRequestDto;
import by.sergey.carrentapp.domain.dto.model.ModelUpdateRequestDto;
import by.sergey.carrentapp.domain.model.EngineType;
import by.sergey.carrentapp.domain.model.Transmission;
import by.sergey.carrentapp.service.BrandService;
import by.sergey.carrentapp.service.ModelService;
import by.sergey.carrentapp.service.exception.ModelBadRequestException;
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

@Controller
@RequestMapping("/models")
@RequiredArgsConstructor
public class ModelController {

    private static final String SUCCESS_ATTRIBUTE = "success_message";

    private final ModelService modelService;
    private final BrandService brandService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    public String findAllModels(Model model, @ModelAttribute ModelFilter modelFilter){
        model.addAttribute("models", modelService.getAll());
        model.addAttribute("transmissions", Transmission.values());
        model.addAttribute("engines", EngineType.values());
        model.addAttribute("modelFilter", modelFilter);
        return "layout/model/models";
    }

    @GetMapping("/model-create")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String create(Model model, @ModelAttribute ModelCreateRequestDto modelDto) {
        model.addAttribute("model", modelDto);
        model.addAttribute("transmissions", Transmission.values());
        model.addAttribute("engines", EngineType.values());
        model.addAttribute("brands", brandService.getAll());
        return "layout/model/model-create";
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String createModel(@ModelAttribute @Valid ModelCreateRequestDto modelDto,
                              RedirectAttributes redirectAttributes) {
        return modelService.create(modelDto)
                .map(model -> {
                    redirectAttributes.addAttribute(SUCCESS_ATTRIBUTE, "You create new model successfully");
                    return "redirect:/models/" + model.getId();
                }).orElseThrow(() -> new ModelBadRequestException(HttpStatus.BAD_REQUEST, "Can not create brand. Please check input parameters"));
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        return modelService.getById(id)
                .map(modelResponseDto -> {
                    model.addAttribute("model", modelResponseDto);
                    model.addAttribute("transmissions", Transmission.values());
                    model.addAttribute("engines", EngineType.values());
                    return "layout/model/model";
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    public String findAllByFilter(Model model, ModelFilter modelFilter) {
        model.addAttribute("models", modelService.getAllByFilter(modelFilter));
        model.addAttribute("transmissions", Transmission.values());
        model.addAttribute("engines", EngineType.values());
        return "layout/model/models";
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute @Valid ModelUpdateRequestDto modelUpdateRequestDto) {
        return modelService.update(id, modelUpdateRequestDto)
                .map(model -> "redirect:/models/{id}")
                .orElseThrow(() -> new ModelBadRequestException(HttpStatus.BAD_REQUEST, "can not update model, please check input parameters."));
    }


    @PostMapping("{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String delete(@PathVariable("id") Long id) {
        if (!modelService.deleteById(id)) {
            throw new NotFoundException(String.format("Model with id %s doesn't exist.", id));
        }
        return "redirect:/models";
    }

}
