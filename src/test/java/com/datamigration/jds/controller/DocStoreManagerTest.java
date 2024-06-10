package com.datamigration.jds.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.datamigration.jds.model.docstore.JivsDocument;
import com.datamigration.jds.model.dto.DocumentDTO;
import com.datamigration.jds.persistence.docstore.DocumentDao;
import com.datamigration.jds.persistence.param.DocumentParamDao;
import com.datamigration.jds.service.DocumentService;
import com.datamigration.jds.util.BaseSingletonTest;
import com.datamigration.jds.util.exceptions.checked.JPEPersistenceException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class DocStoreManagerTest extends BaseSingletonTest {

	private final DocStoreManager docStoreManager = new DocStoreManager(new DocumentService(new DocumentDao(), new DocumentParamDao()));
	private final UUID creatorId = UUID.randomUUID();

	private JivsDocument createDocument() {
		return new JivsDocument("Document".getBytes(StandardCharsets.UTF_8), "Document1", "JIVSDOCUMENT",
			creatorId, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), null);
	}

	private JivsDocument createDocumentWithParams() {
		Map<String, String> params = new HashMap<>();
		params.put("paramKey1", "paramValue1");
		params.put("paramKey2", "paramValue2");

		return new JivsDocument("Document".getBytes(StandardCharsets.UTF_8), "Document2", "JIVSDOCUMENTPARAMS",
			creatorId, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), params);
	}

	@AfterEach
	public void afterEach() {
		truncateDb();
	}

	private DocumentDTO createAndInsertDocument() throws JPEPersistenceException {
		JivsDocument jivsDocument = createDocument();
		DocumentDTO createdDocument = docStoreManager.create(jivsDocument);
		assertNotNull(createdDocument);
		assertNotNull(createdDocument.id());
		return createdDocument;
	}

	@Test
	void testCreateDocument() throws JPEPersistenceException {
		DocumentDTO createdDocument = createAndInsertDocument();
		assertNotNull(createdDocument);
		assertNotNull(createdDocument.id());
	}

	@Test
	void testGetById() throws JPEPersistenceException {
		DocumentDTO createdDocument = createAndInsertDocument();
		DocumentDTO dbDocument =  docStoreManager.getById(createdDocument.id());
		assertNotNull(dbDocument);
		assertEquals(createdDocument.id(), dbDocument.id());
	}

	@Test
	void testGetByFileName() throws JPEPersistenceException {
		DocumentDTO createdDocument = createAndInsertDocument();
		DocumentDTO dbDocument =  docStoreManager.getByFileName("Document1");
		assertNotNull(dbDocument);
		assertEquals(createdDocument.fileName(), "Document1");
	}

	@Test
	void testGetByDocumentType() throws JPEPersistenceException {
		createAndInsertDocument();
		List<DocumentDTO> dbDocument =  docStoreManager.getByDocumentType("JIVSDOCUMENT");
		assertEquals(1, dbDocument.size());
	}

	@Test
	void testGetByDocumentTypeNotFound() throws JPEPersistenceException {
		List<DocumentDTO> dbDocument =  docStoreManager.getByDocumentType("some_kind_of_non_existent_document_type");
		assertEquals(0, dbDocument.size());
	}

	@Test
	void testGetByCreator() throws JPEPersistenceException {
		DocumentDTO createdDocument = createAndInsertDocument();
		docStoreManager.create(createDocumentWithParams());
		List<DocumentDTO> dbDocument =  docStoreManager.getByCreator(createdDocument.creator());
		assertEquals(2, dbDocument.size());
		assertEquals(creatorId, dbDocument.getFirst().creator());
		assertEquals(creatorId, dbDocument.getLast().creator());
	}

	@Test
	void testGetByCreatedAt() throws JPEPersistenceException {
		createAndInsertDocument();
		List<DocumentDTO> dbDocument =  docStoreManager.getByCreatedAt(LocalDateTime.now());
		assertEquals(1, dbDocument.size());
	}

	@Test
	void testGetByCustomerId() throws JPEPersistenceException {
		DocumentDTO createdDocument = createAndInsertDocument();
		List<DocumentDTO> dbDocument =  docStoreManager.getByCustomerId(createdDocument.customerFK());
		assertEquals(1, dbDocument.size());
	}

	@Test
	void testGetBySystemId() throws JPEPersistenceException {
		DocumentDTO createdDocument = createAndInsertDocument();
		List<DocumentDTO> dbDocument =  docStoreManager.getBySystemId(createdDocument.systemFk());
		assertEquals(1, dbDocument.size());
	}

	@Test
	void testGetByCaseId() throws JPEPersistenceException {
		DocumentDTO createdDocument = createAndInsertDocument();
		List<DocumentDTO> dbDocument =  docStoreManager.getByCaseId(createdDocument.caseId());
		assertEquals(1, dbDocument.size());
	}

	@Test
	void testGetAllAsList() throws JPEPersistenceException {
		createAndInsertDocument();
		docStoreManager.create(createDocumentWithParams());
		List<DocumentDTO> dbDocument =  docStoreManager.getAllAsList();
		assertEquals(2, dbDocument.size());
	}

	@Test
	void testUpdateParams() throws JPEPersistenceException {
		DocumentDTO createdDocument = docStoreManager.create(createDocumentWithParams());
		Map<String, String> newParams = new HashMap<>();
		newParams.put("paramKeyUpdated", "paramValueUpdated");
		docStoreManager.updateParams(createdDocument.id(), newParams);

		DocumentDTO insertedDocument = docStoreManager.getById(createdDocument.id());
		assertEquals(1, insertedDocument.params().size());
		assertTrue(insertedDocument.params().containsKey("paramKeyUpdated"));
	}

	@Test
	void testDelete() throws JPEPersistenceException {
		DocumentDTO createdDocument = docStoreManager.create(createDocumentWithParams());
		boolean deleteFlag = docStoreManager.delete(createdDocument.id());
		assertTrue(deleteFlag);
	}
}