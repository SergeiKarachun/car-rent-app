package by.sergey.carrentapp.mapper.accident;

import by.sergey.carrentapp.domain.dto.accident.AccidentCreateRequestDto;
import by.sergey.carrentapp.domain.entity.Accident;
import by.sergey.carrentapp.domain.entity.Order;
import by.sergey.carrentapp.mapper.CreateMapper;
import by.sergey.carrentapp.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccidentCreateMapper implements CreateMapper<AccidentCreateRequestDto, Accident> {

    private final OrderRepository orderRepository;
    private Accident accident;

    @Override
    public Accident mapToEntity(AccidentCreateRequestDto requestDto) {
        var order = getOrder(requestDto.orderId());
        accident = Accident.builder()
                .description(requestDto.description())
                .damage(requestDto.damage())
                .build();

        order.ifPresent(ord -> ord.setAccident(accident));
        return accident;
    }

    private Optional<Order> getOrder(Long orderId) {
        return Optional.ofNullable(orderId)
                .flatMap(orderRepository::findById);
    }
}
