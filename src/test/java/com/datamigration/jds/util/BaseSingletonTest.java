package com.datamigration.jds.util;

import com.datamigration.jds.persistence.DatabaseManager;
import com.datamigration.jds.util.exceptions.checked.JDSException;

public abstract class BaseSingletonTest extends JDSTest {

	static {
		try {
			DatabaseManager dbm = DatabaseManager.getInstance();
			dbm.initializeDatabase();
		} catch (JDSException e) {
			throw new RuntimeException(e);
		}
	}
}
