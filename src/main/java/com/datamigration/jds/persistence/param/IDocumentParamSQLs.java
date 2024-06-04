package com.datamigration.jds.persistence.param;

public interface IDocumentParamSQLs {

	String CREATE_DOCUMENT_PARAMS_TABLE_SQL = """
		IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='JIVS_DOCUMENT_PARAM' and xtype='U')
		CREATE TABLE jds.JIVS_DOCUMENT_PARAM (
		    ID            uniqueidentifier default newsequentialid() not null
		        constraint JIVS_DOCUMENT_PARAM_pk
		        primary key,
		 	DOCUMENT_ID uniqueidentifier
				constraint JIVS_DOCUMENT_PARAM_JIVS_DOCSTORE_ID_fk
					references jds.JIVS_DOCSTORE,
		    [KEY] nvarchar(100) not null,
		    [VALUE] nvarchar(max) not null
		)
		""";

	String INSERT_PARAMS_SQL = """
		INSERT INTO jds.JIVS_DOCUMENT_PARAM(DOCUMENT_ID, KEY, VALUE)
		OUTPUT INSERTED.ID
		VALUES (?, ?, ?)
		""";

	String SELECT_DOCUMENT_PARAMS_BY_ID = """
		SELECT KEY, VALUE
		FROM jds.JIVS_DOCUMENT_PARAM
		WHERE DOCUMENT_ID = ?
		""";

	String DELETE_BY_DOCUMENT_ID_SQL = """
		DELETE FROM jds.JIVS_DOCUMENT_PARAM
		WHERE ID = ?
		""";
}
