package org.elasticsearch.index.analysis.py;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.python.core.PyDictionary;
import org.python.core.PyList;
import org.python.core.PyString;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public final class PyJiebaTokenFilter extends TokenFilter {

	PyJiebaSegmenter segmenter;

    private Iterator<?> tokenIter;
    private PyList array;
    private String type;

    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

    public PyJiebaTokenFilter(String type, File pluginFile, File configFile, TokenStream input) {
        super(input);
        this.type = type;
        segmenter = PyJiebaSegmenter.getInstance(pluginFile, configFile);
    }


    @Override
    public boolean incrementToken() throws IOException {
        if (tokenIter == null || !tokenIter.hasNext()) {
            if (input.incrementToken()) {
                if (type.equals("index")) 
                    array = segmenter.cut_for_index(termAtt.toString());
                else
                    array = segmenter.cut_for_search(termAtt.toString());
                tokenIter = array.iterator();                
                if (!tokenIter.hasNext()) return false;
            } else {
                return false; // no more sentences, end of stream!
            }
        }
        // WordTokenFilter must clear attributes, as it is creating new tokens.
        clearAttributes();

        PyDictionary nextWord = (PyDictionary) tokenIter.next();
        String w = nextWord.get(new PyString("word")).asString();
        termAtt.copyBuffer(w.toCharArray(), 0, w.length());
        return true;
    }  
    
    @Override
    public void reset() throws IOException {
        super.reset();
        tokenIter = null;
    }

}
