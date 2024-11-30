package zerobase.reservation.dto;

import lombok.*;
import zerobase.reservation.domain.Member;
import zerobase.reservation.domain.Reservation;
import zerobase.reservation.domain.Review;
import zerobase.reservation.domain.Store;

import java.time.LocalDateTime;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private Long memberId;
    private Long storeId;
    private Long reservationId;
    private Long reviewId;
    private String content;

    public static Review toReviewEntity(Member member, Store store, Reservation reservation, ReviewDto reviewDto) {
        return new Review().builder()
                .member(member)
                .store(store)
                .reservation(reservation)
                .content(reviewDto.getContent())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
