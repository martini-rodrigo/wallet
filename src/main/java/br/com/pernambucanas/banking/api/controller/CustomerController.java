package br.com.pernambucanas.banking.api.controller;

import br.com.pernambucanas.banking.api.dto.CustomerInputDTO;
import br.com.pernambucanas.banking.api.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping(produces = "application/json")
    public ResponseEntity<Void> save(@Valid @RequestBody CustomerInputDTO inputDTO) {
        customerService.save(inputDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
