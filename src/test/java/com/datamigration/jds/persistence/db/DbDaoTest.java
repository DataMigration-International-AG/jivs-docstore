package com.datamigration.jds.persistence.db;

import com.datamigration.jds.persistence.DatabaseManager;
import com.datamigration.jds.util.ITestSQLs;
import com.datamigration.jds.util.exceptions.checked.JDSPersistenceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.JdbcDatabaseContainer.NoDriverFoundException;

class DbDaoTest {
	@Test
	void afterCreateJDSSchema_thereShouldBeTheSchema() throws NoDriverFoundException, SQLException, JDSPersistenceException {
		DatabaseManager databaseManager = DatabaseManager.getInstance();
		databaseManager.createJDSSchema();
		try (Connection connection = databaseManager.connect()) {
			PreparedStatement preparedStatement = connection.prepareStatement(ITestSQLs.SELECT_JDS_SCHEMA);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				int result = rs.getInt(1);
				Assertions.assertEquals(1, result);
			}
		}
	}
}