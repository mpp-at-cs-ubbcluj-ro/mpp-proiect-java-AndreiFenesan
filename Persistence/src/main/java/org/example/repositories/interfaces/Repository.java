package org.example.repositories.interfaces;



import org.models.Entity;

import java.util.Optional;

public interface Repository<ID, T extends Entity<ID>> {
    /**
     *
     * @param entity entity to save
     * @return the saved entity
     */
    Optional<T> save(T entity);

    /**
     *
     * @param id of the entity to delete
     * @return the deleted entity
     */
    Optional<T> delete(ID id);

    /**
     * updates the entity
     * @param entity the entity to update
     * @return the old entity
     */
    Optional<T> update(T entity);

    /**
     *
     * @return all entities
     */
    Iterable<T> findAll();

    /**
     *
     * @param id search value
     * @return the entity with id
     */
    Optional<T> findOneById(ID id);

}
