package com.datamigration.jds.service;

import com.datamigration.jds.model.docstore.JivsDocStore;
import com.datamigration.jds.model.dto.DocStoreDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocStoreService {

	private static final Logger logger = LoggerFactory.getLogger(DocStoreService.class);

	public DocStoreDTO insert(JivsDocStore jivsDocStore) {

		return new DocStoreDTO();
	}
}
