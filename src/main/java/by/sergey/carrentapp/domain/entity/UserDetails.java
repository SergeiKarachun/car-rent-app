package by.sergey.carrentapp.domain.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"user", "driverLicense"})
@EqualsAndHashCode(exclude = {"user", "driverLicense"})
@Entity(name = "user_details")
public class UserDetails extends AuditingEntity<Long>{

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
    private LocalDateTime birthday;

    @Builder.Default
    @Column(updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime registrationDate = LocalDateTime.now();

    @OneToOne(mappedBy = "userDetails", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private DriverLicense driverLicense;

    public void setUser(User user){
        user.setUserDetails(this);
        this.user = user;
    }
}
