package com.datamigration.jds.util.exceptions.unchecked;

import com.datamigration.jds.util.exceptions.ErrorCode;
import java.io.Serial;

public class JDSUncheckedNotImplementedException extends JDSUncheckedException {

	@Serial
	private static final long serialVersionUID = -7621608925601572329L;

	public JDSUncheckedNotImplementedException() {
		super(ErrorCode.NOT_IMPLEMENTED);
	}
}
