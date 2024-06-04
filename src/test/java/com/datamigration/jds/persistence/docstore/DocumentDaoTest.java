package com.datamigration.jds.persistence.docstore;

import com.datamigration.jds.model.docstore.JivsDocument;
import com.datamigration.jds.model.dto.DocumentDTO;
import com.datamigration.jds.persistence.DatabaseManager;
import com.datamigration.jds.util.BaseSingletonTest;
import com.datamigration.jds.util.DTOUtil;
import com.datamigration.jds.util.ITestSQLs;
import com.datamigration.jds.util.exceptions.checked.JPEPersistenceException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.JdbcDatabaseContainer.NoDriverFoundException;

class DocumentDaoTest extends BaseSingletonTest {

	private static final IDocumentDao  documentDao = new DocumentDao();


	@AfterEach
	public void afterEach() {
		truncateDb();
	}

	private JivsDocument createDocument() {
		return new JivsDocument("Document".getBytes(StandardCharsets.UTF_8), "Document", "Completeness",
			UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), null, false);
	}

	@Test
	void createTables() {
	}

	@Test
	void afterInsertDocumentStore_thereShouldBeADocument()
		throws JPEPersistenceException, NoDriverFoundException, SQLException {

		JivsDocument jivsDocument = createDocument();
		DocumentDTO documentDTO = DTOUtil.toDocumentDTO(jivsDocument);
		DocumentDTO insertedDocumentDTO = documentDao.insert(documentDTO);
		Assertions.assertNotNull(insertedDocumentDTO);
		try (Connection connection = DatabaseManager.getInstance().connect()) {
			PreparedStatement preparedStatement = connection.prepareStatement(ITestSQLs.SELECT_DOCUMENT_SQL);
			preparedStatement.setObject(1, insertedDocumentDTO.id());
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				UUID dbId = UUID.fromString(rs.getString(1));
				String fileName = rs.getString(2);
				UUID creator = UUID.fromString(rs.getString(3));
				boolean deleted = rs.getBoolean(4);

				Assertions.assertEquals(insertedDocumentDTO.id(), dbId);
				Assertions.assertEquals(insertedDocumentDTO.fileName(), fileName);
				Assertions.assertEquals(insertedDocumentDTO.creator(), creator);
				Assertions.assertEquals(insertedDocumentDTO.deleted(), deleted);
			}
		}
	}

	@Test
	void getById() {
	}

	@Test
	void getByDocumentType() {
	}

	@Test
	void getByFileName() {
	}

	@Test
	void getByCreator() {
	}

	@Test
	void getByCreatedAt() {
	}

	@Test
	void getByCustomerId() {
	}

	@Test
	void getBySystemId() {
	}

	@Test
	void getByCaseId() {
	}

	@Test
	void getAllAsList() {
	}

	@Test
	void update() {
	}

	@Test
	void updateParams() {
	}

	@Test
	void updateDeleteFlag() {
	}
}