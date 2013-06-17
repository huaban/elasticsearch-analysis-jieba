package org.elasticsearch.plugin.analysis.jieba;

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

	public List<String> segmentSentence(String sentence) {
		List<String> result = Collections.emptyList();

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
				result = new ArrayList<String>();
				for (int i = 0; i < array.size(); ++i) {
					JSONObject obj = array.getJSONObject(i);
					result.add(obj.getString("word"));
				}
				return result;
			}
		}

		return result;
	}
}
