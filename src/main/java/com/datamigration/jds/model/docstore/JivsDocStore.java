package com.datamigration.jds.model.docstore;

import com.datamigration.jds.model.dto.DocStoreDTO;
import com.datamigration.jds.service.DocStoreService;
import java.time.LocalDateTime;
import java.util.UUID;

public class JivsDocStore {

	private final DocStoreService docStoreService = new DocStoreService();

	private UUID id;
	private UUID customerFK;
	private UUID systemFK;
	private UUID caseId;
	private UUID creator;
	private String fileBin;
	private String filename;
	private String filetype;
	private String params;
	private boolean deleted;
	private LocalDateTime created;

	public JivsDocStore(String fileBin, String filename, String filetype, UUID creator, UUID customerFK,
		boolean deleted, LocalDateTime created) {
		this.fileBin = fileBin;
		this.filename = filename;
		this.filetype = filetype;
		this.creator = creator;
		this.customerFK = customerFK;
		this.deleted = deleted;
		this.created = created;
	}

	public DocStoreDTO create(JivsDocStore jivsDocStore) {
		return docStoreService.insert(jivsDocStore);
	}
}
