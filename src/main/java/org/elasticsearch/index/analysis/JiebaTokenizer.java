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
    private StringBuffer sb;

    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
    
    private int offset = 0;

    public JiebaTokenizer(String url, Reader input) {
        super(input);
        sb = new StringBuffer();
        segmenter = new JiebaSegmenter(url);
    }

    public JiebaTokenizer(String ip, Integer port, String type, String key, Reader input) {
        super(input);
        sb = new StringBuffer();
        segmenter = new JiebaSegmenter(ip, port, type, key);
    }

    @Override
    public boolean incrementToken() throws IOException {
        if (tokenIter == null) {
            int len = -1;
            char[] buffer = new char[1024];
            while ((len = input.read(buffer)) != -1) {
                sb.append(buffer, 0, len);
            }

            array = segmenter.segmentSentence(sb.toString());
            if (null == array) return false;
            tokenIter = array.iterator();
        }
        if (!tokenIter.hasNext()) return false;

        // WordTokenFilter must clear attributes, as it is creating new tokens.
        clearAttributes();

        JSONObject nextWord = (JSONObject) tokenIter.next();
        int startOffset = nextWord.getInteger("s");
        int endOffset = nextWord.getInteger("e");
        offset = endOffset;
        offsetAtt.setOffset(startOffset, endOffset);
        termAtt.copyBuffer(nextWord.getString("w").toCharArray(), 0, endOffset - startOffset);
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
        sb = new StringBuffer();
    }

}
