package org.elasticsearch.plugin.analysis.jieba;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.index.analysis.Utility;
import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;

public class JiebaSegmenterTest {

	@Test
	public void test() throws Exception {

		String output = Utility.restPost("http://183.136.223.174:8000/_segment?type=all", "hello,world".getBytes());
		if (null != output && output.length() > 0) {
			DefaultJSONParser parser = new DefaultJSONParser(output);
			JSONArray array = (JSONArray) parser.parse();
			List<String> result = new ArrayList<String>();
			for (int i = 0; i < array.size(); ++i) {
				JSONObject obj = array.getJSONObject(i);
				System.out.println(obj.toJSONString());
				result.add(obj.getString("word"));
			}
			System.out.println(result);
		}

	}

}
