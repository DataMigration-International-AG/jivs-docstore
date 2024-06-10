package com.datamigration.jds.persistence.docstore;

import com.datamigration.jds.model.entity.docstore.JivsDocument;
import com.datamigration.jds.model.entity.docstoreparam.JivsDocumentParam;
import com.datamigration.jds.model.dto.DocumentDTO;
import com.datamigration.jds.persistence.DatabaseManager;
import com.datamigration.jds.persistence.param.DocumentParamDao;
import com.datamigration.jds.persistence.param.IDocumentParamDao;
import com.datamigration.jds.util.BaseSingletonTest;
import com.datamigration.jds.util.DTOUtil;
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
		DocumentDTO documentDTO = DTOUtil.toDocumentDTO(jivsDocument);
		DocumentDTO insertedDocumentDTO = documentDao.insert(documentDTO);
		Assertions.assertNotNull(insertedDocumentDTO);
		try (Connection connection = DatabaseManager.getInstance().connect()) {
			PreparedStatement preparedStatement = connection.prepareStatement(ITestSQLs.SELECT_DOCUMENT_BY_ID_SQL);
			preparedStatement.setObject(1, insertedDocumentDTO.id());
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				UUID dbId = UUID.fromString(rs.getString(1));
				String fileName = rs.getString(2);
				UUID creator = UUID.fromString(rs.getString(3));
				boolean deleted = rs.getBoolean(4);

				Assertions.assertEquals(insertedDocumentDTO.id(), dbId);
				Assertions.assertEquals(insertedDocumentDTO.fileName(), fileName);
				Assertions.assertEquals(insertedDocumentDTO.creatorId(), creator);
				Assertions.assertFalse(deleted);
			}
		}
	}

	@Test
	void afterInsertDocumentParams_thereShouldBeADocumentParams()
		throws JDSPersistenceException, NoDriverFoundException, SQLException {

		JivsDocument jivsDocument = createDocumentWithParams();
		DocumentDTO documentDTO = DTOUtil.toDocumentDTO(jivsDocument);
		DocumentDTO insertedDocumentDTO = documentDao.insert(documentDTO);
		Assertions.assertNotNull(insertedDocumentDTO);
		try (Connection connection = DatabaseManager.getInstance().connect()) {
			PreparedStatement preparedStatement = connection.prepareStatement(ITestSQLs.SELECT_DOCUMENT_BY_ID_SQL);
			preparedStatement.setObject(1, insertedDocumentDTO.id());
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				UUID dbId = UUID.fromString(rs.getString(1));
				String fileName = rs.getString(2);
				UUID creator = UUID.fromString(rs.getString(3));
				boolean deleted = rs.getBoolean(4);

				Assertions.assertEquals(insertedDocumentDTO.id(), dbId);
				Assertions.assertEquals(insertedDocumentDTO.fileName(), fileName);
				Assertions.assertEquals(insertedDocumentDTO.creatorId(), creator);
				Assertions.assertFalse(deleted);
			}
		}

		documentParamDao.insert(new JivsDocumentParam(insertedDocumentDTO.id(), jivsDocument.getParams()));

		try (Connection connection = DatabaseManager.getInstance().connect()) {
			PreparedStatement preparedStatement = connection.prepareStatement(ITestSQLs.SELECT_ALL_DOCUMENT_PARAMS_BY_DOCUMENT_ID_SQL);
			preparedStatement.setObject(1, insertedDocumentDTO.id());
			ResultSet rs = preparedStatement.executeQuery();
			Map<String, String> dbParams = new HashMap<>();
			UUID dbId = null;
			while (rs.next()) {
				dbId = UUID.fromString(rs.getString(1));
				dbParams.put(rs.getString(2), rs.getString(3));
			}

			Assertions.assertEquals(insertedDocumentDTO.id(), dbId);
			Assertions.assertEquals(jivsDocument.getParams().size(), dbParams.size());
			Assertions.assertEquals(true, dbParams.containsKey("paramKey1"));
			Assertions.assertEquals(true, dbParams.containsKey("paramKey2"));
		}
	}

	@Test
	void afterGetByFileName_thereShouldBeTheDocumentWithTheFileName() throws JDSPersistenceException, SQLException {
		JivsDocument jivsDocument = createDocumentWithParams();
		DocumentDTO documentDTO = DTOUtil.toDocumentDTO(jivsDocument);
		DocumentDTO insertedDocumentDTO = documentDao.insert(documentDTO);
		Assertions.assertNotNull(insertedDocumentDTO);

		try (Connection connection = DatabaseManager.getInstance().connect()) {
			PreparedStatement preparedStatement = connection.prepareStatement(ITestSQLs.SELECT_DOCUMENT_BY_FILE_NAME_SQL);
			preparedStatement.setObject(1, insertedDocumentDTO.fileName());
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				UUID dbId = UUID.fromString(rs.getString(1));
				String fileName = rs.getString(2);
				UUID creator = UUID.fromString(rs.getString(3));
				boolean deleted = rs.getBoolean(4);

				Assertions.assertEquals(insertedDocumentDTO.id(), dbId);
				Assertions.assertEquals(insertedDocumentDTO.fileName(), fileName);
				Assertions.assertEquals(insertedDocumentDTO.creatorId(), creator);
				Assertions.assertEquals(insertedDocumentDTO.deleted(), deleted);
			}
		}
	}

	@Test
	void afterGetByDocumentType_thereShouldBeTheDocumentsWithTheDocumentType()
		throws JDSPersistenceException, SQLException {

		JivsDocument jivsDocument1 = createDocument();
		DocumentDTO documentDTO1 = DTOUtil.toDocumentDTO(jivsDocument1);
		DocumentDTO insertedDocumentDTO1 = documentDao.insert(documentDTO1);

		JivsDocument jivsDocument2 = createDocumentWithParams();
		DocumentDTO documentDTO2 = DTOUtil.toDocumentDTO(jivsDocument2);
		DocumentDTO insertedDocumentDTO2 = documentDao.insert(documentDTO2);

		Assertions.assertNotNull(insertedDocumentDTO1);
		Assertions.assertNotNull(insertedDocumentDTO2);

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
		DocumentDTO documentDTO1 = DTOUtil.toDocumentDTO(jivsDocument1);
		DocumentDTO insertedDocumentDTO1 = documentDao.insert(documentDTO1);

		JivsDocument jivsDocument2 = createDocumentWithParams();
		DocumentDTO documentDTO2 = DTOUtil.toDocumentDTO(jivsDocument2);
		DocumentDTO insertedDocumentDTO2 = documentDao.insert(documentDTO2);

		Assertions.assertNotNull(insertedDocumentDTO1);
		Assertions.assertNotNull(insertedDocumentDTO2);

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
		DocumentDTO documentDTO1 = DTOUtil.toDocumentDTO(jivsDocument1);
		DocumentDTO insertedDocumentDTO1 = documentDao.insert(documentDTO1);

		JivsDocument jivsDocument2 = createDocumentWithParams();
		DocumentDTO documentDTO2 = DTOUtil.toDocumentDTO(jivsDocument2);
		DocumentDTO insertedDocumentDTO2 = documentDao.insert(documentDTO2);

		Assertions.assertNotNull(insertedDocumentDTO1);
		Assertions.assertNotNull(insertedDocumentDTO2);

		try (Connection connection = DatabaseManager.getInstance().connect()) {
			PreparedStatement preparedStatement = connection.prepareStatement(IDocumentSQLs.SELECT_DOCUMENT_BY_CREATED_SQL);
			preparedStatement.setObject(1, insertedDocumentDTO1.created());
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
		DocumentDTO documentDTO1 = DTOUtil.toDocumentDTO(jivsDocument1);
		DocumentDTO insertedDocumentDTO1 = documentDao.insert(documentDTO1);

		JivsDocument jivsDocument2 = createDocumentWithParams();
		DocumentDTO documentDTO2 = DTOUtil.toDocumentDTO(jivsDocument2);
		DocumentDTO insertedDocumentDTO2 = documentDao.insert(documentDTO2);

		Assertions.assertNotNull(insertedDocumentDTO1);
		Assertions.assertNotNull(insertedDocumentDTO2);

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
		DocumentDTO documentDTO1 = DTOUtil.toDocumentDTO(jivsDocument1);
		DocumentDTO insertedDocumentDTO1 = documentDao.insert(documentDTO1);

		JivsDocument jivsDocument2 = createDocumentWithParams();
		DocumentDTO documentDTO2 = DTOUtil.toDocumentDTO(jivsDocument2);
		DocumentDTO insertedDocumentDTO2 = documentDao.insert(documentDTO2);

		Assertions.assertNotNull(insertedDocumentDTO1);
		Assertions.assertNotNull(insertedDocumentDTO2);

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
		DocumentDTO documentDTO1 = DTOUtil.toDocumentDTO(jivsDocument1);
		DocumentDTO insertedDocumentDTO1 = documentDao.insert(documentDTO1);

		JivsDocument jivsDocument2 = createDocumentWithParams();
		DocumentDTO documentDTO2 = DTOUtil.toDocumentDTO(jivsDocument2);
		DocumentDTO insertedDocumentDTO2 = documentDao.insert(documentDTO2);

		Assertions.assertNotNull(insertedDocumentDTO1);
		Assertions.assertNotNull(insertedDocumentDTO2);

		try (Connection connection = DatabaseManager.getInstance().connect()) {
			PreparedStatement preparedStatement = connection.prepareStatement(ITestSQLs.SELECT_DOCUMENT_BY_CASE_ID_SQL);
			preparedStatement.setObject(1, insertedDocumentDTO1.caseId());
			ResultSet rs = preparedStatement.executeQuery();
			List<UUID> ids = new ArrayList<>();
			while (rs.next()) {
				UUID dbId = UUID.fromString(rs.getString(1));
				ids.add(dbId);
				UUID caseID = UUID.fromString(rs.getString(2));

				Assertions.assertEquals(insertedDocumentDTO1.caseId(), caseID);
			}
			Assertions.assertEquals(1, ids.size());
		}
	}

	@Test
	void afterGetAllByList_thereShouldBeTheDocuments()
	throws JDSPersistenceException, SQLException {

		JivsDocument jivsDocument1 = createDocument();
		DocumentDTO documentDTO1 = DTOUtil.toDocumentDTO(jivsDocument1);
		DocumentDTO insertedDocumentDTO1 = documentDao.insert(documentDTO1);

		JivsDocument jivsDocument2 = createDocumentWithParams();
		DocumentDTO documentDTO2 = DTOUtil.toDocumentDTO(jivsDocument2);
		DocumentDTO insertedDocumentDTO2 = documentDao.insert(documentDTO2);

		Assertions.assertNotNull(insertedDocumentDTO1);
		Assertions.assertNotNull(insertedDocumentDTO2);

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
		DocumentDTO documentDTO = DTOUtil.toDocumentDTO(jivsDocument);
		DocumentDTO insertedDocumentDTO = documentDao.insert(documentDTO);
		Assertions.assertNotNull(insertedDocumentDTO);

		Map<String, String> newParams = new HashMap<>();
		newParams.put("paramKeyUpdated", "paramValueUpdated");
		documentParamDao.insert(new JivsDocumentParam(insertedDocumentDTO.id(), jivsDocument.getParams()));
		documentParamDao.updateParams(insertedDocumentDTO.id(), newParams);

		try (Connection connection = DatabaseManager.getInstance().connect()) {
			PreparedStatement preparedStatement = connection.prepareStatement(ITestSQLs.SELECT_ALL_DOCUMENT_PARAMS_BY_DOCUMENT_ID_SQL);
			preparedStatement.setObject(1, insertedDocumentDTO.id());
			ResultSet rs = preparedStatement.executeQuery();
			Map<String, String> dbParams = new HashMap<>();
			UUID dbId = null;
			while (rs.next()) {
				dbId = UUID.fromString(rs.getString(1));
				dbParams.put(rs.getString(2), rs.getString(3));
			}

			Assertions.assertEquals(insertedDocumentDTO.id(), dbId);
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
		DocumentDTO documentDTO = DTOUtil.toDocumentDTO(jivsDocument);
		DocumentDTO insertedDocumentDTO = documentDao.insert(documentDTO);
		Assertions.assertNotNull(insertedDocumentDTO);
		Assertions.assertFalse(insertedDocumentDTO.deleted());

		documentDao.updateDeleteFlag(insertedDocumentDTO.id());

		try (Connection connection = DatabaseManager.getInstance().connect()) {
			PreparedStatement preparedStatement = connection.prepareStatement(ITestSQLs.SELECT_DOCUMENT_BY_ID_SQL);
			preparedStatement.setObject(1, insertedDocumentDTO.id());
			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				UUID id = UUID.fromString(rs.getString(1));
				boolean deleted = rs.getBoolean(4);

				Assertions.assertEquals(insertedDocumentDTO.id(), id);
				Assertions.assertTrue(deleted);
			}
		}
	}
}