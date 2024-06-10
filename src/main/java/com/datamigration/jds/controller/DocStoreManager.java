package com.datamigration.jds.controller;

import com.datamigration.jds.model.entity.docstore.JivsDocument;
import com.datamigration.jds.model.dto.DocumentDTO;
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

	public DocumentDTO create(JivsDocument jivsDocument) throws JDSPersistenceException {
		DocumentDTO documentDTO = documentService.insert(jivsDocument);
		return documentDTO;
	}

	/**
	 * Gets the document by the given id
	 * @param id the id of the document
	 * @return DocumentDTO
	 * @throws JDSPersistenceException if there is an error while establishing the database connection
	 * */
	public DocumentDTO getById(UUID id) throws JDSPersistenceException {
		Optional<DocumentDTO> documentDTO = documentService.getById(id);

		if (documentDTO.isEmpty()) {
			logger.info("Document with id {} not found", id);
			return null;
		}

		DocumentDTO documentDTOWithParams = getDocumentParams(documentDTO.get());
		return documentDTOWithParams;
	}

	/**
	 * Gets the document by the given filename
	 * @param fileName the fileName of the document
	 * @return DocumentDTO
	 * @throws JDSPersistenceException if there is an error while establishing the database connection
	 * */
	public DocumentDTO getByFileName(String fileName) throws JDSPersistenceException {
		Optional<DocumentDTO> documentDTO = documentService.getByFileName(fileName);

		if (documentDTO.isEmpty()) {
			logger.info("Document with name {} not found", fileName);
			return null;
		}

		DocumentDTO documentDTOWithParams = getDocumentParams(documentDTO.get());
		return documentDTOWithParams;
	}

	/**
	 * Gets the documents for the provided document type
	 * @param documentType the type of the document
	 * @return List of DocumentDTOs
	 * @throws JDSPersistenceException if there is an error while establishing the database connection
	 * */
	public List<DocumentDTO> getByDocumentType(String documentType) throws JDSPersistenceException {
		List<DocumentDTO> documentDTOs = documentService.getByDocumentType(documentType);
		return getDocumentWithParams(documentDTOs);
	}

	/**
	 * Gets the documents for the provided creatorId id
	 * @param id the creatorId id of the document
	 * @return List of DocumentDTOs
	 * @throws JDSPersistenceException if there is an error while establishing the database connection
	 * */
	public List<DocumentDTO> getByCreator(UUID id) throws JDSPersistenceException {
		List<DocumentDTO> documentDTOs = documentService.getByCreator(id);
		return getDocumentWithParams(documentDTOs);
	}

	/**
	 * Gets the documents which were created on the same day as the given date
	 * @param date the date which to filter on
	 * @return List of DocumentDTOs
	 * @throws JDSPersistenceException if there is an error while establishing the database connection
	 * */
	public List<DocumentDTO> getByCreatedAt(LocalDate date) throws JDSPersistenceException {
		List<DocumentDTO> documentDTOs = documentService.getByCreatedAt(date.atStartOfDay());
		return getDocumentWithParams(documentDTOs);
	}

	/**
	 * Gets the documents for the provided customer id
	 * @param id the customer id of the document
	 * @return List of DocumentDTOs
	 * @throws JDSPersistenceException if there is an error while establishing the database connection
	 * */
	public List<DocumentDTO> getByCustomerId(UUID id) throws JDSPersistenceException {
		List<DocumentDTO> documentDTOs = documentService.getByCustomerId(id);
		return getDocumentWithParams(documentDTOs);
	}

	/**
	 * Gets the documents for the provided system id
	 * @param id the system id of the document
	 * @return List of DocumentDTOs
	 * @throws JDSPersistenceException if there is an error while establishing the database connection
	 * */
	public List<DocumentDTO> getBySystemId(UUID id) throws JDSPersistenceException {
		List<DocumentDTO> documentDTOs = documentService.getBySystemId(id);
		return getDocumentWithParams(documentDTOs);
	}

	/**
	 * Gets the document for the provided case id
	 * @param id the case id of the document
	 * @return List of DocumentDTOs
	 * @throws JDSPersistenceException if there is an error while establishing the database connection
	 * */
	public List<DocumentDTO> getByCaseId(UUID id) throws JDSPersistenceException {
		List<DocumentDTO> documentDTOs = documentService.getByCaseId(id);
		return getDocumentWithParams(documentDTOs);
	}

	/**
	 * Gets all documents
	 * @return List of DocumentDTOs
	 * @throws JDSPersistenceException if there is an error while establishing the database connection
	 * */
	public List<DocumentDTO> getAllAsList() throws JDSPersistenceException {
		List<DocumentDTO> documentDTOs = documentService.getAllAsList();
		return getDocumentWithParams(documentDTOs);
	}

	/**
	 * Updates the documents params
	 * @param id the id of the document
	 * @param params the params map of the document
	 * @return param map
	 * @throws JDSPersistenceException if there is an error while establishing the database connection
	 * */
	public Map<String, String> updateParams(UUID id, Map<String, String> params) throws JDSPersistenceException {
		Map<String, String> result = documentService.updateParams(id, params);
		return result;
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

	private List<DocumentDTO> getDocumentWithParams(List<DocumentDTO> documentDTOList) throws JDSPersistenceException {
		List<DocumentDTO> result = new ArrayList<>();
		for (DocumentDTO documentDTO : documentDTOList) {
			DocumentDTO documentDTOWithParams = getDocumentParams(documentDTO);
			result.add(documentDTOWithParams);
		}
		return result;
	}

	private DocumentDTO getDocumentParams(DocumentDTO documentDTO) throws JDSPersistenceException {
		Map<String, String> result = documentService.getParams(documentDTO.id());
		if (!result.isEmpty()) {
			documentDTO.params().putAll(result);
		}
		return documentDTO;
	}

}
