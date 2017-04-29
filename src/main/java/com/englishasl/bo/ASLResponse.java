package com.englishasl.bo;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class ASLResponse {

	private String sentence;

	private EngilshTagResponse engilshTagResponse;

	private EnglishGrammarCheckResponse englishGrammarCheckResponse;

	private HttpStatus httpStatus;

	private ErrorObject errorObject;

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public EngilshTagResponse getEngilshTagResponse() {
		return engilshTagResponse;
	}

	public void setEngilshTagResponse(EngilshTagResponse engilshTagResponse) {
		this.engilshTagResponse = engilshTagResponse;
	}

	public EnglishGrammarCheckResponse getEnglishGrammarCheckResponse() {
		return englishGrammarCheckResponse;
	}

	public void setEnglishGrammarCheckResponse(EnglishGrammarCheckResponse englishGrammarCheckResponse) {
		this.englishGrammarCheckResponse = englishGrammarCheckResponse;
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
