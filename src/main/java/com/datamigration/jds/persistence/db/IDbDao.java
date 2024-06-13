package com.datamigration.jds.persistence.db;


import com.datamigration.jds.util.exceptions.checked.JDSPersistenceException;

public interface IDbDao {

	void createJDSSchema() throws JDSPersistenceException;
}
