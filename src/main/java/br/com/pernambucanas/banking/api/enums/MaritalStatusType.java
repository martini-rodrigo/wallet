package br.com.pernambucanas.banking.api.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MaritalStatusType {

	SINGLE,
	MARRIED,
	DIVORCED,
	WIDOWED,
	SEPARATED,
	JUDICIALLY_SEPARATED,
	COMMON_LAW,
	OTHERS;
}
