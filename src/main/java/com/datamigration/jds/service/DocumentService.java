package com.datamigration.jds.service;

import com.datamigration.jds.model.docstore.JivsDocument;
import com.datamigration.jds.model.dto.DocumentDTO;
import com.datamigration.jds.persistence.docstore.IDocumentDao;
import com.datamigration.jds.util.DTOUtil;
import com.datamigration.jds.util.exceptions.checked.JPEPersistenceException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocumentService {

	private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);
	private final IDocumentDao docStoreDao;

	public DocumentService(IDocumentDao docStoreDao) {
		this.docStoreDao = docStoreDao;
	}

	public DocumentDTO insert(JivsDocument docStore) throws JPEPersistenceException {
		DocumentDTO documentDTO = DTOUtil.toDocStoreDTO(docStore);
		DocumentDTO insertedDocumentDTO = docStoreDao.insert(documentDTO);
		return insertedDocumentDTO;
	}

	public boolean delete(UUID id) throws JPEPersistenceException {
		boolean deleted = docStoreDao.delete(id);
		return deleted;
	}
}
