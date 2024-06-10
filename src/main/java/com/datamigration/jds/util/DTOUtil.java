package com.datamigration.jds.util;

import com.datamigration.jds.model.docstore.JivsDocument;
import com.datamigration.jds.model.dto.DocumentDTO;

public class DTOUtil {

	public static DocumentDTO toDocumentDTO(JivsDocument docStore) {
		return new DocumentDTO(docStore.getId(), docStore.getFileBin(), docStore.getFilename(), docStore.getDocumentType(),
			docStore.getCreatorId(), docStore.getCreated(), docStore.getCustomerId(), docStore.getSystemId(),
			docStore.getCaseId(), docStore.getParams(), docStore.isDeleted());
	}
}
