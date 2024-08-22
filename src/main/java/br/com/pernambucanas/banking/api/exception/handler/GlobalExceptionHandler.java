package br.com.pernambucanas.banking.api.exception.handler;

import br.com.pernambucanas.banking.api.controller.data.ResponseError;
import br.com.pernambucanas.banking.api.exception.BusinessException;
import br.com.pernambucanas.banking.api.exception.NotFoundException;
import br.com.pernambucanas.banking.api.exception.UnauthorizedException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        return new ResponseEntity<Object>(new ResponseError(errors), status);
    }

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
