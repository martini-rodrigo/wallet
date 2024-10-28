package br.com.pernambucanas.banking.api.service;

import br.com.pernambucanas.banking.api.dto.CustomerInputDTO;
import br.com.pernambucanas.banking.api.queue.producer.CustomerCreateProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerCreateProducer customerCreateProducer;

    private final BankingAccountService bankingAccountService;

    public void save(CustomerInputDTO inputDTO) {
        var accountNumberGeneratorOutputDTO = bankingAccountService.numberGenerator(inputDTO.getCompanyId());
        var account = new CustomerInputDTO.CostumerAccountInputDTO(accountNumberGeneratorOutputDTO.getNumber(),
                accountNumberGeneratorOutputDTO.getDigit());
        inputDTO.setAccount(account);

        customerCreateProducer.publish(inputDTO);
    }
}
