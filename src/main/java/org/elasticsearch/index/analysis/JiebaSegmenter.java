package org.elasticsearch.index.analysis;

import java.io.IOException;
import java.io.Reader;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.DefaultJSONParser;

public class JiebaSegmenter {
    private String url;
        
    public JiebaSegmenter(String url) {
        this.url = url;
    }
    
	public JiebaSegmenter(String ip, Integer port, String type, String key) {
	    this(String.format("http://%s:%d/_segment?type=%s&key=%s", ip, port, type, key));
	    if (!type.equals("index") && !type.equals("search"))
	        throw new IllegalArgumentException("type should be index or search!");
	}
	

	public JSONArray segmentSentence(Reader in) {
		String output = null;
		try {
			output = Utility.restPost(this.url, in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (null != output && output.length() > 0) {
			DefaultJSONParser parser = new DefaultJSONParser(output);
			JSONArray array = (JSONArray) parser.parse();
			if (array.size() > 0) {
				return array;
			}
		}

		return new JSONArray();
	}
}
