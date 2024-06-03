package com.datamigration.jds.model.dto;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record DocumentDTO(UUID id, byte[] fileBin, String fileName, String fileType, UUID creator,
						  LocalDateTime created, UUID customerFK, UUID systemFk, UUID caseId, Map<String, String> params,
						  boolean deleted) {

}
