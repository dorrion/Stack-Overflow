package com.codestates.auth.filter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.codestates.auth.JwtTokenizer;
import com.codestates.auth.LoginDto;
import com.codestates.user.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authenticationManager;
	private final JwtTokenizer jwtTokenizer;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenizer jwtTokenizer) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenizer = jwtTokenizer;
	}

	// 내부에서 인증을 시도하는 로직 구현
	@SneakyThrows
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		ObjectMapper objectMapper = new ObjectMapper();

		LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);

		UsernamePasswordAuthenticationToken authenticationToken =
			new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

		return authenticationManager.authenticate(authenticationToken);
	}

	// 인증에 성공할 경우
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			FilterChain chain, Authentication authResult) throws ServletException, IOException {
		User user = (User) authResult.getPrincipal();

		String accessToken = delegateAccessToken(user);
		String refreshToken = delegateRefreshToken(user);

		response.setHeader("Authorization", "Bearer" + accessToken);
		response.setHeader("Refresh", refreshToken);

		this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
	}

	private String delegateAccessToken(User user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("username", user.getEmail());
		claims.put("roles", user.getRoles());

		String subject = user.getEmail();
		Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
		String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
		String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

		return accessToken;
	}

	private String delegateRefreshToken(User user) {
		String subject = user.getEmail();
		Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
		String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

		String refreshToken = jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);

		return refreshToken;
	}
}
