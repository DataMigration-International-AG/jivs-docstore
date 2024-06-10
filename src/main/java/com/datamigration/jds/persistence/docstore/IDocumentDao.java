package com.datamigration.jds.persistence.docstore;

import com.datamigration.jds.model.dto.DocumentDTO;
import com.datamigration.jds.persistence.IDao;
import com.datamigration.jds.util.exceptions.checked.JPEPersistenceException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IDocumentDao extends IDao<DocumentDTO, UUID> {

	/**
	 * A method to create tables in the database.
	 *
	 * @throws JPEPersistenceException exception thrown if there is an issue with persistence
	 */
	void createTables() throws JPEPersistenceException;

	Optional<List<DocumentDTO>> getByDocumentType(String documentType) throws JPEPersistenceException;

	Optional<DocumentDTO> getByFileName(String fileName) throws JPEPersistenceException;

	Optional<List<DocumentDTO>> getByCreator(UUID id) throws JPEPersistenceException;

	Optional<List<DocumentDTO>> getByCreatedAt(LocalDateTime dateTime) throws JPEPersistenceException;

	Optional<List<DocumentDTO>> getByCustomerId(UUID id) throws JPEPersistenceException;

	Optional<List<DocumentDTO>> getBySystemId(UUID id) throws JPEPersistenceException;

	Optional<List<DocumentDTO>> getByCaseId(UUID id) throws JPEPersistenceException;

	/**
	 * Soft delete, sets the deleted flag of  the entity by the id in the database.
	 * @param id the id of the entity
	 * @throws JPEPersistenceException if an error occurs during persisting
	 */
	boolean updateDeleteFlag(UUID id) throws JPEPersistenceException;
}
