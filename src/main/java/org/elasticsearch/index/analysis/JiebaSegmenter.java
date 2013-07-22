package org.elasticsearch.index.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;

public class JiebaSegmenter {
    private String url;

    public JiebaSegmenter(String url) {
        this.url = url;
    }
    
	public JiebaSegmenter(String ip, Integer port, String type, String key) {
	    this(String.format("http://%s:%d/_segment?type=%s&key=%s", ip, port, type, key));
	}
	

	public JSONArray segmentSentence(String sentence) {
		String output = null;
		try {
			output = Utility.restPost(this.url, sentence.getBytes());
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
