package com.starlab.withreact.todo.security;

import com.starlab.withreact.todo.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * 사용자 정보를 받아 JWT를 생성한다.
 */
@Slf4j
@Service
public class TokenProvider {

    private static final String SECRET_KEY = "NMA8JPctFuna59f5zxcasfqqgadasdasdasqweqwqwqwdgzxvzxdasd";

    public String create(UserEntity userEntity) {
        // 기한은 지금부터 1일로 지정
        Date expiryDate = Date.from(
                Instant.now().plus(1, ChronoUnit.DAYS)
        );

        // signWith이 deprecated되었고 Key 값을 생성하고 서명을 진행해야 한다. [ 길이가 256 비트 이상 (32글자이상)이어야 한다. ]
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        // JWT Token 생성하기
        // payload -> 서명 정보
        return Jwts.builder()
                .setSubject(userEntity.getId()) // sub
                .setIssuer("demo app") // iss
                .setIssuedAt(new Date()) // iat
                .setExpiration(expiryDate) // exp
        .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact(); // 토큰 생성.
    }

    public String validateAndGetUserId(String jwt) {
        // parseClaimsJws 메서드에서 Base64로 디코딩 및 파싱한다.
        // 헤더와 페이로드를 setSigningKey로 넘어온 시크릿을 통해 서명하고 token의 서명과 비교한다. -> 위조되지 않으면 페이로드(claims)를 리턴하고, 위조한 경우 예외를 던진다.
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(jwt);

        return claims.getBody().getSubject();
    }
}
