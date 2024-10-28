package br.com.pernambucanas.banking.api.service;

import br.com.pernambucanas.banking.api.client.BankingAccountClient;
import br.com.pernambucanas.banking.api.client.dto.AccountNumberGeneratorOutputDTO;
import br.com.pernambucanas.banking.api.dto.CustomerInputDTO;
import br.com.pernambucanas.banking.api.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.BufferOverflowException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BankingAccountService {

    private final BankingAccountClient bankingAccountClient;

    public AccountNumberGeneratorOutputDTO numberGenerator(Long companyId) {
        var accountNumberGeneratorOutputDTO = bankingAccountClient.numberGenerator(companyId);
        if (Objects.isNull(accountNumberGeneratorOutputDTO.getNumber())) {
            throw new BusinessException("Error while generate account number.");
        }
        return accountNumberGeneratorOutputDTO;
    }
}
