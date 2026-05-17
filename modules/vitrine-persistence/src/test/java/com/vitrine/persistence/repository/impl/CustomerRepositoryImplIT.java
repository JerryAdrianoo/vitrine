package com.vitrine.persistence.repository.impl;

import com.vitrine.api.model.Customer;
import com.vitrine.api.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Teste de integração: exercita CustomerRepositoryImpl contra um MySQL real,
 * subido pelo Testcontainers via a URL jdbc:tc: do database.properties de teste.
 * Sem mocks — o JPQL, o mapeamento da entidade e as constraints rodam de verdade.
 */
public class CustomerRepositoryImplIT {

    // Tipo declarado como a interface (DIP): o teste valida o contrato,
    // a instância é a implementação concreta que queremos exercitar.
    private final CustomerRepository repository = new CustomerRepositoryImpl();

    // Testes de integração compartilham o mesmo container e schema.
    // Sem esta limpeza, linhas de um teste vazariam para o próximo e quebrariam
    // as asserções de contagem (findAll, countAll, paginação).
    @BeforeEach
    void cleanCustomersTable() {
        repository.findAll().forEach(customer -> repository.delete(customer.getId()));
    }

    @Test
    public void saveThenFindByIdReturnsPersistedCustomer() {
        Customer customer = buildCustomer("alice@email.com", "11111111111");
        repository.save(customer);

        // em.persist escreveu o ID gerado de volta na entidade — por isso
        // customer.getId() já está populado aqui.
        Optional<Customer> found = repository.findById(customer.getId());

        assertTrue(found.isPresent());
        assertEquals("alice@email.com", found.get().getEmail());
    }

    @Test
    public void findByEmailReturnsCustomerWhenExists() {
        repository.save(buildCustomer("bob@email.com", "22222222222"));

        Optional<Customer> found = repository.findByEmail("bob@email.com");

        assertTrue(found.isPresent());
        assertEquals("22222222222", found.get().getCpf());
    }

    @Test
    public void findByEmailReturnsEmptyWhenAbsent() {
        Optional<Customer> found = repository.findByEmail("ghost@email.com");

        assertTrue(found.isEmpty());
    }

    @Test
    public void findByCpfReturnsCustomerWhenExists() {
        repository.save(buildCustomer("carol@email.com", "33333333333"));

        Optional<Customer> found = repository.findByCpf("33333333333");

        assertTrue(found.isPresent());
        assertEquals("carol@email.com", found.get().getEmail());
    }

    @Test
    public void findAllReturnsAllSavedCustomers() {
        repository.save(buildCustomer("dave@email.com", "44444444444"));
        repository.save(buildCustomer("erin@email.com", "55555555555"));

        List<Customer> all = repository.findAll();

        assertEquals(2, all.size());
    }

    @Test
    public void updateChangesPersistedFields() {
        Customer customer = buildCustomer("frank@email.com", "66666666666");
        repository.save(customer);

        customer.setName("Frank Updated");
        repository.update(customer);

        Optional<Customer> found = repository.findById(customer.getId());
        assertTrue(found.isPresent());
        assertEquals("Frank Updated", found.get().getName());
    }

    @Test
    public void deleteRemovesCustomer() {
        Customer customer = buildCustomer("grace@email.com", "77777777777");
        repository.save(customer);

        repository.delete(customer.getId());

        assertTrue(repository.findById(customer.getId()).isEmpty());
    }

    @Test
    public void findAllPaginatedRespectsPageAndSize() {
        repository.save(buildCustomer("hugo@email.com", "88888888888"));
        repository.save(buildCustomer("ivan@email.com", "99999999999"));
        repository.save(buildCustomer("jane@email.com", "10101010101"));

        List<Customer> firstPage = repository.findAllPaginated(0, 2);
        List<Customer> secondPage = repository.findAllPaginated(1, 2);

        assertEquals(2, firstPage.size());
        assertEquals(1, secondPage.size());
        assertEquals(3, repository.countAll());
    }

    // CPF precisa ter exatamente 11 caracteres (VARCHAR(11) + @Size(11,11)).
    // passwordHash é nullable = false — sem ele o INSERT estoura constraint.
    private Customer buildCustomer(String email, String cpf) {
        Customer customer = new Customer();
        customer.setName("Test Customer");
        customer.setEmail(email);
        customer.setCpf(cpf);
        customer.setAddress("Street A, 123");
        customer.setPasswordHash("hashed-password");
        return customer;
    }
}
