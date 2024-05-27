package com.datamigration.jds.persistence.db;

import com.datamigration.jds.persistence.AbstractDatabaseManager;
import com.datamigration.jds.util.exceptions.ErrorCode;
import com.datamigration.jds.util.exceptions.checked.JPEPersistenceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbDao extends AbstractDatabaseManager implements IDbDao {

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
