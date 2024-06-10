package com.datamigration.jds.util.exceptions.unchecked;

import com.datamigration.jds.util.exceptions.ErrorCode;
import java.io.Serial;

public class JDSUncheckedConfigurationException extends JDSUncheckedException {

	@Serial
	private static final long serialVersionUID = -7621608925601572329L;

	public JDSUncheckedConfigurationException(ErrorCode errorCode) {
		super(errorCode);
	}
}
