package com.vitrine.service.impl;

import com.vitrine.api.model.Customer;
import com.vitrine.api.repository.CustomerRepository;
import com.vitrine.api.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class CustomerServiceImpl implements CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void register(Customer customer) {
        customerRepository.findByEmail(customer.getEmail())
                .ifPresent(c -> {
                    logger.warn("Registration rejected — email already exists: {}", customer.getEmail());
                    throw new IllegalArgumentException("E-mail already exists: " + customer.getEmail());
                });
        customerRepository.findByCpf(customer.getCpf())
                .ifPresent(c -> {
                    logger.warn("Registration rejected — CPF already exists: {}", customer.getCpf());
                    throw new IllegalArgumentException("CPF already exists: " + customer.getCpf());
                });

        customerRepository.save(customer);
        logger.info("Customer registered: {}", customer.getEmail());
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public void update(Customer customer) {
        customerRepository.update(customer);
    }

    @Override
    public void delete(Long id) {
        customerRepository.delete(id);
        logger.info("Customer deleted: {}", id);
    }
}
