package com.datamigration.jds.persistence.db;


import com.datamigration.jds.util.exceptions.checked.JPEPersistenceException;

public interface IDbDao {

	void createJPESchema() throws JPEPersistenceException;
}
