package com.datamigration.jds.util.exceptions.unchecked;


import com.datamigration.jds.util.exceptions.ErrorCode;
import java.io.Serial;

public class JDSUncheckedPersistenceException extends JDSUncheckedException {

	@Serial
	private static final long serialVersionUID = 7718828512143293558L;

	public JDSUncheckedPersistenceException(Exception e, ErrorCode code) {
		super(e, code);
	}

	public JDSUncheckedPersistenceException(ErrorCode code) {
		super(code);
	}

}
