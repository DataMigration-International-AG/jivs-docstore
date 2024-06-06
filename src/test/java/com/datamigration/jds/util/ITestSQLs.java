package com.datamigration.jds.util;

public interface ITestSQLs {

	String TRUNCATE_DB = """
		IF EXISTS (SELECT 1 FROM sys.tables WHERE name = '[JIVS-DOCSTORE]')
			DELETE FROM [jds].[JIVS-DOCSTORE];
		IF EXISTS (SELECT 1 FROM sys.tables WHERE name = '[JIVS-DOCUMENT-PARAM]')
			DELETE FROM [jds].[JIVS-DOCUMENT-PARAM];
		""";

	String SELECT_DOCUMENT_SQL = """
		SELECT ID, FILENAME, CREATOR, DELETED
		FROM [jds].[JIVS-DOCSTORE]
		WHERE ID = ?
		""";

	String SELECT_ALL_DOCUMENT_PARAMS_SQL = """
		SELECT [DOCUMENT-ID], [KEY], [VALUE]
		FROM [jds].[JIVS-DOCUMENT-PARAM]
		WHERE [DOCUMENT-ID] = ?
		""";
}
