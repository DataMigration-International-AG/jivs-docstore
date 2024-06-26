package com.datamigration.jds.util.exceptions.checked;

import com.datamigration.jds.util.exceptions.ErrorCode;
import java.io.Serial;

/**
 * This is the libraries base exception holding the ErrorrCode handling.
 */
public abstract class JDSException extends Exception {

	@Serial
	private static final long serialVersionUID = 1L;

	private final ErrorCode code;

	public JDSException(ErrorCode code) {
		super(code.getMessage());
		this.code = code;
	}

	public JDSException(String message, Throwable cause, ErrorCode code) {
		super(code.getMessage(message), cause);
		this.code = code;
	}

	public JDSException(String message, ErrorCode code) {
		super(code.getMessage(message));
		this.code = code;
	}

	public JDSException(Throwable cause, ErrorCode code) {
		super(code.getMessage(), cause);
		this.code = code;
	}

	public ErrorCode getCode() {
		return this.code;
	}
}
