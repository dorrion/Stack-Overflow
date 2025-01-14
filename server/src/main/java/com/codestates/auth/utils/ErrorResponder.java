package com.codestates.auth.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.codestates.response.ErrorResponse;
import com.google.gson.Gson;

// 에러 출력 스트림 생성
public class ErrorResponder {
	public static void sendErrorResponse(HttpServletResponse response, HttpStatus status) throws IOException {
		Gson gson = new Gson();
		ErrorResponse errorResponse = ErrorResponse.of(status);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(status.value());
		response.getWriter().write(gson.toJson(errorResponse, ErrorResponse.class));
	}
}
