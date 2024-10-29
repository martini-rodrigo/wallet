package br.com.pernambucanas.banking.api.exception;

import java.text.MessageFormat;

public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = -5569638903058178199L;

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, Object... param) {
		this(MessageFormat.format(message, param));
	}
}
