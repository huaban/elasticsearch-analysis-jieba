package org.elasticsearch.index.analysis;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public final class JiebaTokenFilter extends TokenFilter {

    JiebaSegmenter segmenter;

    private Iterator<Object> tokenIter;
    private JSONArray array;

    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

    public JiebaTokenFilter(String url, TokenStream input) {
        super(input);
        segmenter = new JiebaSegmenter(url);
    }

    public JiebaTokenFilter(String ip, Integer port, String type, String key, TokenStream input) {
        super(input);
        segmenter = new JiebaSegmenter(ip, port, type, key);
    }


    @Override
    public boolean incrementToken() throws IOException {
        if (tokenIter == null || !tokenIter.hasNext()) {
            if (input.incrementToken()) {
                array = segmenter.segmentSentence(new StringReader(termAtt.toString()));
                tokenIter = array.iterator();
                if (!tokenIter.hasNext()) return false;
            } else {
                return false; // no more sentences, end of stream!
            }
        }
        // WordTokenFilter must clear attributes, as it is creating new tokens.
        clearAttributes();

        JSONObject nextWord = (JSONObject) tokenIter.next();
        String w = nextWord.getString("word");
        termAtt.copyBuffer(w.toCharArray(), 0, w.length());
        return true;
    }  
    
    @Override
    public void reset() throws IOException {
        super.reset();
        tokenIter = null;
    }

}
