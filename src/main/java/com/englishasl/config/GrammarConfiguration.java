package com.englishasl.config;

import java.util.ArrayList;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.englishasl")
@EnableConfigurationProperties
public class GrammarConfiguration {

	@Bean
	public BeWords beWords() {
		return new BeWords();
	}

	@Bean
	public VerbTags verbTags() {
		return new VerbTags();
	}

	@Bean
	public NounTags nounTags() {
		return new NounTags();
	}

	@Bean
	public ValidPOS validPOS() {
		return new ValidPOS();
	}

	@Bean
	public TimeWords timeWords() {
		return new TimeWords();
	}

	@Bean
	public NegationWords negationWords() {
		return new NegationWords();
	}

	@ConfigurationProperties(prefix = "grammarWordsList")
	public static class BeWords {

		private ArrayList<String> beWords = new ArrayList<String>();

		public ArrayList<String> getBeWords() {
			return beWords;
		}

		public void setBeWords(ArrayList<String> beWords) {
			this.beWords = beWords;
		}

	}

	@ConfigurationProperties(prefix = "grammarWordsList")
	public static class VerbTags {

		private ArrayList<String> verbTags = new ArrayList<String>();

		private ArrayList<String> adverbTags = new ArrayList<String>();

		public ArrayList<String> getVerbTags() {
			return verbTags;
		}

		public void setVerbTags(ArrayList<String> verbTags) {
			this.verbTags = verbTags;
		}

		public ArrayList<String> getAdverbTags() {
			return adverbTags;
		}

		public void setAdverbTags(ArrayList<String> adverbTags) {
			this.adverbTags = adverbTags;
		}

	}

	@ConfigurationProperties(prefix = "grammarWordsList")
	public static class NounTags {

		private ArrayList<String> nounTags = new ArrayList<String>();

		private ArrayList<String> pluralNounTags = new ArrayList<String>();

		private ArrayList<String> adjectiveTags = new ArrayList<String>();

		public ArrayList<String> getNounTags() {
			return nounTags;
		}

		public void setNounTags(ArrayList<String> nounTags) {
			this.nounTags = nounTags;
		}

		public ArrayList<String> getPluralNounTags() {
			return pluralNounTags;
		}

		public void setPluralNounTags(ArrayList<String> pluralNounTags) {
			this.pluralNounTags = pluralNounTags;
		}

		public ArrayList<String> getAdjectiveTags() {
			return adjectiveTags;
		}

		public void setAdjectiveTags(ArrayList<String> adjectiveTags) {
			this.adjectiveTags = adjectiveTags;
		}
	}

	@ConfigurationProperties(prefix = "grammarWordsList")
	public static class ValidPOS {

		private ArrayList<String> validPos = new ArrayList<String>();

		public ArrayList<String> getValidPos() {
			return validPos;
		}

		public void setValidPos(ArrayList<String> validPos) {
			this.validPos = validPos;
		}

	}

	@ConfigurationProperties(prefix = "grammarWordsList")
	public static class TimeWords {

		private ArrayList<String> timeWords = new ArrayList<String>();

		public ArrayList<String> getTimeWords() {
			return timeWords;
		}

		public void setTimeWords(ArrayList<String> timeWords) {
			this.timeWords = timeWords;
		}

	}

	@ConfigurationProperties(prefix = "grammarWordsList")
	public static class NegationWords {

		private ArrayList<String> negationWords = new ArrayList<String>();

		public ArrayList<String> getNegationWords() {
			return negationWords;
		}

		public void setNegationWords(ArrayList<String> negationWords) {
			this.negationWords = negationWords;
		}

	}
}
