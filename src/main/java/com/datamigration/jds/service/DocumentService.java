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

	public Optional<DocumentDTO> getById(UUID id) throws JPEPersistenceException {
		Optional<DocumentDTO> documentDTO = documentDao.getById(id);
		return documentDTO;
	}

	public Optional<DocumentDTO> getByFileName(String fileName) throws JPEPersistenceException {
		Optional<DocumentDTO> documentDTO = documentDao.getByFileName(fileName);
		return documentDTO;
	}

	public Optional<List<DocumentDTO>> getByDocumentType(String documentType) throws JPEPersistenceException {
		Optional<List<DocumentDTO>> documentDTOs = documentDao.getByDocumentType(documentType);
		return documentDTOs;
	}

	public Optional<List<DocumentDTO>> getByCreator(UUID id) throws JPEPersistenceException {
		Optional<List<DocumentDTO>> documentDTOs = documentDao.getByCreator(id);
		return documentDTOs;
	}

	public Optional<List<DocumentDTO>> getByCreatedAt(LocalDateTime dateTime) throws JPEPersistenceException {
		Optional<List<DocumentDTO>> documentDTOs = documentDao.getByCreatedAt(dateTime);
		return documentDTOs;
	}

	public Optional<List<DocumentDTO>> getByCustomerId(UUID id) throws JPEPersistenceException {
		Optional<List<DocumentDTO>> documentDTOs = documentDao.getByCustomerId(id);
		return documentDTOs;
	}

	public Optional<List<DocumentDTO>> getBySystemId(UUID id) throws JPEPersistenceException {
		Optional<List<DocumentDTO>> documentDTOs = documentDao.getBySystemId(id);
		return documentDTOs;
	}

	public Optional<List<DocumentDTO>> getByCaseId(UUID id) throws JPEPersistenceException {
		Optional<List<DocumentDTO>> documentDTOs = documentDao.getByCaseId(id);
		return documentDTOs;
	}

	public Optional<List<DocumentDTO>> getAllAsList() throws JPEPersistenceException {
		Optional<List<DocumentDTO>> documentDTOs = documentDao.getAllAsList();
		return documentDTOs;
	}

	public boolean delete(UUID id) throws JPEPersistenceException {
		boolean deleted = documentDao.updateDeleteFlag(id);
		return deleted;
	}

	public Optional<Map<String, String>> getParams(UUID id) throws JPEPersistenceException {
		Optional<Map<String, String>> params = documentParamDao.getParams(id);
		return params;
	}

	public void updateParams(UUID id, Map<String, String> params) throws JPEPersistenceException {
		documentParamDao.updateParams(id, params);
	}
}
