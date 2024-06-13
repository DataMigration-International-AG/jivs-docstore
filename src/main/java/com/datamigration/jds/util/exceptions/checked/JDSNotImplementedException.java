package com.datamigration.jds.util.exceptions.checked;

import com.datamigration.jds.util.exceptions.ErrorCode;
import java.io.Serial;

/**
 * This is the libraries exception if something is not implemented. Uses the ErrorCode.NOT_IMPLEMENTED
 */
public class JDSNotImplementedException extends JDSException {

	@Serial
	private static final long serialVersionUID = -7621608925601572329L;

	public JDSNotImplementedException() {
		super(ErrorCode.NOT_IMPLEMENTED);
	}
}
