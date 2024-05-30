package com.datamigration.jds.persistence.docstore;

import com.datamigration.jds.model.dto.DocumentDTO;
import com.datamigration.jds.persistence.IDao;
import com.datamigration.jds.util.exceptions.checked.JPEPersistenceException;
import java.util.UUID;

public interface IDocumentDao extends IDao<DocumentDTO, UUID> {

	/**
	 * A method to create tables in the database.
	 *
	 * @throws JPEPersistenceException exception thrown if there is an issue with persistence
	 */
	void createTables() throws JPEPersistenceException;

	/**
	 * Deletes the entity by the id in the database. Throws an exception if an error occurs.
	 *
	 * @param id the id of the entity
	 * @throws JPEPersistenceException if an error occurs during persisting
	 */
	boolean delete(UUID id) throws JPEPersistenceException;
}
