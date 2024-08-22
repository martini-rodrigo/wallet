package br.com.pernambucanas.banking.api.exception.handler;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.pernambucanas.banking.api.controller.data.ResponseError;
import br.com.pernambucanas.banking.api.exception.BusinessException;
import br.com.pernambucanas.banking.api.exception.NotFoundException;
import br.com.pernambucanas.banking.api.exception.UnauthorizedException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<Object> handleBusinessException(BusinessException ex, WebRequest request) {
		List<String> errors = List.of(Optional.ofNullable(ex.getCause()).filter(Objects::nonNull).map(Object::toString)
				.orElse(ex.getMessage()));
		return new ResponseEntity<Object>(new ResponseError(errors), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
		List<String> errors = List.of(ex.getMessage());
		return new ResponseEntity<Object>(new ResponseError(errors), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<Object> handleUnauthorizedException(Exception ex) {
		List<String> errors = List.of(Optional.ofNullable(ex.getCause()).filter(Objects::nonNull).map(Object::toString)
				.orElse(ex.getMessage()));
		return new ResponseEntity<Object>(new ResponseError(errors), HttpStatus.UNAUTHORIZED);
	}


	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleInternalServerErrorException(Exception ex) {
		List<String> errors = List.of(Optional.ofNullable(ex.getCause()).filter(Objects::nonNull).map(Object::toString)
				.orElse(ex.getMessage()));
		return new ResponseEntity<Object>(new ResponseError(errors), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
