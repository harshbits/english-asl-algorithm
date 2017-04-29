package com.englishasl.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.englishasl.bo.EngilshTagResponse;
import com.englishasl.bo.EnglishGrammarCheckResponse;
import com.englishasl.bo.ErrorObject;
import com.englishasl.bo.WordTagging;
import com.englishasl.service.EnglishGrammarCheck;
import com.englishasl.service.EnglishParserService;
import com.englishasl.service.TrainnedModelParser;

import edu.stanford.nlp.trees.Tree;


public class EnglishParserServiceImpl implements EnglishParserService {

	private static Logger log = LoggerFactory.getLogger(EnglishParserServiceImpl.class);
	
	@Autowired
	private TrainnedModelParser trainnedModelParser;
	
	@Autowired
	private EnglishGrammarCheck englishGrammarCheck;
	@Override
	public EngilshTagResponse getParsedSentence(String sentence, boolean grammarCheck) {
		
		EngilshTagResponse response = new EngilshTagResponse();
		
		try {
			response.setInputSentence(sentence);
			if(grammarCheck){
				EnglishGrammarCheckResponse checkSentence = englishGrammarCheck.checkGrammar(sentence);
				if(checkSentence.getHttpStatus() == HttpStatus.OK){
					sentence = checkSentence.getSentence();
					response.setGrammarCorrected(sentence);
					response.setEnglishGrammarCheckResponse(checkSentence);
				}else{
					response.setEnglishGrammarCheckResponse(new EnglishGrammarCheckResponse());
					response.setGrammarCorrected("Failed to parse: "+ checkSentence.getErrorObject().getMessage());
				}
			}else{
				response.setEnglishGrammarCheckResponse(new EnglishGrammarCheckResponse());
				response.setGrammarCorrected("Grammar Check = FALSE");
			}
			
			Tree tree = trainnedModelParser.getLexicalizedParserTree(sentence);
			if(tree != null){
				List<WordTagging> tagWords = new ArrayList<>();
				List<Tree> leaves = tree.getLeaves();
				for (Tree leaf : leaves) {
					Tree parent = leaf.parent(tree);
					tagWords.add(new WordTagging(leaf.label().value(), parent.label().value()));
				}
				response.setHttpStatus(HttpStatus.OK);
				response.setTagWords(tagWords);
			}else{
				throw new Exception("Parsed Tree is Empty/Null.");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			ErrorObject errorObject = new ErrorObject(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage());
			response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setErrorObject(errorObject);
		}
		return response;
	}
}
