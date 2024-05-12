package roomescape.service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.dto.request.LoginRequest;
import roomescape.infrastructure.CookieManager;
import roomescape.infrastructure.auth.Token;
import roomescape.infrastructure.auth.TokenManager;

@Service
public class LoginService {
    private final CookieManager cookieManager;
    private final TokenManager tokenManager;
    private final MemberService memberService;

    public LoginService(
            final CookieManager cookieManager,
            final TokenManager tokenManager,
            final MemberService memberService) {
        this.cookieManager = cookieManager;
        this.tokenManager = tokenManager;
        this.memberService = memberService;
    }

    public Cookie login(LoginRequest loginRequest) {
        Member member = memberService.findMemberByEmailAndPassword(loginRequest.email(), loginRequest.password());
        validateLogin(loginRequest, member);
        Token token = tokenManager.generate(member);
        return cookieManager.generate(token);
    }

    public Member check(Cookie[] cookies) {
        Token token = cookieManager.getToken(cookies);
        Long memberId = tokenManager.getMemberId(token);
        return memberService.findMemberById(memberId);
    }

    public Cookie logOut(Cookie[] cookies) {
        return cookieManager.makeResetCookie(cookies);
    }

    private void validateLogin(LoginRequest loginRequest, Member member) {
        if (loginRequest.password().equals(member.getPassword())) {
            return;
        }
        throw new IllegalArgumentException("[ERROR] 아이디 혹은 비밀번호가 일치 하지 않습니다.");
    }
}
