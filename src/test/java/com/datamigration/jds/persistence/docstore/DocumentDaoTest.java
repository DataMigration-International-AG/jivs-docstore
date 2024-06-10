package com.datamigration.jds.persistence.docstore;

import com.datamigration.jds.model.entity.docstore.JivsDocument;
import com.datamigration.jds.model.entity.docstoreparam.JivsDocumentParam;
import com.datamigration.jds.persistence.DatabaseManager;
import com.datamigration.jds.persistence.param.DocumentParamDao;
import com.datamigration.jds.persistence.param.IDocumentParamDao;
import com.datamigration.jds.util.BaseSingletonTest;
import com.datamigration.jds.util.ITestSQLs;
import com.datamigration.jds.util.exceptions.checked.JDSPersistenceException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.JdbcDatabaseContainer.NoDriverFoundException;

class DocumentDaoTest extends BaseSingletonTest {

	private static final IDocumentDao  documentDao = new DocumentDao();
	private static final IDocumentParamDao documentParamDao = new DocumentParamDao();
	private final String DOCUMENT_TYPE = "Completeness";
	private final UUID creatorId = UUID.randomUUID();
	private final UUID customerId = UUID.randomUUID();
	private final UUID systemId = UUID.randomUUID();

	@AfterEach
	public void afterEach() {
		truncateDb();
	}

	private JivsDocument createDocument() {
		return new JivsDocument("Document".getBytes(StandardCharsets.UTF_8), "Document1", DOCUMENT_TYPE,
			creatorId, customerId, systemId, UUID.randomUUID(), null);
	}

	private JivsDocument createDocumentWithParams() {
		Map<String, String> params = new HashMap<>();
		params.put("paramKey1", "paramValue1");
		params.put("paramKey2", "paramValue2");

		return new JivsDocument("Document".getBytes(StandardCharsets.UTF_8), "Document2", DOCUMENT_TYPE,
			creatorId, customerId, systemId, UUID.randomUUID(), params);
	}

	@Test
	void afterInsertDocument_thereShouldBeADocument()
		throws JDSPersistenceException, NoDriverFoundException, SQLException {

		JivsDocument jivsDocument = createDocument();
		JivsDocument insertedDocument = documentDao.insert(jivsDocument);
		Assertions.assertNotNull(insertedDocument);
		try (Connection connection = DatabaseManager.getInstance().connect()) {
			PreparedStatement preparedStatement = connection.prepareStatement(ITestSQLs.SELECT_DOCUMENT_BY_ID_SQL);
			preparedStatement.setObject(1, insertedDocument.getId());
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				UUID dbId = UUID.fromString(rs.getString(1));
				String fileName = rs.getString(2);
				UUID creator = UUID.fromString(rs.getString(3));
				boolean deleted = rs.getBoolean(4);

				Assertions.assertEquals(insertedDocument.getId(), dbId);
				Assertions.assertEquals(insertedDocument.getFilename(), fileName);
				Assertions.assertEquals(insertedDocument.getCreatorId(), creator);
				Assertions.assertFalse(deleted);
			}
		}
	}

	@Test
	void afterInsertDocumentParams_thereShouldBeADocumentParams()
		throws JDSPersistenceException, NoDriverFoundException, SQLException {

		JivsDocument jivsDocument = createDocumentWithParams();
		JivsDocument insertedDocument = documentDao.insert(jivsDocument);
		Assertions.assertNotNull(insertedDocument);
		try (Connection connection = DatabaseManager.getInstance().connect()) {
			PreparedStatement preparedStatement = connection.prepareStatement(ITestSQLs.SELECT_DOCUMENT_BY_ID_SQL);
			preparedStatement.setObject(1, insertedDocument.getId());
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				UUID dbId = UUID.fromString(rs.getString(1));
				String fileName = rs.getString(2);
				UUID creatorId = UUID.fromString(rs.getString(3));
				boolean deleted = rs.getBoolean(4);

				Assertions.assertEquals(insertedDocument.getId(), dbId);
				Assertions.assertEquals(insertedDocument.getFilename(), fileName);
				Assertions.assertEquals(insertedDocument.getCreatorId(), creatorId);
				Assertions.assertFalse(deleted);
			}
		}

		documentParamDao.insert(new JivsDocumentParam(insertedDocument.getId(), jivsDocument.getParams()));

		try (Connection connection = DatabaseManager.getInstance().connect()) {
			PreparedStatement preparedStatement = connection.prepareStatement(ITestSQLs.SELECT_ALL_DOCUMENT_PARAMS_BY_DOCUMENT_ID_SQL);
			preparedStatement.setObject(1, insertedDocument.getId());
			ResultSet rs = preparedStatement.executeQuery();
			Map<String, String> dbParams = new HashMap<>();
			UUID dbId = null;
			while (rs.next()) {
				dbId = UUID.fromString(rs.getString(1));
				dbParams.put(rs.getString(2), rs.getString(3));
			}

			Assertions.assertEquals(insertedDocument.getId(), dbId);
			Assertions.assertEquals(jivsDocument.getParams().size(), dbParams.size());
			Assertions.assertEquals(true, dbParams.containsKey("paramKey1"));
			Assertions.assertEquals(true, dbParams.containsKey("paramKey2"));
		}
	}

	@Test
	void afterGetByFileName_thereShouldBeTheDocumentWithTheFileName() throws JDSPersistenceException, SQLException {
		JivsDocument jivsDocument = createDocumentWithParams();
		JivsDocument insertedDocument = documentDao.insert(jivsDocument);
		Assertions.assertNotNull(insertedDocument);

		try (Connection connection = DatabaseManager.getInstance().connect()) {
			PreparedStatement preparedStatement = connection.prepareStatement(ITestSQLs.SELECT_DOCUMENT_BY_FILE_NAME_SQL);
			preparedStatement.setObject(1, insertedDocument.getFilename());
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				UUID dbId = UUID.fromString(rs.getString(1));
				String fileName = rs.getString(2);
				UUID creatorId = UUID.fromString(rs.getString(3));
				boolean deleted = rs.getBoolean(4);

				Assertions.assertEquals(insertedDocument.getId(), dbId);
				Assertions.assertEquals(insertedDocument.getFilename(), fileName);
				Assertions.assertEquals(insertedDocument.getCreatorId(), creatorId);
				Assertions.assertEquals(insertedDocument.isDeleted(), deleted);
			}
		}
	}

	@Test
	void afterGetByDocumentType_thereShouldBeTheDocumentsWithTheDocumentType()
		throws JDSPersistenceException, SQLException {

		JivsDocument jivsDocument1 = createDocument();
		JivsDocument insertedDocument1 = documentDao.insert(jivsDocument1);

		JivsDocument jivsDocument2 = createDocumentWithParams();
		JivsDocument insertedDocument2 = documentDao.insert(jivsDocument2);

		Assertions.assertNotNull(insertedDocument1);
		Assertions.assertNotNull(insertedDocument2);

		try (Connection connection = DatabaseManager.getInstance().connect()) {
			PreparedStatement preparedStatement = connection.prepareStatement(ITestSQLs.SELECT_DOCUMENT_BY_DOCUMENT_TYPE_SQL);
			preparedStatement.setObject(1, DOCUMENT_TYPE);
			ResultSet rs = preparedStatement.executeQuery();
			List<UUID> ids = new ArrayList<>();
			while (rs.next()) {
				UUID dbId = UUID.fromString(rs.getString(1));
				ids.add(dbId);
				String docType = rs.getString(2);

				Assertions.assertEquals(DOCUMENT_TYPE, docType);
			}
			Assertions.assertEquals(2, ids.size());
		}
	}


	@Test
	void afterGetByCreator_thereShouldBeTheDocumentsOfTheCreator()
		throws JDSPersistenceException, SQLException {

		JivsDocument jivsDocument1 = createDocument();
		JivsDocument insertedDocument1 = documentDao.insert(jivsDocument1);

		JivsDocument jivsDocument2 = createDocumentWithParams();
		JivsDocument insertedDocument2 = documentDao.insert(jivsDocument2);

		Assertions.assertNotNull(insertedDocument1);
		Assertions.assertNotNull(insertedDocument2);

		try (Connection connection = DatabaseManager.getInstance().connect()) {
			PreparedStatement preparedStatement = connection.prepareStatement(ITestSQLs.SELECT_DOCUMENT_BY_CREATOR_SQL);
			preparedStatement.setObject(1, creatorId);
			ResultSet rs = preparedStatement.executeQuery();
			List<UUID> ids = new ArrayList<>();
			while (rs.next()) {
				UUID dbId = UUID.fromString(rs.getString(1));
				ids.add(dbId);
				UUID creator = UUID.fromString(rs.getString(2));

				Assertions.assertEquals(creatorId, creator);
			}
			Assertions.assertEquals(2, ids.size());
		}
	}

	@Test
	void afterGetByCreatedAt_thereShouldBeTheDocumentsOfDate()
		throws JDSPersistenceException, SQLException {

		JivsDocument jivsDocument1 = createDocument();
		JivsDocument insertedDocument1 = documentDao.insert(jivsDocument1);

		JivsDocument jivsDocument2 = createDocumentWithParams();
		JivsDocument insertedDocument2 = documentDao.insert(jivsDocument2);

		Assertions.assertNotNull(insertedDocument1);
		Assertions.assertNotNull(insertedDocument2);

		try (Connection connection = DatabaseManager.getInstance().connect()) {
			PreparedStatement preparedStatement = connection.prepareStatement(IDocumentSQLs.SELECT_DOCUMENT_BY_CREATED_SQL);
			preparedStatement.setObject(1, insertedDocument1.getCreatedAt());
			ResultSet rs = preparedStatement.executeQuery();
			List<UUID> ids = new ArrayList<>();
			while (rs.next()) {
				UUID dbId = UUID.fromString(rs.getString(1));
				ids.add(dbId);
			}
			Assertions.assertEquals(2, ids.size());
		}
	}

	@Test
	void afterGetByCustomerFK_thereShouldBeTheDocumentsOfTheCustomer()
		throws JDSPersistenceException, SQLException {

		JivsDocument jivsDocument1 = createDocument();
		JivsDocument insertedDocument1 = documentDao.insert(jivsDocument1);

		JivsDocument jivsDocument2 = createDocumentWithParams();
		JivsDocument insertedDocument2 = documentDao.insert(jivsDocument2);

		Assertions.assertNotNull(insertedDocument1);
		Assertions.assertNotNull(insertedDocument2);

		try (Connection connection = DatabaseManager.getInstance().connect()) {
			PreparedStatement preparedStatement = connection.prepareStatement(ITestSQLs.SELECT_DOCUMENT_BY_CUSTOMER_FK_SQL);
			preparedStatement.setObject(1, customerId);
			ResultSet rs = preparedStatement.executeQuery();
			List<UUID> ids = new ArrayList<>();
			while (rs.next()) {
				UUID dbId = UUID.fromString(rs.getString(1));
				ids.add(dbId);
				UUID customerId = UUID.fromString(rs.getString(2));

				Assertions.assertEquals(this.customerId, customerId);
			}
			Assertions.assertEquals(2, ids.size());
		}
	}

	@Test
	void afterGetBySystemFK_thereShouldBeTheDocumentsOfTheSystem()
		throws JDSPersistenceException, SQLException {

		JivsDocument jivsDocument1 = createDocument();
		JivsDocument insertedDocument1 = documentDao.insert(jivsDocument1);

		JivsDocument jivsDocument2 = createDocumentWithParams();
		JivsDocument insertedDocument2 = documentDao.insert(jivsDocument2);

		Assertions.assertNotNull(insertedDocument1);
		Assertions.assertNotNull(insertedDocument2);

		try (Connection connection = DatabaseManager.getInstance().connect()) {
			PreparedStatement preparedStatement = connection.prepareStatement(ITestSQLs.SELECT_DOCUMENT_BY_SYSTEM_FK_SQL);
			preparedStatement.setObject(1, systemId);
			ResultSet rs = preparedStatement.executeQuery();
			List<UUID> ids = new ArrayList<>();
			while (rs.next()) {
				UUID dbId = UUID.fromString(rs.getString(1));
				ids.add(dbId);
				UUID systemId = UUID.fromString(rs.getString(2));

				Assertions.assertEquals(this.systemId, systemId);
			}
			Assertions.assertEquals(2, ids.size());
		}
	}

	@Test
	void afterGetByCaseID_thereShouldBeTheDocumentsOfTheCase()
		throws JDSPersistenceException, SQLException {

		JivsDocument jivsDocument1 = createDocument();
		JivsDocument insertedDocument1 = documentDao.insert(jivsDocument1);

		JivsDocument jivsDocument2 = createDocumentWithParams();
		JivsDocument insertedDocument2 = documentDao.insert(jivsDocument2);

		Assertions.assertNotNull(insertedDocument1);
		Assertions.assertNotNull(insertedDocument2);

		try (Connection connection = DatabaseManager.getInstance().connect()) {
			PreparedStatement preparedStatement = connection.prepareStatement(ITestSQLs.SELECT_DOCUMENT_BY_CASE_ID_SQL);
			preparedStatement.setObject(1, insertedDocument1.getCaseId());
			ResultSet rs = preparedStatement.executeQuery();
			List<UUID> ids = new ArrayList<>();
			while (rs.next()) {
				UUID dbId = UUID.fromString(rs.getString(1));
				ids.add(dbId);
				UUID caseID = UUID.fromString(rs.getString(2));

				Assertions.assertEquals(insertedDocument1.getCaseId(), caseID);
			}
			Assertions.assertEquals(1, ids.size());
		}
	}

	@Test
	void afterGetAllByList_thereShouldBeTheDocuments()
	throws JDSPersistenceException, SQLException {

		JivsDocument jivsDocument1 = createDocument();
		JivsDocument insertedDocument1 = documentDao.insert(jivsDocument1);

		JivsDocument jivsDocument2 = createDocumentWithParams();
		JivsDocument insertedDocument2 = documentDao.insert(jivsDocument2);

		Assertions.assertNotNull(insertedDocument1);
		Assertions.assertNotNull(insertedDocument2);

		try (Connection connection = DatabaseManager.getInstance().connect()) {
			PreparedStatement preparedStatement = connection.prepareStatement(IDocumentSQLs.SELECT_ALL_DOCUMENTS_SQL);
			ResultSet rs = preparedStatement.executeQuery();
			List<UUID> ids = new ArrayList<>();
			while (rs.next()) {
				UUID dbId = UUID.fromString(rs.getString(1));
				ids.add(dbId);
			}
			Assertions.assertEquals(2, ids.size());
		}
	}

	@Test
	void afterUpdateDocumentParams_theDatabaseDocumentParamsShouldBeUpdated()
		throws JDSPersistenceException, SQLException {

		JivsDocument jivsDocument = createDocumentWithParams();
		JivsDocument insertedDocument = documentDao.insert(jivsDocument);
		Assertions.assertNotNull(insertedDocument);

		Map<String, String> newParams = new HashMap<>();
		newParams.put("paramKeyUpdated", "paramValueUpdated");
		documentParamDao.insert(new JivsDocumentParam(insertedDocument.getId(), jivsDocument.getParams()));
		documentParamDao.updateParams(insertedDocument.getId(), newParams);

		try (Connection connection = DatabaseManager.getInstance().connect()) {
			PreparedStatement preparedStatement = connection.prepareStatement(ITestSQLs.SELECT_ALL_DOCUMENT_PARAMS_BY_DOCUMENT_ID_SQL);
			preparedStatement.setObject(1, insertedDocument.getId());
			ResultSet rs = preparedStatement.executeQuery();
			Map<String, String> dbParams = new HashMap<>();
			UUID dbId = null;
			while (rs.next()) {
				dbId = UUID.fromString(rs.getString(1));
				dbParams.put(rs.getString(2), rs.getString(3));
			}

			Assertions.assertEquals(insertedDocument.getId(), dbId);
			Assertions.assertEquals(newParams.size(), dbParams.size());
			Assertions.assertTrue(dbParams.containsKey("paramKeyUpdated"));
			Assertions.assertFalse(dbParams.containsKey("paramKey1"));
			Assertions.assertFalse(dbParams.containsKey("paramKey2"));
		}
	}

	@Test
	void afterSoftDelete_theDatabaseDeleteFlagShouldBeTrue()
		throws JDSPersistenceException, SQLException {

		JivsDocument jivsDocument = createDocument();
		JivsDocument insertedDocument = documentDao.insert(jivsDocument);
		Assertions.assertNotNull(insertedDocument);
		Assertions.assertFalse(insertedDocument.isDeleted());

		documentDao.setDeleteFlagTrue(insertedDocument.getId());

		try (Connection connection = DatabaseManager.getInstance().connect()) {
			PreparedStatement preparedStatement = connection.prepareStatement(ITestSQLs.SELECT_DOCUMENT_BY_ID_SQL);
			preparedStatement.setObject(1, insertedDocument.getId());
			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				UUID id = UUID.fromString(rs.getString(1));
				boolean deleted = rs.getBoolean(4);

				Assertions.assertEquals(insertedDocument.getId(), id);
				Assertions.assertTrue(deleted);
			}
		}
	}
}