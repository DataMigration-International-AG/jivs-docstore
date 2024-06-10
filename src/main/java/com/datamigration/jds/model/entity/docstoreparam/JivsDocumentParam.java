package com.datamigration.jds.model.entity.docstoreparam;

import java.util.Map;
import java.util.UUID;

public class JivsDocumentParam {

	private UUID documentId;
	private Map<String, String> params;

	public JivsDocumentParam(UUID documentId, Map<String, String> params) {
		this.documentId = documentId;
		this.params = params;
	}

	public UUID getDocumentId() {
		return documentId;
	}

	public void setDocumentId(UUID documentId) {
		this.documentId = documentId;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}
}
