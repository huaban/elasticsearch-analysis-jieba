package org.elasticsearch.index.analysis;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public final class JiebaTokenizer extends Tokenizer {

	JiebaSegmenter segmenter;

    private Iterator<Object> tokenIter;
    private JSONArray array;

	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
    
    private int offset = 0;

	public JiebaTokenizer(String url, Reader input) {
		super(input);
		segmenter = new JiebaSegmenter(url);
	}

    public JiebaTokenizer(String ip, Integer port, String type, String key, Reader input) {
		super(input);
        segmenter = new JiebaSegmenter(ip, port, type, key);
    }

	@Override
	public boolean incrementToken() throws IOException {
		if (tokenIter == null) {
			array = segmenter.segmentSentence(input);
			tokenIter = array.iterator();
		}
		if (!tokenIter.hasNext())
			return false;

		// WordTokenFilter must clear attributes, as it is creating new tokens.
		clearAttributes();

        JSONObject nextWord = (JSONObject) tokenIter.next();
        int startOffset = nextWord.getInteger("start_offset");
        int endOffset = nextWord.getInteger("end_offset");
        offset = endOffset;
        offsetAtt.setOffset(startOffset, endOffset);
        termAtt.copyBuffer(nextWord.getString("word").toCharArray(), 0, endOffset - startOffset);
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
