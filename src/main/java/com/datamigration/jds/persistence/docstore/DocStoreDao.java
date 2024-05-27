package com.datamigration.jds.persistence.docstore;

import com.datamigration.jds.model.docstore.JivsDocStore;
import com.datamigration.jds.persistence.AbstractDatabaseManager;
import com.datamigration.jds.util.exceptions.ErrorCode;
import com.datamigration.jds.util.exceptions.checked.JPEPersistenceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocStoreDao extends AbstractDatabaseManager implements IDocStoreDao {

	private static final Logger logger = LoggerFactory.getLogger(DocStoreDao.class);


	/**
	 * createTables - A method to create tables in the database.
	 *
	 * @throws JPEPersistenceException exception thrown if there is an issue with persistence
	 */
	@Override
	public void createTables() throws JPEPersistenceException {
		try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(
			IDocStoreSQLs.CREATE_DOCSTORE_TABLE_SQL)) {
			preparedStatement.execute();
		} catch (SQLException e) {
			throw new JPEPersistenceException(e, ErrorCode.DB_CREATE_ERROR);
		}
	}

	@Override
	public Optional<JivsDocStore> getById(UUID id) throws JPEPersistenceException {
		return Optional.empty();
	}

	@Override
	public Optional<List<JivsDocStore>> getAllAsList() throws JPEPersistenceException {
		return Optional.empty();
	}

	@Override
	public JivsDocStore insert(JivsDocStore jivsDocStore) throws JPEPersistenceException {
		return null;
	}

	@Override
	public void update(JivsDocStore jivsDocStore) throws JPEPersistenceException {

	}
}
