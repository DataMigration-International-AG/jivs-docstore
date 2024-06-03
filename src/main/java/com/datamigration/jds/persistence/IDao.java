package com.datamigration.jds.persistence;

import com.datamigration.jds.util.exceptions.checked.JPEPersistenceException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * This interface defines the basic CRUD operations for a database entity.
 *
 * @param <T> the type of the database entity
 * @param <U> the type of the ID of the database entity
 */
public interface IDao<T, U> {

	Optional<T> getById(U id) throws JPEPersistenceException;

	Optional<List<T>> getByDocumentType(String documentType) throws JPEPersistenceException;

	Optional<T> getByFileName(String fileName) throws JPEPersistenceException;

	Optional<List<T>> getByCreator(UUID id) throws JPEPersistenceException;

	Optional<List<T>> getByCreatedAt(LocalDateTime dateTime) throws JPEPersistenceException;

	Optional<List<T>> getByCustomerId(UUID id) throws JPEPersistenceException;

	Optional<List<T>> getBySystemId(UUID id) throws JPEPersistenceException;

	Optional<List<T>> getByCaseId(UUID id) throws JPEPersistenceException;

	/**
	 * Get all entities from the database. Returns an empty list if no entry was found.
	 *
	 * @return a list of all entities
	 * @throws JPEPersistenceException if an error occurs during fetch
	 */
	Optional<List<T>> getAllAsList() throws JPEPersistenceException;

	/**
	 * Insert the given entity into the database. Returns an entity with an id on success and throws an exception if an
	 * error occurs.
	 *
	 * @param t the entity to insert
	 * @return the inserted entity with an id
	 * @throws JPEPersistenceException if an error occurs during persisting
	 */
	T insert(T t) throws JPEPersistenceException;

	/**
	 * Update the given entity in the database. Throws an exception if an error occurs.
	 *
	 * @param t the entity to update
	 * @throws JPEPersistenceException if an error occurs during persisting
	 */
	void update(T t) throws JPEPersistenceException;
}