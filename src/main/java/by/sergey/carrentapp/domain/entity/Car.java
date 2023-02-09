package by.sergey.carrentapp.domain.entity;

import by.sergey.carrentapp.domain.model.Color;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"brand", "model", "category", "orders"})
@EqualsAndHashCode(of = "vin")
@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    private Model model;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Enumerated(EnumType.STRING)
    private Color color;

    private Integer year;

    private String carNumber;

    @Column(unique = true, nullable = false)
    private String vin;

    @Builder.Default
    private Boolean repaired = Boolean.TRUE;

    private String image;


    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Builder.Default
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

}
