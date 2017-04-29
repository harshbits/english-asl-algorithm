package com.englishasl.service;

import org.springframework.stereotype.Service;

import com.englishasl.bo.EnglishGrammarCheckResponse;

@Service
public interface EnglishGrammarCheck {

	public EnglishGrammarCheckResponse checkGrammar(String sentence);
}
