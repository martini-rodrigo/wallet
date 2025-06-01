package br.com.recargapay.wallet.exception;

import br.com.recargapay.wallet.controller.data.ResponseError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String INVALID_OPERATION = "An error occurred while processing your request. Please try again later.";

    // validating input objects in REST APIs
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        return new ResponseEntity<>(new ResponseError(errors), status);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<Object> handleInsufficientFundsException(InsufficientFundsException ex) {
        return new ResponseEntity<>(new ResponseError(List.of(ex.getMessage())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateOperationException.class)
    public ResponseEntity<Object> handleDuplicateOperationException(DuplicateOperationException ex) {
        return new ResponseEntity<>(new ResponseError(List.of(ex.getMessage())), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Object> handleMissingHeader(MissingRequestHeaderException ex) {
        if (ex.getHeaderName().equalsIgnoreCase("Idempotency-Key")) {
            return new ResponseEntity<>(new ResponseError(List.of("The 'Idempotency-Key' header is required.")),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseError(List.of("Missing required header: " + ex.getHeaderName())),
                HttpStatus.BAD_REQUEST);
    }



    // prevent business errors from being exposed
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException ex) {
        log.error("An error occurred:", ex);
        return new ResponseEntity<>(new ResponseError(List.of(INVALID_OPERATION)), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
        log.error("An error occurred:", ex);
        return new ResponseEntity<>(new ResponseError(List.of(INVALID_OPERATION)), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleInternalServerErrorException(Exception ex) {
        log.error("An error occurred:", ex);
        return new ResponseEntity<>(new ResponseError(List.of(INVALID_OPERATION)), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
