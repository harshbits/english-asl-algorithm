package com.englishasl.bo;

import java.io.Serializable;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class EngilshTagResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String inputSentence;

	private String grammarCorrected;

	private List<WordTagging> tagWords;

	private boolean question;

	private EnglishGrammarCheckResponse englishGrammarCheckResponse;

	private HttpStatus httpStatus;

	private ErrorObject errorObject;

	public String getInputSentence() {
		return inputSentence;
	}

	public void setInputSentence(String inputSentence) {
		this.inputSentence = inputSentence;
	}

	public String getGrammarCorrected() {
		return grammarCorrected;
	}

	public void setGrammarCorrected(String grammarCorrected) {
		this.grammarCorrected = grammarCorrected;
	}

	public List<WordTagging> getTagWords() {
		return tagWords;
	}

	public void setTagWords(List<WordTagging> tagWords) {
		this.tagWords = tagWords;
	}

	public boolean isQuestion() {
		return question;
	}

	public void setQuestion(boolean question) {
		this.question = question;
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
