package by.sergey.carrentapp.mapper.accident;

import by.sergey.carrentapp.domain.dto.accident.AccidentUpdateRequestDto;
import by.sergey.carrentapp.domain.entity.Accident;
import by.sergey.carrentapp.domain.entity.Order;
import by.sergey.carrentapp.mapper.UpdateMapper;
import by.sergey.carrentapp.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccidentUpdateMapper implements UpdateMapper<AccidentUpdateRequestDto, Accident> {

    private final OrderRepository orderRepository;

    @Override
    public Accident mapToEntity(AccidentUpdateRequestDto dto, Accident entity) {
        merge(dto, entity);
        return entity;
    }

    @Override
    public void merge(AccidentUpdateRequestDto dto, Accident entity) {
        entity.setDescription(dto.description());
        entity.setDamage(dto.damage());
        var order = getOrder(entity.getOrder().getId());
        order.ifPresent(or -> or.setAccident(entity));
    }

    private Optional<Order> getOrder(Long orderId) {
        return Optional.ofNullable(orderId)
                .flatMap(orderRepository::findById);
    }
}
