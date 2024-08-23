package br.com.pernambucanas.banking.api.service;

import br.com.pernambucanas.banking.api.dto.CustomerInputDTO;
import br.com.pernambucanas.banking.api.queue.producer.CustomerCreateProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerCreateProducer customerCreateProducer;

    public void save(CustomerInputDTO inputDTO) {
        customerCreateProducer.publish(inputDTO);
    }
}
