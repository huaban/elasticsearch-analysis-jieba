package org.elasticsearch.index.analysis;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Test;

public class JiebaTokenizerTest {

    @Test
    public void test() throws IOException {
        Tokenizer tokenizer = new JiebaTokenizer("http://183.136.223.174:8000/_segment", new StringReader("中华人民共和国"));
        while(tokenizer.incrementToken() == true) {
            CharTermAttribute termAtt = tokenizer.getAttribute(CharTermAttribute.class);
            System.out.println(String.format("term:%s", termAtt.toString()));
        }
        tokenizer.setReader(new StringReader("我是中国人"));
        tokenizer.reset();
        while(tokenizer.incrementToken() == true) {
            CharTermAttribute termAtt = tokenizer.getAttribute(CharTermAttribute.class);
            System.out.println(String.format("term:%s", termAtt.toString()));
        }
        
    }

}
