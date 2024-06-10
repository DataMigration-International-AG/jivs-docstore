package com.datamigration.jds.persistence.docstore;

import static com.datamigration.jds.persistence.DatabaseManager.connect;

import com.datamigration.jds.model.dto.DocumentDTO;
import com.datamigration.jds.util.exceptions.ErrorCode;
import com.datamigration.jds.util.exceptions.checked.JPEPersistenceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocumentDao implements IDocumentDao {

	private static final Logger logger = LoggerFactory.getLogger(DocumentDao.class);

	/**
	 * createTables - A method to create tables in the database.
	 *
	 * @throws JPEPersistenceException exception thrown if there is an issue with persistence
	 */
	@Override
	public void createTables() throws JPEPersistenceException {
		try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentSQLs.CREATE_DOCSTORE_TABLE_SQL)) {
			preparedStatement.execute();
		} catch (SQLException e) {
			throw new JPEPersistenceException(e, ErrorCode.DB_CREATE_ERROR);
		}
	}

	@Override
	public DocumentDTO insert(DocumentDTO documentDTO) throws JPEPersistenceException {
		DocumentDTO result;

		try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentSQLs.INSERT_DOCUMENT_SQL, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setBytes(1, documentDTO.fileBin());
			preparedStatement.setString(2, documentDTO.fileName());
			preparedStatement.setString(3, documentDTO.documenType());
			preparedStatement.setObject(4, documentDTO.creator());
			preparedStatement.setObject(5, documentDTO.created());
			preparedStatement.setObject(6, documentDTO.customerFK());
			preparedStatement.setObject(7, documentDTO.systemFk());
			preparedStatement.setObject(8, documentDTO.caseId());
			preparedStatement.setBoolean(9, documentDTO.deleted());
			try (ResultSet rs = preparedStatement.executeQuery()) {
				if (rs.next()) {
					UUID id = UUID.fromString(rs.getString(1));
					result = new DocumentDTO(id, documentDTO.fileBin(), documentDTO.fileName(), documentDTO.documenType(),
						documentDTO.creator(), documentDTO.created(), documentDTO.customerFK(), documentDTO.systemFk(),
						documentDTO.caseId(), documentDTO.params(), documentDTO.deleted());
				} else {
					throw new JPEPersistenceException(ErrorCode.DB_NO_RESULT_ERROR);
				}
			} catch (SQLException e) {
				throw new JPEPersistenceException(e, ErrorCode.DB_WRITE_ERROR);
			}
		} catch (SQLException e) {
			throw new JPEPersistenceException(e, ErrorCode.DB_WRITE_ERROR);
		}
		return result;
	}

	@Override
	public void update(DocumentDTO documentDTO) throws JPEPersistenceException {

	}

	@Override
	public Optional<DocumentDTO> getById(UUID id) throws JPEPersistenceException {
		Optional<DocumentDTO> result = Optional.empty();
		try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentSQLs.SELECT_DOCUMENT_BY_ID)) {
			preparedStatement.setObject(1, id);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				if (rs.next()) {
					DocumentDTO documentDTO = createDocumentDTO(rs);
					result = Optional.of(documentDTO);
				}
			}
		} catch (Exception e) {
			throw new JPEPersistenceException(e, ErrorCode.DB_READ_ERROR);
		}
		return result;
	}

	@Override
	public Optional<List<DocumentDTO>> getByDocumentType(String documentType) throws JPEPersistenceException {
		Optional<List<DocumentDTO>> result;
		try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentSQLs.SELECT_DOCUMENT_BY_DOCUMENT_TYPE_SQL)) {
			preparedStatement.setString(1, documentType);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				List<DocumentDTO> list = new ArrayList<>();
				while (rs.next()) {
					DocumentDTO documentDTO = createDocumentDTO(rs);
					list.add(documentDTO);
				}
				result = Optional.of(list);
			}
		} catch (Exception e) {
			throw new JPEPersistenceException(e, ErrorCode.DB_READ_ERROR);
		}
		return result;
	}

	@Override
	public Optional<DocumentDTO> getByFileName(String fileName) throws JPEPersistenceException {
		Optional<DocumentDTO> result = Optional.empty();
		try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentSQLs.SELECT_DOCUMENT_BY_FILENAME_SQL)) {
			preparedStatement.setString(1, fileName);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				if (rs.next()) {
					DocumentDTO documentDTO = createDocumentDTO(rs);
					result = Optional.of(documentDTO);
				}
			}
		} catch (Exception e) {
			throw new JPEPersistenceException(e, ErrorCode.DB_READ_ERROR);
		}
		return result;
	}

	@Override
	public Optional<List<DocumentDTO>> getByCreator(UUID id) throws JPEPersistenceException {
		Optional<List<DocumentDTO>> result;
		try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentSQLs.SELECT_DOCUMENT_BY_CREATOR_SQL)) {
			preparedStatement.setObject(1, id);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				List<DocumentDTO> list = new ArrayList<>();
				while (rs.next()) {
					DocumentDTO documentDTO = createDocumentDTO(rs);
					list.add(documentDTO);
				}
				result = Optional.of(list);
			}
		} catch (Exception e) {
			throw new JPEPersistenceException(e, ErrorCode.DB_READ_ERROR);
		}
		return result;
	}

	@Override
	public Optional<List<DocumentDTO>> getByCreatedAt(LocalDateTime dateTime) throws JPEPersistenceException {
		Optional<List<DocumentDTO>> result;
		try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentSQLs.SELECT_DOCUMENT_BY_CREATED_SQL)) {
			preparedStatement.setObject(1, dateTime);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				List<DocumentDTO> list = new ArrayList<>();
				while (rs.next()) {
					DocumentDTO documentDTO = createDocumentDTO(rs);
					list.add(documentDTO);
				}
				result = Optional.of(list);
			}
		} catch (Exception e) {
			throw new JPEPersistenceException(e, ErrorCode.DB_READ_ERROR);
		}
		return result;
	}

	@Override
	public Optional<List<DocumentDTO>> getByCustomerId(UUID id) throws JPEPersistenceException {
		Optional<List<DocumentDTO>> result;
		try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentSQLs.SELECT_DOCUMENT_BY_CUSTOMER_FK_SQL)) {
			preparedStatement.setObject(1, id);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				List<DocumentDTO> list = new ArrayList<>();
				while (rs.next()) {
					DocumentDTO documentDTO = createDocumentDTO(rs);
					list.add(documentDTO);
				}
				result = Optional.of(list);
			}
		} catch (Exception e) {
			throw new JPEPersistenceException(e, ErrorCode.DB_READ_ERROR);
		}
		return result;
	}

	@Override
	public Optional<List<DocumentDTO>> getBySystemId(UUID id) throws JPEPersistenceException {
		Optional<List<DocumentDTO>> result;
		try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentSQLs.SELECT_DOCUMENT_BY_SYSTEM_FK_SQL)) {
			preparedStatement.setObject(1, id);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				List<DocumentDTO> list = new ArrayList<>();
				while (rs.next()) {
					DocumentDTO documentDTO = createDocumentDTO(rs);
					list.add(documentDTO);
				}
				result = Optional.of(list);
			}
		} catch (Exception e) {
			throw new JPEPersistenceException(e, ErrorCode.DB_READ_ERROR);
		}
		return result;
	}

	@Override
	public Optional<List<DocumentDTO>> getByCaseId(UUID id) throws JPEPersistenceException {
		Optional<List<DocumentDTO>> result;
		try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentSQLs.SELECT_DOCUMENT_BY_CASE_ID_SQL)) {
			preparedStatement.setObject(1, id);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				List<DocumentDTO> processList = new ArrayList<>();
				while (rs.next()) {
					DocumentDTO documentDTO = createDocumentDTO(rs);
					processList.add(documentDTO);
				}
				result = Optional.of(processList);
			} catch (SQLException e) {
				throw new JPEPersistenceException(e, ErrorCode.DB_READ_ERROR);
			}
		} catch (SQLException e) {
			throw new JPEPersistenceException(e, ErrorCode.DB_READ_ERROR);
		}
		return result;
	}

	@Override
	public Optional<List<DocumentDTO>> getAllAsList() throws JPEPersistenceException {
		Optional<List<DocumentDTO>> result;
		try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentSQLs.SELECT_ALL_DOCUMENTS_SQL)) {
			try (ResultSet rs = preparedStatement.executeQuery()) {
				List<DocumentDTO> processList = new ArrayList<>();
				while (rs.next()) {
					DocumentDTO documentDTO = createDocumentDTO(rs);
					processList.add(documentDTO);
				}
				result = Optional.of(processList);
			} catch (SQLException e) {
				throw new JPEPersistenceException(e, ErrorCode.DB_READ_ERROR);
			}
		} catch (SQLException e) {
			throw new JPEPersistenceException(e, ErrorCode.DB_READ_ERROR);
		}
		return result;
	}

//	@Override
//	public void update(DocumentDTO documentDTO) throws JPEPersistenceException {
//		try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(
//			IDocumentSQLs.UPDATE)) {
//			preparedStatement.setBytes(1, documentDTO.fileBin());
//			preparedStatement.setString(2, documentDTO.fileName());
//			preparedStatement.setString(3, documentDTO.documenType());
//			preparedStatement.setObject(4, documentDTO.creator());
//			preparedStatement.setObject(5, documentDTO.customerFK());
//			preparedStatement.setObject(6, documentDTO.systemFk());
//			preparedStatement.setObject(7, documentDTO.caseId());
//			preparedStatement.setBoolean(8, documentDTO.deleted());
//			preparedStatement.executeUpdate();
//		} catch (SQLException e) {
//			throw new JPEPersistenceException(e, ErrorCode.DB_WRITE_ERROR);
//		}
//	}


	@Override
	public boolean updateDeleteFlag(UUID id) throws JPEPersistenceException {
		int result;
		try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentSQLs.UPDATE_DOCUMENT_DELETE_FLAG_SQL)) {
			preparedStatement.setBoolean(1, true);
			preparedStatement.setObject(2, id);
			result = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new JPEPersistenceException(e, ErrorCode.DB_WRITE_ERROR);
		}
		return result == 1;
	}

	/**
	 * Creates the document dto from result set.
	 *
	 * @param rs result set
	 * @return document dto
	 * @throws SQLException Exception while extracting result
	 */
	private DocumentDTO createDocumentDTO(ResultSet rs) throws SQLException {
		UUID id = UUID.fromString(rs.getString(1));
		byte[] fileBin = rs.getBytes(2);
		String fileName = rs.getString(3);
		String fileType = rs.getString(4);
		UUID creator = UUID.fromString(rs.getString(5));
		LocalDateTime created = rs.getObject(6, LocalDateTime.class);
		UUID customerFK = UUID.fromString(rs.getString(7));
		UUID systemFK = UUID.fromString(rs.getString(8));
		UUID caseId = UUID.fromString(rs.getString(9));
		boolean deleted = rs.getBoolean(10);
		Map<String, String> params = new HashMap<>();

		DocumentDTO documentDTO = new DocumentDTO(id, fileBin, fileName, fileType, creator, created, customerFK,
			systemFK, caseId, params, deleted);

		return documentDTO;
	}
}
