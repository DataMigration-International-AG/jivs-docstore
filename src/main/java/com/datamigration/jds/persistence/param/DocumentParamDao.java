package com.datamigration.jds.persistence.param;

import static com.datamigration.jds.persistence.DatabaseManager.connect;

import com.datamigration.jds.model.entity.docstoreparam.JivsDocumentParam;
import com.datamigration.jds.util.exceptions.ErrorCode;
import com.datamigration.jds.util.exceptions.checked.JDSPersistenceException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocumentParamDao implements IDocumentParamDao {

	private static final Logger logger = LoggerFactory.getLogger(DocumentParamDao.class);

	@Override
	public void createTables() throws JDSPersistenceException {
		try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentParamSQLs.CREATE_DOCUMENT_PARAMS_TABLE_SQL)) {
			preparedStatement.execute();
			logger.info("JIVS-DOCUMENT-PARAM table created.");
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new JDSPersistenceException(e, ErrorCode.DB_CREATE_ERROR);
		}
	}

	@Override
	public Optional<JivsDocumentParam> getById(UUID id) throws JDSPersistenceException {
		throw new JDSPersistenceException(ErrorCode.NOT_IMPLEMENTED);
	}

	@Override
	public List<JivsDocumentParam> getAllAsList() throws JDSPersistenceException {
		throw new JDSPersistenceException(ErrorCode.NOT_IMPLEMENTED);
	}

	@Override
	public JivsDocumentParam insert(JivsDocumentParam jivsDocumentParam) throws JDSPersistenceException {
		JivsDocumentParam insertedJivsDocumentParam = null;
		Map<String, String> resultMap = new HashMap<>();

		for (Entry<String, String> entry : jivsDocumentParam.getParams().entrySet()) {
			try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(
				IDocumentParamSQLs.INSERT_PARAMS_SQL, Statement.RETURN_GENERATED_KEYS)) {

				preparedStatement.setObject(1, jivsDocumentParam.getDocumentId());
				preparedStatement.setString(2, entry.getKey());
				preparedStatement.setString(3, entry.getValue());
				try (ResultSet rs = preparedStatement.executeQuery()) {
					if (rs.next()) {
						resultMap.put(entry.getKey(), entry.getValue());
					} else {
						logger.error("No params inserted for document {}", jivsDocumentParam.getDocumentId());
						throw new JDSPersistenceException(ErrorCode.DB_NO_RESULT_ERROR);
					}
				} catch (SQLException e) {
					logger.error(e.getMessage(), e);
					throw new JDSPersistenceException(e, ErrorCode.DB_WRITE_ERROR);
				}

			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
				throw new JDSPersistenceException(e, ErrorCode.DB_WRITE_ERROR);
			}
		}

		if (!resultMap.isEmpty()) {
			insertedJivsDocumentParam = new JivsDocumentParam(jivsDocumentParam.getDocumentId(), resultMap);
		}

		return insertedJivsDocumentParam;
	}

	@Override
	public void update(JivsDocumentParam jivsDocumentParam) throws JDSPersistenceException {
		throw new JDSPersistenceException(ErrorCode.NOT_IMPLEMENTED);
	}


	@Override
	public Map<String, String> updateParams(UUID id, Map<String, String> params) throws JDSPersistenceException {
		deleteByDocumentId(id);

		for (Entry<String, String> entry : params.entrySet()) {

			try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(
				IDocumentParamSQLs.INSERT_PARAMS_SQL)) {
				preparedStatement.setObject(1, id);
				preparedStatement.setString(2, entry.getKey());
				preparedStatement.setString(3, entry.getValue());
				preparedStatement.executeQuery();
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
				throw new JDSPersistenceException(e, ErrorCode.DB_WRITE_ERROR);
			}
		}

		return params;
	}

	@Override
	public Map<String, String> getParams(UUID id) throws JDSPersistenceException {
		Map<String, String> params = new HashMap<>();
		try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentParamSQLs.SELECT_DOCUMENT_PARAMS_BY_ID)) {
			preparedStatement.setObject(1, id);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					String key = rs.getString(1);
					String value = rs.getString(2);
					params.put(key, value);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new JDSPersistenceException(e, ErrorCode.DB_READ_ERROR);
		}
		return params;
	}

	private void deleteByDocumentId(UUID id) throws JDSPersistenceException {
		try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocumentParamSQLs.DELETE_BY_DOCUMENT_ID_SQL)) {

			preparedStatement.setObject(1, id);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new JDSPersistenceException(e, ErrorCode.DB_WRITE_ERROR);
		}
	}
}
