package com.datamigration.jds.util;

import com.datamigration.jds.persistence.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class JDSTest {
	public void truncateDb() {
		try (Connection connection = DatabaseManager.getInstance().connect()) {
			PreparedStatement preparedStatement = connection.prepareStatement(
				ITestSQLs.TRUNCATE_DB);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}