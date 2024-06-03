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
	 * Updates the params of the entity by the id in the database. Throws an exception if an error occurs.
	 *
	 * @param id the id of the entity
	 * @param params the params of the entity
	 * @throws JPEPersistenceException if an error occurs during persisting
	 */
	boolean updateParams(UUID id, String params) throws JPEPersistenceException;

	/**
	 * Deletes the entity by the id in the database. Throws an exception if an error occurs.
	 *
	 * @param id the id of the entity
	 * @throws JPEPersistenceException if an error occurs during persisting
	 */
	boolean updateDeleteFlag(UUID id) throws JPEPersistenceException;
}
