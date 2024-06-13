package com.datamigration.jds.model.dto;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record DocumentDTO(UUID id, byte[] fileBin, String filename, String documenType, UUID creatorId,
						  LocalDateTime created, UUID customerId, UUID systemId, UUID caseId, Map<String, String> params,
						  boolean deleted) {

}
