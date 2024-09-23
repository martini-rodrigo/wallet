package br.com.pernambucanas.banking.api.service;

import br.com.pernambucanas.banking.api.dto.CustomerInputDTO;
import br.com.pernambucanas.banking.api.queue.producer.CustomerCreateProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerCreateProducer customerCreateProducer;

    public void save(CustomerInputDTO inputDTO) {
        customerCreateProducer.publish(inputDTO);
    }
}
