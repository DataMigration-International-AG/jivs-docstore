package com.datamigration.jds.persistence.docstore;

import com.datamigration.jds.model.docstore.JivsDocument;
import com.datamigration.jds.model.docstoreparam.JivsDocumentParam;
import com.datamigration.jds.model.dto.DocumentDTO;
import com.datamigration.jds.persistence.DatabaseManager;
import com.datamigration.jds.persistence.param.DocumentParamDao;
import com.datamigration.jds.persistence.param.IDocumentParamDao;
import com.datamigration.jds.util.BaseSingletonTest;
import com.datamigration.jds.util.DTOUtil;
import com.datamigration.jds.util.ITestSQLs;
import com.datamigration.jds.util.exceptions.checked.JPEPersistenceException;
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

	@AfterEach
	public void afterEach() {
		truncateDb();
	}

	private JivsDocument createDocument() {
		return new JivsDocument("Document".getBytes(StandardCharsets.UTF_8), "Document1", DOCUMENT_TYPE,
			creatorId, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), null, false);
	}

	private JivsDocument createDocumentWithParams() {
		Map<String, String> params = new HashMap<>();
		params.put("paramKey1", "paramValue1");
		params.put("paramKey2", "paramValue2");

		return new JivsDocument("Document".getBytes(StandardCharsets.UTF_8), "Document2", DOCUMENT_TYPE,
			creatorId, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), params, false);
	}

	@Test
	void createTables() {
	}

	@Test
	void afterInsertDocument_thereShouldBeADocument()
		throws JPEPersistenceException, NoDriverFoundException, SQLException {

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
				Assertions.assertEquals(insertedDocumentDTO.creator(), creator);
				Assertions.assertEquals(insertedDocumentDTO.deleted(), deleted);
			}
		}
	}

	@Test
	void afterInsertDocumentParams_thereShouldBeADocumentParams()
		throws JPEPersistenceException, NoDriverFoundException, SQLException {

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
				Assertions.assertEquals(insertedDocumentDTO.creator(), creator);
				Assertions.assertEquals(insertedDocumentDTO.deleted(), deleted);
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
	void afterGetByFileName_thereShouldBeTheDocumentWithTheFileName() throws JPEPersistenceException, SQLException {
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
				Assertions.assertEquals(insertedDocumentDTO.creator(), creator);
				Assertions.assertEquals(insertedDocumentDTO.deleted(), deleted);
			}
		}
	}

	@Test
	void afterGetByDocumentType_thereShouldBeTheDocumentsWithTheDocumentType()
		throws JPEPersistenceException, SQLException {

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
		throws JPEPersistenceException, SQLException {

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
	void afterGetAllByList_thereShouldBeTheDocuments()
	throws JPEPersistenceException, SQLException {

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
		throws JPEPersistenceException, SQLException {

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
			Assertions.assertEquals(true, dbParams.containsKey("paramKeyUpdated"));
			Assertions.assertEquals(false, dbParams.containsKey("paramKey1"));
			Assertions.assertEquals(false, dbParams.containsKey("paramKey2"));
		}
	}

	@Test
	void updateParams() {
	}

	@Test
	void updateDeleteFlag() {
	}
}