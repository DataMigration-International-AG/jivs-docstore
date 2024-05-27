package com.datamigration.jds.util.exceptions.unchecked;


import com.datamigration.jds.util.exceptions.ErrorCode;
import java.io.Serial;

public class JPEUncheckedPersistenceException extends JPEUncheckedException{

	@Serial
	private static final long serialVersionUID = 7718828512143293558L;

	public JPEUncheckedPersistenceException(Exception e, ErrorCode code) {
		super(e, code);
	}

	public JPEUncheckedPersistenceException(ErrorCode code) {
		super(code);
	}

}
