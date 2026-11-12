package ek.osnb.demo.coffeeshop.order;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/staff/orders")
class StaffOrderController {

    private final OrderService service;

    StaffOrderController(OrderService service){
        this.service = service;
    }

    @GetMapping
    List<OrderSummaryResponse> findAll(){
        return service.findAllOrders();
    }
}
