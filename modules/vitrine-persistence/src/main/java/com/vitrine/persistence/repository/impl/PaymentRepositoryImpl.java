package com.vitrine.persistence.repository.impl;

import com.vitrine.api.model.Payment;
import com.vitrine.api.repository.PaymentRepository;
import com.vitrine.persistence.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.Optional;

public class PaymentRepositoryImpl implements PaymentRepository {

    @Override
    public Optional<Payment> findById(Long id) {
        EntityManager em = HibernateUtil.createEntityManager();
        try {
            return Optional.ofNullable(em.find(Payment.class, id));
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Payment> findByOrderId(Long orderId) {
        EntityManager em = HibernateUtil.createEntityManager();
        try {
            return Optional.of(
                    em.createQuery("SELECT p FROM Payment p WHERE p.order.id = :orderId", Payment.class)
                            .setParameter("orderId", orderId)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    @Override
    public void save(Payment payment) {
        EntityManager em = HibernateUtil.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(payment);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Payment payment) {
        EntityManager em = HibernateUtil.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(payment);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
