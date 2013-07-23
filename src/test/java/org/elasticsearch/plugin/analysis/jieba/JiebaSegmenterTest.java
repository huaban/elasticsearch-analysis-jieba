package org.elasticsearch.plugin.analysis.jieba;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import junit.framework.TestCase;

import org.elasticsearch.index.analysis.Utility;
import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;


public class JiebaSegmenterTest extends TestCase {

    private String type;
    private String url;

    @Override
    protected void setUp() throws Exception {

    }

    @Test
    public void test() throws Exception {

        String output =
                Utility.restPost("http://183.136.223.174:8000/_segment?type=all", new StringReader("中华人民共和国"));
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

    public void test_jieba_thrift(String line) throws IOException {

    }

    @Test
    public void test_multi_segment_speed() throws IOException {
        int threadNum = 30;
        ExecutorService es = Executors.newFixedThreadPool(threadNum);
        List<Future<?>> futures = new ArrayList<Future<?>>();
        long start = System.currentTimeMillis();
        for (int i = 0; i < threadNum; ++i) {
            futures.add(es.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        File file = new File("/home/matrix/Downloads/pins.json");
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        while (br.ready()) {
                            String line = br.readLine();
                            Utility.restPost("http://127.0.0.1:8000/_segment?type=all", line.getBytes());
                        }
                        br.close();
                    }
                    catch(IOException e) {
                        
                    }
                }
            }));
        }
        while(!futures.isEmpty()) {
            Iterator<Future<?>> iter = futures.iterator();
            while (iter.hasNext()) {
                Future<?> future = iter.next();
                if (future.isDone())
                    iter.remove();
            }
        }
        long elapsed = (System.currentTimeMillis() - start);
        System.out.println(String.format("time escape:%d, rate:%fkb/s", elapsed,
                (new File("/home/matrix/Downloads/pins.json").length() * threadNum) / 1024.0f / (elapsed * 1.0 / 1000.0f)));
        
    }
    
    public void test_speed() throws IOException {
        File file = new File("/home/matrix/Downloads/pins.json");
        BufferedReader br = new BufferedReader(new FileReader(file));
        long start = System.currentTimeMillis();
        while (br.ready()) {
            String line = br.readLine();
            Utility.restPost("http://127.0.0.1:8000/_segment?type=all", line.getBytes());
        }
        br.close();
        long elapsed = (System.currentTimeMillis() - start);
        System.out.println(String.format("time escape:%d, rate:%fkb/s", elapsed,
                (file.length() * 1.0) / 1000.0f / (elapsed * 1.0 / 1000.0f)));
    }


}
