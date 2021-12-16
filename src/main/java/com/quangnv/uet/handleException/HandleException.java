package com.quangnv.uet.handleException;

import java.nio.file.AccessDeniedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.quangnv.uet.exception.ResoureException;

@RestControllerAdvice
public class HandleException extends ResponseEntityExceptionHandler {

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ResoureException> handlerAccessDeniedException(final AccessDeniedException ex,
			final HttpServletRequest request, final HttpServletResponse response) {

		ResoureException authzErrorResponse = new ResoureException(
				"Sorry, You're not authorized to access this resource");

		return new ResponseEntity<>(authzErrorResponse, HttpStatus.FORBIDDEN);
	}
}
