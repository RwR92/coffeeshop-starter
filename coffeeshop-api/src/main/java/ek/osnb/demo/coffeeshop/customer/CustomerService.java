package ek.osnb.demo.coffeeshop.customer;

import ek.osnb.demo.coffeeshop.common.ConflictException;
import ek.osnb.demo.coffeeshop.common.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
class CustomerService implements CustomerApi {

    private final CustomerRepository repository;

    CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    // ---------- Used by CustomerController ----------

    @Transactional(readOnly = true)
    CustomerResponse findMyProfile(String email) {
        Optional<Customer> customer = repository.findByEmail(Customer.normalizeEmail(email));

        if(customer.isEmpty()){
            throw new NotFoundException("No profile yet");
        }

        return CustomerMapper.toResponse(customer.get());
    }

    CustomerResponse saveMyProfile(String email, ProfileRequest request) {
        Customer customer;
        Optional<Customer> existing = repository.findByEmail(Customer.normalizeEmail(email));

        if(existing.isPresent()){
            customer = existing.get();

            customer.updateDetails(request.name(), customer.getEmail(), request.street(), request.postalCode(), request.city());
        }else {
            customer = repository.save(Customer.create(request.name(), email, request.street(), request.postalCode(), request.city()));
        }

        return CustomerMapper.toResponse(customer);
    }

    // ---------- CustomerApi: used by other features ----------

    @Override
    @Transactional(readOnly = true)
    public CustomerInfo getCustomer(Long customerId) {
        return CustomerMapper.toInfo(findCustomer(customerId));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerInfo> findCustomerByEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }

        return repository.findByEmail(Customer.normalizeEmail(email))
                .map(CustomerMapper::toInfo);
    }

    private Customer findCustomer(Long id) {
        Optional<Customer> result = repository.findById(id);

        if (result.isEmpty()) {
            throw new NotFoundException("Customer not found: " + id);
        }

        return result.get();
    }
}
