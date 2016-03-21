package com.nutrisystem.orange.java.text;

import java.io.IOException;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.FilteringTokenFilter;
import org.apache.lucene.util.Version;

/**
 * Filter all token with numbers
 */
public final class NumberFilter extends FilteringTokenFilter {
  private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
  
  /**
   * Create a new LowerCaseFilter, that normalizes token text to lower case.
   * 
   * @param matchVersion See <a href="#version">above</a>
   * @param in TokenStream to filter
   */
  public NumberFilter(TokenStream in) {
    super(Version.LUCENE_45, in);
  }
  
@Override
protected boolean accept() throws IOException {
    for (int i = 0; i < termAtt.length(); i++) {
	if (!Character.isLetter(termAtt.charAt(i)))
	    return false;
    }
    return true;
}
}
