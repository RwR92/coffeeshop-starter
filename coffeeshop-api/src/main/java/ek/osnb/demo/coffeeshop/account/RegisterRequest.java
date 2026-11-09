package ek.osnb.demo.coffeeshop.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

record RegisterRequest(
        @NotBlank
        @Email
        @Size(max = Account.EMAIL_MAX_LENGTH)
        String email,

        @NotBlank
        @Size(min = 8, max = 72)
        String password
) {
}
