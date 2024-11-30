package zerobase.reservation.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import zerobase.reservation.domain.Member;
import zerobase.reservation.dto.MemberDto;
import zerobase.reservation.service.MemberService;

import javax.transaction.Transactional;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@Transactional
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/member")
    @ApiOperation(value="member생성", notes="member생성")
    public ResponseEntity<MemberDto> create(@RequestBody MemberDto memberDto) {
        Member member = memberService.join(memberDto);
        memberDto = new MemberDto().builder()
                .username(member.getUsername())
                .build();
        return ResponseEntity.ok(memberDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberDto memberDto) {
        Optional<MemberDto> loginResult = memberService.login(memberDto);
        if (loginResult.isPresent()) {
            return ResponseEntity.ok(loginResult.get());
        } else {
            // 로그인 실패 시 명확한 에러 메시지와 함께 HTTP 상태 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("로그인 정보가 올바르지 않습니다.");
        }
    }

}
