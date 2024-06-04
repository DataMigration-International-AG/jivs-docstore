package com.datamigration.jds.persistence.param;

import com.datamigration.jds.model.dto.DocumentDTO;
import com.datamigration.jds.persistence.IDao;
import com.datamigration.jds.util.exceptions.checked.JPEPersistenceException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface IDocumentParamDao extends IDao<DocumentDTO, UUID> {

	void createTables() throws JPEPersistenceException;

	/**
	 * Updates the params of the entity by the id in the database. Throws an exception if an error occurs.
	 *
	 * @param id     the id of the entity
	 * @param params the params of the entity
	 * @throws JPEPersistenceException if an error occurs during persisting
	 */
	void updateParams(UUID id, Map<String, String> params) throws JPEPersistenceException;

	Optional<Map<String, String>> getParams(UUID id) throws JPEPersistenceException;
}
