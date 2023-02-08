package by.sergey.carrentapp.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "userDetails")
@EqualsAndHashCode(of = "number")
@Entity
@Table
public class DriverLicense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_details_id")
    private UserDetails userDetails;

    @Column(nullable = false, unique = true)
    private String number;

    @Column(nullable = false)
    private LocalDate issueDate;

    @Column(nullable = false)
    private LocalDate expirationDate;
}
