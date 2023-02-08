package by.sergey.carrentapp.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "order")
@EqualsAndHashCode(exclude = "order")
@Entity
@Table
public class Accident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private LocalDate accidentDate;

    private String description;

    @Column(precision = 10, scale = 2)
    private BigDecimal damage;

}
