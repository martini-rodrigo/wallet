package br.com.pernambucanas.banking.api.exception.handler;

import br.com.pernambucanas.banking.api.controller.data.ResponseError;
import br.com.pernambucanas.banking.api.exception.BusinessException;
import br.com.pernambucanas.banking.api.exception.NotFoundException;
import br.com.pernambucanas.banking.api.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class GlobalExceptionHandlerTest {
	private GlobalExceptionHandler globalExceptionHandler;

	@BeforeEach
	public void setUp() {
		globalExceptionHandler = new GlobalExceptionHandler();
	}

	@Test
	public void testHandleBusinessException() {
		BusinessException ex = new BusinessException("Business error");
		WebRequest request = mock(WebRequest.class);

		ResponseEntity<Object> response = globalExceptionHandler.handleBusinessException(ex, request);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		ResponseError responseBody = (ResponseError) response.getBody();
		assert responseBody != null;
		assertEquals(List.of("Business error"), responseBody.getErrors());
		assertNotNull(responseBody.getTimestamp());
	}

	@Test
	public void testHandleNotFoundException() {
		NotFoundException ex = new NotFoundException("Not found error");

		ResponseEntity<Object> response = globalExceptionHandler.handleNotFoundException(ex);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		ResponseError responseBody = (ResponseError) response.getBody();
		assert responseBody != null;
		assertEquals(List.of("Not found error"), responseBody.getErrors());
		assertNotNull(responseBody.getTimestamp());
	}

	@Test
	public void testHandleInternalServerErrorException() {
		Exception ex = new Exception("Internal server error");

		ResponseEntity<Object> response = globalExceptionHandler.handleInternalServerErrorException(ex);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		ResponseError responseBody = (ResponseError) response.getBody();
		assert responseBody != null;
		assertEquals(List.of("Internal server error"), responseBody.getErrors());
		assertNotNull(responseBody.getTimestamp());
	}
}
