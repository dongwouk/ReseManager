package zerobase.reservation.domain;

import lombok.*;
import zerobase.reservation.type.MemberStatus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;

    private String password;
    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    @OneToMany(mappedBy = "member")
    private List<Store> store = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Reservation> reservation = new ArrayList<>();
}
