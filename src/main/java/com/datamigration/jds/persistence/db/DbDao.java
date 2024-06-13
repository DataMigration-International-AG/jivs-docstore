package com.datamigration.jds.persistence.db;

import com.datamigration.jds.persistence.DatabaseManager;
import com.datamigration.jds.util.exceptions.ErrorCode;
import com.datamigration.jds.util.exceptions.checked.JDSPersistenceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbDao implements IDbDao {

	private static final Logger logger = LoggerFactory.getLogger(DbDao.class);

	@Override
	public void createJDSSchema() throws JDSPersistenceException {
		DatabaseManager databaseManager = DatabaseManager.getInstance();
		try (Connection connection = databaseManager.connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDbSQLs.CREATE_JDS_SCHEMA_SQL)) {
			preparedStatement.execute();
			logger.info("JDS Schema created");
		} catch (SQLException | JDSPersistenceException e) {
			logger.error(e.getMessage(), e);
			throw new JDSPersistenceException(e, ErrorCode.DB_CREATE_ERROR);
		}
	}
}
