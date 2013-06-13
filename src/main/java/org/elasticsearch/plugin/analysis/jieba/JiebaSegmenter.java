package org.elasticsearch.plugin.analysis.jieba;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;

public class JiebaSegmenter {
  private HttpClient client = new HttpClient(new HttpClientParams(),new SimpleHttpConnectionManager(true) );    
  private String url;
  private PostMethod post;
    
  public JiebaSegmenter(String url) {
    this.url = url;
    post = new PostMethod(this.url);
  }
  
  public List<String> segmentSentence(String sentence) {
      List<String> result = Collections.emptyList();
      try {
		RequestEntity input = new StringRequestEntity(sentence, "text/plain",
				"UTF-8");
		post.setRequestEntity(input);

		int status = client.executeMethod(post);
		if (status != 201 && status != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + status);
		}  
		byte[] bytes = post.getResponseBody();
		if (bytes != null && bytes.length > 0) {
		  DefaultJSONParser parser = new DefaultJSONParser(new String(bytes));
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
      }
      catch (Exception he) {
        
      }
      finally {
        post.releaseConnection();
      }
      
      return result;
  }
}
