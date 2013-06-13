package org.elasticsearch.plugin.analysis.jieba;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;

public class JiebaSegmenterTest {
  
  @Test
  public void test() throws Exception {
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod("http://183.136.223.174:8000/_segment?type=pos");
    RequestEntity input = new StringRequestEntity("我是中国人,中华人民共和国", "text/plain", "UTF-8");
    post.setRequestEntity(input);	

    int status = client.executeMethod(post);
    if (status != 201 && status != 200) {
      throw new RuntimeException("Failed : HTTP error code : " + status);
    }
    byte[] bytes = post.getResponseBody();
    if (bytes != null && bytes.length > 0) {
      DefaultJSONParser parser = new DefaultJSONParser(new String(bytes));
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
