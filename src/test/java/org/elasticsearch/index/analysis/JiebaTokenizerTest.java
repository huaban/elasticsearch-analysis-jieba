package org.elasticsearch.index.analysis;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.elasticsearch.index.analysis.py.PyJiebaTokenizer;
import org.junit.Test;

public class JiebaTokenizerTest {

    @Test
    public void test() throws IOException {
        Tokenizer tokenizer = new JiebaTokenizer("183.136.223.174", 8000, "index", "4IWf3Ul9OuI1x", new StringReader("中华人民共和国"));
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
    
    @Test
    public void test_py() throws IOException {
        Tokenizer tokenizer = new PyJiebaTokenizer("index", new File("."), new File("data"), new StringReader("中华人民共和国"));
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
    Tokenizer tokenizer = new JiebaTokenizer("183.136.223.174", 8000, "index", "4IWf3Ul9OuI1x", new StringReader("中华人民共和国"));
    
    @Test
    public void test_segment_speed() throws IOException {
        File file = new File("/home/matrix/Downloads/pins.json");
        BufferedReader br = new BufferedReader(new FileReader(file));
        long start = System.currentTimeMillis();
        while (br.ready()) {
            String line = br.readLine();
            tokenizer.setReader(new StringReader(line));
            tokenizer.reset();
            while(tokenizer.incrementToken() == true) {
                CharTermAttribute termAtt = tokenizer.getAttribute(CharTermAttribute.class);
                System.out.println(termAtt.toString());
            }
        }
        br.close();
        long elapsed = (System.currentTimeMillis() - start);
        System.out.println(String.format("time escape:%d, rate:%fkb/s", elapsed, (file.length() * 1.0)/1000.0f/(elapsed*1.0/1000.0f)));

    }

}
