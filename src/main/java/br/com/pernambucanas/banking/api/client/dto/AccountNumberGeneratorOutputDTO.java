package br.com.pernambucanas.banking.api.client.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountNumberGeneratorOutputDTO {

    private Long number;
    private Integer digit;

}
