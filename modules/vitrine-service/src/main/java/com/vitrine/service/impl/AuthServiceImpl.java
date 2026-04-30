package com.vitrine.service.impl;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.vitrine.api.model.Customer;
import com.vitrine.api.repository.CustomerRepository;
import com.vitrine.api.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final CustomerRepository customerRepository;

    public AuthServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer authenticate(String email, String password) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.warn("Login attempt with unknown email: {}", email);
                    return new SecurityException("Invalid credentials");
                });

        boolean valid = BCrypt.verifyer().verify(password.toCharArray(), customer.getPasswordHash()).verified;

        if (!valid) {
            logger.warn("Invalid password attempt for email: {}", email);
            throw new SecurityException("Invalid credentials");
        }

        return customer;
    }
}
