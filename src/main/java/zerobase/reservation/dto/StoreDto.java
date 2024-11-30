package zerobase.reservation.dto;

import lombok.*;
import zerobase.reservation.domain.Member;
import zerobase.reservation.domain.Store;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreDto {
    private Long storeId;
    private Long memberId;
    private String name;
    private String location;
    private String description;
    private double x;
    private double y;
    
    public static Store toStoreEntity(Member member, StoreDto storeDto) {

        return new Store().builder()
                .member(member)
                .name(storeDto.getName())
                .location(storeDto.getLocation())
                .description(storeDto.getDescription())
                .x(storeDto.getX())
                .y(storeDto.getY())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
