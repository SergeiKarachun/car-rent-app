package by.sergey.carrentapp.http.controller;

import by.sergey.carrentapp.domain.dto.filterdto.OrderFilter;
import by.sergey.carrentapp.domain.dto.order.OrderCreateRequestDto;
import by.sergey.carrentapp.domain.dto.order.OrderResponseDto;
import by.sergey.carrentapp.domain.dto.order.OrderUpdateRequestDto;
import by.sergey.carrentapp.domain.dto.order.OrderUserReportDto;
import by.sergey.carrentapp.domain.model.OrderStatus;
import by.sergey.carrentapp.service.CarService;
import by.sergey.carrentapp.service.OrderService;
import by.sergey.carrentapp.service.exception.NotFoundException;
import by.sergey.carrentapp.service.exception.OrderBadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private static final String SUCCESS_ATTRIBUTE = "success_message";
    private static final String ERROR_ATTRIBUTE = "error_message";
    private final CarService carService;
    private final OrderService orderService;

    @GetMapping
    public String getAll(Model model,
                         @ModelAttribute OrderFilter orderFilter,
                         @RequestParam(required = false, defaultValue = "1") Integer page,
                         @RequestParam(required = false, defaultValue = "10") Integer size) {
        Page<OrderResponseDto> ordersPage = orderService.getAll(orderFilter, page-1, size);
        model.addAttribute("ordersPage", ordersPage);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("filter", orderFilter);
        model.addAttribute("statuses", OrderStatus.values());
        return "layout/order/orders";
    }

    @GetMapping("/order-create")
    public String createView(Model model,
                             @ModelAttribute OrderCreateRequestDto orderCreateRequestDto,
                             @RequestParam("carId") Long carId) {
        model.addAttribute("order", orderCreateRequestDto);
        model.addAttribute("car", carService.getById(carId).get());
        return "layout/order/order-create";
    }

    @PostMapping()
    public String create(@ModelAttribute OrderCreateRequestDto orderCreateRequestDto, RedirectAttributes redirectAttributes) {
        Optional<OrderResponseDto> order = orderService.create(orderCreateRequestDto);

        if (order.isEmpty()) {
            redirectAttributes.addAttribute(ERROR_ATTRIBUTE, "Car is unavailable for these dates. Please choose other dates or car");
            return "redirect:/cars";
        }
        redirectAttributes.addAttribute(SUCCESS_ATTRIBUTE, "You have successfully created new order.");
        return "redirect:/orders/" + order.get().getId();
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") Long id, Model model) {
        return orderService.getById(id)
                .map(order -> {
                    model.addAttribute("order", order);
                    model.addAttribute("cars", carService.getAll());
                    return "layout/order/order";
                })
                .orElseThrow(() -> new NotFoundException(String.format("Order with id %s does not exist.", id)));
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute OrderUpdateRequestDto orderUpdateRequestDto,
                         RedirectAttributes redirectAttributes) {
        Optional<OrderResponseDto> order = orderService.update(id, orderUpdateRequestDto);

        if (order.isEmpty()) {
            redirectAttributes.addAttribute(ERROR_ATTRIBUTE, "Order can't be update.");
        }

        return "redirect:/orders/{id}";
    }

    @PostMapping("/{id}/change-status")
    public String changeStatus(@PathVariable("id") Long id, @RequestParam OrderStatus status) {
        return orderService.changeOrderStatus(id, status)
                .map(order -> "redirect:/orders/{id}")
                .orElseThrow(() -> new OrderBadRequestException("Order status hasn't been changed."));
    }

    @GetMapping("/by-status")
    public String findAllByStatus(Model model,
                                  @ModelAttribute OrderFilter orderFilter,
                                  @RequestParam OrderStatus orderStatus) {
        List<OrderResponseDto> orders = orderService.getAllByStatus(orderStatus);
        PageImpl<OrderResponseDto> ordersPage = new PageImpl<>(orders);

        model.addAttribute("ordersPage", ordersPage);
        model.addAttribute("filter", orderFilter);
        model.addAttribute("statuses", OrderStatus.values());
        return "layout/order/orders";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        if (!orderService.deleteById(id)) {
            throw new NotFoundException(String.format("Order with id %s doesn't exist", id));
        }
        return "redirect:/orders";
    }

    @GetMapping("/user/{id}")
    public String getAllById(Model model,
                             @PathVariable("id") Long id,
                             @RequestParam(required = false) OrderStatus orderStatus,
                             @RequestParam(required = false) BigDecimal sum,
                             @RequestParam(required = false, defaultValue = "1") Integer page,
                             @RequestParam(required = false, defaultValue = "10") Integer size) {
        List<OrderUserReportDto> userOrders = orderService.getAllByUserId(id, orderStatus, sum);
        PageImpl<OrderUserReportDto> ordersPage = new PageImpl<>(userOrders);
        model.addAttribute("ordersPage", ordersPage);
        model.addAttribute("statuses", OrderStatus.values());
        model.addAttribute("page", page);
        model.addAttribute("size", size);

        return "layout/order/user-orders";
    }
}
