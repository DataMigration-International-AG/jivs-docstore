package com.datamigration.jds.service;

import com.datamigration.jds.model.docstore.JivsDocument;
import com.datamigration.jds.model.dto.DocumentDTO;
import com.datamigration.jds.persistence.docstore.IDocumentDao;
import com.datamigration.jds.util.DTOUtil;
import com.datamigration.jds.util.exceptions.checked.JPEPersistenceException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocumentService {

	private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);
	private final IDocumentDao documentDao;

	public DocumentService(IDocumentDao documentDao) {
		this.documentDao = documentDao;
	}

	public DocumentDTO insert(JivsDocument docStore) throws JPEPersistenceException {
		DocumentDTO documentDTO = DTOUtil.toDocumentDTO(docStore);
		DocumentDTO insertedDocumentDTO = documentDao.insert(documentDTO);
		return insertedDocumentDTO;
	}

	public Optional<DocumentDTO> getById(UUID id) throws JPEPersistenceException {
		Optional<DocumentDTO> documentDTO = documentDao.getById(id);
		return documentDTO;
	}

	public Optional<List<DocumentDTO>> getAllAsList() throws JPEPersistenceException {
		Optional<List<DocumentDTO>> documentDTOs = documentDao.getAllAsList();
		return documentDTOs;
	}

	public void update(JivsDocument docStore) throws JPEPersistenceException {
		DocumentDTO documentDTO = DTOUtil.toDocumentDTO(docStore);
		documentDao.update(documentDTO);
	}

	public boolean delete(UUID id) throws JPEPersistenceException {
		boolean deleted = documentDao.updateDeleteFlag(id);
		return deleted;
	}
}
