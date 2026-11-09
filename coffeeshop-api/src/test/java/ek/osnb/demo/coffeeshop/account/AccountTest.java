package ek.osnb.demo.coffeeshop.account;

import ek.osnb.demo.coffeeshop.common.InvalidInputException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountTest {

    @Test
    void createNormalizesTheEmail(){
        Account account = Account.create(" Rune@Example.COM","hash",Role.CUSTOMER);

        assertEquals("rune@example.com",account.getEmail());
    }

    @Test
    void createKeepsTheHashAndRole(){
        Account account = Account.create("rune@example.com","hash",Role.STAFF);

        assertEquals("hash", account.getPasswordHash());
        assertEquals(Role.STAFF, account.getRole());
    }


    @Test
    void createRejectsBlankEmail(){
        assertThrows(InvalidInputException.class,
                () -> Account.create("  ", "hash", Role.CUSTOMER));
    }

    @Test
    void createRejectsMissingRole(){
        assertThrows(InvalidInputException.class,
                () -> Account.create("rune@example.com","hash", null));
    }
}
