package com.vitrine.persistence.repository.impl;

import com.vitrine.api.model.Order;
import com.vitrine.api.repository.OrderRepository;
import com.vitrine.persistence.util.HibernateUtil;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class OrderRepositoryImpl implements OrderRepository {

    @Override
    public Optional<Order> findById(Long id) {
        EntityManager em = HibernateUtil.createEntityManager();
        try {
            return Optional.ofNullable(em.find(Order.class, id));
        } finally {
            em.close();
        }
    }

    @Override
    public List<Order> findByCustomerId(Long customerId) {
        EntityManager em = HibernateUtil.createEntityManager();
        try {
            return em.createQuery("SELECT o FROM Order o WHERE o.customer.id = :customerId", Order.class)
                    .setParameter("customerId", customerId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Order> findAll() {
        EntityManager em = HibernateUtil.createEntityManager();
        try {
            return em.createQuery("SELECT o FROM Order o", Order.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void save(Order order) {
        EntityManager em = HibernateUtil.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(order);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Order order) {
        EntityManager em = HibernateUtil.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(order);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
