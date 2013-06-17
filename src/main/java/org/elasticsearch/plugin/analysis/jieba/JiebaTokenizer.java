package org.elasticsearch.plugin.analysis.jieba;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;


public class JiebaTokenizer extends TokenFilter {
  
  JiebaSegmenter segmenter;
  
  private Iterator<String> tokenIter;
  private List<String> tokenBuffer;
    
  private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    
  public JiebaTokenizer(String url, TokenStream input) {
    super(input);
    segmenter = new JiebaSegmenter(url);
  }

  
  @Override
  public boolean incrementToken() throws IOException {
    if (tokenIter == null || !tokenIter.hasNext()) {
      // there are no remaining tokens from the current sentence... are there more sentences?
      if (input.incrementToken()) {

        tokenBuffer = segmenter.segmentSentence(termAtt.toString());
        tokenIter = tokenBuffer.iterator();

        if (!tokenIter.hasNext())
          return false;
      } else {
        return false; // no more sentences, end of stream!
      }
    } 
    // WordTokenFilter must clear attributes, as it is creating new tokens.
    clearAttributes();

    String nextWord = tokenIter.next();
    termAtt.copyBuffer(nextWord.toCharArray(), 0, nextWord.length());
    return true;    
  }
  
  @Override
  public void reset() throws IOException {
    super.reset();
    tokenIter = null;
  }

}
