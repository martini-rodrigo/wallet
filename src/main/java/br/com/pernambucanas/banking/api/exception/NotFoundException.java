package br.com.pernambucanas.banking.api.exception;

import java.text.MessageFormat;

public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = -7145397009155641900L;

	public NotFoundException() {
		super();
	}

	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(Throwable cause) {
		super(cause);
	}

	public NotFoundException(String message, Object... param) {
		this(MessageFormat.format(message, param));
	}
}
