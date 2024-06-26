package com.datamigration.jds.persistence;

import com.datamigration.jds.util.exceptions.checked.JDSPersistenceException;
import java.util.List;
import java.util.Optional;

/**
 * This interface defines the basic CRUD operations for a database entity.
 *
 * @param <T> the type of the database entity
 * @param <U> the type of the ID of the database entity
 */
public interface IDao<T, U> {

	Optional<T> getById(U id) throws JDSPersistenceException;

	/**
	 * Get all entities from the database. Returns an empty list if no entry was found.
	 *
	 * @return a list of all entities
	 * @throws JDSPersistenceException if an error occurs during fetch
	 */
	List<T> getAllAsList() throws JDSPersistenceException;

	/**
	 * Insert the given entity into the database. Returns an entity with an id on success and throws an exception if an
	 * error occurs.
	 *
	 * @param t the entity to insert
	 * @return the inserted entity with an id
	 * @throws JDSPersistenceException if an error occurs during persisting
	 */
	T insert(T t) throws JDSPersistenceException;

	/**
	 * Update the given entity in the database. Throws an exception if an error occurs.
	 *
	 * @param t the entity to update
	 * @throws JDSPersistenceException if an error occurs during persisting
	 */
	void update(T t) throws JDSPersistenceException;
}