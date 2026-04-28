package com.vitrine.service.impl;

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
        Customer customer = buildCustomer("test@email.com", "12345678901");

        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.empty());
        when(customerRepository.findByCpf(customer.getCpf())).thenReturn(Optional.empty());

        customerService.register(customer);

        verify(customerRepository).save(customer);
    }

    @Test
    public void registerDuplicatedEmailThrowsException() {
        Customer customer = buildCustomer("test@email.com", "12345678901");

        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));

        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> customerService.register(customer)
        );

        assertTrue(illegalArgumentException.getMessage().contains(customer.getEmail()));
        verify(customerRepository, never()).save(any());
    }

    @Test
    public void registerDuplicatedCPFThrowsException() {
        Customer customer = buildCustomer("test@email.com", "12345678901");

        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.empty());
        when(customerRepository.findByCpf(customer.getCpf())).thenReturn(Optional.of(customer));

        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> customerService.register(customer)
        );

        assertTrue(illegalArgumentException.getMessage().contains(customer.getCpf()));
        verify(customerRepository, never()).save(any());
    }

    private Customer buildCustomer(String email, String cpf) {
        Customer customer = new Customer();
        customer.setName("Test");
        customer.setEmail(email);
        customer.setCpf(cpf);
        customer.setAddress("Street A, 123");

        return customer;
    }
}
