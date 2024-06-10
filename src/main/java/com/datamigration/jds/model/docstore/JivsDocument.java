package com.datamigration.jds.model.docstore;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class JivsDocument {

	private UUID id;
	private UUID customerFK;
	private UUID systemFK;
	private UUID caseId;
	private UUID creator;
	private byte[] fileBin;
	private Map<String, String> params;
	private String filename;
	private String documentType;
	private boolean deleted;
	private LocalDateTime created;

	public JivsDocument(byte[] fileBin, String filename, String documentType, UUID creator, UUID customerFK, UUID systemFK,
		UUID caseId, Map<String, String> params) {
		this.fileBin = fileBin;
		this.filename = filename;
		this.documentType = documentType;
		this.creator = creator;
		this.customerFK = customerFK;
		this.systemFK = systemFK;
		this.caseId = caseId;
		this.params = params;
		this.deleted = false;
		this.created = LocalDateTime.now();
	}

	public JivsDocument(byte[] fileBin, String filename, String documentType, UUID creator, UUID customerFK, UUID systemFK,
		UUID caseId, Map<String, String> params, boolean deleted) {
		this.fileBin = fileBin;
		this.filename = filename;
		this.documentType = documentType;
		this.creator = creator;
		this.customerFK = customerFK;
		this.systemFK = systemFK;
		this.caseId = caseId;
		this.params = params;
		this.deleted = deleted;
		this.created = LocalDateTime.now();
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

	public String getDocumentType() {
		return documentType;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public void setCustomerFK(UUID customerFK) {
		this.customerFK = customerFK;
	}

	public void setSystemFK(UUID systemFK) {
		this.systemFK = systemFK;
	}

	public void setCaseId(UUID caseId) {
		this.caseId = caseId;
	}

	public void setCreator(UUID creator) {
		this.creator = creator;
	}

	public void setFileBin(byte[] fileBin) {
		this.fileBin = fileBin;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}
}
