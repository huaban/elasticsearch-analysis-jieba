package org.elasticsearch.index.analysis;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.JiebaSegmenter.SegMode;
import com.huaban.analysis.jieba.SegToken;

public final class JiebaTokenFilter extends TokenFilter {

    JiebaSegmenter segmenter;

    private Iterator<SegToken> tokenIter;
    private List<SegToken> array;
    private String type;

    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
    private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);

    public JiebaTokenFilter(String type, TokenStream input) {
        super(input);
        this.type = type;
        segmenter = new JiebaSegmenter();
    }

    @Override
    public boolean incrementToken() throws IOException {
        if (tokenIter == null || !tokenIter.hasNext()) {
            if (input.incrementToken()) {
                if (type.equals("index"))
                    array = segmenter
                            .process(termAtt.toString(), SegMode.INDEX);
                else if (type.equals("other")) {
                    array = new ArrayList<SegToken>();
                    String token = termAtt.toString();
                    char[] ctoken = token.toCharArray();
                    for (int i = 0; i < ctoken.length; i++) {
                        /* 全角=>半角 */
                        if (ctoken[i] > 0xFF00 && ctoken[i] < 0xFF5F)
                            ctoken[i] = (char) (ctoken[i] - 0xFEE0);

                        /* 大写=>小写 */
                        if (ctoken[i] > 0x40 && ctoken[i] < 0x5b)
                            ctoken[i] = (char) (ctoken[i] + 0x20);
                    }
                    token = String.valueOf(ctoken);
                    array.add(new SegToken(token, 0, token.length()));
                } else
                    array = segmenter.process(termAtt.toString(),
                            SegMode.SEARCH);
                tokenIter = array.iterator();
                if (!tokenIter.hasNext())
                    return false;
            } else {
                return false; // no more sentences, end of stream!
            }
        }
        // WordTokenFilter must clear attributes, as it is creating new tokens.
        clearAttributes();

        SegToken token = tokenIter.next();
        offsetAtt.setOffset(token.startOffset, token.endOffset);
        String tokenString = token.word;
        termAtt.copyBuffer(tokenString.toCharArray(), 0, tokenString.length());
        typeAtt.setType("word");
        return true;
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        tokenIter = null;
    }

}
