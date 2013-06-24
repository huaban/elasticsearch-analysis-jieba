package org.elasticsearch.plugin.analysis.jieba;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
	
	public void jieba(String line) throws IOException {
	    		String output = Utility.restPost("http://127.0.0.1:8000/_segment?type=all", line.getBytes());
		if (null != output && output.length() > 0) {
			DefaultJSONParser parser = new DefaultJSONParser(output);
			JSONArray array = (JSONArray) parser.parse();
			List<String> result = new ArrayList<String>();
			for (int i = 0; i < array.size(); ++i) {
				JSONObject obj = array.getJSONObject(i);
				result.add(obj.getString("word"));
			}
		}
	}

    
    @Test
    public void test_speed() throws IOException {
        File file = new File("/home/matrix/Downloads/pins.json");
        BufferedReader br = new BufferedReader(new FileReader(file));
        long start = System.currentTimeMillis();
        while(br.ready()) {
            String line = br.readLine();
            jieba(line);
        }
        br.close();
        System.out.println(String.format("time escape:%d", (System.currentTimeMillis() - start)));
    }
   

}
