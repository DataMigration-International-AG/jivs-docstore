package com.datamigration.jds.persistence.docstore;

import com.datamigration.jds.model.dto.DocumentDTO;
import com.datamigration.jds.persistence.IDao;
import com.datamigration.jds.util.exceptions.checked.JDSPersistenceException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IDocumentDao extends IDao<DocumentDTO, UUID> {

	/**
	 * A method to create tables in the database.
	 *
	 * @throws JDSPersistenceException exception thrown if there is an issue with persistence
	 */
	void createTables() throws JDSPersistenceException;

	Optional<List<DocumentDTO>> getByDocumentType(String documentType) throws JDSPersistenceException;

	Optional<DocumentDTO> getByFileName(String fileName) throws JDSPersistenceException;

	Optional<List<DocumentDTO>> getByCreator(UUID id) throws JDSPersistenceException;

	Optional<List<DocumentDTO>> getByCreatedAt(LocalDateTime dateTime) throws JDSPersistenceException;

	Optional<List<DocumentDTO>> getByCustomerId(UUID id) throws JDSPersistenceException;

	Optional<List<DocumentDTO>> getBySystemId(UUID id) throws JDSPersistenceException;

	Optional<List<DocumentDTO>> getByCaseId(UUID id) throws JDSPersistenceException;

	/**
	 * Soft delete, sets the deleted flag of  the entity by the id in the database.
	 * @param id the id of the entity
	 * @throws JDSPersistenceException if an error occurs during persisting
	 */
	boolean setDeleteFlagTrue(UUID id) throws JDSPersistenceException;
}
