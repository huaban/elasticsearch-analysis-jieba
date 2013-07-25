package org.elasticsearch.index.analysis;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.python.core.PyDictionary;
import org.python.core.PyList;
import org.python.core.PyString;


public final class PyJiebaTokenizer extends Tokenizer {

	PyJiebaSegmenter segmenter;

    private Iterator<?> tokenIter;
    private PyList array;
    private String type;

	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
    
    private int offset = 0;

	public PyJiebaTokenizer(String type, Reader input) {
		super(input);
		this.type = type;
		segmenter = PyJiebaSegmenter.getInstance();
	}

	@Override
	public boolean incrementToken() throws IOException {
		if (tokenIter == null) {
		    if (type.equals("index"))
		        array = segmenter.cut_for_index(input);
		    else
		        array = segmenter.cut_for_search(input);
		    tokenIter = array.iterator();
		}
		if (!tokenIter.hasNext())
			return false;

		// WordTokenFilter must clear attributes, as it is creating new tokens.
		clearAttributes();

        PyDictionary nextWord = (PyDictionary) tokenIter.next();
        int startOffset = nextWord.get(new PyString("start_offset")).asInt();
        int endOffset = nextWord.get(new PyString("end_offset")).asInt();
        offset = endOffset;
        offsetAtt.setOffset(startOffset, endOffset);
        termAtt.copyBuffer(nextWord.get(new PyString("word")).asString().toCharArray(), 0, endOffset - startOffset);
		return true;
	}
    
	@Override
    public final void end() {
      // set final offset
      final int finalOffset = correctOffset(offset);
      this.offsetAtt.setOffset(finalOffset, finalOffset);
    }    

    @Override
	public void reset() throws IOException {
		super.reset();
		tokenIter = null;
        offset = 0;
	}

}
