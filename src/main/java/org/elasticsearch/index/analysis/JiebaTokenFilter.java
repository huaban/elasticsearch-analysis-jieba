package org.elasticsearch.index.analysis;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
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
		else
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
	termAtt.copyBuffer(token.token.toCharArray(), 0, token.token.length());
	typeAtt.setType("word");
	return true;
    }

    @Override
    public void reset() throws IOException {
	super.reset();
	tokenIter = null;
    }

}
