package com.datamigration.jds.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.datamigration.jds.model.docstore.JivsDocument;
import com.datamigration.jds.model.dto.DocumentDTO;
import com.datamigration.jds.persistence.docstore.DocumentDao;
import com.datamigration.jds.persistence.param.DocumentParamDao;
import com.datamigration.jds.service.DocumentService;
import com.datamigration.jds.util.BaseSingletonTest;
import com.datamigration.jds.util.exceptions.checked.JDSPersistenceException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
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

	private DocumentDTO createAndInsertDocument() throws JDSPersistenceException {
		JivsDocument jivsDocument = createDocument();
		DocumentDTO createdDocument = docStoreManager.create(jivsDocument);
		assertNotNull(createdDocument);
		assertNotNull(createdDocument.id());
		return createdDocument;
	}

	@Test
	void testCreateDocument() throws JDSPersistenceException {
		DocumentDTO createdDocument = createAndInsertDocument();
		assertNotNull(createdDocument);
		assertNotNull(createdDocument.id());
	}

	@Test
	void testGetById() throws JDSPersistenceException {
		DocumentDTO createdDocument = createAndInsertDocument();
		DocumentDTO dbDocument =  docStoreManager.getById(createdDocument.id());
		assertNotNull(dbDocument);
		assertEquals(createdDocument.id(), dbDocument.id());
	}

	@Test
	void testGetByFileName() throws JDSPersistenceException {
		DocumentDTO createdDocument = createAndInsertDocument();
		DocumentDTO dbDocument =  docStoreManager.getByFileName("Document1");
		assertNotNull(dbDocument);
		assertEquals(createdDocument.fileName(), "Document1");
	}

	@Test
	void testGetByDocumentType() throws JDSPersistenceException {
		createAndInsertDocument();
		List<DocumentDTO> dbDocument =  docStoreManager.getByDocumentType("JIVSDOCUMENT");
		assertEquals(1, dbDocument.size());
	}

	@Test
	void testGetByDocumentTypeNotFound() throws JDSPersistenceException {
		List<DocumentDTO> dbDocument =  docStoreManager.getByDocumentType("some_kind_of_non_existent_document_type");
		assertEquals(0, dbDocument.size());
	}

	@Test
	void testGetByCreator() throws JDSPersistenceException {
		DocumentDTO createdDocument = createAndInsertDocument();
		docStoreManager.create(createDocumentWithParams());
		List<DocumentDTO> dbDocument =  docStoreManager.getByCreator(createdDocument.creatorId());
		assertEquals(2, dbDocument.size());
		assertEquals(creatorId, dbDocument.getFirst().creatorId());
		assertEquals(creatorId, dbDocument.getLast().creatorId());
	}

	@Test
	void testGetByCreatedAt() throws JDSPersistenceException {
		createAndInsertDocument();
		List<DocumentDTO> dbDocument =  docStoreManager.getByCreatedAt(LocalDate.now());
		assertEquals(1, dbDocument.size());
	}

	@Test
	void testGetByCustomerId() throws JDSPersistenceException {
		DocumentDTO createdDocument = createAndInsertDocument();
		List<DocumentDTO> dbDocument =  docStoreManager.getByCustomerId(createdDocument.customerId());
		assertEquals(1, dbDocument.size());
	}

	@Test
	void testGetBySystemId() throws JDSPersistenceException {
		DocumentDTO createdDocument = createAndInsertDocument();
		List<DocumentDTO> dbDocument =  docStoreManager.getBySystemId(createdDocument.systemId());
		assertEquals(1, dbDocument.size());
	}

	@Test
	void testGetByCaseId() throws JDSPersistenceException {
		DocumentDTO createdDocument = createAndInsertDocument();
		List<DocumentDTO> dbDocument =  docStoreManager.getByCaseId(createdDocument.caseId());
		assertEquals(1, dbDocument.size());
	}

	@Test
	void testGetAllAsList() throws JDSPersistenceException {
		createAndInsertDocument();
		docStoreManager.create(createDocumentWithParams());
		List<DocumentDTO> dbDocument =  docStoreManager.getAllAsList();
		assertEquals(2, dbDocument.size());
	}

	@Test
	void testUpdateParams() throws JDSPersistenceException {
		DocumentDTO createdDocument = docStoreManager.create(createDocumentWithParams());
		Map<String, String> newParams = new HashMap<>();
		newParams.put("paramKeyUpdated", "paramValueUpdated");
		docStoreManager.updateParams(createdDocument.id(), newParams);

		DocumentDTO insertedDocument = docStoreManager.getById(createdDocument.id());
		assertEquals(1, insertedDocument.params().size());
		assertTrue(insertedDocument.params().containsKey("paramKeyUpdated"));
	}

	@Test
	void testDelete() throws JDSPersistenceException {
		DocumentDTO createdDocument = docStoreManager.create(createDocumentWithParams());
		boolean deleteFlag = docStoreManager.delete(createdDocument.id());
		assertTrue(deleteFlag);
	}
}