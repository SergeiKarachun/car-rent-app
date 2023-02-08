package by.sergey.carrentapp.domain.entity;

import lombok.*;
import org.springframework.data.util.Lazy;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "order")
@EqualsAndHashCode(exclude = "order")
@Builder
@Entity
@Table(name = "carrentaltime")
public class CarRentalTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(nullable = false)
    private LocalDate startRentalDate;

    @Column(nullable = false)
    private LocalDate endRentalDate;


}


