package com.sergiu.customer.customer;

import com.sergiu.customer.exception.CustomerNotFoundException;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper mapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper mapper) {
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    public String createCustomer(@Valid CustomerRequest customerRequest) {
        var customer = customerRepository.save(mapper.toCustomer(customerRequest));
        return customer.getId();
    }

    public void updateCustomer(@Valid CustomerRequest customerRequest) {
        Customer customer = customerRepository.findById(Integer.valueOf(customerRequest.id()))
                .orElseThrow(() -> new CustomerNotFoundException(
                        format("Customer with id %s not found", customerRequest.id())
                ));

        mergerCustomer(customer, customerRequest);
        customerRepository.save(customer);
    }

    private void mergerCustomer(Customer customer, CustomerRequest customerRequest) {
        if(StringUtils.isNotBlank(customerRequest.firstName())) {
            customer.setFirstName(customerRequest.firstName());
        }
        if(StringUtils.isNotBlank(customerRequest.lastName())) {
            customer.setLastName(customerRequest.lastName());
        }
        if(StringUtils.isNotBlank(customerRequest.email())) {
            customer.setEmail(customerRequest.email());
        }
        if(customerRequest.address() != null) {
            customer.setAddress(customerRequest.address());
        }

    }

    public List<CustomerResponse> findAllCustomer() {
        return customerRepository.findAll()
                .stream()
                .map(mapper::fromCustomer)
                .collect(Collectors.toList());
    }

    public Boolean existsById(String customerId) {
        return customerRepository.findById(Integer.valueOf(customerId))
                .isPresent();
    }

    public CustomerResponse findById(String customerId) {
        return customerRepository.findById(Integer.valueOf(customerId))
                .map(mapper::fromCustomer)
                .orElseThrow(() -> new CustomerNotFoundException(format("No customer found with the provided ID:: %s", customerId)));
    }

    public void deleteCustomer(String customerId) {
        customerRepository.deleteById(Integer.valueOf(customerId));
    }
}
