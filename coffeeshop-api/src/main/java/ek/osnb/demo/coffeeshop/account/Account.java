package ek.osnb.demo.coffeeshop.account;

import ek.osnb.demo.coffeeshop.common.InvalidInputException;
import jakarta.persistence.*;

import java.util.Locale;

@Entity
@Table(name= "accounts")
class Account {

    static final int EMAIL_MAX_LENGTH = 254;

    // @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    // private Long id;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = EMAIL_MAX_LENGTH)
    private String email;

    @Column(nullable = false, length = 100)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    protected Account() {
        // Required by JPA
    }

    private Account(String email, String passwordHash, Role role) {
        // 1. valider alt FØRST (som Customer.updateDetails):
        //    email og passwordHash via required(...), role != null
        //    → ellers throw new InvalidInputException("An account must have a ...")
        // 2. sæt felterne, email som normalizeEmail(email)
        updateDetails(email, passwordHash, role);
    }

    static Account create(String email, String passwordHash, Role role) {
        return new Account(email, passwordHash, role);
    }

    void updateDetails(String email, String passwordHash, Role role){
        String validEmail = normalizeEmail(required(email,"email"));
        String validPassHash = required(passwordHash,"password");
        if(role == null){
            throw new InvalidInputException("An account must have role");
        }

        this.email = validEmail;
        this.passwordHash = validPassHash;
        this.role = role;
    }
    static String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private static String required(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new InvalidInputException("A customer must have a " + field);
        }

        return value.trim();
    }

    Long getId() {
        return id;
    }

    String getEmail() {
        return email;
    }

    String getPasswordHash() {
        return passwordHash;
    }

    Role getRole() {
        return role;
    }

}
