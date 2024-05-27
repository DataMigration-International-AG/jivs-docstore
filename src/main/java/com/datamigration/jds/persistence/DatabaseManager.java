package com.datamigration.jds.persistence;


import com.datamigration.jds.persistence.db.DbDao;
import com.datamigration.jds.persistence.db.IDbDao;
import com.datamigration.jds.util.exceptions.checked.JPEPersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseManager extends AbstractDatabaseManager {

	private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
	private static DatabaseManager INSTANCE;
	private static final IDbDao dbDao = new DbDao();

	/**
	 * A description of the entire Java function.
	 *
	 * @return description of return value
	 */
	public static DatabaseManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DatabaseManager();
		}
		return INSTANCE;
	}

	/**
	 * Resets the value of the INSTANCE variable to null.
	 */
	public static void reset() {
		INSTANCE = null;
	}

	/**
	 * Creates the JPE schema by delegating the call to the dbDao object.
	 *
	 * @throws JPEPersistenceException if there is an error during the creation of the JPE schema
	 */
	public void createJPESchema() throws JPEPersistenceException {
		dbDao.createJPESchema();
	}

	/**
	 * createTables - A method to create tables in the database.
	 *
	 * @throws JPEPersistenceException exception thrown if there is an issue with persistence
	 */
	public void createTables() throws JPEPersistenceException {
		logger.debug("Tables created");
	}

}
