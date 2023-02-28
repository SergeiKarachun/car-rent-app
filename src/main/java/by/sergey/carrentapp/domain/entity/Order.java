package by.sergey.carrentapp.domain.entity;

import by.sergey.carrentapp.domain.model.OrderStatus;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"user", "car", "rentalTime", "accidents"})
@EqualsAndHashCode(exclude = {"user", "car", "rentalTime", "accidents"})
@Builder
@Entity
@Table(name = "orders")
public class Order extends AuditingEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Column(nullable = false)
    private String passport;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal sum;

    @OneToOne(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private RentalTime rentalTime;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Accident> accidents = new HashSet<>();

    public void setAccident(Accident accident){
        accidents.add(accident);
        accident.setOrder(this);
    }

    public void setUser(User user){
        this.user = user;
        user.getOrders().add(this);
    }

    public void setCar(Car car){
        this.car = car;
        car.getOrders().add(this);
    }
}
