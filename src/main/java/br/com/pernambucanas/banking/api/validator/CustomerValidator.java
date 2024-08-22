package br.com.pernambucanas.banking.api.validator;

import br.com.pernambucanas.banking.api.dto.CustomerInputDTO;
import br.com.pernambucanas.banking.api.enums.MaritalStatusType;
import br.com.pernambucanas.banking.api.utils.DocumentUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class CustomerValidator implements ConstraintValidator<CustomerConstraint, CustomerInputDTO> {
    @Override
    public boolean isValid(CustomerInputDTO inputDTO, ConstraintValidatorContext context) {
        List<String> error = new ArrayList<>();

        if (StringUtils.isBlank(inputDTO.getName())) {
            error.add("Name is required.");
        }
        if (StringUtils.isBlank(inputDTO.getDocument())) {
            error.add("Document is required.");
        }
        if (StringUtils.isNotBlank(inputDTO.getDocument())) {
            if (!DocumentUtils.isValidCPF(inputDTO.getDocument())) {
                error.add("Document invalid.");
            }
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
        if (Objects.isNull(inputDTO.getManagerId())) {
            error.add("Manager id is required.");
        }

        if (!error.isEmpty()) {
            context.disableDefaultConstraintViolation();
            error.forEach(o -> context.buildConstraintViolationWithTemplate(o).addConstraintViolation());
            return false;
        }
        return true;
    }
}
