package com.codestates.auth;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;

@Component
public class JwtTokenizer {
	@Getter
	@Value("${jwt.secret-key}")
	private String secretKey;

	@Getter
	@Value(("${jwt.access-token-expiration-minutes}"))
	private int accessTokenExpirationMinutes;

	@Getter
	@Value("${jwt.refresh-token-expiration-minutes}")
	private int refreshTokenExpirationMinutes;

	public String encodeBase64SecretKey(String secretKey) {
		return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
	}

	// 인증된 사용자에게 JWT 최초 발급
	public String generateAccessToken(Map<String, Object> claims, String subject,
			Date expiration, String base64EncodedSecretKey) {
		Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

		return Jwts.builder()
			.setClaims(claims).setSubject(subject).setIssuedAt(Calendar.getInstance().getTime())
			.setExpiration(expiration).signWith(key).compact();
	}

	//  Refresh Token 생성
	public String generateRefreshToken(String subject, Date expiration, String base64EncodedSecretKey) {
		Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

		return Jwts.builder()
			.setSubject(subject).setIssuedAt(Calendar.getInstance().getTime())
			.setExpiration(expiration).signWith(key).compact();
	}

	public Jws<Claims> getClaims(String jws, String base64EncodedSecretKey) {
		Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

		Jws<Claims> claims = Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(jws);
		return claims;
	}

	// JWT 검증
	public void verifySignature(String jws, String base64EncodedSecretKey) {
		Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

		Jwts.parserBuilder()
			.setSigningKey(key).build()
			.parseClaimsJws(jws);
	}

	// JWT 만료 일시 지정
	public Date getTokenExpiration(int expirationMinutes) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, expirationMinutes);
		Date expiration = calendar.getTime();

		return expiration;
	}

	// JWT 의 서명에 사용할 Secret Key 를 생성
	private Key getKeyFromBase64EncodedKey(String base64EncodedSecretKey) {
		byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
		Key key = Keys.hmacShaKeyFor(keyBytes);

		return key;
	}
}
