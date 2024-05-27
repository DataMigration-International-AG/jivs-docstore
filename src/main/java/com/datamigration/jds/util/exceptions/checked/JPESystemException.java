package com.datamigration.jds.util.exceptions.checked;

import com.datamigration.jds.util.exceptions.ErrorCode;
import java.io.Serial;

/**
 * This is a library internal exception indicating that an internal processing failed.
 */
public class JPESystemException extends JPEException {

	@Serial
	private static final long serialVersionUID = 8372895108907546200L;

	public JPESystemException(Exception e, ErrorCode code) {
		super(e, code);
	}

	public JPESystemException(ErrorCode code) {
		super(code);
	}

}
