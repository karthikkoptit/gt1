package com.nutrisystem.orange.java.text;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.KeywordAttribute;
import org.apache.lucene.util.Version;

/**
 * Parsing original text into key words and phrases.
 * 
 */
public class EnglishTextParser {
    /**
     * Parses and rank orders the highest frequency words/phrases (longest
     * phrase = 3 words)
     * 
     * @param ReasonDown
     *            String attribute values, and the output file.
     * @throws LangDetectException
     */
    public static Set<String> parse(String text) {
	Set<String> tokenSet = new LinkedHashSet<>();

	Analyzer analyzer = null;
	// Analyzer analyzer = null;
	TokenStream ts = null;

	try {
	    Version matchVersion = Version.LUCENE_45;
	    analyzer = new EnglishTextAnalyzer(matchVersion);
	    ts = analyzer.tokenStream("myfield", new StringReader(text));
	    ts.addAttribute(KeywordAttribute.class);
	    ts.reset();
	    while (ts.incrementToken()) {
		String token = ts.getAttribute(CharTermAttribute.class).toString();
		if (!token.contains(String.valueOf(ShingleFilter.FILLER_TOKEN[0]))) {
		    token = token.trim();
		    tokenSet.add(token);
		}
	    }
	    ts.end();
	} catch (Exception e) {
	    throw new RuntimeException(e);
	} finally {
	    try {
		ts.close();
	    } catch (IOException e) {
		throw new RuntimeException(e);
	    }
	    analyzer.close();
	}	
	return tokenSet;
    }
}
