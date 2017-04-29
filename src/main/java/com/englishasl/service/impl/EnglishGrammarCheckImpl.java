package com.englishasl.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.languagetool.JLanguageTool;
import org.languagetool.rules.RuleMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.englishasl.bo.EnglishGrammarCheckResponse;
import com.englishasl.bo.ErrorObject;
import com.englishasl.service.EnglishGrammarCheck;
import com.englishasl.service.TrainnedModelParser;


public class EnglishGrammarCheckImpl implements EnglishGrammarCheck {
	
	private static Logger log = LoggerFactory.getLogger(EnglishGrammarCheckImpl.class);
	
	@Autowired
	private TrainnedModelParser trainnedModelParser;
	
	@Override
	public EnglishGrammarCheckResponse checkGrammar(String sentence) {
		EnglishGrammarCheckResponse response = new EnglishGrammarCheckResponse();
		try{
			JLanguageTool langTool = trainnedModelParser.getJLanguageTool();
			if(langTool !=null){
				Map<String, String> suggessions = new HashMap<>();
				List<RuleMatch> matches = new ArrayList<>();
				matches = langTool.check(sentence);
				StringBuffer sb = new StringBuffer(sentence);
				int difference = 0;
				for (RuleMatch match : matches) {
					suggessions.put(match.getFromPos() + "-" + match.getToPos(), match.getMessage());
					sb.replace(match.getFromPos() + difference, match.getToPos() + difference, match.getSuggestedReplacements().get(0));
					difference = difference + match.getSuggestedReplacements().get(0).length() - (match.getToPos() - match.getFromPos());
				}
				response.setSentence(sb.toString());
				response.setSuggessions(suggessions);
				response.setHttpStatus(HttpStatus.OK);
			}else{
				throw new Exception("JLanguageTool is not defined/Null.");
			}
		}catch(Exception e)
		{
			log.error(e.getMessage());
			ErrorObject errorObject = new ErrorObject(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage());
			response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setErrorObject(errorObject);
		}
		return response;
	}

}
