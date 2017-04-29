package com.englishasl.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.englishasl.bo.ASLResponse;
import com.englishasl.bo.EngilshTagResponse;
import com.englishasl.bo.ErrorObject;
import com.englishasl.bo.WordTagging;
import com.englishasl.config.GrammarConfiguration.BeWords;
import com.englishasl.config.GrammarConfiguration.NegationWords;
import com.englishasl.config.GrammarConfiguration.NounTags;
import com.englishasl.config.GrammarConfiguration.TimeWords;
import com.englishasl.config.GrammarConfiguration.ValidPOS;
import com.englishasl.config.GrammarConfiguration.VerbTags;
import com.englishasl.service.ASLConversionService;
import com.englishasl.service.EnglishParserService;
import com.englishasl.service.TrainnedModelParser;

import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.dictionary.MorphologicalProcessor;

public class ASLConversionServiceImpl implements ASLConversionService {

	private static Logger log = LoggerFactory.getLogger(ASLConversionServiceImpl.class);
	
	private boolean isTimeItemFirst = false;
	
	@Autowired
	private EnglishParserService englishParserService;
	
	@Autowired
	private TrainnedModelParser trainnedModelParser;

	@Autowired
	private VerbTags verbTags; 
	
	@Autowired
	private NounTags nounTags;
	
	@Autowired
	private BeWords beWords;
	
	@Autowired
	private ValidPOS validPos;
	
	@Autowired
	private TimeWords timeWords;
	
	@Autowired
	private NegationWords negationWords;

	@Override
	public ASLResponse getASLSentence(String sentence) {
		ASLResponse response = new ASLResponse();
		try{
			
			List<WordTagging> tagWords = new ArrayList<>(); 
			
			//Check correctness of spellings and grammar & Return POS for sentence
			EngilshTagResponse engilshTagResponse = englishParserService.getParsedSentence(sentence, true);
			if(engilshTagResponse.getHttpStatus() == HttpStatus.OK){
				response.setEngilshTagResponse(engilshTagResponse);
				response.setEnglishGrammarCheckResponse(engilshTagResponse.getEnglishGrammarCheckResponse());
				tagWords = engilshTagResponse.getTagWords();
			}else{
				ErrorObject errorObject = new ErrorObject(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
						"Failed to parse english sentence: "+engilshTagResponse.getErrorObject().getMessage());
				response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
				response.setErrorObject(errorObject);
			}
			
			//step 1 - Determine context of sentence, i.e. subject, predicate, active/inactive.
			tagWords = deleteInvalidPOSCapitalize(tagWords);
			
			//Step 2 - Bring time items first
			tagWords = bringTimeItemsFirst(tagWords);
			
			//Step 3 - Bring past tense items first in time items are not already
			if(!isTimeItemFirst){
				tagWords = bringPastItemsFirst(tagWords);
			}
			
			//Step 4 - Push negative items at the end of sentence
			tagWords = pushNegationItemsLast(tagWords);
			
			//Step 5 - Delete Articles from the sentence
			tagWords = deleteArticles(tagWords);
			
			//Step 5 - Delete be words from the sentence
			tagWords = deleteBeWords(tagWords);
			
			//Step 6 - Get Real verb word & For any plural noun, convert to singular
			tagWords = replaceVerbWords(tagWords);
			tagWords = replacePluralNounWords(tagWords);
			
			//Step 7 - change verb-adverb order & adjective-noun order
			tagWords = replaceVerbAdverb(tagWords);
			tagWords = replaceAdjectiveNoun(tagWords);
			
			String aslSentence = getASLSentence(tagWords);
			
			response.setSentence(aslSentence);
			response.setHttpStatus(HttpStatus.OK);
			
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
			ErrorObject errorObject = new ErrorObject(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage());
			response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setErrorObject(errorObject);
		}
		return response;
	}
	
	
	/**
	 * Delete invalid POS tagging from the list.
	 * & 
	 * Capitalize all words
	 * 
	 * @param tagWords
	 * @return
	 */
	private List<WordTagging> deleteInvalidPOSCapitalize(List<WordTagging> tagWords) {
		List<WordTagging> tagWordsOutput = new ArrayList<>();
		for (WordTagging wordTag : tagWords) {
			if(validPos.getValidPos().stream().anyMatch(str -> str.trim().equals(wordTag.getTag()))){
				WordTagging wt = new WordTagging();
				wt.setWord(wordTag.getWord().toUpperCase());
				wt.setTag(wordTag.getTag());
				tagWordsOutput.add(wt);
			}
		}
		return tagWordsOutput;
	}
	
	/**
	 * Bring time items first such as Morning, afternoon, etc.
	 * 
	 * @param tagWords
	 * @return
	 */
	private List<WordTagging> bringTimeItemsFirst(List<WordTagging> tagWords) {
		List<WordTagging> tagWordsOutput = new ArrayList<>();
		for(WordTagging wordTag: tagWords){
			if(timeWords.getTimeWords().stream().anyMatch(str -> str.trim().equals(wordTag.getWord()))){
				tagWordsOutput.add(wordTag);
				isTimeItemFirst = true;
			}
		}
		for(WordTagging wordTag: tagWords){
			if(!(timeWords.getTimeWords().stream().anyMatch(str -> str.trim().equals(wordTag.getWord())))){
				tagWordsOutput.add(wordTag);
			}
		}
		return tagWordsOutput;
	}
	
	/**
	 * Bring past tense items first such as Morning, afternoon, etc.
	 * 
	 * @param tagWords
	 * @return
	 */
	private List<WordTagging> bringPastItemsFirst(List<WordTagging> tagWords) {
		List<WordTagging> tagWordsOutput = new ArrayList<>();
		for(WordTagging wordTag: tagWords){
			if(timeWords.getTimeWords().stream().anyMatch(str -> str.trim().equals(wordTag.getWord()))){
				tagWordsOutput.add(wordTag);
				isTimeItemFirst = true;
			}
		}
		for(WordTagging wordTag: tagWords){
			if(!(timeWords.getTimeWords().stream().anyMatch(str -> str.trim().equals(wordTag.getWord())))){
				tagWordsOutput.add(wordTag);
			}
		}
		return tagWordsOutput;
	}
	
	/**
	 * Push negative items at end of the sentence
	 * 
	 * @param tagWords
	 * @return
	 */
	private List<WordTagging> pushNegationItemsLast(List<WordTagging> tagWords) {
		List<WordTagging> tagWordsOutput = new ArrayList<>();
		for(WordTagging wordTag: tagWords){
			if(!negationWords.getNegationWords().stream().anyMatch(str -> str.trim().equals(wordTag.getWord()))){
				tagWordsOutput.add(wordTag);
			}
		}
		for(WordTagging wordTag: tagWords){
			if(negationWords.getNegationWords().stream().anyMatch(str -> str.trim().equals(wordTag.getWord()))){
				tagWordsOutput.add(wordTag);
			}
		}
		return tagWordsOutput;
	}
	

	/**
	 * Delete determiner from list, i.e. A, An, The
	 * OR
	 * Preposition or subordinating conjunction, i.e. in, on, to, from, with
	 * 
	 * @param tagWords
	 */
	private List<WordTagging> deleteArticles(List<WordTagging> tagWords) {
		List<WordTagging> tagWordsOutput = new ArrayList<>();
		for (WordTagging wordTag : tagWords) {
			// DT - stands for determiner
			// IN - stands for Preposition or subordinating conjunction
			if (!((wordTag.getTag().equals("DT")) || (wordTag.getTag().equals("TO") ||
					(wordTag.getTag().equals("IN"))))){
//					|| verbTags.getVerbTags().stream().anyMatch(str -> str.trim().equals("IN")) )) {
				tagWordsOutput.add(wordTag);
			}
		}
		return tagWordsOutput;
	}

	/**
	 * Delete be words
	 * 
	 * @param tagWords
	 */
	private List<WordTagging> deleteBeWords(List<WordTagging> tagWords) throws Exception{
		List<WordTagging> tagWordsOutput = new ArrayList<>();
		for(WordTagging wordTag:  tagWords){
			if(!(beWords.getBeWords().stream().anyMatch(str -> str.trim().equals(wordTag.getWord())))){
				tagWordsOutput.add(wordTag);
			}
		}
		return tagWordsOutput;
	}

	/**
	 * Replace verb words with real form of verb
	 * 
	 * @param tagWords
	 * @return
	 * @throws Exception
	 */
	private List<WordTagging> replaceVerbWords(List<WordTagging> tagWords) throws Exception{
		List<WordTagging> tagWordsOutput = new ArrayList<>();
		for(WordTagging wordTag:  tagWords){
			if(verbTags.getVerbTags().stream().anyMatch(str -> str.trim().equals(wordTag.getTag()))){
				WordTagging newTag = new WordTagging();
				String word = getVerbWord(wordTag.getWord());
				newTag.setWord(word);
				newTag.setTag(wordTag.getTag());
				tagWordsOutput.add(newTag);
			}else{
				tagWordsOutput.add(wordTag);
			}
		}
		return tagWordsOutput;
	}
	
	
	/**
	 * Replace plural noun word with singular noun word
	 * 
	 * @param tagWords
	 * @return
	 * @throws Exception
	 */
	private List<WordTagging> replacePluralNounWords(List<WordTagging> tagWords) throws Exception{
		List<WordTagging> tagWordsOutput = new ArrayList<>();
		for(WordTagging wordTag:  tagWords){
			if(nounTags.getPluralNounTags().stream().anyMatch(str -> str.trim().equals(wordTag.getTag()))){
				WordTagging newTag = new WordTagging();
				String word = getSingularNoun(wordTag.getWord());
				newTag.setWord(word);
				newTag.setTag(wordTag.getTag());
				tagWordsOutput.add(newTag);
			}else{
				tagWordsOutput.add(wordTag);
			}
		}
		return tagWordsOutput;
	}
	
	/**
	 * Replace verb and adverb order
	 *  
	 * @param tagWords
	 * @return
	 * @throws Exception
	 */
	private List<WordTagging> replaceVerbAdverb(List<WordTagging> tagWords) throws Exception{
		WordTagging[] tempArray = tagWords.toArray(new WordTagging[tagWords.size()]);
		for(int i=0; i < tempArray.length; i++){
			WordTagging w1 = tempArray[i];
			if(verbTags.getAdverbTags().stream().anyMatch(str -> str.trim().equals(w1.getTag()))){
				if( i != tempArray.length - 1){
					WordTagging w2 = tempArray[i+1];
					if(verbTags.getVerbTags().stream().anyMatch(str -> str.trim().equals(w2.getTag()))){
						tempArray[i] = w2;
						tempArray[i + 1] = w1;
					}
				}
				
			}
		}
		return Arrays.asList(tempArray);
	}
	
	/**
	 * Replace Adjective and noun order
	 * 
	 * @param tagWords
	 * @return
	 * @throws Exception
	 */
	private List<WordTagging> replaceAdjectiveNoun(List<WordTagging> tagWords) throws Exception{
		WordTagging[] tempArray = tagWords.toArray(new WordTagging[tagWords.size()]);
		for(int i=0; i < tempArray.length; i++){
			WordTagging w1 = tempArray[i];
			if(nounTags.getAdjectiveTags().stream().anyMatch(str -> str.trim().equals(w1.getTag()))){
				if( i != tempArray.length - 1){
					WordTagging w2 = tempArray[i+1];
					if(nounTags.getNounTags().stream().anyMatch(str -> str.trim().equals(w2.getTag()))){
						tempArray[i] = w2;
						tempArray[i + 1] = w1;
					}
				}
			}
		}
		return Arrays.asList(tempArray);
	}
	
	/**
	 * Get string from list of string
	 * 
	 * @param tagWords
	 * @return
	 * @throws Exception
	 */
	private String getASLSentence(List<WordTagging> tagWords) throws Exception {
		String sentence = "";
		boolean isFirst = true;
		for (WordTagging wordTag : tagWords) {
			if(isFirst){
				sentence = sentence + wordTag.getWord();
				isFirst = false;
			}else{
				sentence = sentence + " " + wordTag.getWord();
			}
		}
		return sentence;
	}

	/**
	 * Get Verb present tense word
	 * 
	 * @param verbWord
	 */
	private String getVerbWord(String verbWord) throws Exception {
		try{
			MorphologicalProcessor morphologicalProcessor = trainnedModelParser.getMorphologicalProcessor();
			IndexWord word = morphologicalProcessor.lookupBaseForm(POS.VERB, verbWord);
			return word.getLemma().toString().toUpperCase();
		}catch(Exception e){
			log.error("getVerbWord Error: "+e);
			return verbWord.toUpperCase();
		}
	}

	/**
	 * Get Singular noun for plural form
	 * 
	 * @param inputNoun
	 * @return
	 */
	private String getSingularNoun(String inputNoun){
		try{
			MorphologicalProcessor morphologicalProcessor = trainnedModelParser.getMorphologicalProcessor();
			IndexWord word = morphologicalProcessor.lookupBaseForm(POS.NOUN, inputNoun);
			return word.getLemma().toString().toUpperCase();
		}catch(Exception e){
			log.error("getSingularNoun Error: "+e);
			return inputNoun.toUpperCase();
		}
	}
}
