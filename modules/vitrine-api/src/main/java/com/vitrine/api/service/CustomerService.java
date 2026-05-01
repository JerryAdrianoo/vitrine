package com.vitrine.api.service;

import com.vitrine.api.dto.CustomerRequest;
import com.vitrine.api.dto.PageResponse;
import com.vitrine.api.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    void register(CustomerRequest request);
    Optional<Customer> findById(Long id);
    Optional<Customer> findByEmail(String email);
    List<Customer> findAll();
    PageResponse<Customer> findAllPaginated(int page, int size);
    void update(Customer customer);
    void delete(Long id);
}
