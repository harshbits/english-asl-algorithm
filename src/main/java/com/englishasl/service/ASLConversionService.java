package com.englishasl.service;

import org.springframework.stereotype.Service;

import com.englishasl.bo.ASLResponse;

@Service
public interface ASLConversionService {

	public ASLResponse getASLSentence(String sentence);

}
