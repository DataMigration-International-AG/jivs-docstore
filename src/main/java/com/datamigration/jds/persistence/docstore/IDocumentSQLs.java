package com.datamigration.jds.persistence.docstore;

public interface IDocumentSQLs {

String CREATE_DOCSTORE_TABLE_SQL = """
		IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='JIVS_DOCSTORE' and xtype='U')
				CREATE TABLE jds.JIVS_DOCSTORE (
				    ID            uniqueidentifier default newsequentialid() not null
				        constraint JIVS_DOCSTORE_pk
				        primary key,
				 	FILE-BIN UNIQUEIDENTIFIER not null,
				 	FILENAME nvarchar(max) not null,
				 	DOCUMENT-TYPE varchar(40) not null,
				 	CREATOR UNIQUEIDENTIFIER not null,
				    CREATED datetime not null,
				    CUSTOMER-FK UNIQUEIDENTIFIER not null,
				    SYSTEM-FK UNIQUEIDENTIFIER,
				    CASE-ID UNIQUEIDENTIFIER,
				    PARAM nvarchar(max) not null,
				    DELETED bit default 0 not null,
				)
		""";

	String INSERT_DOCUMENT_SQL = """
		INSERT INTO jds.JIVS_DOCSTORE(FILE-BIN, FILE-NAME, FILE-TYPE, CREATOR, CUSTOMER-FKEY, SYSTEM-FKEY, CASE-ID, PARAM, DELETED)
		OUTPUT INSERTED.ID
		VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
		""";

	String SELECT_DOCUMENT_BY_ID = """
		SELECT *
		FROM jds.JIVS_DOCSTORE
		WHERE ID = ?
		""";

	String SELECT_ALL_DOCUMENTS_SQL = """
		SELECT *
		FROM jds.JIVS_DOCSTORE
		""";

	String SELECT_DOCUMENT_BY_ID_SQL = """
			SELECT *
			FROM jds.JIVS_DOCSTORE
			WHERE ID = ?
			AND DELETED = 0
		""";

	String SELECT_DOCUMENT_BY_FILENAME_SQL = """
			SELECT *
			FROM jds.JIVS_DOCSTORE
			WHERE FILENAME = ?
			AND DELETED = 0
		""";

	String SELECT_DOCUMENT_BY_DOCUMENT_TYPE_SQL = """
			SELECT *
			FROM jds.JIVS_DOCSTORE
			WHERE DOCUMENT-TYPE = ?
			AND DELETED = 0
		""";

	String SELECT_DOCUMENT_BY_CREATOR_SQL = """
			SELECT *
			FROM jds.JIVS_DOCSTORE
			WHERE CREATOR = ?
			AND DELETED = 0
		""";

	String SELECT_DOCUMENT_BY_CREATED_SQL = """
			SELECT *
			FROM jds.JIVS_DOCSTORE
			WHERE CREATED = ?
			AND DELETED = 0
		""";

	String SELECT_DOCUMENT_BY_CUSTOMER_FK_SQL = """
			SELECT *
			FROM jds.JIVS_DOCSTORE
			WHERE CUSTOMER-FK = ?
			AND DELETED = 0
		""";

	String SELECT_DOCUMENT_BY_SYSTEM_FK_SQL = """
			SELECT *
			FROM jds.JIVS_DOCSTORE
			WHERE SYSTEM-FK = ?
			AND DELETED = 0
		""";

	String SELECT_DOCUMENT_BY_CASE_ID_SQL = """
			SELECT *
			FROM jds.JIVS_DOCSTORE
			WHERE CASE-ID = ?
			AND DELETED = 0
		""";

	String UPDATE_DOCUMENT_DELETE_FLAG_SQL = """
		UPDATE jds.JIVS_DOCSTORE
		SET DELETED = ?
		WHERE ID = ?
		""";

	String UPDATE_DOCUMENT_PARAM_SQL = """
		UPDATE jds.JIVS_DOCSTORE
		SET PARAM = ?
		WHERE ID = ?
		""";
}
