package dev.changuii.project.security.service;

import dev.changuii.project.enums.ErrorCode;
import dev.changuii.project.exception.CustomException;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    Logger log = LoggerFactory.getLogger(JwtProvider.class);

    //TODO: 환경변수로 바꿔야함
    private String secretKey = "218552aaff8e56c91489540619ab3cb62fa2ae044a1158b9562464a606f9e123";

    private final long accessTokenValidMillisecond = 1000L * 10 * 60 * 60; // 10분
    private final long refreshTokenValidMillisecond = 1000L * 60 * 60 * 60; // 1시간

    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT access token 생성
    public String createAccessToken(String nickname) {
        Claims claims = Jwts.claims().setSubject(nickname);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)      // 데이터
                .setIssuedAt(now)       // 토큰 발생일자
                .setExpiration(new Date(now.getTime() + accessTokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)    // 암호화 알고리즘
                .compact();
    }

    // JWT refresh token 생성
    public String createRefreshToken(String nickname) {
        Claims claims = Jwts.claims().setSubject(nickname);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)      // 데이터
                .setIssuedAt(now)       // 토큰 발생일자
                .setExpiration(new Date(now.getTime() + refreshTokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)    // 암호화 알고리즘
                .compact();
    }

    // Jwt 토큰으로 인증 정보를 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    //
    /**
     * Jwt 토큰에서 회원 구별 정보 추출
     * */
    public String getEmail(String token) {
//        return Jwts.parser().setSigningKey(securityKey).parseClaimsJws(token).getBody().getSubject();
        return getClaims(token).getBody().getSubject();
    }


    // Jwt 토큰 추출
    /**
     * Jwt 토큰 추출
     * */
    public String getToken(HttpServletRequest request){
        String token = resolveToken(request);
        if (validToken(token)) {
            return getEmail(token);
        } else {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    public boolean validToken(String token){
        try {
            log.info("토큰 유효성 검사");
            Jws<Claims> claims = getClaims(token);
            log.info("토큰 유효성 검사 성공");
            return !claims.getBody().getExpiration().before(new Date());
        }catch (NullPointerException ex)
        {
            log.info("토큰이 없음");
            throw ex;
        } catch (Exception ex){
            log.info("토큰 유효성 실패");
            throw ex;
        }
    }


    // Request의 Header에서 token 파싱 : "X-AUTH-TOKEN: jwt토큰"
    public String resolveToken(HttpServletRequest req) {
        return req.getHeader("X-AUTH-TOKEN");
    }

    // Jwt 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
//        Jws<Claims> claims = Jwts.parser().setSigningKey(securityKey).parseClaimsJws(jwtToken);
        Jws<Claims> claims = getClaims(jwtToken);
        return !claims.getBody().getExpiration().before(new Date());
    }

    private Jws<Claims> getClaims(String jwt) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt);
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
            throw ex;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token!!!!");
            throw ex;
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
            throw ex;
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
            throw ex;
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is inappropriate.");
            throw ex;
        }
    }


}
