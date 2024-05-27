package com.datamigration.jds.persistence.docstore;

import com.datamigration.jds.model.docstore.JivsDocStore;
import com.datamigration.jds.persistence.IDao;
import com.datamigration.jds.util.exceptions.checked.JPEPersistenceException;
import java.util.UUID;

public interface IDocStoreDao extends IDao<JivsDocStore, UUID> {
	/**
	 * A method to create tables in the database.
	 *
	 * @throws JPEPersistenceException exception thrown if there is an issue with persistence
	 */
	void createTables() throws JPEPersistenceException;

}
