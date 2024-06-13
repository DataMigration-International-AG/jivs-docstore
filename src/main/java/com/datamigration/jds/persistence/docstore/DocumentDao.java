package com.datamigration.jds.persistence.docstore;

import com.datamigration.jds.model.entity.docstore.JivsDocument;
import com.datamigration.jds.persistence.DatabaseManager;
import com.datamigration.jds.util.exceptions.ErrorCode;
import com.datamigration.jds.util.exceptions.checked.JDSPersistenceException;
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
	 * @throws JDSPersistenceException exception thrown if there is an issue with persistence
	 */
	@Override
	public void createTables() throws JDSPersistenceException {
		
		try (Connection connection =  DatabaseManager
			.getInstance().connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentSQLs.CREATE_DOCSTORE_TABLE_SQL)) {
			preparedStatement.execute();
			logger.info("JIVS-DOCTSTORE table created.");
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new JDSPersistenceException(e, ErrorCode.DB_CREATE_ERROR);
		}
	}

	@Override
	public JivsDocument insert(JivsDocument document) throws JDSPersistenceException {
		JivsDocument result;

		try (Connection connection =  DatabaseManager.getInstance().connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentSQLs.INSERT_DOCUMENT_SQL)) {
			preparedStatement.setBytes(1, document.getFileBin());
			preparedStatement.setString(2, document.getFilename());
			preparedStatement.setString(3, document.getDocumentType());
			preparedStatement.setObject(4, document.getCreatorId());
			preparedStatement.setObject(5, document.getCreatedAt());
			preparedStatement.setObject(6, document.getCustomerId());
			preparedStatement.setObject(7, document.getSystemId());
			preparedStatement.setObject(8, document.getCaseId());
			preparedStatement.setBoolean(9, false);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				if (rs.next()) {
					UUID id = UUID.fromString(rs.getString(1));
					document.setId(id);
					result = document;
				} else {
					logger.error("Document could not be inserted, no ID generated.");
					throw new JDSPersistenceException(ErrorCode.DB_NO_RESULT_ERROR);
				}
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new JDSPersistenceException(e, ErrorCode.DB_WRITE_ERROR);
		}
		return result;
	}

	@Override
	public void update(JivsDocument document) throws JDSPersistenceException {
		throw new JDSPersistenceException(ErrorCode.NOT_IMPLEMENTED);
	}

	@Override
	public Optional<JivsDocument> getById(UUID id) throws JDSPersistenceException {
		Optional<JivsDocument> result = Optional.empty();
		try (Connection connection =  DatabaseManager.getInstance().connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentSQLs.SELECT_DOCUMENT_BY_ID)) {
			preparedStatement.setObject(1, id);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				if (rs.next()) {
					JivsDocument document = createDocument(rs);
					result = Optional.of(document);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new JDSPersistenceException(e, ErrorCode.DB_READ_ERROR);
		}
		return result;
	}

	@Override
	public List<JivsDocument> getByDocumentTypeAsList(String documentType) throws JDSPersistenceException {
		List<JivsDocument> result = new ArrayList<>();
		try (Connection connection =  DatabaseManager.getInstance().connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentSQLs.SELECT_DOCUMENT_BY_DOCUMENT_TYPE_SQL)) {
			preparedStatement.setString(1, documentType);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					JivsDocument document = createDocument(rs);
					result.add(document);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new JDSPersistenceException(e, ErrorCode.DB_READ_ERROR);
		}
		return result;
	}

	@Override
	public Optional<JivsDocument> getByFileName(String fileName) throws JDSPersistenceException {
		Optional<JivsDocument> result = Optional.empty();
		try (Connection connection =  DatabaseManager.getInstance().connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentSQLs.SELECT_DOCUMENT_BY_FILENAME_SQL)) {
			preparedStatement.setString(1, fileName);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				if (rs.next()) {
					JivsDocument document = createDocument(rs);
					result = Optional.of(document);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new JDSPersistenceException(e, ErrorCode.DB_READ_ERROR);
		}
		return result;
	}

	@Override
	public List<JivsDocument> getByCreatorAsList(UUID id) throws JDSPersistenceException {
		List<JivsDocument> result = new ArrayList<>();
		try (Connection connection =  DatabaseManager.getInstance().connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentSQLs.SELECT_DOCUMENT_BY_CREATOR_SQL)) {
			preparedStatement.setObject(1, id);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					JivsDocument document = createDocument(rs);
					result.add(document);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new JDSPersistenceException(e, ErrorCode.DB_READ_ERROR);
		}
		return result;
	}

	@Override
	public List<JivsDocument> getByCreatedAtAsList(LocalDateTime dateTime) throws JDSPersistenceException {
		List<JivsDocument> result = new ArrayList<>();
		try (Connection connection =  DatabaseManager.getInstance().connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentSQLs.SELECT_DOCUMENT_BY_CREATED_SQL)) {
			preparedStatement.setObject(1, dateTime);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					JivsDocument document = createDocument(rs);
					result.add(document);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new JDSPersistenceException(e, ErrorCode.DB_READ_ERROR);
		}
		return result;
	}

	@Override
	public List<JivsDocument> getByCustomerIdAsList(UUID id) throws JDSPersistenceException {
		List<JivsDocument> result = new ArrayList<>();
		try (Connection connection =  DatabaseManager.getInstance().connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentSQLs.SELECT_DOCUMENT_BY_CUSTOMER_FK_SQL)) {
			preparedStatement.setObject(1, id);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					JivsDocument document = createDocument(rs);
					result.add(document);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new JDSPersistenceException(e, ErrorCode.DB_READ_ERROR);
		}
		return result;
	}

	@Override
	public List<JivsDocument> getBySystemIdAsList(UUID id) throws JDSPersistenceException {
		List<JivsDocument> result = new ArrayList<>();
		try (Connection connection =  DatabaseManager.getInstance().connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentSQLs.SELECT_DOCUMENT_BY_SYSTEM_FK_SQL)) {
			preparedStatement.setObject(1, id);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					JivsDocument document = createDocument(rs);
					result.add(document);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new JDSPersistenceException(e, ErrorCode.DB_READ_ERROR);
		}
		return result;
	}

	@Override
	public List<JivsDocument> getByCaseIdAsList(UUID id) throws JDSPersistenceException {
		List<JivsDocument> result = new ArrayList<>();
		try (Connection connection =  DatabaseManager.getInstance().connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentSQLs.SELECT_DOCUMENT_BY_CASE_ID_SQL)) {
			preparedStatement.setObject(1, id);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					JivsDocument document = createDocument(rs);
					result.add(document);
				}
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new JDSPersistenceException(e, ErrorCode.DB_READ_ERROR);
		}
		return result;
	}

	@Override
	public List<JivsDocument> getAllAsList() throws JDSPersistenceException {
		List<JivsDocument> result = new ArrayList<>();
		try (Connection connection =  DatabaseManager.getInstance().connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentSQLs.SELECT_ALL_DOCUMENTS_SQL)) {
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					JivsDocument document = createDocument(rs);
					result.add(document);
				}
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new JDSPersistenceException(e, ErrorCode.DB_READ_ERROR);
		}

		if (result.isEmpty()) {
			logger.error("No documents found");
			throw new JDSPersistenceException(ErrorCode.DB_NO_RESULT_ERROR);
		}

		return result;
	}

	@Override
	public boolean setDeleteFlagTrue(UUID id) throws JDSPersistenceException {
		int result;
		try (Connection connection =  DatabaseManager.getInstance().connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentSQLs.UPDATE_DOCUMENT_DELETE_FLAG_SQL)) {
			preparedStatement.setBoolean(1, true);
			preparedStatement.setObject(2, id);
			result = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new JDSPersistenceException(e, ErrorCode.DB_WRITE_ERROR);
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
	private JivsDocument createDocument(ResultSet rs) throws SQLException {
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

		JivsDocument document = new JivsDocument(fileBin, fileName, fileType, creator, customerFK,
			systemFK, caseId, params);
		document.setId(id);
		document.setCreatedAt(created);
		document.setDeleted(deleted);

		return document;
	}
}
