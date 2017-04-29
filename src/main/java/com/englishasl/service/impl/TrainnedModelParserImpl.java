package com.englishasl.service.impl;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.rules.Rule;
import org.languagetool.rules.spelling.SpellingCheckRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.englishasl.service.TrainnedModelParser;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.Tree;
import net.sf.extjwnl.dictionary.Dictionary;
import net.sf.extjwnl.dictionary.MorphologicalProcessor;

public class TrainnedModelParserImpl implements TrainnedModelParser {

	private static Logger log = LoggerFactory.getLogger(TrainnedModelParserImpl.class);

	// Tagging sentences with POS
	@Override
	public Tree getLexicalizedParserTree(String input) {
		try {
			String PCG_MODEL = "trainnedModels/lexparser/englishPCFG.ser.gz";
			TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(),
					"invertible=true");
			LexicalizedParser parser = LexicalizedParser.loadModel(PCG_MODEL);
			Tokenizer<CoreLabel> tokenizer = tokenizerFactory.getTokenizer(new StringReader(input));
			List<CoreLabel> tokens = tokenizer.tokenize();
			return parser.apply(tokens);
		} catch (Exception e) {
			log.error("LexicalizedParserTree init Error: " + e.getStackTrace());
			return null;
		}
	}

	// Check correctness of spellings
	@Override
	public JLanguageTool getJLanguageTool() {
		try {

			JLanguageTool langTool = new JLanguageTool(new AmericanEnglish());

			// statistical ngram data corpus from google:
			// langTool.activateLanguageModelRules(new
			// File("/data/google-ngram-data"));

			// exclude words from spell checking
			for (Rule rule : langTool.getAllActiveRules()) {
				if (rule instanceof SpellingCheckRule) {
					List<String> wordsToIgnore = Arrays.asList("");
					((SpellingCheckRule) rule).addIgnoreTokens(wordsToIgnore);
				}
			}
			// ignore phrases from spell checking
			for (Rule rule : langTool.getAllActiveRules()) {
				if (rule instanceof SpellingCheckRule) {
					((SpellingCheckRule) rule).acceptPhrases(Arrays.asList(""));
				}
			}

			return langTool;
		} catch (Exception e) {
			log.error("JLanguageTool init error: " + e.getStackTrace());
			return null;
		}
	}

	@Override
	public MorphologicalProcessor getMorphologicalProcessor() {
		try {
			Dictionary wordnet = Dictionary.getDefaultResourceInstance();
			return wordnet.getMorphologicalProcessor();
		} catch (Exception e) {
			log.error("MorphologicalProcessor init Error: " + e.getStackTrace());
			return null;
		}
	}

}
