package ek.osnb.demo.coffeeshop.account;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
class AccountController {

    private final AccountService service;

    AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping("/register")
    void register(@Valid @RequestBody RegisterRequest request) {
        service.register(request);
    }

    @GetMapping("/me")
    MeResponse me(Authentication authentication){
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(role -> role.startsWith("ROLE_"))
                .toList();

        return new MeResponse(authentication.getName(),roles);
    }


}
