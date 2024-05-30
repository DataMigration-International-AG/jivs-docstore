package com.datamigration.jds.controller;

import com.datamigration.jds.model.docstore.JivsDocument;
import com.datamigration.jds.model.dto.DocumentDTO;
import com.datamigration.jds.service.DocumentService;
import com.datamigration.jds.util.exceptions.checked.JPEPersistenceException;
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

	public boolean delete(UUID id) throws JPEPersistenceException {
		boolean deleted = documentService.delete(id);

		if (!deleted) {
			logger.debug("Could not delete document with id: {}", id);
		}

		return deleted;
	}
}
