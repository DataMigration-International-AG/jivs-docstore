package com.datamigration.jds.persistence.param;

public interface IDocumentParamSQLs {

	String CREATE_DOCUMENT_PARAMS_TABLE_SQL = """
		IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='[JIVS-DOCUMENT-PARAM]' and xtype='U')
		CREATE TABLE [jds].[JIVS-DOCUMENT-PARAM] (
		    ID            uniqueidentifier default newsequentialid() not null
		        constraint [JIVS-DOCUMENT-PARAM-pk]
		        primary key,
		 	[DOCUMENT-ID] uniqueidentifier
				constraint [JIVS-DOCUMENT-PARAM-JIVS-DOCSTORE-ID-fk]
					references [jds].[JIVS-DOCSTORE] ON DELETE CASCADE,
		    [KEY] nvarchar(100) not null,
		    [VALUE] nvarchar(max) not null
		)
		""";

	String INSERT_PARAMS_SQL = """
		INSERT INTO [jds].[JIVS-DOCUMENT-PARAM] ([DOCUMENT-ID], [KEY], [VALUE])
		OUTPUT INSERTED.ID
		VALUES (?, ?, ?)
		""";

	String SELECT_DOCUMENT_PARAMS_BY_ID = """
		SELECT [KEY], [VALUE]
		FROM [jds].[JIVS-DOCUMENT-PARAM]
		WHERE [DOCUMENT-ID] = ?
		""";

	String DELETE_BY_DOCUMENT_ID_SQL = """
		DELETE FROM [jds].[JIVS-DOCUMENT-PARAM]
		WHERE [DOCUMENT-ID] = ?
		""";
}
