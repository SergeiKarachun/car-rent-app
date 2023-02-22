package by.sergey.carrentapp.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "userDetails")
@EqualsAndHashCode(of = "number")
@Entity
@Table(name = "driver_license")
public class DriverLicense implements BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_details_id")
    private UserDetails userDetails;

    @Column(nullable = false, unique = true)
    private String number;

    @Column(nullable = false)
    private LocalDateTime issueDate;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    public void setUserDetails(UserDetails userDetails){
        userDetails.setDriverLicense(this);
        this.userDetails = userDetails;
    }
}
