package by.sergey.carrentapp.domain.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"user", "driverLicense"})
@EqualsAndHashCode(exclude = {"user", "driverLicense"})
@Entity
@Table(name = "userdetails")
public class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Embedded
    private UserContact userContact;

    @Column(nullable = false)
    private LocalDate birthday;

    @Builder.Default
    @Column(updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDate registrationDate = LocalDate.now();

    @Builder.Default
    @OneToMany(mappedBy = "userDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DriverLicense> driverLicense = new HashSet<>();


}
