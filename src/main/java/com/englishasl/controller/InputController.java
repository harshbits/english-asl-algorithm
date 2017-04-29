package com.englishasl.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.englishasl.bo.ASLResponse;
import com.englishasl.bo.EngilshTagResponse;
import com.englishasl.bo.EnglishGrammarCheckResponse;
import com.englishasl.bo.ErrorObject;
import com.englishasl.service.ASLConversionService;
import com.englishasl.service.EnglishGrammarCheck;
import com.englishasl.service.EnglishParserService;

/**
 * 
 * @author harshbhavsar
 *
 */


@Controller
public class InputController {

	private static Logger log = LoggerFactory.getLogger(InputController.class);

	@Autowired
	private EnglishParserService englishParserService;
	
	@Autowired
	private EnglishGrammarCheck englishGrammarCheck;
	
	@Autowired
	private ASLConversionService aslConversionService;
	
	/**
	 * This service will tag English words with POS
	 * 
	 * @param sentence
	 * @param grammarCheck
	 * @return
	 */
	@RequestMapping(value = "/entag", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> englishTagging(@RequestParam(required = true) String sentence,
			@RequestParam(required = true) boolean grammarCheck) {
		log.info("Input sentence : " + sentence);
		log.info("Grammar Check? : " + grammarCheck);
		EngilshTagResponse result = new EngilshTagResponse();
		try{
			result = englishParserService.getParsedSentence(sentence, grammarCheck);
			if(result.getHttpStatus() == HttpStatus.OK){
				return new ResponseEntity<EngilshTagResponse>(result, HttpStatus.OK);
			}else{
				return new ResponseEntity<ErrorObject>(result.getErrorObject(), result.getHttpStatus());
			}
		}catch(Exception e){
			ErrorObject response = new ErrorObject(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
			return new ResponseEntity<ErrorObject>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * This service will check correctness of English sentence
	 * 
	 * @param sentence
	 * @return
	 */
	@RequestMapping(value = "/encheck", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> englishGrammarCheck(@RequestParam(required = true) String sentence) {
		log.info("Input sentence: " + sentence);
		EnglishGrammarCheckResponse result = new EnglishGrammarCheckResponse();
		try {
			result = englishGrammarCheck.checkGrammar(sentence);
			if(result.getHttpStatus() == HttpStatus.OK){
				return new ResponseEntity<EnglishGrammarCheckResponse>(result, HttpStatus.OK);
			}else{
				return new ResponseEntity<ErrorObject>(result.getErrorObject(), result.getHttpStatus());
			}
		} catch (Exception e) {
			ErrorObject response = new ErrorObject(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
			return new ResponseEntity<ErrorObject>(response, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * This service will convert English sentence to ASL sentence
	 * 
	 * @param sentence
	 * @return
	 */
	@RequestMapping(value = "/entoasl", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> englishToASL(@RequestParam(required = true) String sentence) {
		log.info("Input sentence: " + sentence);
		ASLResponse result = new ASLResponse();
		try {
			result = aslConversionService.getASLSentence(sentence);
			if (result.getHttpStatus() == HttpStatus.OK) {
				return new ResponseEntity<ASLResponse>(result, HttpStatus.OK);
			} else {
				return new ResponseEntity<ErrorObject>(result.getErrorObject(), result.getHttpStatus());
			}
		} catch (Exception e) {
			ErrorObject response = new ErrorObject(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
			return new ResponseEntity<ErrorObject>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@ResponseBody
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> hadnleException(Exception e) {
		ErrorObject response = new ErrorObject(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
		return new ResponseEntity<ErrorObject>(response, HttpStatus.BAD_REQUEST);
	}
	
}
