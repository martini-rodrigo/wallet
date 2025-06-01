package br.com.recargapay.wallet.exception;

public class BusinessException extends RuntimeException {

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, Object... param) {
		this(String.format(message.replace("{0}", "%s"), param));
	}
}
