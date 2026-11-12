package ek.osnb.demo.coffeeshop.customer;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
class CustomerController {

    private final CustomerService service;

    CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping("/me")
    CustomerResponse findMyProfile(Authentication authentication) {
        return service.findMyProfile(authentication.getName());
    }

    @PutMapping("/me")
    CustomerResponse saveMyProfile(Authentication authentication, @Valid @RequestBody ProfileRequest request) {
        return service.saveMyProfile(authentication.getName(), request);
    }

}
