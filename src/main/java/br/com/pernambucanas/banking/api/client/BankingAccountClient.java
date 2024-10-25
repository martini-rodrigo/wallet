package br.com.pernambucanas.banking.api.client;

import br.com.pernambucanas.banking.api.client.dto.AccountNumberGeneratorOutputDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "baking-account-client", url = "${feign-client.banking.url}")
public interface BankingAccountClient {

	@PostMapping("/banking-account/accounts/number-generator/{companyId}")
	AccountNumberGeneratorOutputDTO numberGenerator(@PathVariable Long companyId);

}
