package br.com.recargapay.wallet.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Object... param) {
        this(String.format(message.replace("{0}", "%s"), param));
    }

}
