package com.datamigration.jds;

import com.datamigration.jds.persistence.DatabaseManager;
import com.datamigration.jds.util.DatabaseConfig;
import com.datamigration.jds.util.exceptions.checked.JDSException;
import com.datamigration.jds.util.exceptions.checked.JDSPersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JivsDocStore {

	private static final Logger logger = LoggerFactory.getLogger(JivsDocStore.class);

	/**
	 * Load configuration and create required tables in the database.
	 *
	 * @param configPath path to the configuration file
	 * @throws JDSPersistenceException if an error occurs during the initialization of the database
	 */
	public static void initializeDatabase(String configPath) throws JDSPersistenceException {
		DatabaseConfig.getInstance(configPath);
		DatabaseManager databaseManager = DatabaseManager.getInstance();
		databaseManager.createJDSSchema();
		databaseManager.createTables();
	}

	public static void main(String[] args) {
		try {
			initializeDatabase("config.properties");
		} catch (JDSException e) {
			logger.error("Error running main class", e);
		}
	}
}
