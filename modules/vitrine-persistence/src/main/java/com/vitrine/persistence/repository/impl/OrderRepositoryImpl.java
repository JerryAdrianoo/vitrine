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
        EntityManager entityManager = HibernateUtil.createEntityManager();
        try {
            List<Order> result = entityManager.createQuery(
                            "SELECT o FROM Order o " +
                                "JOIN FETCH o.customer " +
                                "JOIN FETCH o.items i " +
                                "JOIN FETCH i.product " +
                                "WHERE o.id = :id", Order.class)
                    .setParameter("id", id)
                    .getResultList();

            return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Order> findByCustomerId(Long customerId) {
        EntityManager entityManager = HibernateUtil.createEntityManager();
        try {
            return entityManager.createQuery(
                            "SELECT DISTINCT o FROM Order o " +
                                    "JOIN FETCH o.customer " +
                                    "JOIN FETCH o.items i " +
                                    "JOIN FETCH i.product " +
                                    "WHERE o.customer.id = :customerId", Order.class)
                    .setParameter("customerId", customerId)
                    .getResultList();
        } finally {
            entityManager.close();
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
    public List<Order> findAllPaginated(int page, int size) {
        EntityManager em = HibernateUtil.createEntityManager();

        int offset = page * size;
        try {
            return em.createQuery("SELECT p FROM Order p", Order.class)
                    .setFirstResult(offset)
                    .setMaxResults(size)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public long countAll() {
        EntityManager em = HibernateUtil.createEntityManager();

        try {
            return em.createQuery("SELECT COUNT(p) FROM Order p", Long.class)
                    .getSingleResult();
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
