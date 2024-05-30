package com.datamigration.jds.util.exceptions.unchecked;

import com.datamigration.jds.util.exceptions.ErrorCode;
import java.io.Serial;

public class JPEUncheckedNotImplementedException extends JPEUncheckedException {

	@Serial
	private static final long serialVersionUID = -7621608925601572329L;

	public JPEUncheckedNotImplementedException() {
		super(ErrorCode.NOT_IMPLEMENTED);
	}
}
