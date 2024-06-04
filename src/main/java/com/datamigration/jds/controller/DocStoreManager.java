package com.datamigration.jds.controller;

import com.datamigration.jds.model.docstore.JivsDocument;
import com.datamigration.jds.model.dto.DocumentDTO;
import com.datamigration.jds.service.DocumentService;
import com.datamigration.jds.util.exceptions.checked.JPEPersistenceException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages the execution of the {@link JivsDocument}. Is responsible for handling all the documents
 */
public class DocStoreManager {

	private static final Logger logger = LoggerFactory.getLogger(DocStoreManager.class);

	private final DocumentService documentService;

	public DocStoreManager(DocumentService documentService) {
		this.documentService = documentService;
	}

	public DocumentDTO create(JivsDocument jivsDocument) throws JPEPersistenceException {
		DocumentDTO documentDTO = documentService.insert(jivsDocument);
		return documentDTO;
	}

	public DocumentDTO getById(UUID id) throws JPEPersistenceException {
		Optional<DocumentDTO> documentDTO = documentService.getById(id);

		if (documentDTO.isEmpty()) {
			logger.info("Document with id {} not found", id);
			return null;
		}

		DocumentDTO documentDTOWithParams = getDocumentParams(documentDTO.get());
		return documentDTOWithParams;
	}

	public DocumentDTO getByFileName(String fileName) throws JPEPersistenceException {
		Optional<DocumentDTO> documentDTO = documentService.getByFileName(fileName);

		if (documentDTO.isEmpty()) {
			logger.info("Document with name {} not found", fileName);
			return null;
		}

		DocumentDTO documentDTOWithParams = getDocumentParams(documentDTO.get());
		return documentDTOWithParams;
	}

	public List<DocumentDTO> getByDocumentType(String documentType) throws JPEPersistenceException {
		Optional<List<DocumentDTO>> documentDTOs = documentService.getByDocumentType(documentType);
		return getParamsForList(documentDTOs);
	}

	public List<DocumentDTO> getByCreator(UUID id) throws JPEPersistenceException {
		Optional<List<DocumentDTO>> documentDTOs = documentService.getByCreator(id);
		return getParamsForList(documentDTOs);
	}

	public List<DocumentDTO> getByCreatedAt(LocalDateTime dateTime) throws JPEPersistenceException {
		Optional<List<DocumentDTO>> documentDTOs = documentService.getByCreatedAt(dateTime);
		return getParamsForList(documentDTOs);
	}

	public List<DocumentDTO> getByCustomerId(UUID id) throws JPEPersistenceException {
		Optional<List<DocumentDTO>> documentDTOs = documentService.getByCustomerId(id);
		return getParamsForList(documentDTOs);
	}

	public List<DocumentDTO> getBySystemId(UUID id) throws JPEPersistenceException {
		Optional<List<DocumentDTO>> documentDTOs = documentService.getBySystemId(id);
		return getParamsForList(documentDTOs);
	}

	public List<DocumentDTO> getByCaseId(UUID id) throws JPEPersistenceException {
		Optional<List<DocumentDTO>> documentDTOs = documentService.getByCaseId(id);
		return getParamsForList(documentDTOs);
	}

	public List<DocumentDTO> getAllAsList() throws JPEPersistenceException {
		Optional<List<DocumentDTO>> documentDTOs = documentService.getAllAsList();
		return getParamsForList(documentDTOs);
	}

	private List<DocumentDTO> getParamsForList(Optional<List<DocumentDTO>> documentDTOs)
		throws JPEPersistenceException {
		List<DocumentDTO> documentDTOList = new ArrayList<>();
		if (documentDTOs.isPresent()) {
			for (DocumentDTO documentDTO : documentDTOs.get()) {
				DocumentDTO documentDTOWithParams = getDocumentParams(documentDTO);
				documentDTOList.add(documentDTOWithParams);
			}
		}
		return documentDTOList;
	}

	private DocumentDTO getDocumentParams(DocumentDTO documentDTO) throws JPEPersistenceException {
		Optional<Map<String, String>> result = documentService.getParams(documentDTO.id());
		result.ifPresent(map -> documentDTO.params().putAll(map));
		return documentDTO;
	}

	public void update(JivsDocument jivsDocument) throws JPEPersistenceException {
		documentService.update(jivsDocument);
	}

	public void updateParams(UUID id, Map<String, String> params) throws JPEPersistenceException {
		documentService.updateParams(id, params);
	}

	/*
	 * Does not delete the entity immediately. It sets the delete flag for the given entity
	 * @param id the id of the entity
	 * @throws JPEPersistenceException if there is an error while establishing the database connection
	 * */
	public boolean delete(UUID id) throws JPEPersistenceException {
		boolean deleted = documentService.delete(id);

		if (!deleted) {
			logger.debug("Could not delete document with id: {}", id);
		}

		return deleted;
	}
}
