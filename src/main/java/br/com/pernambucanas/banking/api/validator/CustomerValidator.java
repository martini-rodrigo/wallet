package br.com.pernambucanas.banking.api.validator;

import br.com.pernambucanas.banking.api.dto.CustomerInputDTO;
import br.com.pernambucanas.banking.api.utils.DocumentUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

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
            if(!DocumentUtils.isValidCPF(inputDTO.getDocument())){
                error.add("Document invalid.");
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
