package com.datamigration.jds.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to load the database properties
 */
public class DatabaseConfig {

	private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class.getName());
	private static DatabaseConfig INSTANCE;
	private Properties dbProperties;
	private String configPath;
	private String dbUsername;
	private String dbPassword;
	private String dbUrl;

	private DatabaseConfig(String configPath) {
		this.configPath = configPath;
		loadProperties();
	}

	private DatabaseConfig(String dbUsername, String dbPassword, String dbUrl) {
		this.dbUsername = dbUsername;
		this.dbPassword = dbPassword;
		this.dbUrl = dbUrl;
	}

	/**
	 * Loads the database properties
	 *
	 * @param configPath path of the config file
	 */
	public static void getInstance(String configPath) {
		if (INSTANCE == null) {
			INSTANCE = new DatabaseConfig(configPath);
		}
	}

	/**
	 * Loads the database properties without a config.properties
	 *
	 * @param dbUsername username of the database
	 * @param dbPassword password of the database
	 * @param dbUrl      url of the database
	 */
	public static void getInstanceWithParameters(String dbUsername, String dbPassword, String dbUrl) {
		if (INSTANCE == null) {
			INSTANCE = new DatabaseConfig(dbUsername, dbPassword, dbUrl);
		}
	}

	public static DatabaseConfig getInstance() {
		if (INSTANCE == null) {
			getInstance("config.properties");
		}
		return INSTANCE;
	}

	/**
	 * Resets the instance to null, so it can be reinitialized again.
	 */
	public static void reset() {
		INSTANCE = null;
	}

	/**
	 * Loads the database properties of a given config file
	 */
	private void loadProperties() {
		ClassLoader classLoader = DatabaseConfig.class.getClassLoader();
		try (InputStream inputStream = classLoader.getResourceAsStream(configPath)) {
			dbProperties = new Properties();
			dbProperties.load(inputStream);
		} catch (IOException e) {
			logger.error(e.getMessage());
			return;
		}
		dbUsername = dbProperties.getProperty("jpe.mssql.username");
		dbPassword = dbProperties.getProperty("jpe.mssql.password");
		dbUrl = dbProperties.getProperty("jpe.mssql.url");

		if ((dbUsername == null || dbUsername.isEmpty()) || (dbPassword == null || dbPassword.isEmpty()) || (
			dbUrl == null || dbUrl.isEmpty())) {
			logger.error("Jivs-Process-Engine: Database properties not defined correctly!");
			throw new RuntimeException("Jivs-Process-Engine: Database properties not defined correctly!");
		}
	}

	public String getDbUsername() {
		return dbUsername;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public String getDbUrl() {
		return dbUrl;
	}
}
