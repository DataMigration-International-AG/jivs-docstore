package com.datamigration.jds.util;

public interface ITestSQLs {

	String TRUNCATE_DB = """
		IF EXISTS (SELECT 1 FROM sys.tables WHERE name = 'JIVS_DOCUMENT_STORE')
			DELETE FROM jds.JIVS_DOCUMENT_STORE;
		IF EXISTS (SELECT 1 FROM sys.tables WHERE name = 'JIVS_DOCUMENT_PARAM')
			DELETE FROM jds.JIVS_DOCUMENT_PARAM;
		""";

	String SELECT_DOCUMENT_SQL = """
		SELECT ID, FILENAME, CREATOR, DELETED
		FROM jds.JIVS_DOCSTORE
		WHERE ID = ?
		""";
}
