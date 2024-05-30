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
import java.util.List;
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
	public Optional<DocumentDTO> getById(UUID id) throws JPEPersistenceException {
		return Optional.empty();
	}

	@Override
	public Optional<List<DocumentDTO>> getAllAsList() throws JPEPersistenceException {
		return Optional.empty();
	}

	@Override
	public DocumentDTO insert(DocumentDTO documentDTO) throws JPEPersistenceException {
		DocumentDTO result;

		try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentSQLs.INSERT_DOCUMENT_SQL, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setBytes(1, documentDTO.fileBin());
			preparedStatement.setString(2, documentDTO.fileName());
			preparedStatement.setString(3, documentDTO.fileType());
			preparedStatement.setObject(4, documentDTO.creator());
			preparedStatement.setObject(5, LocalDateTime.now());
			preparedStatement.setObject(6, documentDTO.customerFK());
			preparedStatement.setObject(7, documentDTO.systemFk());
			preparedStatement.setObject(8, documentDTO.caseId());
			preparedStatement.setBoolean(9, documentDTO.deleted());
			try (ResultSet rs = preparedStatement.executeQuery()) {
				if (rs.next()) {
					UUID id = UUID.fromString(rs.getString(1));
					result = new DocumentDTO(id, documentDTO.fileBin(), documentDTO.fileName(), documentDTO.fileType(),
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
	public boolean delete(UUID id) throws JPEPersistenceException {
		int result;
		try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentSQLs.DELETE_DOCUMENT_SQL)) {
			preparedStatement.setObject(1, id);
			result = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new JPEPersistenceException(e, ErrorCode.DB_WRITE_ERROR);
		}
		return result == 1;
	}
}
