package com.vitrine.service.impl;

import com.vitrine.api.dto.CustomerRequest;
import com.vitrine.api.model.Customer;
import com.vitrine.api.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomerServiceImplTest {

    private CustomerRepository customerRepository;
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setup() {
        customerRepository = mock(CustomerRepository.class);
        customerService = new CustomerServiceImpl(customerRepository);
    }

    @Test
    public void registerHappyPathSavesCustomer() {
        CustomerRequest request = buildRequest("test@email.com", "12345678901");

        when(customerRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(customerRepository.findByCpf(request.getCpf())).thenReturn(Optional.empty());

        customerService.register(request);

        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    public void registerDuplicatedEmailThrowsException() {
        CustomerRequest request = buildRequest("test@email.com", "12345678901");

        when(customerRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new Customer()));

        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> customerService.register(request)
        );

        assertTrue(illegalArgumentException.getMessage().contains(request.getEmail()));
        verify(customerRepository, never()).save(any());
    }

    @Test
    public void registerDuplicatedCPFThrowsException() {
        CustomerRequest request = buildRequest("test@email.com", "12345678901");

        when(customerRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(customerRepository.findByCpf(request.getCpf())).thenReturn(Optional.of(new Customer()));

        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> customerService.register(request)
        );

        assertTrue(illegalArgumentException.getMessage().contains(request.getCpf()));
        verify(customerRepository, never()).save(any());
    }

    private CustomerRequest buildRequest(String email, String cpf) {
        CustomerRequest request = new CustomerRequest();
        request.setName("Test");
        request.setEmail(email);
        request.setCpf(cpf);
        request.setAddress("Street A, 123");
        request.setPassword("password123");
        return request;
    }
}
