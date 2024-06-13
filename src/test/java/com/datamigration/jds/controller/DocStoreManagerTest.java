package com.datamigration.jds.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.datamigration.jds.model.entity.docstore.JivsDocument;
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

	private JivsDocument createAndInsertDocument() throws JDSPersistenceException {
		JivsDocument jivsDocument = createDocument();
		JivsDocument createdDocument = docStoreManager.create(jivsDocument);
		assertNotNull(createdDocument);
		assertNotNull(createdDocument.getId());
		return createdDocument;
	}

	@Test
	void testCreateDocument() throws JDSPersistenceException {
		JivsDocument createdDocument = createAndInsertDocument();
		assertNotNull(createdDocument);
		assertNotNull(createdDocument.getId());
	}

	@Test
	void testGetById() throws JDSPersistenceException {
		JivsDocument createdDocument = createAndInsertDocument();
		JivsDocument dbDocument =  docStoreManager.getById(createdDocument.getId());
		assertNotNull(dbDocument);
		assertEquals(createdDocument.getId(), dbDocument.getId());
	}

	@Test
	void testGetByFileName() throws JDSPersistenceException {
		JivsDocument createdDocument = createAndInsertDocument();
		JivsDocument dbDocument =  docStoreManager.getByFileName("Document1");
		assertNotNull(dbDocument);
		assertEquals(createdDocument.getFilename(), "Document1");
	}

	@Test
	void testGetByDocumentType() throws JDSPersistenceException {
		createAndInsertDocument();
		List<JivsDocument> dbDocument =  docStoreManager.getByDocumentTypeAsList("JIVSDOCUMENT");
		assertEquals(1, dbDocument.size());
	}

	@Test
	void testGetByDocumentTypeNotFound() throws JDSPersistenceException {
		List<JivsDocument> dbDocument =  docStoreManager.getByDocumentTypeAsList("some_kind_of_non_existent_document_type");
		assertEquals(0, dbDocument.size());
	}

	@Test
	void testGetByCreator() throws JDSPersistenceException {
		JivsDocument createdDocument = createAndInsertDocument();
		docStoreManager.create(createDocumentWithParams());
		List<JivsDocument> dbDocument =  docStoreManager.getByCreatorAsList(createdDocument.getCreatorId());
		assertEquals(2, dbDocument.size());
		assertEquals(creatorId, dbDocument.getFirst().getCreatorId());
		assertEquals(creatorId, dbDocument.getLast().getCreatorId());
	}

	@Test
	void testGetByCreatedAt() throws JDSPersistenceException {
		createAndInsertDocument();
		List<JivsDocument> dbDocument =  docStoreManager.getByCreatedAtAsList(LocalDate.now());
		assertEquals(1, dbDocument.size());
	}

	@Test
	void testGetByCustomerId() throws JDSPersistenceException {
		JivsDocument createdDocument = createAndInsertDocument();
		List<JivsDocument> dbDocument =  docStoreManager.getByCustomerIdAsList(createdDocument.getCustomerId());
		assertEquals(1, dbDocument.size());
	}

	@Test
	void testGetBySystemId() throws JDSPersistenceException {
		JivsDocument createdDocument = createAndInsertDocument();
		List<JivsDocument> dbDocument =  docStoreManager.getBySystemIdAsList(createdDocument.getSystemId());
		assertEquals(1, dbDocument.size());
	}

	@Test
	void testGetByCaseId() throws JDSPersistenceException {
		JivsDocument createdDocument = createAndInsertDocument();
		List<JivsDocument> dbDocument =  docStoreManager.getByCaseIdAsList(createdDocument.getCaseId());
		assertEquals(1, dbDocument.size());
	}

	@Test
	void testGetAllAsList() throws JDSPersistenceException {
		createAndInsertDocument();
		docStoreManager.create(createDocumentWithParams());
		List<JivsDocument> dbDocument =  docStoreManager.getAllAsList();
		assertEquals(2, dbDocument.size());
	}

	@Test
	void testUpdateParams() throws JDSPersistenceException {
		JivsDocument createdDocument = docStoreManager.create(createDocumentWithParams());
		Map<String, String> newParams = new HashMap<>();
		newParams.put("paramKeyUpdated", "paramValueUpdated");
		docStoreManager.updateParams(createdDocument.getId(), newParams);

		JivsDocument insertedDocument = docStoreManager.getById(createdDocument.getId());
		assertEquals(1, insertedDocument.getParams().size());
		assertTrue(insertedDocument.getParams().containsKey("paramKeyUpdated"));
	}

	@Test
	void testDelete() throws JDSPersistenceException {
		JivsDocument createdDocument = docStoreManager.create(createDocumentWithParams());
		boolean deleteFlag = docStoreManager.delete(createdDocument.getId());
		assertTrue(deleteFlag);
	}
}