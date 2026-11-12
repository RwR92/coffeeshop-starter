package ek.osnb.demo.coffeeshop.order;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface OrderRepository extends JpaRepository<Order, Long> {

    // Loads the lines in the same query: the summary counts the items of each order
    @EntityGraph(attributePaths = "lines")
    List<Order> findByCustomerIdOrderByPlacedAtDesc(Long customerId);

    // Empty if the order doesn't exist or belongs to another customer
    @EntityGraph(attributePaths = "lines")
    Optional<Order> findByIdAndCustomerId(Long id, Long customerId);

    // Every order, newest first. The lines are loaded too: the summary counts the items
    @EntityGraph(attributePaths = "lines")
    List<Order> findAllByOrderByPlacedAtDesc();
}
