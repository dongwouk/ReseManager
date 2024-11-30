package zerobase.reservation.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberOwnedStoreReservationsDto {
    private String storeName;
    private String reservationMemberName;
    private LocalDateTime createdAt;
    private LocalDateTime reservationAt;
}
