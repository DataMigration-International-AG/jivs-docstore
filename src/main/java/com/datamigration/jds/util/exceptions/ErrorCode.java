package com.datamigration.jds.util.exceptions;

/**
 * Error codes for this library. Since we may want to reuse the http error codes for better readability by users of this
 * library we start our specific error codes, which do not have a representation in the http error codes, at 600. 6xx
 * stands for errors occuring in the data access layer.
 */
public enum ErrorCode {

	DB_GENERAL_ERROR("JPE-600", "Error accessing the database"),
	DB_READ_ERROR("JPE-601", "Error reading from the database"),
	DB_CONNECT_ERROR("JPE-602", "Error connecting to the database"),
	DB_WRITE_ERROR("JPE-603", "Error writing to the database"),
	DB_EXECUTE_ERROR("JPE-604", "Error executing statement on the database"),
	DB_CREATE_ERROR("JPE-605", "Error trying to create an object in the database"),
	DB_CONFIG_ERROR("JPE-606", "Error regarding the libraries db configuration"),
	DB_NO_RESULT_ERROR("JPE-607", "Error. No result was returned."),

	SYSTEM_CONVERT_ERROR("JPE-701", "Error transforming DTOs to business objects"),
	MISSING_EMPTY_CONSTRUCTOR_ERROR("JPE-702", "Error, empty constructor for business class is missing"),
	WATCHER_CONFIG_ERROR("JPE-703", "Error regarding the libraries watcher configuration"),

	NOT_IMPLEMENTED("JPE-901", "Method not implemented");

	private final String code;
	private final String title;

	ErrorCode(String code, String title) {
		this.code = code;
		this.title = title;
	}

	public String getCode() {
		return code;
	}

	public String getTitle() {
		return title;
	}

	public String getMessage() {
		return code + " : " + title;
	}

	public String getMessage(String message) {
		return getMessage() + " : " + message;
	}
}
