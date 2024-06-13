package com.datamigration.jds.persistence.docstore;

import com.datamigration.jds.model.entity.docstore.JivsDocument;
import com.datamigration.jds.persistence.IDao;
import com.datamigration.jds.util.exceptions.checked.JDSPersistenceException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IDocumentDao extends IDao<JivsDocument, UUID> {

	/**
	 * A method to create tables in the database.
	 *
	 * @throws JDSPersistenceException exception thrown if there is an issue with persistence
	 */
	void createTables() throws JDSPersistenceException;

	List<JivsDocument> getByDocumentTypeAsList(String documentType) throws JDSPersistenceException;

	Optional<JivsDocument> getByFileName(String fileName) throws JDSPersistenceException;

	List<JivsDocument> getByCreatorAsList(UUID id) throws JDSPersistenceException;

	List<JivsDocument> getByCreatedAtAsList(LocalDateTime dateTime) throws JDSPersistenceException;

	List<JivsDocument> getByCustomerIdAsList(UUID id) throws JDSPersistenceException;

	List<JivsDocument> getBySystemIdAsList(UUID id) throws JDSPersistenceException;

	List<JivsDocument> getByCaseIdAsList(UUID id) throws JDSPersistenceException;

	/**
	 * Soft delete, sets the deleted flag of  the entity by the id in the database.
	 * @param id the id of the entity
	 * @throws JDSPersistenceException if an error occurs during persisting
	 */
	boolean setDeleteFlagTrue(UUID id) throws JDSPersistenceException;
}
