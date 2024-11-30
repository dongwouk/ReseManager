package zerobase.reservation.dto;

import lombok.*;
import zerobase.reservation.domain.Member;
import zerobase.reservation.type.MemberStatus;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    private String username;
    private String password;
    private MemberStatus memberStatus;

    public static Member toMemberEntity(MemberDto memberDto) {
        return new Member().builder()
                .username(memberDto.getUsername())
                .password(memberDto.getPassword())
                .memberStatus(memberDto.getMemberStatus())
                .build();
    }
}
