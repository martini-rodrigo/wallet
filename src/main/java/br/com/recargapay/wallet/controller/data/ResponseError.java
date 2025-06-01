package br.com.recargapay.wallet.controller.data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import lombok.Getter;

@Getter
public class ResponseError {

	private LocalDateTime timestamp = LocalDateTime.now(ZoneOffset.UTC);
	private List<String> errors;

	public ResponseError(List<String> errors) {
		this.errors = errors;
	}

}
