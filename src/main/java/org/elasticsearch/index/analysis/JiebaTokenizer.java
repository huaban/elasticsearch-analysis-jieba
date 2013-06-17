package org.elasticsearch.index.analysis;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public final class JiebaTokenizer extends Tokenizer {

	JiebaSegmenter segmenter;

	private Iterator<String> tokenIter;
	private List<String> tokenBuffer;

	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

	public JiebaTokenizer(String url, Reader input) {
		super(input);
		segmenter = new JiebaSegmenter(url);
	}

	@Override
	public boolean incrementToken() throws IOException {
		if (tokenIter == null) {
			tokenBuffer = segmenter.segmentSentence(termAtt.toString());
			tokenIter = tokenBuffer.iterator();
		}
		if (!tokenIter.hasNext())
			return false;
		
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
