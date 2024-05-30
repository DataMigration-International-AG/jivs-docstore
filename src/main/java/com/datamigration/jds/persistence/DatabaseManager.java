package com.datamigration.jds.persistence;


import com.datamigration.jds.persistence.db.DbDao;
import com.datamigration.jds.persistence.db.IDbDao;
import com.datamigration.jds.util.DatabaseConfig;
import com.datamigration.jds.util.exceptions.ErrorCode;
import com.datamigration.jds.util.exceptions.checked.JPEPersistenceException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseManager {

	private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
	private static final DatabaseConfig databaseConfig = DatabaseConfig.getInstance();
	private static final IDbDao dbDao = new DbDao();
	private static DatabaseManager INSTANCE;

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
	 * This function establishes a connection to the database using the provided database configuration.
	 *
	 * @return Connection object representing the established database connection
	 * @throws JPEPersistenceException if there is an error while establishing the database connection
	 */
	public static Connection connect() throws JPEPersistenceException {
		Connection result;
		try {
			if (databaseConfig.getDbUrl() != null && databaseConfig.getDbUsername() != null
				&& databaseConfig.getDbPassword() != null) {
				result = DriverManager.getConnection(databaseConfig.getDbUrl(), databaseConfig.getDbUsername(),
					databaseConfig.getDbPassword());
				logger.debug("Connected to {}", result.getMetaData().getURL());
			} else {
				logger.error("{} : dbUrl={}, dbUser={}, pwdWasSet={}", ErrorCode.DB_CONFIG_ERROR.getTitle(),
					databaseConfig.getDbUrl(), databaseConfig.getDbUsername(), databaseConfig.getDbPassword() != null);
				throw new JPEPersistenceException(ErrorCode.DB_CONFIG_ERROR);
			}
		} catch (SQLException e) {
			throw new JPEPersistenceException(e, ErrorCode.DB_CONNECT_ERROR);
		}
		return result;
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
