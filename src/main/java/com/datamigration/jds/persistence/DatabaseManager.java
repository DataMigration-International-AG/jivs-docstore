package com.datamigration.jds.persistence;


import com.datamigration.jds.persistence.db.DbDao;
import com.datamigration.jds.persistence.db.IDbDao;
import com.datamigration.jds.persistence.docstore.DocumentDao;
import com.datamigration.jds.persistence.docstore.IDocumentDao;
import com.datamigration.jds.persistence.param.DocumentParamDao;
import com.datamigration.jds.persistence.param.IDocumentParamDao;
import com.datamigration.jds.util.exceptions.ErrorCode;
import com.datamigration.jds.util.exceptions.checked.JDSPersistenceException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseManager {

	private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
	private static DatabaseManager INSTANCE;
	private static IDbDao dbDao;
	private static IDocumentDao documentDao;
	private static IDocumentParamDao documentParamDao;
	private final Config config;

	private DatabaseManager() {
			dbDao = new DbDao();
			documentDao = new DocumentDao();
			documentParamDao = new DocumentParamDao();
			config = ConfigProvider.getConfig();
	}

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
	 * @throws JDSPersistenceException if there is an error while establishing the database connection
	 */
	public Connection connect() throws JDSPersistenceException {
		Connection result;
		try {
			String dbUrl = config.getOptionalValue("jds.mssql.url", String.class).orElse(null);
			String dbUsername = config.getOptionalValue("jds.mssql.username", String.class).orElse(null);
			String dbPassword = config.getOptionalValue("jds.mssql.password", String.class).orElse(null);
			if (dbUrl != null && dbUsername != null && dbPassword != null) {
				result = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
				logger.debug("Connected to {}", result.getMetaData().getURL());
			} else {
				logger.error("Missing parameter to create a connection.");
				throw new JDSPersistenceException(ErrorCode.DB_CONFIG_ERROR);
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new JDSPersistenceException(e, ErrorCode.DB_CONNECT_ERROR);
		}
		return result;
	}

	/**
	 * Creates the JDS schema by delegating the call to the dbDao object.
	 *
	 * @throws JDSPersistenceException if there is an error during the creation of the JPE schema
	 */
	public void createJDSSchema() throws JDSPersistenceException {
		dbDao.createJDSSchema();
	}

	/**
	 * createTables - A method to create tables in the database.
	 *
	 * @throws JDSPersistenceException exception thrown if there is an issue with persistence
	 */
	public void createTables() throws JDSPersistenceException {
		documentDao.createTables();
		documentParamDao.createTables();
		logger.debug("Tables created");
	}

	/**
	 * Load configuration and create required tables in the database.
	 *
	 * @throws JDSPersistenceException if an error occurs during the initialization of the database
	 */
	public void initializeDatabase() throws JDSPersistenceException {
		createJDSSchema();
		createTables();
	}
}
