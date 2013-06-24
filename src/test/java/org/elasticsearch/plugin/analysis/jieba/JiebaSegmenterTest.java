package org.elasticsearch.plugin.analysis.jieba;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.thrift.TException;
import org.elasticsearch.index.analysis.Utility;
import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.huaban.segment.HubanSegmentClient;
import com.huaban.thrift.ConnectionManager;
import com.huaban.thrift.ConnectionProvider;
import com.huaban.thrift.GenericConnectionProvider;

public class JiebaSegmenterTest extends TestCase {

    private String type;
    private String url;
    private HubanSegmentClient client;
    private ConnectionManager connManager;
    private ConnectionProvider connProvider;
    
    
	@Override
    protected void setUp() throws Exception {
	    try {
            connProvider = new GenericConnectionProvider("127.0.0.1", 8000, 1000);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }	
	    connManager = new ConnectionManager();
	    connManager.setConnectionProvider(connProvider);
	    client = new HubanSegmentClient(connManager);
    }

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
	
	public void jieba(String line) throws IOException, TException {
	    String output = client.segment("all", line);
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
	
	public void test_jieba_thrift(String line) throws IOException {
	    
	}

    
    @Test
    public void test_speed() throws IOException, TException {
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
