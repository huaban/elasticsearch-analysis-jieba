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
    private StringBuffer sb;

    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

    public JiebaTokenizer(String url, Reader input) {
        super(input);
        sb = new StringBuffer();
        segmenter = new JiebaSegmenter(url);
    }
    
    public JiebaTokenizer(String ip, Integer port, String type, Reader input) {
        super(input);
        sb = new StringBuffer();
        segmenter = new JiebaSegmenter(ip, port, type);
    }    

    @Override
    public boolean incrementToken() throws IOException {
        if (tokenIter == null) {
            int len = -1;
            char[] buffer = new char[1024];
            while ((len = input.read(buffer)) != -1) {
                sb.append(buffer, 0, len);
            }

            tokenBuffer = segmenter.segmentSentence(sb.toString());
            tokenIter = tokenBuffer.iterator();
        }
        if (!tokenIter.hasNext()) return false;

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
        sb = new StringBuffer();
    }

}
