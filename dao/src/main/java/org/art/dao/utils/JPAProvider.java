package org.art.dao.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnitUtil;
import java.util.HashMap;
import java.util.Map;

/**
 * JPA Entity Manager provider.
 */
public class JPAProvider {

    private static final Logger LOG = LogManager.getLogger(JPAProvider.class);

    private static final String DEFAULT_PERSIST_UNIT_NAME = "org.art.dao";

    private static final Map<String, EntityManagerFactory> EM_FACTORIES = new HashMap<>();

    private static ThreadLocal<EntityManager> threadCache = new ThreadLocal<>();

    //Default persistence unit eager initialization
    static {
        try {
            EntityManagerFactory defaultEMFactory = Persistence.createEntityManagerFactory(DEFAULT_PERSIST_UNIT_NAME);
            EM_FACTORIES.put(DEFAULT_PERSIST_UNIT_NAME, defaultEMFactory);
        } catch (Exception ex) {
            LOG.error("Default Entity Manger Factory: initialization failed!", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Returns the default Entity Manager Factory.
     */
    public static EntityManagerFactory getEMFactory() {
        return EM_FACTORIES.get(DEFAULT_PERSIST_UNIT_NAME);
    }

    /**
     * Returns the Entity Manager Factory with a specified unit name.
     */
    public static EntityManagerFactory getEMFactory(String persistUnitName) {
        EntityManagerFactory emFactory = EM_FACTORIES.get(persistUnitName);
        if (emFactory == null) {
            synchronized (JPAProvider.class) {
                emFactory = Persistence.createEntityManagerFactory(persistUnitName);
                EM_FACTORIES.put(persistUnitName, emFactory);
            }
        }
        return emFactory;
    }

    /**
     * Returns an Entity Manger provided by the default Entity Manager Factory.
     */
    public static EntityManager getEntityManager() {
        return EM_FACTORIES.get(DEFAULT_PERSIST_UNIT_NAME).createEntityManager();
    }

    /**
     * Returns Persistent Unit Util helper provided by the default Entity Manager Factory.
     */
    public static PersistenceUnitUtil getPersistenceUnitUtil() {
        return EM_FACTORIES.get(DEFAULT_PERSIST_UNIT_NAME).getPersistenceUnitUtil();
    }

    public static EntityManager getThreadCachedEM() {
        EntityManager em = threadCache.get();
        if (em == null) {
            em = getEntityManager();
            threadCache.set(em);
            return em;
        }
        return em;
    }


    /**
     * Closes the default Entity Manager Factory.
     */
    public static void closeEMFactory() {
        EM_FACTORIES.get(DEFAULT_PERSIST_UNIT_NAME).close();
    }
}

