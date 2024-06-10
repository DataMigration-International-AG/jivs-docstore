package com.datamigration.jds.persistence.param;

import com.datamigration.jds.model.entity.docstoreparam.JivsDocumentParam;
import com.datamigration.jds.persistence.IDao;
import com.datamigration.jds.util.exceptions.checked.JDSPersistenceException;
import java.util.Map;
import java.util.UUID;

public interface IDocumentParamDao extends IDao<JivsDocumentParam, UUID> {

	void createTables() throws JDSPersistenceException;

	/**
	 * Updates the params of the entity by the id in the database. Throws an exception if an error occurs.
	 *
	 * @param id     the id of the entity
	 * @param params the params of the entity
	 * @return param map
	 * @throws JDSPersistenceException if an error occurs during persisting
	 */
	Map<String, String> updateParams(UUID id, Map<String, String> params) throws JDSPersistenceException;

	Map<String, String> getParams(UUID id) throws JDSPersistenceException;
}
