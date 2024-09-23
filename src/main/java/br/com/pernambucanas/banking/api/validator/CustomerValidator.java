package br.com.pernambucanas.banking.api.validator;

import br.com.pernambucanas.banking.api.dto.CustomerInputDTO;
import br.com.pernambucanas.banking.api.enums.AccountType;
import br.com.pernambucanas.banking.api.enums.GenderType;
import br.com.pernambucanas.banking.api.enums.MaritalStatusType;
import br.com.pernambucanas.banking.api.utils.DocumentUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;

import java.util.*;
import java.util.regex.Pattern;

public class CustomerValidator implements ConstraintValidator<CustomerConstraint, CustomerInputDTO> {

    @Override
    public boolean isValid(CustomerInputDTO inputDTO, ConstraintValidatorContext context) {
        List<String> error = new ArrayList<>();

        if (StringUtils.isBlank(inputDTO.getName())) {
            error.add("Name is required.");
        }

        if (StringUtils.isBlank(inputDTO.getDocument())) {
            error.add("Document is required.");
        } else {
            if (!Pattern.matches("[a-zA-Z0-9]+", inputDTO.getDocument())) {
                error.add("Document must not contain special characters.");
            }
            if (!DocumentUtils.isValidCPF(inputDTO.getDocument())) {
                error.add("Invalid document.");
            }
        }

        if (StringUtils.isNotBlank(inputDTO.getEmail())
                && !new EmailValidator().isValid(inputDTO.getEmail(), context)) {
            error.add("Invalid email.");
        }

        if (StringUtils.isNotBlank(inputDTO.getMaritalStatus())) {
            var isInvalidMaritalStatus = Arrays.asList(MaritalStatusType.values()).stream()
                    .noneMatch(o -> o.name().equals(inputDTO.getMaritalStatus()));
            if (isInvalidMaritalStatus) {
                error.add(String.format("Invalid marital status. Valid types: %s.", EnumSet.allOf(MaritalStatusType.class)));
            }
        }

        if (Objects.isNull(inputDTO.getBirthDate())) {
            error.add("Birth date is required.");
        }

        if (StringUtils.isBlank(inputDTO.getGender())) {
            error.add("Gender is required.");
        } else {
            var isInvalidSexType = Arrays.asList(GenderType.values()).stream()
                    .noneMatch(o -> o.name().equals(inputDTO.getGender()));
            if (isInvalidSexType) {
                error.add(String.format("Invalid gender type. Valid types: %s.", EnumSet.allOf(GenderType.class)));
            }
        }

        if (Objects.isNull(inputDTO.getManagerId())) {
            error.add("Manager id is required.");
        }

        if (Objects.nonNull(inputDTO.getAccount()) && StringUtils.isNotBlank(inputDTO.getAccount().getType())) {
            var isInvalidAccountType = Arrays.asList(AccountType.values()).stream()
                    .noneMatch(o -> o.name().equals(inputDTO.getAccount().getType()));
            if (isInvalidAccountType) {
                error.add(String.format("Invalid account type. Valid types: %s.", EnumSet.allOf(AccountType.class)));
            }
        }

        if (Objects.isNull(inputDTO.getAddress())) {
            error.add("Address is required.");
        } else {
            var address = inputDTO.getAddress();
            if (StringUtils.isBlank(address.getAddress())) {
                error.add("Address is required.");
            }
            if (StringUtils.isBlank(address.getNumber())) {
                error.add("Address number is required.");
            }
            if (StringUtils.isBlank(address.getCity())) {
                error.add("Address city is required.");
            }
            if (StringUtils.isBlank(address.getPostalCode())) {
                error.add("Address postal code is required.");
            }
            if (StringUtils.isBlank(address.getState())) {
                error.add("Address postal code is required.");
            }
        }
        if (!error.isEmpty()) {
            context.disableDefaultConstraintViolation();
            error.forEach(o -> context.buildConstraintViolationWithTemplate(o).addConstraintViolation());
            return false;
        }
        return true;
    }
}
