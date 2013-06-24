package org.elasticsearch.index.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.apache.http.conn.params.ConnManagerParamBean;
import org.apache.thrift.TException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.huaban.segment.HubanSegmentClient;
import com.huaban.thrift.ConnectionManager;
import com.huaban.thrift.ConnectionProvider;
import com.huaban.thrift.GenericConnectionProvider;

public class JiebaSegmenter {
	private String type;
    private String url;
    private HubanSegmentClient client;
    private ConnectionManager connManager;
    private ConnectionProvider connProvider;

    public JiebaSegmenter(String url) {
        this.url = url;
    }
    
	public JiebaSegmenter(String ip, Integer port, String type) {
	    this(String.format("%s:%d?type=%s", ip, port, type));
	    this.type = type;	
	    try {
            connProvider = new GenericConnectionProvider(ip, port, 1000);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }	
	    connManager = new ConnectionManager();
	    connManager.setConnectionProvider(connProvider);
	    client = new HubanSegmentClient(connManager);
	}
	
	public List<String> segmentWithThrift(String sentence) {
		List<String> result = Collections.emptyList();

		String output = null;
		try {
            output = client.segment(this.type, sentence);
        } catch (TException e) {
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
