package br.com.pernambucanas.banking.api.controller;

import br.com.pernambucanas.banking.api.controller.response.AboutResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/help")
public class HelpController {

    @GetMapping("/about")
    public ResponseEntity<AboutResponse> getAbout() {

        return ResponseEntity.ok(
                new AboutResponse()
        );
    }
}
