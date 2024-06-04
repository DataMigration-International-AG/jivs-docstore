package com.datamigration.jds.persistence.param;

import static com.datamigration.jds.persistence.DatabaseManager.connect;

import com.datamigration.jds.model.docstoreparam.JivsDocumentParam;
import com.datamigration.jds.persistence.docstore.IDocumentSQLs;
import com.datamigration.jds.util.exceptions.ErrorCode;
import com.datamigration.jds.util.exceptions.checked.JPEPersistenceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
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
	public Optional<JivsDocumentParam> getById(UUID id) throws JPEPersistenceException {
		return Optional.empty();
	}

	@Override
	public Optional<List<JivsDocumentParam>> getAllAsList() throws JPEPersistenceException {
		return Optional.empty();
	}

	@Override
	public JivsDocumentParam insert(JivsDocumentParam jivsDocumentParam) throws JPEPersistenceException {
		JivsDocumentParam insertedJivsDocumentParam = null;
		UUID id = null;
		Map<String, String> resultMap = new HashMap<>();

		try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentSQLs.INSERT_DOCUMENT_SQL, Statement.RETURN_GENERATED_KEYS)) {

			for (Entry<String, String> entry : jivsDocumentParam.getParams().entrySet()) {
				preparedStatement.setObject(1, jivsDocumentParam.getDocumentId());
				preparedStatement.setString(2, entry.getKey());
				preparedStatement.setString(3, entry.getValue());
				try (ResultSet rs = preparedStatement.executeQuery()) {

					if (rs.next()) {
						id = UUID.fromString(rs.getString(1));
						resultMap.put(entry.getKey(), entry.getValue());

					} else {
						throw new JPEPersistenceException(ErrorCode.DB_NO_RESULT_ERROR);
					}
				} catch (SQLException e) {
					throw new JPEPersistenceException(e, ErrorCode.DB_WRITE_ERROR);
				}
			}

		} catch (SQLException e) {
			throw new JPEPersistenceException(e, ErrorCode.DB_WRITE_ERROR);
		}

		if (id != null) {
			insertedJivsDocumentParam = new JivsDocumentParam(id, jivsDocumentParam.getDocumentId(), resultMap);
		}

		return insertedJivsDocumentParam;
	}

	@Override
	public void update(JivsDocumentParam jivsDocumentParam) throws JPEPersistenceException {

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

	@Override
	public Optional<Map<String, String>> getParams(UUID id) throws JPEPersistenceException {
		Optional<Map<String, String>> params;
		try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentParamSQLs.SELECT_DOCUMENT_PARAMS_BY_ID)) {
			preparedStatement.setObject(1, id);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				Map<String, String> result = new HashMap<>();
				while (rs.next()) {
					String key = rs.getString(1);
					String value = rs.getString(2);
					result.put(key, value);
				}
				params = Optional.of(result);
			}
		} catch (Exception e) {
			throw new JPEPersistenceException(e, ErrorCode.DB_READ_ERROR);
		}
		return params;
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
