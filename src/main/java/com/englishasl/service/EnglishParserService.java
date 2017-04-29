package com.englishasl.service;

import org.springframework.stereotype.Service;

import com.englishasl.bo.EngilshTagResponse;

@Service
public interface EnglishParserService {
	
	public EngilshTagResponse getParsedSentence(String sentence, boolean grammarCheck);

}
