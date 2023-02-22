package by.sergey.carrentapp.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"order", "car"})
@EqualsAndHashCode(exclude = {"order", "car"})
@Builder
@Entity
@Table(name = "rental_time")
public class RentalTime implements BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Column(nullable = false)
    private LocalDateTime startRentalDate;

    @Column(nullable = false)
    private LocalDateTime endRentalDate;

    public void setOrder(Order order){
        order.setRentalTime(this);
        this.order = order;
    }

    public void setCar(Car car){
        this.car = car;
        this.car.getRentalTimeList().add(this);

    }

}


