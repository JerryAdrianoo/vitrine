package com.vitrine.persistence.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.flywaydb.core.Flyway;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class HibernateUtil {

    private static final EntityManagerFactory entityManagerFactory = buildEntityManagerFactory();

    private static EntityManagerFactory buildEntityManagerFactory() {
        Properties properties = new Properties();
        try (InputStream inputStream = HibernateUtil.class
                .getClassLoader()
                .getResourceAsStream("database.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error when loading database.properties", e);
        }

        Flyway.configure()
                .dataSource(
                        properties.getProperty("db.url"),
                        properties.getProperty("db.user"),
                        properties.getProperty("db.password")
                )
                .locations("classpath:db/migration")
                .load()
                .migrate();

        Map<String, Object> config = new HashMap<>();
        config.put("jakarta.persistence.jdbc.driver", properties.getProperty("db.driver"));
        config.put("jakarta.persistence.jdbc.url", properties.getProperty("db.url"));
        config.put("jakarta.persistence.jdbc.user", properties.getProperty("db.user"));
        config.put("jakarta.persistence.jdbc.password", properties.getProperty("db.password"));
        config.put("hibernate.hbm2ddl.auto", properties.getProperty("hibernate.hbm2ddl.auto"));
        config.put("hibernate.show_sql", properties.getProperty("hibernate.show_sql"));
        config.put("hibernate.physical_naming_strategy", properties.getProperty("hibernate.physical_naming_strategy"));

        return Persistence.createEntityManagerFactory("vitrinePU", config);
    }

    public static EntityManager createEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    private static void shutdown() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}
