package com.vitrine.service.impl;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.vitrine.api.dto.CustomerRequest;
import com.vitrine.api.dto.PageResponse;
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
    public void register(CustomerRequest request) {
        customerRepository.findByEmail(request.getEmail())
                .ifPresent(c -> {
                    logger.warn("Registration rejected — email already exists: {}", request.getEmail());
                    throw new IllegalArgumentException("E-mail already exists: " + request.getEmail());
                });
        customerRepository.findByCpf(request.getCpf())
                .ifPresent(c -> {
                    logger.warn("Registration rejected — CPF already exists: {}", request.getCpf());
                    throw new IllegalArgumentException("CPF already exists: " + request.getCpf());
                });

        String hash = BCrypt.withDefaults()
                .hashToString(12, request.getPassword().toCharArray());

        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setCpf(request.getCpf());
        customer.setAddress(request.getAddress());
        customer.setPasswordHash(hash);

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
    public PageResponse<Customer> findAllPaginated(int page, int size) {
        List<Customer> items = customerRepository.findAllPaginated(page, size);
        long total = customerRepository.countAll();
        return new PageResponse<>(items, page, size, total);
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
