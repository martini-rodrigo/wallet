package br.com.pernambucanas.banking.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum MaritalStatusType {

	SINGLE(1),
	MARRIED(2),
	DIVORCED(3),
	WIDOWED(4),
	SEPARATED(5),
	JUDICIALLY_SEPARATED(6),
	COMMON_LAW(7),
	OTHERS(8);

	@Getter
	Integer id;
}
