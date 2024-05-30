package com.datamigration.jds.controller;

import com.datamigration.jds.model.docstore.JivsDocument;
import com.datamigration.jds.model.dto.DocumentDTO;
import com.datamigration.jds.service.DocumentService;
import com.datamigration.jds.util.exceptions.checked.JPEPersistenceException;
import java.util.ArrayList;
import java.util.List;
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

		return documentDTO.get();
	}

	public List<DocumentDTO> getAllAsList() throws JPEPersistenceException {
		Optional<List<DocumentDTO>> documentDTOs = documentService.getAllAsList();
		return documentDTOs.orElseGet(ArrayList::new);
	}

	public DocumentDTO getByDocumentType(String documentType) throws JPEPersistenceException {
		return null;
	}

	public void update(JivsDocument jivsDocument) throws JPEPersistenceException {
		documentService.update(jivsDocument);
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
