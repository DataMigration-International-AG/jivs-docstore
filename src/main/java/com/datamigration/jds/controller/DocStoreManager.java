package com.datamigration.jds.controller;

import com.datamigration.jds.model.entity.docstore.JivsDocument;
import com.datamigration.jds.service.DocumentService;
import com.datamigration.jds.util.exceptions.checked.JDSPersistenceException;
import java.time.LocalDate;
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

	public JivsDocument create(JivsDocument jivsDocument) throws JDSPersistenceException {
		JivsDocument document = documentService.insert(jivsDocument);
		return document;
	}

	/**
	 * Gets the document by the given id
	 * @param id the id of the document
	 * @return Document
	 * @throws JDSPersistenceException if there is an error while establishing the database connection
	 * */
	public JivsDocument getById(UUID id) throws JDSPersistenceException {
		Optional<JivsDocument> document = documentService.getById(id);

		if (document.isEmpty()) {
			logger.info("Document with id {} not found", id);
			return null;
		}

		JivsDocument documentWithParams = addParamsToDocument(document.get());
		return documentWithParams;
	}

	/**
	 * Gets the document by the given filename
	 * @param fileName the filename of the document
	 * @return Document
	 * @throws JDSPersistenceException if there is an error while establishing the database connection
	 * */
	public JivsDocument getByFileName(String fileName) throws JDSPersistenceException {
		Optional<JivsDocument> document = documentService.getByFileName(fileName);

		if (document.isEmpty()) {
			logger.info("Document with name {} not found", fileName);
			return null;
		}

		JivsDocument documentWithParams = addParamsToDocument(document.get());
		return documentWithParams;
	}

	/**
	 * Gets the documents for the provided document type
	 * @param documentType the type of the document
	 * @return List of documents
	 * @throws JDSPersistenceException if there is an error while establishing the database connection
	 * */
	public List<JivsDocument> getByDocumentType(String documentType) throws JDSPersistenceException {
		List<JivsDocument> documents = documentService.getByDocumentType(documentType);
		return getDocumentWithParams(documents);
	}

	/**
	 * Gets the documents for the provided creatorId id
	 * @param id the creatorId id of the document
	 * @return List of documents
	 * @throws JDSPersistenceException if there is an error while establishing the database connection
	 * */
	public List<JivsDocument> getByCreator(UUID id) throws JDSPersistenceException {
		List<JivsDocument> documents = documentService.getByCreator(id);
		return getDocumentWithParams(documents);
	}

	/**
	 * Gets the documents which were created on the same day as the given date
	 * @param date the date which to filter on
	 * @return List of documents
	 * @throws JDSPersistenceException if there is an error while establishing the database connection
	 * */
	public List<JivsDocument> getByCreatedAt(LocalDate date) throws JDSPersistenceException {
		List<JivsDocument> documents = documentService.getByCreatedAt(date.atStartOfDay());
		return getDocumentWithParams(documents);
	}

	/**
	 * Gets the documents for the provided customer id
	 * @param id the customer id of the document
	 * @return List of documents
	 * @throws JDSPersistenceException if there is an error while establishing the database connection
	 * */
	public List<JivsDocument> getByCustomerId(UUID id) throws JDSPersistenceException {
		List<JivsDocument> documents = documentService.getByCustomerId(id);
		return getDocumentWithParams(documents);
	}

	/**
	 * Gets the documents for the provided system id
	 * @param id the system id of the document
	 * @return List of documents
	 * @throws JDSPersistenceException if there is an error while establishing the database connection
	 * */
	public List<JivsDocument> getBySystemId(UUID id) throws JDSPersistenceException {
		List<JivsDocument> documents = documentService.getBySystemId(id);
		return getDocumentWithParams(documents);
	}

	/**
	 * Gets the document for the provided case id
	 * @param id the case id of the document
	 * @return List of documents
	 * @throws JDSPersistenceException if there is an error while establishing the database connection
	 * */
	public List<JivsDocument> getByCaseId(UUID id) throws JDSPersistenceException {
		List<JivsDocument> documents = documentService.getByCaseId(id);
		return getDocumentWithParams(documents);
	}

	/**
	 * Gets all documents
	 * @return List of documents
	 * @throws JDSPersistenceException if there is an error while establishing the database connection
	 * */
	public List<JivsDocument> getAllAsList() throws JDSPersistenceException {
		List<JivsDocument> documents = documentService.getAllAsList();
		return getDocumentWithParams(documents);
	}

	/**
	 * Updates the documents params
	 * @param id the id of the document
	 * @param params the params map of the document
	 * @throws JDSPersistenceException if there is an error while establishing the database connection
	 * */
	public void updateParams(UUID id, Map<String, String> params) throws JDSPersistenceException {
		documentService.updateParams(id, params);
	}

	/**
	 * Does not delete the document immediately. It sets the delete flag for the given document
	 * @param id the id of the document
	 * @throws JDSPersistenceException if there is an error while establishing the database connection
	 * */
	public boolean delete(UUID id) throws JDSPersistenceException {
		boolean deleted = documentService.delete(id);

		if (!deleted) {
			logger.debug("Could not delete document with id: {}", id);
		}

		return deleted;
	}

	private List<JivsDocument> getDocumentWithParams(List<JivsDocument> documents) throws JDSPersistenceException {
		List<JivsDocument> result = new ArrayList<>();
		for (JivsDocument document : documents) {
			JivsDocument documentWithParams = addParamsToDocument(document);
			result.add(documentWithParams);
		}
		return result;
	}

	private JivsDocument addParamsToDocument(JivsDocument document) throws JDSPersistenceException {
		Map<String, String> result = documentService.getParams(document.getId());
		if (!result.isEmpty()) {
			document.setParams(result);
		}
		return document;
	}

}
