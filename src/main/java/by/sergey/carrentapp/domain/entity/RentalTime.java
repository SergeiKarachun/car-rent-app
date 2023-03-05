package by.sergey.carrentapp.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"order"})
@EqualsAndHashCode(exclude = {"order"})
@Builder
@Entity
@Table(name = "rental_time")
public class RentalTime extends AuditingEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(nullable = false)
    private LocalDateTime startRentalDate;

    @Column(nullable = false)
    private LocalDateTime endRentalDate;

    public void setOrder(Order order){
        order.setRentalTime(this);
        this.order = order;
    }

}


