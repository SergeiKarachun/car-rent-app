package by.sergey.carrentapp.integration.http.controller;

import by.sergey.carrentapp.domain.dto.order.OrderResponseDto;
import by.sergey.carrentapp.domain.model.OrderStatus;
import by.sergey.carrentapp.integration.IntegrationTestBase;
import by.sergey.carrentapp.integration.auth.WithMockCustomUser;
import by.sergey.carrentapp.integration.utils.builder.TestDtoBuilder;
import by.sergey.carrentapp.service.*;
import by.sergey.carrentapp.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static by.sergey.carrentapp.integration.http.controller.OrderControllerTestIT.MOCK_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@AutoConfigureMockMvc
@RequiredArgsConstructor
@WithMockCustomUser(username = MOCK_USERNAME, authorities = {"ADMIN", "CLIENT"})
class OrderControllerTestIT extends IntegrationTestBase {

    static final String MOCK_USERNAME = "admin@gmail.com";
    static final String ENDPOINT = "/orders";
    private final MockMvc mockMvc;
    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final CarService carService;
    private final UserService userService;
    private final BrandService brandService;
    private final ModelService modelService;
    private final CategoryService categoryService;
    private final OrderService orderService;


    @Test
    void getAll() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT);
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("layout/order/orders"))
                .andExpect(model().attributeExists("ordersPage"))
                .andReturn();
        var ordersPage = ((Page<OrderResponseDto>) result.getModelAndView().getModel().get("ordersPage")).getContent();
        assertThat(ordersPage).hasSize(3);
    }

    @Test
    void createView() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/order-create");
        mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("carId", "4")
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("layout/order/order-create"))
                .andExpect(model().attributeExists("order"));
    }

    @Test
    void create() throws Exception {
        var orderRequestDto = TestDtoBuilder.createOrderRequestDto(2L, 6L);

        var uriBuilder = fromUriString(ENDPOINT);
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("userId", orderRequestDto.getUserId().toString())
                                .param("carId", orderRequestDto.getCarId().toString())
                                .param("passport", orderRequestDto.getPassport())
                                .param("startRentalDate", orderRequestDto.getStartRentalDate().toString())
                                .param("endRentalDate", orderRequestDto.getEndRentalDate().toString())
                )
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void getById() throws Exception {
        var orderRequestDto = TestDtoBuilder.createOrderRequestDto(1L, 6L);
        var expected = orderService.create(orderRequestDto).get();

        assertExpectedIsSaved(expected, expected.getId());
    }

    @Test
    void update() throws Exception {
        var orderRequestDto = TestDtoBuilder.createOrderRequestDto(1L, 6L);
        var savedOrder = orderService.create(orderRequestDto).get();

        assertExpectedIsSaved(savedOrder, savedOrder.getId());

        var orderToUpdate = TestDtoBuilder.createOrderUpdateRequestDTO(4L);
        var uriBuilder = fromUriString(ENDPOINT + "/" + savedOrder.getId() + "/update");
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("carId", orderToUpdate.getCarId().toString())
                                .param("startRentalDate", orderToUpdate.getStartRentalDate().toString())
                                .param("endRentalDate", orderToUpdate.getEndRentalDate().toString())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ENDPOINT + "/" + savedOrder.getId()));
    }

    @Test
    void changeStatus() throws Exception {
        var orderRequestDto = TestDtoBuilder.createOrderRequestDto(1L, 6L);
        var savedOrder = orderService.create(orderRequestDto).get();

        assertExpectedIsSaved(savedOrder, savedOrder.getId());

        var uriBuilder = fromUriString(ENDPOINT + "/" + savedOrder.getId() + "/change-status");
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("status", OrderStatus.APPROVED.name())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ENDPOINT + "/" + savedOrder.getId()));
    }

    @Test
    void findAllByStatus() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/by-status");
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .param("userFirstName", "")
                                .param("userLastName", "")
                                .param("carNumber", "")
                                .param("orderStatus", OrderStatus.DECLINED.name())
                                .param("sum", "")
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("ordersPage"))
                .andReturn();

        var ordersPage = ((Page<OrderResponseDto>) result.getModelAndView().getModel().get("ordersPage")).getContent();
        assertThat(ordersPage).isEmpty();
    }

    @Test
    void delete() throws Exception {
        var orderRequestDto = TestDtoBuilder.createOrderRequestDto(1L, 4L);
        var savedOrder = orderService.create(orderRequestDto).get();

        assertExpectedIsSaved(savedOrder, savedOrder.getId());

        var uriBuilder = fromUriString(ENDPOINT + "/" + savedOrder.getId() + "/delete");
        mockMvc.perform(
                        post(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ENDPOINT));

        var result = assertThrowsExactly(NotFoundException.class, () -> orderService.getById(savedOrder.getId()));

        assertEquals("404 NOT_FOUND \"Order with id 5 does not exist.\"", result.getMessage());
    }

    @Test
    void getAllByUserId() throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/user/1");
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("ordersPage"))
                .andReturn();

        var ordersPage = ((Page<OrderResponseDto>) result.getModelAndView().getModel().get("ordersPage")).getContent();
        assertThat(ordersPage).hasSize(2);
    }

    private void assertExpectedIsSaved(OrderResponseDto expected, Long id) throws Exception {
        var uriBuilder = fromUriString(ENDPOINT + "/" + id);
        var result = mockMvc.perform(
                        get(uriBuilder.build().encode().toUri())
                                .headers(httpHeaders)
                                .accept(MediaType.TEXT_HTML)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("order"))
                .andReturn();

        var order = (OrderResponseDto) result.getModelAndView().getModel().get("order");

        assertThat(order.getId()).isEqualTo(id);
        assertThat(order.getDate()).isEqualTo(expected.getDate());
        assertThat(order.getSum()).isEqualTo(expected.getSum());
        assertThat(order.getCar()).isEqualTo(expected.getCar());
        assertThat(order.getUser()).isEqualTo(expected.getUser());
    }

}