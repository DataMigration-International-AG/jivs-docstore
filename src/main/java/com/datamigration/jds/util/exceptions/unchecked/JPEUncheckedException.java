package com.datamigration.jds.util.exceptions.unchecked;

import com.datamigration.jds.util.exceptions.ErrorCode;
import java.io.Serial;

public abstract class JPEUncheckedException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;

	private final ErrorCode code;

	public JPEUncheckedException(ErrorCode code) {
		super(code.getMessage());
		this.code = code;
	}

	public JPEUncheckedException(String message, Throwable cause, ErrorCode code) {
		super(code.getMessage(message), cause);
		this.code = code;
	}

	public JPEUncheckedException(String message, ErrorCode code) {
		super(code.getMessage(message));
		this.code = code;
	}

	public JPEUncheckedException(Throwable cause, ErrorCode code) {
		super(code.getMessage(), cause);
		this.code = code;
	}

	public ErrorCode getCode() {
		return this.code;
	}

}
