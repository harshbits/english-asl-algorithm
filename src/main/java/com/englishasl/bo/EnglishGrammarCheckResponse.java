package com.englishasl.bo;

import java.util.Map;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class EnglishGrammarCheckResponse {

	private String sentence;

	private Map<String, String> suggessions;

	private HttpStatus httpStatus;

	private ErrorObject errorObject;

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public Map<String, String> getSuggessions() {
		return suggessions;
	}

	public void setSuggessions(Map<String, String> suggessions) {
		this.suggessions = suggessions;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public ErrorObject getErrorObject() {
		return errorObject;
	}

	public void setErrorObject(ErrorObject errorObject) {
		this.errorObject = errorObject;
	}

}
