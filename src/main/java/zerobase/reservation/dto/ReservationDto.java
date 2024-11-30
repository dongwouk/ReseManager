package zerobase.reservation.dto;

import lombok.*;
import zerobase.reservation.domain.Member;
import zerobase.reservation.domain.Reservation;
import zerobase.reservation.domain.Review;
import zerobase.reservation.domain.Store;
import zerobase.reservation.type.ReservationStatus;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDto {
    private Long memberId;
    private Long storeId;
    private Long reservationId;
    private LocalDateTime time;
    private ReservationStatus reservationStatus;


    public static Reservation toReservationEntity(
            Member member,
            Store store,
            Review review,
            ReservationDto reservationDto
    ) {
        return new Reservation().builder()
                .member(member)
                .store(store)
                .review(review)
                .reservationStatus(reservationDto.getReservationStatus())
                .time(reservationDto.getTime())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
