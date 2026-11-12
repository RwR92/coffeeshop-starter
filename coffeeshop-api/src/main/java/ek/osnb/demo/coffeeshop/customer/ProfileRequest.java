package ek.osnb.demo.coffeeshop.customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

record ProfileRequest(@NotBlank @Size(max = Customer.NAME_MAX_LENGTH) String name,
                      @NotBlank @Size(max = Customer.STREET_MAX_LENGTH) String street,
                      @NotBlank @Size(max = Customer.POSTAL_CODE_MAX_LENGTH) String postalCode,
                      @NotBlank @Size(max = Customer.CITY_MAX_LENGTH) String city) {
}
