package com.datamigration.jds;

import com.datamigration.jds.persistence.DatabaseManager;
import com.datamigration.jds.util.DatabaseConfig;
import com.datamigration.jds.util.exceptions.checked.JPEException;
import com.datamigration.jds.util.exceptions.checked.JPEPersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JivsDocStore {

	private static final Logger logger = LoggerFactory.getLogger(JivsDocStore.class);

	/**
	 * Load configuration and create required tables in the database.
	 *
	 * @param configPath path to the configuration file
	 * @throws JPEPersistenceException if an error occurs during the initialization of the database
	 */
	private static void initializeDatabase(String configPath) throws JPEPersistenceException {
		DatabaseConfig.getInstance(configPath);
		DatabaseManager databaseManager = DatabaseManager.getInstance();
		databaseManager.createJDSSchema();
		databaseManager.createTables();
	}

	public static void main(String[] args) {
		try {
			initializeDatabase("config.properties");
		} catch (JPEException e) {
			logger.error("Error running main class", e);
		}
	}
}
