package ek.osnb.demo.coffeeshop.order;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
class OrderController {

    private final OrderService service;

    OrderController(OrderService service) {
        this.service = service;
    }

    // Checkout: turns a cart into an order
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    OrderResponse placeOrder(Authentication authentication, @Valid @RequestBody PlaceOrderRequest request) {
        return service.placeOrder(authentication.getName(), request.cartId());
    }

    @GetMapping
    List<OrderSummaryResponse> findOwnOrders(Authentication authentication) {
        return service.findOwnOrders(authentication.getName());
    }

    @GetMapping("/{id}")
    OrderResponse findById(Authentication authentication, @PathVariable Long id){
        if(isStaff(authentication)){
            return service.findById(id);
        }
        return service.findOwnOrder(authentication.getName(), id);
    }

    @PostMapping("/{id}/cancel")
    OrderResponse cancel (Authentication authentication, @PathVariable Long id){
        return service.cancelOwn(authentication.getName(), id);
    }

    // Should only be allowed for staff - a job for Spring Security later
    @PostMapping("/{id}/ship")
    OrderResponse ship(@PathVariable Long id) {
        return service.ship(id);
    }

    private static boolean isStaff(Authentication authentication){
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_STAFF"));
    }
}
