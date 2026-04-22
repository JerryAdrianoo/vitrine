package com.vitrine.persistence.repository.impl;

import com.vitrine.api.model.Stock;
import com.vitrine.api.repository.StockRepository;
import com.vitrine.persistence.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.Optional;

public class StockRepositoryImpl implements StockRepository {

    @Override
    public Optional<Stock> findById(Long id) {
        EntityManager em = HibernateUtil.createEntityManager();
        try {
            return Optional.ofNullable(em.find(Stock.class, id));
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Stock> findByProductId(Long productId) {
        EntityManager em = HibernateUtil.createEntityManager();
        try {
            return Optional.of(
                    em.createQuery("SELECT s FROM Stock s WHERE s.product.id = :productId", Stock.class)
                            .setParameter("productId", productId)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    @Override
    public void save(Stock stock) {
        EntityManager em = HibernateUtil.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(stock);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Stock stock) {
        EntityManager em = HibernateUtil.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(stock);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
