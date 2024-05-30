package com.datamigration.jds.persistence.db;

import static com.datamigration.jds.persistence.DatabaseManager.connect;

import com.datamigration.jds.util.exceptions.ErrorCode;
import com.datamigration.jds.util.exceptions.checked.JPEPersistenceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbDao implements IDbDao {

	@Override
	public void createJPESchema() throws JPEPersistenceException {
		try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDbSQLs.CREATE_JDS_SCHEMA)) {
			preparedStatement.execute();
		} catch (SQLException | JPEPersistenceException e) {
			throw new JPEPersistenceException(e, ErrorCode.DB_CREATE_ERROR);
		}
	}
}
