package com.datamigration.jds.util;

import com.datamigration.jds.model.docstore.JivsDocument;
import com.datamigration.jds.model.dto.DocumentDTO;

public class DTOUtil {

	public static DocumentDTO toDocStoreDTO(JivsDocument docStore) {
		return new DocumentDTO(docStore.getId(), docStore.getFileBin(), docStore.getFilename(), docStore.getFiletype(),
			docStore.getCreator(), docStore.getCreated(), docStore.getCustomerFK(), docStore.getSystemFK(),
			docStore.getCaseId(), docStore.getParams(), docStore.isDeleted());
	}
}
