package org.art.services;

import org.art.services.exceptions.ServiceBusinessException;
import org.art.services.exceptions.ServiceSystemException;
import org.art.dao.exceptions.DAOSystemException;

public interface Service<T> {

    /**
     * This service method saves entity to the database
     *
     * @param t entity
     * @return entity with its ID from the database
     * @throws ServiceSystemException if {@link DAOSystemException}
     *                                was thrown during the saving operation to the database
     */
    T save(T t) throws ServiceSystemException;

    /**
     * This service method reads (gets) entity from the database by its ID
     *
     * @param id entity's ID
     * @return entity with required ID
     * @throws ServiceSystemException   if {@link DAOSystemException}
     *                                  was thrown during the reading operation from the database
     * @throws ServiceBusinessException if no entity with such ID was found
     */
    T get(long id) throws ServiceSystemException, ServiceBusinessException;

    /**
     * This service method updates entity in the database
     *
     * @param t entity with fields you need to update
     * @throws ServiceSystemException   if {@link DAOSystemException}
     *                                  was thrown during the reading operation from the database
     * @throws ServiceBusinessException if entity was not found in the database
     */
    void update(T t) throws ServiceSystemException, ServiceBusinessException;

    /**
     * Method deletes entity from the database
     *
     * @param id entity ID you need to delete
     * @return amount of deleted entities
     * @throws ServiceSystemException   if {@link DAOSystemException}
     *                                  was thrown during the reading operation from the database
     * @throws ServiceBusinessException if entity was not found in the database
     */
    void delete(long id) throws ServiceSystemException, ServiceBusinessException;
}
