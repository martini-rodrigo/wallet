package br.com.pernambucanas.banking.api.exception;

public class UnauthorizedException extends RuntimeException {

	private static final long serialVersionUID = 7299964576895085565L;

	public UnauthorizedException(String message) {
		super(message);
	}

	public UnauthorizedException(Throwable cause) {
		super(cause);
	}
}
