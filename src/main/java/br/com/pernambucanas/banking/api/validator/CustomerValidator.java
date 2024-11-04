package br.com.pernambucanas.banking.api.validator;

import br.com.pernambucanas.banking.api.dto.CustomerInputDTO;
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
        error.addAll(validationPersonalData(inputDTO, context));
        error.addAll(validationPersonalDocument(inputDTO));
        error.addAll(validationAddress(inputDTO));

        if (!error.isEmpty()) {
            context.disableDefaultConstraintViolation();
            error.forEach(o -> context.buildConstraintViolationWithTemplate(o).addConstraintViolation());
            return false;
        }
        return true;
    }

    private List<String> validationPersonalData(CustomerInputDTO inputDTO, ConstraintValidatorContext context) {
        List<String> error = new ArrayList<>();

        if (StringUtils.isBlank(inputDTO.getName())) {
            error.add("Name is required.");
        }
        if (Objects.isNull(inputDTO.getBirthDate())) {
            error.add("Birth date is required.");
        }
        if (StringUtils.isNotBlank(inputDTO.getEmail()) && !new EmailValidator().isValid(inputDTO.getEmail(), context)) {
            error.add("Invalid email.");
        }
        if (StringUtils.isNotBlank(inputDTO.getMaritalStatus()) && Arrays.asList(MaritalStatusType.values()).stream()
                .noneMatch(o -> o.name().equals(inputDTO.getMaritalStatus()))) {
            error.add(String.format("Invalid marital status. Valid types: %s.", EnumSet.allOf(MaritalStatusType.class)));
        }
        if (StringUtils.isBlank(inputDTO.getGender())) {
            error.add("Gender is required.");
        } else {
            if (Arrays.asList(GenderType.values()).stream()
                    .noneMatch(o -> o.name().equals(inputDTO.getGender()))) {
                error.add(String.format("Invalid gender type. Valid types: %s.", EnumSet.allOf(GenderType.class)));
            }
        }
        return error;
    }

    private List<String> validationPersonalDocument(CustomerInputDTO inputDTO) {
        List<String> error = new ArrayList<>();

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
        return error;
    }

    private List<String> validationAddress(CustomerInputDTO inputDTO) {
        List<String> error = new ArrayList<>();

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
                error.add("Address state is required.");
            }
        }
        return error;
    }

}

