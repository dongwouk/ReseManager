package zerobase.reservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.reservation.domain.Member;
import zerobase.reservation.dto.MemberDto;
import zerobase.reservation.exception.ReservationException;
import zerobase.reservation.repository.MemberRepository;

import java.util.Optional;

import static zerobase.reservation.dto.MemberDto.toMemberEntity;
import static zerobase.reservation.type.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public Member join(MemberDto memberDto) {
        if(memberRepository.existsByUsername(memberDto.getUsername())){
            throw new ReservationException(ALREADY_EXIST_MEMBERNAME);
        }
        // Check if username contains only English letters (both uppercase and lowercase)
        if (!memberDto.getUsername().matches("^[a-zA-Z]+$")) {
            throw new ReservationException(USERNAME_NOT_INVALID_TYPE);
        }

        // Check if password contains only English letters (both uppercase and lowercase)
        if (!memberDto.getPassword().matches("^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\",.<>\\/?]+$")) {
            throw new ReservationException(PASSWORD_NOT_INVALID_TYPE);
        }

        return memberRepository.save(toMemberEntity(memberDto));
    }


    public Optional<MemberDto> login(MemberDto memberDto) {
        Optional<Member> byUsername = memberRepository.findByUsername(memberDto.getUsername());
        if (byUsername.isPresent()) {
            Member member = byUsername.get();
            if (member.getPassword().equals(memberDto.getPassword())) {
                MemberDto dto = MemberDto.builder()
                        .username(member.getUsername())
                        // 패스워드는 반환하지 않는 것이 좋습니다. (보안 이슈)
                        .memberStatus(member.getMemberStatus())
                        .build();
                return Optional.of(dto);
            }
        }
        return Optional.empty();
    }

}
