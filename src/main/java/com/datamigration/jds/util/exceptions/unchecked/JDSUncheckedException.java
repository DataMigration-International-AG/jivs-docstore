package com.datamigration.jds.util.exceptions.unchecked;

import com.datamigration.jds.util.exceptions.ErrorCode;
import java.io.Serial;

public abstract class JDSUncheckedException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;

	private final ErrorCode code;

	public JDSUncheckedException(ErrorCode code) {
		super(code.getMessage());
		this.code = code;
	}

	public JDSUncheckedException(String message, Throwable cause, ErrorCode code) {
		super(code.getMessage(message), cause);
		this.code = code;
	}

	public JDSUncheckedException(String message, ErrorCode code) {
		super(code.getMessage(message));
		this.code = code;
	}

	public JDSUncheckedException(Throwable cause, ErrorCode code) {
		super(code.getMessage(), cause);
		this.code = code;
	}

	public ErrorCode getCode() {
		return this.code;
	}

}
