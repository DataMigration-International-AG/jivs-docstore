package com.datamigration.jds.model.docstore;

import java.time.LocalDateTime;
import java.util.UUID;

public class JivsDocument {

	private UUID id;
	private UUID customerFK;
	private UUID systemFK;
	private UUID caseId;
	private UUID creator;
	private byte[] fileBin;
	private String filename;
	private String filetype;
	private String params;
	private boolean deleted;
	private LocalDateTime created;

	public JivsDocument(byte[] fileBin, String filename, String filetype, UUID creator, UUID customerFK,
		boolean deleted) {
		this.fileBin = fileBin;
		this.filename = filename;
		this.filetype = filetype;
		this.creator = creator;
		this.customerFK = customerFK;
		this.deleted = deleted;
	}

	public UUID getId() {
		return id;
	}

	public UUID getCustomerFK() {
		return customerFK;
	}

	public UUID getSystemFK() {
		return systemFK;
	}

	public UUID getCaseId() {
		return caseId;
	}

	public UUID getCreator() {
		return creator;
	}

	public byte[] getFileBin() {
		return fileBin;
	}

	public String getFilename() {
		return filename;
	}

	public String getFiletype() {
		return filetype;
	}

	public String getParams() {
		return params;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public LocalDateTime getCreated() {
		return created;
	}
}
