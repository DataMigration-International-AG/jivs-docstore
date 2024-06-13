package com.datamigration.jds.service;

import com.datamigration.jds.model.entity.docstore.JivsDocument;
import com.datamigration.jds.model.entity.docstoreparam.JivsDocumentParam;
import com.datamigration.jds.persistence.docstore.IDocumentDao;
import com.datamigration.jds.persistence.param.IDocumentParamDao;
import com.datamigration.jds.util.exceptions.checked.JDSPersistenceException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocumentService {

	private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);
	private final IDocumentDao documentDao;
	private final IDocumentParamDao documentParamDao;

	public DocumentService(IDocumentDao documentDao, IDocumentParamDao documentParamDao) {
		this.documentDao = documentDao;
		this.documentParamDao = documentParamDao;
	}

	/**
	 * Inserts the document into the database. And also inserts the params associated with the document
	 * if they are not null.
	 *
	 * @param document the document
	 * @return the document
	 * @throws JDSPersistenceException if an error occurs during persisting
	 */
	public JivsDocument insert(JivsDocument document) throws JDSPersistenceException {
		JivsDocument insertedDocument = documentDao.insert(document);

		if (document.getParams() != null && !document.getParams().isEmpty()) {
			JivsDocumentParam jivsDocumentParams = new JivsDocumentParam(insertedDocument.getId(), document.getParams());
			documentParamDao.insert(jivsDocumentParams);
		}

		return insertedDocument;
	}

	/**
	 * Gets a document by its id.
	 *
	 * @param id the id of the document
	 * @return the document
	 * @throws JDSPersistenceException if an error occurs during the get
	 */
	public Optional<JivsDocument> getById(UUID id) throws JDSPersistenceException {
		Optional<JivsDocument> document = documentDao.getById(id);
		return document;
	}

	/**
	 * Gets a document by its filename.
	 *
	 * @param fileName the id of the document
	 * @return the document
	 * @throws JDSPersistenceException if an error occurs during the get
	 */
	public Optional<JivsDocument> getByFileName(String fileName) throws JDSPersistenceException {
		Optional<JivsDocument> document = documentDao.getByFileName(fileName);
		return document;
	}

	/**
	 * Gets an optional list of documents op by the document type.
	 *
	 * @param documentType the type of the document
	 * @return a list of documents
	 * @throws JDSPersistenceException if an error occurs during the get
	 */
	public List<JivsDocument> getByDocumentType(String documentType) throws JDSPersistenceException {
		List<JivsDocument> documents = documentDao.getByDocumentType(documentType);
		return documents;
	}

	/**
	 * Gets an optional list of documents op by the creator id.
	 *
	 * @param id the creator id of the document
	 * @return a list of documents
	 * @throws JDSPersistenceException if an error occurs during the get
	 */
	public List<JivsDocument> getByCreator(UUID id) throws JDSPersistenceException {
		List<JivsDocument> documents = documentDao.getByCreator(id);
		return documents;
	}

	/**
	 * Gets an optional list of documents by the creation date.
	 *
	 * @param dateTime the date of the document
	 * @return a list of documents
	 * @throws JDSPersistenceException if an error occurs during the get
	 */
	public List<JivsDocument> getByCreatedAt(LocalDateTime dateTime) throws JDSPersistenceException {
		List<JivsDocument> documents = documentDao.getByCreatedAt(dateTime);
		return documents;
	}

	/**
	 * Gets an optional list of documents by the customer id.
	 *
	 * @param id the customer id of the document
	 * @return a list of documents
	 * @throws JDSPersistenceException if an error occurs during the get
	 */
	public List<JivsDocument> getByCustomerId(UUID id) throws JDSPersistenceException {
		List<JivsDocument> documents = documentDao.getByCustomerId(id);
		return documents;
	}

	/**
	 * Gets an optional list of documents by the system id.
	 *
	 * @param id the system id of the document
	 * @return a list of documents
	 * @throws JDSPersistenceException if an error occurs during the get
	 */
	public List<JivsDocument> getBySystemId(UUID id) throws JDSPersistenceException {
		List<JivsDocument> documents = documentDao.getBySystemId(id);
		return documents;
	}

	/**
	 * Gets an optional list of documents by the case is.
	 *
	 * @param id the case id of the document
	 * @return a list of documents
	 * @throws JDSPersistenceException if an error occurs during the get
	 */
	public List<JivsDocument> getByCaseId(UUID id) throws JDSPersistenceException {
		List<JivsDocument> documents = documentDao.getByCaseId(id);
		return documents;
	}

	/**
	 * Gets an optional list of all documents.
	 *
	 * @return a list of documents
	 * @throws JDSPersistenceException if an error occurs during the get
	 */
	public List<JivsDocument> getAllAsList() throws JDSPersistenceException {
		List<JivsDocument> documents = documentDao.getAllAsList();
		return documents;
	}

	/**
	 * Updates the delete flag of the document to do a soft delete.
	 *
	 * @param id the id of the document
	 * @return the boolean
	 * @throws JDSPersistenceException if an error occurs during the get
	 */
	public boolean delete(UUID id) throws JDSPersistenceException {
		boolean deleted = documentDao.setDeleteFlagTrue(id);
		return deleted;
	}

	/**
	 * Gets the params of the document.
	 *
	 * @param id the id of the document
	 * @return the param map
	 * @throws JDSPersistenceException if an error occurs during the get
	 */
	public Map<String, String> getParams(UUID id) throws JDSPersistenceException {
		Map<String, String> params = documentParamDao.getParams(id);
		return params;
	}

	/**
	 * Updates the documents params of the document.
	 *
	 * @param id the id of the document
	 * @param params the params of the document
	 * @throws JDSPersistenceException if an error occurs during the get
	 */
	public void updateParams(UUID id, Map<String, String> params) throws JDSPersistenceException {
		documentParamDao.updateParams(id, params);
	}
}
