package com.datamigration.jds.service;

import com.datamigration.jds.model.docstore.JivsDocument;
import com.datamigration.jds.model.docstoreparam.JivsDocumentParam;
import com.datamigration.jds.model.dto.DocumentDTO;
import com.datamigration.jds.persistence.docstore.IDocumentDao;
import com.datamigration.jds.persistence.param.IDocumentParamDao;
import com.datamigration.jds.util.DTOUtil;
import com.datamigration.jds.util.exceptions.checked.JPEPersistenceException;
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
	 * @throws JPEPersistenceException if an error occurs during persisting
	 */
	public DocumentDTO insert(JivsDocument document) throws JPEPersistenceException {
		DocumentDTO documentDTO = DTOUtil.toDocumentDTO(document);
		DocumentDTO insertedDocumentDTO = documentDao.insert(documentDTO);

		if (document.getParams() != null && !document.getParams().isEmpty()) {
			JivsDocumentParam jivsDocumentParams = new JivsDocumentParam(insertedDocumentDTO.id(), document.getParams());
			documentParamDao.insert(jivsDocumentParams);
		}

		document.setId(insertedDocumentDTO.id());
		return DTOUtil.toDocumentDTO(document);
	}

	/**
	 * Gets a document by its id.
	 *
	 * @param id the id of the document
	 * @return the document
	 * @throws JPEPersistenceException if an error occurs during the get
	 */
	public Optional<DocumentDTO> getById(UUID id) throws JPEPersistenceException {
		Optional<DocumentDTO> documentDTO = documentDao.getById(id);
		return documentDTO;
	}

	/**
	 * Gets a document by its fileName.
	 *
	 * @param fileName the id of the document
	 * @return the document
	 * @throws JPEPersistenceException if an error occurs during the get
	 */
	public Optional<DocumentDTO> getByFileName(String fileName) throws JPEPersistenceException {
		Optional<DocumentDTO> documentDTO = documentDao.getByFileName(fileName);
		return documentDTO;
	}

	/**
	 * Gets an optional list of documents op by the document type.
	 *
	 * @param documentType the type of the document
	 * @return an optional list of documents
	 * @throws JPEPersistenceException if an error occurs during the get
	 */
	public Optional<List<DocumentDTO>> getByDocumentType(String documentType) throws JPEPersistenceException {
		Optional<List<DocumentDTO>> documentDTOs = documentDao.getByDocumentType(documentType);
		return documentDTOs;
	}

	/**
	 * Gets an optional list of documents op by the creator id.
	 *
	 * @param id the creator id of the document
	 * @return an optional list of documents
	 * @throws JPEPersistenceException if an error occurs during the get
	 */
	public Optional<List<DocumentDTO>> getByCreator(UUID id) throws JPEPersistenceException {
		Optional<List<DocumentDTO>> documentDTOs = documentDao.getByCreator(id);
		return documentDTOs;
	}

	/**
	 * Gets an optional list of documents by the creation date.
	 *
	 * @param dateTime the date of the document
	 * @return  an optional list of documents
	 * @throws JPEPersistenceException if an error occurs during the get
	 */
	public Optional<List<DocumentDTO>> getByCreatedAt(LocalDateTime dateTime) throws JPEPersistenceException {
		Optional<List<DocumentDTO>> documentDTOs = documentDao.getByCreatedAt(dateTime);
		return documentDTOs;
	}

	/**
	 * Gets an optional list of documents by the customer id.
	 *
	 * @param id the customer id of the document
	 * @return an optional list of documents
	 * @throws JPEPersistenceException if an error occurs during the get
	 */
	public Optional<List<DocumentDTO>> getByCustomerId(UUID id) throws JPEPersistenceException {
		Optional<List<DocumentDTO>> documentDTOs = documentDao.getByCustomerId(id);
		return documentDTOs;
	}

	/**
	 * Gets an optional list of documents by the system id.
	 *
	 * @param id the system id of the document
	 * @return an optional list of documents
	 * @throws JPEPersistenceException if an error occurs during the get
	 */
	public Optional<List<DocumentDTO>> getBySystemId(UUID id) throws JPEPersistenceException {
		Optional<List<DocumentDTO>> documentDTOs = documentDao.getBySystemId(id);
		return documentDTOs;
	}

	/**
	 * Gets an optional list of documents by the case is.
	 *
	 * @param id the case id of the document
	 * @return an optional list of documents
	 * @throws JPEPersistenceException if an error occurs during the get
	 */
	public Optional<List<DocumentDTO>> getByCaseId(UUID id) throws JPEPersistenceException {
		Optional<List<DocumentDTO>> documentDTOs = documentDao.getByCaseId(id);
		return documentDTOs;
	}

	/**
	 * Gets an optional list of all documents.
	 *
	 * @return an optional list of documents
	 * @throws JPEPersistenceException if an error occurs during the get
	 */
	public Optional<List<DocumentDTO>> getAllAsList() throws JPEPersistenceException {
		Optional<List<DocumentDTO>> documentDTOs = documentDao.getAllAsList();
		return documentDTOs;
	}

	/**
	 * Updates the delete flag of the document to do a soft delete.
	 *
	 * @param id the id of the document
	 * @return the boolean
	 * @throws JPEPersistenceException if an error occurs during the get
	 */
	public boolean delete(UUID id) throws JPEPersistenceException {
		boolean deleted = documentDao.updateDeleteFlag(id);
		return deleted;
	}

	/**
	 * Gets an optional list of documents by the document type.
	 *
	 * @param id the id of the document
	 * @return the param map
	 * @throws JPEPersistenceException if an error occurs during the get
	 */
	public Optional<Map<String, String>> getParams(UUID id) throws JPEPersistenceException {
		Optional<Map<String, String>> params = documentParamDao.getParams(id);
		return params;
	}

	/**
	 * Updates the documents params of the document.
	 *
	 * @param id the id of the document
	 * @param params the params of the document
	 * @throws JPEPersistenceException if an error occurs during the get
	 */
	public void updateParams(UUID id, Map<String, String> params) throws JPEPersistenceException {
		documentParamDao.updateParams(id, params);
	}
}
