package com.englishasl.service;

import org.languagetool.JLanguageTool;
import org.springframework.stereotype.Service;

import edu.stanford.nlp.trees.Tree;
import net.sf.extjwnl.dictionary.MorphologicalProcessor;

@Service
public interface TrainnedModelParser {
	
	public Tree getLexicalizedParserTree(String input);
	
	public JLanguageTool getJLanguageTool();
	
	public MorphologicalProcessor getMorphologicalProcessor();

}