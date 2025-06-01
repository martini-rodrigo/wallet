package br.com.recargapay.wallet.service;

import br.com.recargapay.wallet.entity.User;
import br.com.recargapay.wallet.exception.NotFoundException;
import br.com.recargapay.wallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User {0} not found", id));
    }
}
