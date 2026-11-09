package ek.osnb.demo.coffeeshop.account;


import ek.osnb.demo.coffeeshop.common.ConflictException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
class AccountService implements UserDetailsService {

    private final AccountRepository repository;
    private final PasswordEncoder passwordEncoder;

    AccountService(AccountRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    void register(RegisterRequest request) {
        String email = Account.normalizeEmail(request.email());
        if (repository.existsByEmail(email)) {
            throw new ConflictException("An account with this email already exists");
        }
        repository.save(Account.create(email, passwordEncoder.encode(request.password()), Role.CUSTOMER));

    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {

        Optional<Account> result = repository.findByEmail(Account.normalizeEmail(username));

        if (result.isEmpty()) {
            throw new UsernameNotFoundException("No account for " + username);
        }

        Account account = result.get();
        return User.withUsername(account.getEmail())
                .password(account.getPasswordHash())
                .roles(account.getRole().name())
                .build();
    }
}
