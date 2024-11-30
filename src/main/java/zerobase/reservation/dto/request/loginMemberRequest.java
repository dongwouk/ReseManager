package zerobase.reservation.dto.request;

import lombok.Getter;
import lombok.Setter;
import zerobase.reservation.type.MemberStatus;

@Getter @Setter
public class loginMemberRequest {
    private String userId;
    private String password;
    private MemberStatus memberStatus;
}
