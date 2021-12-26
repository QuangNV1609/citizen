package com.quangnv.uet.handleException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.quangnv.uet.dto.ErrorMessage;
import com.quangnv.uet.exception.ResourenotFoundException;

@RestControllerAdvice
public class HandleException extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = UsernameNotFoundException.class)
	public ResponseEntity<ErrorMessage> handlerUsernameNotFoundException(
			final UsernameNotFoundException usernameNotFoundException, final HttpServletRequest request,
			final HttpServletResponse response) {

		ErrorMessage errorMessage = new ErrorMessage(usernameNotFoundException.getMessage(), 304);

		return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.OK);
	}

	@ExceptionHandler(value = ResourenotFoundException.class)
	public ResponseEntity<ErrorMessage> handlerResourenotFoundException(
			final ResourenotFoundException resourenotFoundException, final HttpServletRequest request,
			final HttpServletResponse response) {

		ErrorMessage errorMessage = new ErrorMessage(resourenotFoundException.getMessage(), 404);

		return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.NOT_FOUND);
	}
}
