package com.datamigration.jds.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.datamigration.jds.model.entity.docstore.JivsDocument;
import com.datamigration.jds.model.entity.docstoreparam.JivsDocumentParam;
import com.datamigration.jds.persistence.docstore.DocumentDao;
import com.datamigration.jds.persistence.docstore.IDocumentDao;
import com.datamigration.jds.persistence.param.DocumentParamDao;
import com.datamigration.jds.persistence.param.IDocumentParamDao;
import com.datamigration.jds.util.exceptions.checked.JDSPersistenceException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class DocumentServiceTest {

	private final IDocumentDao documentDao = mock(DocumentDao.class);
	private final IDocumentParamDao documentParamDao = mock(DocumentParamDao.class);
	private final DocumentService documentService = new DocumentService(documentDao, documentParamDao);

	@Test
	void testInsert() throws JDSPersistenceException {
		Map<String, String> params = new HashMap<>();
		params.put("paramKey1", "paramValue1");
		params.put("paramKey2", "paramValue2");

		ArgumentCaptor<JivsDocument> documentCaptor = ArgumentCaptor.forClass(JivsDocument.class);
		JivsDocument jivsDocument = new JivsDocument("Document".getBytes(StandardCharsets.UTF_8),
			"Document", "JivsDocument", UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
			UUID.randomUUID(), params);

		when(documentDao.insert(documentCaptor.capture())).thenReturn(jivsDocument);
		when(documentParamDao.insert(any())).thenReturn(new JivsDocumentParam(UUID.randomUUID(), params));

		documentService.insert(jivsDocument);

		Assertions.assertEquals(jivsDocument.getId(), documentCaptor.getValue().getId());
		Assertions.assertEquals(jivsDocument.getFilename(), documentCaptor.getValue().getFilename());
	}

	@Test
	void testGetById() throws JDSPersistenceException {
		UUID id = UUID.randomUUID();
		JivsDocument jivsDocument = new JivsDocument("Document".getBytes(StandardCharsets.UTF_8),
			"Document", "JivsDocument", UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
			UUID.randomUUID(), null);

		when(documentService.getById(id)).thenReturn(Optional.of(jivsDocument));

		Optional<JivsDocument> result = documentService.getById(id);

		Assertions.assertTrue(result.isPresent());
		Assertions.assertEquals(jivsDocument.getId(), result.get().getId());
		Assertions.assertEquals(jivsDocument.getFilename(), result.get().getFilename());
	}

	@Test
	void testGetByFileName() throws JDSPersistenceException {
		String filename = "Document1";
		JivsDocument jivsDocument = new JivsDocument("Document".getBytes(StandardCharsets.UTF_8),
			filename, "JivsDocument", UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
			UUID.randomUUID(), null);

		when(documentService.getByFileName(filename)).thenReturn(Optional.of(jivsDocument));

		Optional<JivsDocument> result = documentService.getByFileName(filename);

		Assertions.assertTrue(result.isPresent());
		Assertions.assertEquals(jivsDocument.getId(), result.get().getId());
		Assertions.assertEquals(jivsDocument.getFilename(), result.get().getFilename());
	}

	@Test
	void testGetByDocumentType() throws JDSPersistenceException {
		String documentType = "JivsDocument";
		JivsDocument jivsDocument = new JivsDocument("Document".getBytes(StandardCharsets.UTF_8),
			"Document", documentType, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
			UUID.randomUUID(), null);
		jivsDocument.setId(UUID.randomUUID());

		List<JivsDocument> documentList = new ArrayList<>();
		documentList.add(jivsDocument);

		when(documentDao.getByDocumentType(documentType)).thenReturn(documentList);

		List<JivsDocument> result = documentService.getByDocumentType(documentType);

		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(jivsDocument.getId(), result.getFirst().getId());
		Assertions.assertEquals(jivsDocument.getDocumentType(), result.getFirst().getDocumentType());
	}

	@Test
	void testGetByCreator() throws JDSPersistenceException {
		UUID creatorId = UUID.randomUUID();
		JivsDocument jivsDocument = new JivsDocument("Document".getBytes(StandardCharsets.UTF_8),
			"Document", "documentType", creatorId, UUID.randomUUID(), UUID.randomUUID(),
			UUID.randomUUID(), null);
		jivsDocument.setId(UUID.randomUUID());

		List<JivsDocument> documentList = new ArrayList<>();
		documentList.add(jivsDocument);

		when(documentDao.getByCreator(creatorId)).thenReturn(documentList);

		List<JivsDocument> result = documentService.getByCreator(creatorId);

		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(jivsDocument.getId(), result.getFirst().getId());
		Assertions.assertEquals(jivsDocument.getCreatorId(), result.getFirst().getCreatorId());
	}

	@Test
	void testGetByCreatedAt() throws JDSPersistenceException {
		LocalDateTime createdAt = LocalDateTime.now();
		JivsDocument jivsDocument = new JivsDocument("Document".getBytes(StandardCharsets.UTF_8),
			"Document", "documentType", UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
			UUID.randomUUID(), null);
		jivsDocument.setId(UUID.randomUUID());
		jivsDocument.setCreatedAt(createdAt);

		List<JivsDocument> documentList = new ArrayList<>();
		documentList.add(jivsDocument);


		when(documentDao.getByCreatedAt(createdAt)).thenReturn(documentList);

		List<JivsDocument> result = documentService.getByCreatedAt(createdAt);

		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(jivsDocument.getId(), result.getFirst().getId());
		Assertions.assertEquals(jivsDocument.getFilename(), result.getFirst().getFilename());
	}

	@Test
	void testGetByCustomerId() throws JDSPersistenceException {
		UUID customerId = UUID.randomUUID();
		JivsDocument jivsDocument = new JivsDocument("Document".getBytes(StandardCharsets.UTF_8),
			"Document", "documentType", UUID.randomUUID(), customerId, UUID.randomUUID(),
			UUID.randomUUID(), null);
		jivsDocument.setId(UUID.randomUUID());
		List<JivsDocument> documentList = new ArrayList<>();
		documentList.add(jivsDocument);

		when(documentDao.getByCustomerId(customerId)).thenReturn(documentList);

		List<JivsDocument> result = documentService.getByCustomerId(customerId);

		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(jivsDocument.getId(), result.getFirst().getId());
		Assertions.assertEquals(jivsDocument.getCustomerId(), result.getFirst().getCustomerId());
	}

	@Test
	void testGetBySystemId() throws JDSPersistenceException {
		UUID systemId = UUID.randomUUID();
		JivsDocument jivsDocument = new JivsDocument("Document".getBytes(StandardCharsets.UTF_8),
			"Document", "documentType", UUID.randomUUID(), UUID.randomUUID(), systemId,
			UUID.randomUUID(), null);
		jivsDocument.setId(UUID.randomUUID());
		List<JivsDocument> documentList = new ArrayList<>();
		documentList.add(jivsDocument);


		when(documentDao.getBySystemId(systemId)).thenReturn(documentList);

		List<JivsDocument> result = documentService.getBySystemId(systemId);

		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(jivsDocument.getId(), result.getFirst().getId());
		Assertions.assertEquals(jivsDocument.getSystemId(), result.getFirst().getSystemId());
	}

	@Test
	void testGetByCaseId() throws JDSPersistenceException {
		UUID caseId = UUID.randomUUID();
		JivsDocument jivsDocument = new JivsDocument("Document".getBytes(StandardCharsets.UTF_8),
			"Document", "documentType", UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
			caseId, null);
		jivsDocument.setId(UUID.randomUUID());
		List<JivsDocument> documentList = new ArrayList<>();
		documentList.add(jivsDocument);

		when(documentDao.getByCaseId(caseId)).thenReturn(documentList);

		List<JivsDocument> result = documentService.getByCaseId(caseId);

		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(jivsDocument.getId(), result.getFirst().getId());
		Assertions.assertEquals(jivsDocument.getCaseId(), result.getFirst().getCaseId());
	}

	@Test
	void testGetAllAsList() throws JDSPersistenceException {
		JivsDocument jivsDocument = new JivsDocument("Document".getBytes(StandardCharsets.UTF_8),
			"Document", "documentType", UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
			UUID.randomUUID(), null);
		jivsDocument.setId(UUID.randomUUID());
		List<JivsDocument> documentList = new ArrayList<>();
		documentList.add(jivsDocument);

		when(documentDao.getAllAsList()).thenReturn(documentList);

		List<JivsDocument> result = documentService.getAllAsList();
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(jivsDocument.getId(), result.getFirst().getId());
		Assertions.assertEquals(jivsDocument.getFilename(), result.getFirst().getFilename());
	}

	@Test
	void testDelete() throws JDSPersistenceException {
		UUID id = UUID.randomUUID();
		documentService.delete(id);
		verify(documentDao, times(1)).setDeleteFlagTrue(any(UUID.class));
	}

	@Test
	void testGetParams() throws JDSPersistenceException {
		UUID id = UUID.randomUUID();
		Map<String, String> params = new HashMap<>();
		params.put("paramKey1", "paramValue1");
		params.put("paramKey2", "paramValue2");

		when(documentParamDao.getParams(id)).thenReturn(params);

		Map<String, String> result = documentService.getParams(id);

		Assertions.assertTrue(result.containsKey("paramKey1"));
		Assertions.assertTrue(result.containsKey("paramKey2"));
		Assertions.assertTrue(result.containsValue("paramValue1"));
		Assertions.assertTrue(result.containsValue("paramValue2"));
		Assertions.assertEquals(2, result.size());
	}

	@Test
	void updateParams() throws JDSPersistenceException {
		UUID id = UUID.randomUUID();
		Map<String, String> params = new HashMap<>();
		params.put("paramKey1", "paramValue1");
		documentService.updateParams(id, params);
		verify(documentParamDao, times(1)).updateParams(any(UUID.class), any(Map.class));
	}
}