package ek.osnb.demo.coffeeshop.account;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
class StaffAccountInitData implements CommandLineRunner {

    private final String staffEmail;
    private final String staffPassword;

    private final AccountRepository accounts;
    private final PasswordEncoder passwordEncoder;


    StaffAccountInitData(AccountRepository accounts, PasswordEncoder passwordEncoder, @Value("${coffeeshop.staff.email}") String staffEmail, @Value("${coffeeshop.staff.password}") String staffPassword) {
        this.accounts = accounts;
        this.passwordEncoder = passwordEncoder;
        this.staffEmail = staffEmail;
        this.staffPassword = staffPassword;
    }

    @Override
    @Transactional
    public void run(String... args) {
        String email = Account.normalizeEmail(staffEmail);
        if (accounts.existsByEmail(email)) {
            return;
        }
        accounts.save(Account.create(email, passwordEncoder.encode(staffPassword), Role.STAFF));
    }
}
