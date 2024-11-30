package zerobase.reservation.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import zerobase.reservation.type.ReservationStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class Reservation {
    @Id
    @GeneratedValue
    @Column(name = "reservation_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToOne
    @JoinColumn(name = "review_id")
    @JsonManagedReference
    private Review review;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;
    private LocalDateTime time;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
