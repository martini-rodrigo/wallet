package br.com.pernambucanas.banking.api.exception;

public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = -7145397009155641900L;

	public NotFoundException(String message) {
		super(message);
	}

}
