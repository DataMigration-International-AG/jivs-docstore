package com.datamigration.jds.util;

import com.datamigration.jds.JivsDocStore;
import com.datamigration.jds.util.exceptions.checked.JPEException;

public abstract class BaseSingletonTest extends JDSTest {

	static {
		try {
			JivsDocStore.initializeDatabase("config.properties");
		} catch (JPEException e) {
			throw new RuntimeException(e);
		}
	}
}
