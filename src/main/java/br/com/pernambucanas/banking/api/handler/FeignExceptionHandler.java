package br.com.pernambucanas.banking.api.handler;

import br.com.pernambucanas.banking.api.exception.BusinessException;
import br.com.pernambucanas.banking.api.exception.NotFoundException;
import br.com.pernambucanas.banking.api.exception.UnauthorizedException;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class FeignExceptionHandler implements ErrorDecoder {

	@Override
	public Exception decode(String methodKey, Response response) {
		FeignException exception = FeignException.errorStatus(methodKey, response);
		return switch (HttpStatus.valueOf(response.status())) {
			case UNAUTHORIZED -> new UnauthorizedException(exception.getMessage());
			case BAD_REQUEST -> new BusinessException(exception.getMessage());
			case NOT_FOUND -> new NotFoundException(exception.getMessage());
			default -> exception;
		};
	}
}
