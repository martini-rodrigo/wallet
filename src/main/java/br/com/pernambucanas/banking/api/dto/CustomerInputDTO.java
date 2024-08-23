package br.com.pernambucanas.banking.api.dto;

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
    private String name;
    private String document;
    private String gender;
    private LocalDate birthDate;
    private String maritalStatus;
    private String email;
    private Integer managerId;
    private Integer classificationCode;
    private CostumerAddressInputDTO address;
    private CostumerContactInputDTO contact;
    private CostumerAccountInputDTO account;


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

        private Long id;
        private String agencyId;
        private Long number;
        private String type;
        private Long groupId;
        private Long packageRate;
    }

}
