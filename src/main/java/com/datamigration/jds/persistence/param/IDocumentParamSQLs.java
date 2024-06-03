package com.datamigration.jds.persistence.param;

public interface IDocumentParamSQLs {

	String CREATE_DOCUMENT_PARAMS_TABLE_SQL = """
		IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='JIVS_DOCSTORE_PARAM' and xtype='U')
				CREATE TABLE jds.JIVS_ARAM (
				    ID            uniqueidentifier default newsequentialid() not null
				        constraint JIVS_DOCSTORE_PARAM_pk
				        primary key,
				 	DOCUMENT-ID UNIQUEIDENTIFIER
						constraint JIVS_DOCSTORE_PARAM_ID_fk
									references jpe.JIVS_DOCSTORE,
				 	KEY nvarchar(100) not null,
				 	KEY nvarchar(max) not null
				)
		""";

	String INSERT_PARAMS_SQL = """
		INSERT INTO jds.JIVS_PARAM(DOCUMENT-ID, KEY, VALUE)
		OUTPUT INSERTED.ID
		VALUES (?, ?, ?)
		""";

	String DELETE_BY_DOCUMENT_ID_SQL = """
		DELETE FROM jds.JIVS_DOCSTORE_PARAM
		WHERE ID = ?
		""";
}
