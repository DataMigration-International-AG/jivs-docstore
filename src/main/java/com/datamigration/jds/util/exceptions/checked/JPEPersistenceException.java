package com.datamigration.jds.util.exceptions.checked;


import com.datamigration.jds.util.exceptions.ErrorCode;
import java.io.Serial;

/**
 * This is the libraries persistence exception. This should be used if something fails in the database layer
 */
public class JPEPersistenceException extends JPEException {

	@Serial
	private static final long serialVersionUID = 7718828512143293558L;

	public JPEPersistenceException(Exception e, ErrorCode code) {
		super(e, code);
	}

	public JPEPersistenceException(ErrorCode code) {
		super(code);
	}

}
