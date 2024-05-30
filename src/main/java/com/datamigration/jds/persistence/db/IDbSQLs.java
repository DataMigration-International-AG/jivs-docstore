package com.datamigration.jds.persistence.db;

public interface IDbSQLs {

	String CREATE_JDS_SCHEMA = """
			IF NOT EXISTS (SELECT 1 FROM sys.schemas WHERE name = 'jds')
			BEGIN
				EXEC('CREATE SCHEMA jds');
			END
		""";
}
