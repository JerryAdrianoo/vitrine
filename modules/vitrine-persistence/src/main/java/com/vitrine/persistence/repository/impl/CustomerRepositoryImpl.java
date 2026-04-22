package com.vitrine.persistence.repository.impl;

import com.vitrine.api.model.Customer;
import com.vitrine.api.repository.CustomerRepository;
import com.vitrine.persistence.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.Optional;

public class CustomerRepositoryImpl implements CustomerRepository {

    @Override
    public Optional<Customer> findById(Long id) {
        EntityManager em = HibernateUtil.createEntityManager();
        try {
            return Optional.ofNullable(em.find(Customer.class, id));
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        EntityManager em = HibernateUtil.createEntityManager();
        try {
            return Optional.of(
                    em.createQuery("SELECT c FROM Customer c WHERE c.email = :email", Customer.class)
                            .setParameter("email", email)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Customer> findByCpf(String cpf) {
        EntityManager em = HibernateUtil.createEntityManager();
        try {
            return Optional.of(
                    em.createQuery("SELECT c FROM Customer c WHERE c.cpf = :cpf", Customer.class)
                            .setParameter("cpf", cpf)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Customer> findAll() {
        EntityManager em = HibernateUtil.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM Customer c", Customer.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void save(Customer customer) {
        EntityManager em = HibernateUtil.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(customer);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Customer customer) {
        EntityManager em = HibernateUtil.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(customer);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager em = HibernateUtil.createEntityManager();
        try {
            em.getTransaction().begin();
            Customer customer = em.find(Customer.class, id);
            if (customer != null) {
                em.remove(customer);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
