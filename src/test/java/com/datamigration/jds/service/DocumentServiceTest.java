package com.datamigration.jds.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.datamigration.jds.model.entity.docstore.JivsDocument;
import com.datamigration.jds.model.entity.docstoreparam.JivsDocumentParam;
import com.datamigration.jds.model.dto.DocumentDTO;
import com.datamigration.jds.persistence.docstore.DocumentDao;
import com.datamigration.jds.persistence.docstore.IDocumentDao;
import com.datamigration.jds.persistence.param.DocumentParamDao;
import com.datamigration.jds.persistence.param.IDocumentParamDao;
import com.datamigration.jds.util.DTOUtil;
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

		ArgumentCaptor<DocumentDTO> documentDTOCaptor = ArgumentCaptor.forClass(DocumentDTO.class);
		JivsDocument jivsDocument = new JivsDocument("Document".getBytes(StandardCharsets.UTF_8),
			"Document", "JivsDocument", UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
			UUID.randomUUID(), params);
		DocumentDTO documentDTO = DTOUtil.toDocumentDTO(jivsDocument);

		when(documentDao.insert(documentDTOCaptor.capture())).thenReturn(documentDTO);
		when(documentParamDao.insert(any())).thenReturn(new JivsDocumentParam(UUID.randomUUID(), params));

		documentService.insert(jivsDocument);

		Assertions.assertEquals(documentDTO.id(), documentDTOCaptor.getValue().id());
		Assertions.assertEquals(documentDTO.fileName(), documentDTOCaptor.getValue().fileName());
	}

	@Test
	void testGetById() throws JDSPersistenceException {
		UUID id = UUID.randomUUID();
		DocumentDTO documentDTO = new DocumentDTO(id, "Document".getBytes(StandardCharsets.UTF_8),
			"Document", "JivsDocument", UUID.randomUUID(), LocalDateTime.now(), UUID.randomUUID(), UUID.randomUUID(),
			UUID.randomUUID(), null, false);

		when(documentService.getById(id)).thenReturn(Optional.of(documentDTO));

		Optional<DocumentDTO> result = documentService.getById(id);

		Assertions.assertTrue(result.isPresent());
		Assertions.assertEquals(documentDTO.id(), result.get().id());
		Assertions.assertEquals(documentDTO.fileName(), result.get().fileName());
	}

	@Test
	void testGetByFileName() throws JDSPersistenceException {
		String fileName = "Document1";
		DocumentDTO documentDTO = new DocumentDTO(UUID.randomUUID(), "Document".getBytes(StandardCharsets.UTF_8),
			fileName, "JivsDocument", UUID.randomUUID(), LocalDateTime.now(), UUID.randomUUID(), UUID.randomUUID(),
			UUID.randomUUID(), null, false);

		when(documentService.getByFileName(fileName)).thenReturn(Optional.of(documentDTO));

		Optional<DocumentDTO> result = documentService.getByFileName(fileName);

		Assertions.assertTrue(result.isPresent());
		Assertions.assertEquals(documentDTO.id(), result.get().id());
		Assertions.assertEquals(documentDTO.fileName(), result.get().fileName());
	}

	@Test
	void testGetByDocumentType() throws JDSPersistenceException {
		String documentType = "JivsDocument";
		DocumentDTO documentDTO = new DocumentDTO(UUID.randomUUID(), "Document".getBytes(StandardCharsets.UTF_8),
			"Document", documentType, UUID.randomUUID(), LocalDateTime.now(), UUID.randomUUID(), UUID.randomUUID(),
			UUID.randomUUID(), null, false);

		List<DocumentDTO> documentDTOList = new ArrayList<>();
		documentDTOList.add(documentDTO);

		when(documentDao.getByDocumentType(documentType)).thenReturn(Optional.of(documentDTOList));

		Optional<List<DocumentDTO>> result = documentService.getByDocumentType(documentType);

		Assertions.assertTrue(result.isPresent());
		Assertions.assertEquals(1, result.get().size());
		Assertions.assertEquals(documentDTO.id(), result.get().getFirst().id());
		Assertions.assertEquals(documentDTO.documenType(), result.get().getFirst().documenType());
	}

	@Test
	void testGetByCreator() throws JDSPersistenceException {
		UUID creatorId = UUID.randomUUID();
		DocumentDTO documentDTO = new DocumentDTO(UUID.randomUUID(), "Document".getBytes(StandardCharsets.UTF_8),
			"Document", "documentType", creatorId, LocalDateTime.now(), UUID.randomUUID(), UUID.randomUUID(),
			UUID.randomUUID(), null, false);

		List<DocumentDTO> documentDTOList = new ArrayList<>();
		documentDTOList.add(documentDTO);

		when(documentDao.getByCreator(creatorId)).thenReturn(Optional.of(documentDTOList));

		Optional<List<DocumentDTO>> result = documentService.getByCreator(creatorId);

		Assertions.assertTrue(result.isPresent());
		Assertions.assertEquals(1, result.get().size());
		Assertions.assertEquals(documentDTO.id(), result.get().getFirst().id());
		Assertions.assertEquals(documentDTO.creatorId(), result.get().getFirst().creatorId());
	}

	@Test
	void testGetByCreatedAt() throws JDSPersistenceException {
		LocalDateTime createdAt = LocalDateTime.now();
		DocumentDTO documentDTO = new DocumentDTO(UUID.randomUUID(), "Document".getBytes(StandardCharsets.UTF_8),
			"Document", "documentType", UUID.randomUUID(), createdAt, UUID.randomUUID(), UUID.randomUUID(),
			UUID.randomUUID(), null, false);

		List<DocumentDTO> documentDTOList = new ArrayList<>();
		documentDTOList.add(documentDTO);

		when(documentDao.getByCreatedAt(createdAt)).thenReturn(Optional.of(documentDTOList));

		Optional<List<DocumentDTO>> result = documentService.getByCreatedAt(createdAt);

		Assertions.assertTrue(result.isPresent());
		Assertions.assertEquals(1, result.get().size());
		Assertions.assertEquals(documentDTO.id(), result.get().getFirst().id());
		Assertions.assertEquals(documentDTO.fileName(), result.get().getFirst().fileName());
	}

	@Test
	void testGetByCustomerId() throws JDSPersistenceException {
		UUID customerId = UUID.randomUUID();
		DocumentDTO documentDTO = new DocumentDTO(UUID.randomUUID(), "Document".getBytes(StandardCharsets.UTF_8),
			"Document", "documentType", UUID.randomUUID(), LocalDateTime.now(), customerId, UUID.randomUUID(),
			UUID.randomUUID(), null, false);

		List<DocumentDTO> documentDTOList = new ArrayList<>();
		documentDTOList.add(documentDTO);

		when(documentDao.getByCustomerId(customerId)).thenReturn(Optional.of(documentDTOList));

		Optional<List<DocumentDTO>> result = documentService.getByCustomerId(customerId);

		Assertions.assertTrue(result.isPresent());
		Assertions.assertEquals(1, result.get().size());
		Assertions.assertEquals(documentDTO.id(), result.get().getFirst().id());
		Assertions.assertEquals(documentDTO.customerId(), result.get().getFirst().customerId());
	}

	@Test
	void testGetBySystemId() throws JDSPersistenceException {
		UUID systemId = UUID.randomUUID();
		DocumentDTO documentDTO = new DocumentDTO(UUID.randomUUID(), "Document".getBytes(StandardCharsets.UTF_8),
			"Document", "documentType", UUID.randomUUID(), LocalDateTime.now(), UUID.randomUUID(), systemId,
			UUID.randomUUID(), null, false);

		List<DocumentDTO> documentDTOList = new ArrayList<>();
		documentDTOList.add(documentDTO);

		when(documentDao.getBySystemId(systemId)).thenReturn(Optional.of(documentDTOList));

		Optional<List<DocumentDTO>> result = documentService.getBySystemId(systemId);

		Assertions.assertTrue(result.isPresent());
		Assertions.assertEquals(1, result.get().size());
		Assertions.assertEquals(documentDTO.id(), result.get().getFirst().id());
		Assertions.assertEquals(documentDTO.systemId(), result.get().getFirst().systemId());
	}

	@Test
	void testGetByCaseId() throws JDSPersistenceException {
		UUID caseId = UUID.randomUUID();
		DocumentDTO documentDTO = new DocumentDTO(UUID.randomUUID(), "Document".getBytes(StandardCharsets.UTF_8),
			"Document", "documentType", UUID.randomUUID(), LocalDateTime.now(), UUID.randomUUID(), UUID.randomUUID(),
			caseId, null, false);

		List<DocumentDTO> documentDTOList = new ArrayList<>();
		documentDTOList.add(documentDTO);

		when(documentDao.getByCaseId(caseId)).thenReturn(Optional.of(documentDTOList));

		Optional<List<DocumentDTO>> result = documentService.getByCaseId(caseId);

		Assertions.assertTrue(result.isPresent());
		Assertions.assertEquals(1, result.get().size());
		Assertions.assertEquals(documentDTO.id(), result.get().getFirst().id());
		Assertions.assertEquals(documentDTO.caseId(), result.get().getFirst().caseId());
	}

	@Test
	void testGetAllAsList() throws JDSPersistenceException {
		DocumentDTO documentDTO = new DocumentDTO(UUID.randomUUID(), "Document".getBytes(StandardCharsets.UTF_8),
			"Document", "JivsDocument", UUID.randomUUID(), LocalDateTime.now(), UUID.randomUUID(), UUID.randomUUID(),
			UUID.randomUUID(), null, false);

		List<DocumentDTO> documentDTOList = new ArrayList<>();
		documentDTOList.add(documentDTO);

		when(documentDao.getAllAsList()).thenReturn(Optional.of(documentDTOList));

		Optional<List<DocumentDTO>> result = documentService.getAllAsList();

		Assertions.assertTrue(result.isPresent());
		Assertions.assertEquals(1, result.get().size());
		Assertions.assertEquals(documentDTO.id(), result.get().getFirst().id());
		Assertions.assertEquals(documentDTO.fileName(), result.get().getFirst().fileName());
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

		when(documentParamDao.getParams(id)).thenReturn(Optional.of(params));

		Optional<Map<String, String>> result = documentService.getParams(id);

		Assertions.assertTrue(result.isPresent());
		Assertions.assertTrue(result.get().containsKey("paramKey1"));
		Assertions.assertTrue(result.get().containsKey("paramKey2"));
		Assertions.assertTrue(result.get().containsValue("paramValue1"));
		Assertions.assertTrue(result.get().containsValue("paramValue2"));
		Assertions.assertEquals(2, result.get().size());
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