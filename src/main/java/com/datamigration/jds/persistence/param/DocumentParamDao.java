package com.datamigration.jds.persistence.param;

import static com.datamigration.jds.persistence.DatabaseManager.connect;

import com.datamigration.jds.model.dto.DocumentDTO;
import com.datamigration.jds.persistence.docstore.IDocumentSQLs;
import com.datamigration.jds.util.exceptions.ErrorCode;
import com.datamigration.jds.util.exceptions.checked.JPEPersistenceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

public class DocumentParamDao implements IDocumentParamDao {

	@Override
	public void createTables() throws JPEPersistenceException {
		try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentParamSQLs.CREATE_DOCUMENT_PARAMS_TABLE_SQL)) {
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
		return null;
	}

	@Override
	public void update(DocumentDTO documentDTO) throws JPEPersistenceException {

	}

	@Override
	public void updateParams(UUID id, Map<String, String> params) throws JPEPersistenceException {
		deleteByDocumentId(id);

		try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentParamSQLs.INSERT_PARAMS_SQL)) {

			for (Entry<String, String> entry : params.entrySet()) {
				preparedStatement.setObject(1, id);
				preparedStatement.setString(2, entry.getKey());
				preparedStatement.setString(3, entry.getValue());
				preparedStatement.executeUpdate();
			}

		} catch (SQLException e) {
			throw new JPEPersistenceException(e, ErrorCode.DB_WRITE_ERROR);
		}
	}

	private void deleteByDocumentId(UUID id) throws JPEPersistenceException {
		try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentParamSQLs.DELETE_BY_DOCUMENT_ID_SQL)) {

			preparedStatement.setObject(1, id);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw new JPEPersistenceException(e, ErrorCode.DB_WRITE_ERROR);
		}
	}
}
