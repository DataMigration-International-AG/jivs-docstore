package com.datamigration.jds.util.exceptions.checked;

import com.datamigration.jds.util.exceptions.ErrorCode;
import java.io.Serial;

/**
 * This is a library internal exception indicating that an internal processing failed.
 */
public class JDSSystemException extends JDSException {

	@Serial
	private static final long serialVersionUID = 8372895108907546200L;

	public JDSSystemException(Exception e, ErrorCode code) {
		super(e, code);
	}

	public JDSSystemException(ErrorCode code) {
		super(code);
	}

}
