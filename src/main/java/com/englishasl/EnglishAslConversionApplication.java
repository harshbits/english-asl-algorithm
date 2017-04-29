package com.englishasl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.englishasl.service.ASLConversionService;
import com.englishasl.service.EnglishGrammarCheck;
import com.englishasl.service.EnglishParserService;
import com.englishasl.service.IntelligentLearningAgent;
import com.englishasl.service.TrainnedModelParser;
import com.englishasl.service.impl.ASLConversionServiceImpl;
import com.englishasl.service.impl.EnglishGrammarCheckImpl;
import com.englishasl.service.impl.EnglishParserServiceImpl;
import com.englishasl.service.impl.IntelligentLearningAgentImpl;
import com.englishasl.service.impl.TrainnedModelParserImpl;

@SpringBootApplication
@EnableAutoConfiguration
public class EnglishAslConversionApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnglishAslConversionApplication.class, args);
	}

	@Bean
	public EnglishParserService parser() {
		return new EnglishParserServiceImpl();
	}
	
	@Bean
	public EnglishGrammarCheck grammarCheck() {
		return new EnglishGrammarCheckImpl();
	}

	@Bean
	public ASLConversionService aslConversion() {
		return new ASLConversionServiceImpl();
	}
	
	@Bean
	public TrainnedModelParser trainnedModelParser(){
		return new TrainnedModelParserImpl();
	}
	
	@Bean
	public IntelligentLearningAgent intelligentLearningAgent(){
		return new IntelligentLearningAgentImpl();
	}
	
}
