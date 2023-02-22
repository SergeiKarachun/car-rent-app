package by.sergey.carrentapp.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"cars", "models"})
@EqualsAndHashCode(of = "name")
@Entity
@Table(name = "brand")
public class Brand implements BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Car> cars = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Model> models = new HashSet<>();

    public void setModel(Model model){
        models.add(model);
        model.setBrand(this);
    }
}
