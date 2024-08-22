package br.com.pernambucanas.banking.api.dto;

import br.com.pernambucanas.banking.api.enums.MaritalStatusType;
import br.com.pernambucanas.banking.api.validator.CustomerConstraint;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@CustomerConstraint
public class CustomerInputDTO {

    @JsonIgnore
    private Long companyId;
    private Integer managerId;
    private String name;
    private String document;
    private String sex;
    private LocalDate birthDate;
    private MaritalStatusType maritalStatus;
    private String email;
    private CostumerAdDressInputDTO address;
    private CostumerContactInputDTO contact;


    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CostumerAdDressInputDTO {

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

}
