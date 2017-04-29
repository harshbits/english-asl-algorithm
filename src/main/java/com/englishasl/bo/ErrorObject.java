package com.englishasl.bo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@SuppressWarnings("serial")
public class ErrorObject implements Serializable {
	private String code;
	private String message;

	public ErrorObject() {

	}

	/**
	 * 
	 * /** Constructs for error code and message and message_details
	 * 
	 * @param code
	 *            Error code
	 * @param message
	 *            Error message
	 */
	public ErrorObject(String code, String message) {
		this.code = code;
		this.message = message;
	}

	/**
	 * Method getCode.
	 * 
	 * @return String
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Method setCode.
	 * 
	 * @param code
	 *            String
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Method getMessage.
	 * 
	 * @return String
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Method setMessage.
	 * 
	 * @param message
	 *            String
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
