package br.com.pernambucanas.banking.api.controller;

import br.com.pernambucanas.banking.api.dto.CustomerInputDTO;
import br.com.pernambucanas.banking.api.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping(produces = "application/json")
    public ResponseEntity<Void> save(@Valid @RequestBody CustomerInputDTO inputDTO, @RequestHeader("companyId") Long companyId) {
        inputDTO.setCompanyId(companyId);
        customerService.save(inputDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
