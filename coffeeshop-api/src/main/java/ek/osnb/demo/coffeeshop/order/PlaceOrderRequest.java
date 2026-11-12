package ek.osnb.demo.coffeeshop.order;

import jakarta.validation.constraints.NotNull;

// Ids only: the order looks up the customer, the cart and the products itself
record PlaceOrderRequest(

        @NotNull
        Long cartId
) {
}
