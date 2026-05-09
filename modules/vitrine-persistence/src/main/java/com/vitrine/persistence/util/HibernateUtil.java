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

    private static String resolve(String envKey, String propKey, String defaultValue, Properties properties) {
        String fromEnv = System.getenv(envKey);
        if (fromEnv != null && !fromEnv.isBlank()) {
            return fromEnv;
        }
        String fromProp = properties.getProperty(propKey);
        return fromProp != null ? fromProp : defaultValue;
    }

    private static EntityManagerFactory buildEntityManagerFactory() {
        Properties properties = new Properties();
        // database.properties existe em dev local mas é excluído da imagem Docker
        // (.dockerignore). Quando ausente, seguimos só com env vars + defaults.
        try (InputStream inputStream = HibernateUtil.class
                .getClassLoader()
                .getResourceAsStream("database.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error when loading database.properties", e);
        }

        Flyway.configure()
                .dataSource(
                        resolve("DB_URL", "db.url", null, properties),
                        resolve("DB_USER", "db.user", null, properties),
                        resolve("DB_PASSWORD", "db.password", null, properties)
                )
                .locations("classpath:db/migration")
                .load()
                .migrate();

        Map<String, Object> config = new HashMap<>();
        config.put("jakarta.persistence.jdbc.driver",
                resolve("DB_DRIVER", "db.driver", "com.mysql.cj.jdbc.Driver", properties));
        config.put("jakarta.persistence.jdbc.url",
                resolve("DB_URL", "db.url", null, properties));
        config.put("jakarta.persistence.jdbc.user",
                resolve("DB_USER", "db.user", null, properties));
        config.put("jakarta.persistence.jdbc.password",
                resolve("DB_PASSWORD", "db.password", null, properties));
        config.put("hibernate.hbm2ddl.auto",
                resolve("HIBERNATE_HBM2DDL_AUTO", "hibernate.hbm2ddl.auto", "validate", properties));
        config.put("hibernate.show_sql",
                resolve("HIBERNATE_SHOW_SQL", "hibernate.show_sql", "false", properties));
        config.put("hibernate.physical_naming_strategy",
                resolve("HIBERNATE_PHYSICAL_NAMING_STRATEGY", "hibernate.physical_naming_strategy",
                        "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy", properties));

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
