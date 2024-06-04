package com.datamigration.jds.persistence.docstore;

public interface IDocumentSQLs {

	String CREATE_DOCSTORE_TABLE_SQL = """
		IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='JIVS_DOCSTORE' and xtype='U')
				CREATE TABLE jds.JIVS_DOCSTORE (
				    ID            uniqueidentifier default newsequentialid() not null
				        constraint JIVS_DOCSTORE_pk
				        primary key,
				 	FILE_BIN UNIQUEIDENTIFIER not null,
				 	FILENAME nvarchar(max) not null,
				 	DOCUMENT_TYPE varchar(40) not null,
				 	CREATOR UNIQUEIDENTIFIER not null,
				    CREATED datetime not null,
				    CUSTOMER_FK UNIQUEIDENTIFIER not null,
				    SYSTEM_FK UNIQUEIDENTIFIER,
				    CASE_ID UNIQUEIDENTIFIER,
				    DELETED bit default 0 not null,
				)
		""";

	String INSERT_DOCUMENT_SQL = """
		INSERT INTO jds.JIVS_DOCSTORE(FILE_BIN, FILENAME, DOCUMENT_TYPE, CREATOR, CREATED, CUSTOMER_FK, SYSTEM_FK, CASE_ID, DELETED)
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
			WHERE DOCUMENT_TYPE = ?
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
			WHERE CUSTOMER_FK = ?
			AND DELETED = 0
		""";

	String SELECT_DOCUMENT_BY_SYSTEM_FK_SQL = """
			SELECT *
			FROM jds.JIVS_DOCSTORE
			WHERE SYSTEM_FK = ?
			AND DELETED = 0
		""";

	String SELECT_DOCUMENT_BY_CASE_ID_SQL = """
			SELECT *
			FROM jds.JIVS_DOCSTORE
			WHERE CASE_ID = ?
			AND DELETED = 0
		""";

	String UPDATE_DOCUMENT_DELETE_FLAG_SQL = """
		UPDATE jds.JIVS_DOCSTORE
		SET DELETED = ?
		WHERE ID = ?
		""";
}
