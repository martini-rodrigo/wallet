package br.com.pernambucanas.banking.api.dto;

import br.com.pernambucanas.banking.api.validator.CustomerConstraint;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@CustomerConstraint
public class CustomerInputDTO {

    @Hidden
    private Long companyId;
    @Hidden
    private CostumerAccountInputDTO account;

    private String name;
    private String document;
    private String gender;
    private String rg;
    private LocalDate birthDate;
    private String maritalStatus;
    private String email;
    private CostumerAddressInputDTO address;
    private CostumerContactInputDTO contact;


    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CostumerAddressInputDTO {

        private String address;
        private String number;
        private String city;
        private String state;
        private String neighborhood;
        private String postalCode;
        private String additionalAddress;

    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CostumerContactInputDTO {

        private String areaCode;
        private String phone;

    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CostumerAccountInputDTO {

        private Long number;
        private Integer digit;
    }

}
