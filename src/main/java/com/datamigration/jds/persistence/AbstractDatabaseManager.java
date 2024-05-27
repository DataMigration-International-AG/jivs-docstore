package com.datamigration.jds.persistence;


import com.datamigration.jds.util.DatabaseConfig;
import com.datamigration.jds.util.exceptions.ErrorCode;
import com.datamigration.jds.util.exceptions.checked.JPEPersistenceException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractDatabaseManager {

	private static final Logger logger = LoggerFactory.getLogger(AbstractDatabaseManager.class);
	private final DatabaseConfig databaseConfig;

	protected AbstractDatabaseManager() {
		databaseConfig = DatabaseConfig.getInstance();
	}

	/**
	 * This function establishes a connection to the database using the provided database configuration.
	 *
	 * @return Connection object representing the established database connection
	 * @throws JPEPersistenceException if there is an error while establishing the database connection
	 */
	public Connection connect() throws JPEPersistenceException {
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

}
