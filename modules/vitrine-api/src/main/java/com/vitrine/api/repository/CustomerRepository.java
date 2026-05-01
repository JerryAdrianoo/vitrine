package com.vitrine.api.repository;

import com.vitrine.api.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    Optional<Customer> findById(Long id);
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByCpf(String cpf);
    List<Customer> findAll();
    List<Customer> findAllPaginated(int page, int size);
    long countAll();
    void save(Customer customer);
    void update(Customer customer);
    void delete(Long id);
}
