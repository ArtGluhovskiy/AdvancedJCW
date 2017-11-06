package org.art.dao.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnitUtil;

public class EMUtil {

    private static EntityManagerFactory emFactory;

    public static EntityManager createEntityManager() {
        if (emFactory == null) {
            emFactory = Persistence.createEntityManagerFactory("org.art.dao");
        }
        return emFactory.createEntityManager();
    }

    public static void initEMFactory(String persistenceUnitName) {
        emFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
    }

    public static PersistenceUnitUtil getPersistenceUnitUtil() {
        return emFactory.getPersistenceUnitUtil();
    }

    public static EntityManagerFactory getEmFactory() {
        return emFactory;
    }

    public static void closeEMFactory() {
        emFactory.close();
    }
}

