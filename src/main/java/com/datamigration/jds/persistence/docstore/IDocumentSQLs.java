package com.datamigration.jds.persistence.docstore;

public interface IDocumentSQLs {

	String CREATE_DOCSTORE_TABLE_SQL = """
		IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='JIVS_DOCSTORE' and xtype='U')
		CREATE TABLE jds.JIVS_DOCSTORE (
		    ID            uniqueidentifier default newsequentialid() not null
		        constraint JIVS_DOCSTORE_pk
		        primary key,
		    FILE-BIN varbinary(max),
		    FILE-NAME nvarchar(256) not null,
		    FILE-TYPE nvarchar(40),
		    CREATOR nvarchar(max) not null,
		    CREATED datetime not null,
		    CUSTOMER-FKEY nvarchar(max) not null,
		    SYSTEM-FKEY nvarchar(max),
		    CASE-ID nvarchar(max),
		    PARAM nvarchar(max),
		    DELETED bit default 0 not null
		)
		""";

	String INSERT_DOCUMENT_SQL = """
		INSERT INTO jds.JIVS_DOCSTORE(FILE-BIN, FILE-NAME, FILE-TYPE, CREATOR, CUSTOMER-FKEY, SYSTEM-FKEY, CASE-ID, PARAM, DELETED)
		OUTPUT INSERTED.ID
		VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
		""";

	String DELETE_DOCUMENT_SQL = """
		DELETE FROM jds.JIVS_DOCSTORE
		WHERE ID = ?
		""";
}
