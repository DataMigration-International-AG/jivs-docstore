package com.datamigration.jds.persistence.db;

import static com.datamigration.jds.persistence.DatabaseManager.connect;

import com.datamigration.jds.util.exceptions.ErrorCode;
import com.datamigration.jds.util.exceptions.checked.JDSPersistenceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbDao implements IDbDao {

	@Override
	public void createJDSSchema() throws JDSPersistenceException {
		try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDbSQLs.CREATE_JDS_SCHEMA_SQL)) {
			preparedStatement.execute();
		} catch (SQLException | JDSPersistenceException e) {
			throw new JDSPersistenceException(e, ErrorCode.DB_CREATE_ERROR);
		}
	}
}
