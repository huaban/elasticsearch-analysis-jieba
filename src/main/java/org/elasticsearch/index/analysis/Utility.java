package org.elasticsearch.index.analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Utility {

	/**
	 * REST请求POST方法
	 * 
	 * @param url
	 *            请求的资源地址
	 * @param value
	 *            请求的POST数据
	 * @return string
	 * @throws IOException
	 */
	public static String restPost(String url, Reader in) throws IOException {
	    if (null == url || url.isEmpty()) {
	        throw new IllegalArgumentException("Invalid Url [empty or null]");
	    }
		// 实例一个http资源链接
		HttpURLConnection urlconn = (HttpURLConnection) new URL(url).openConnection();
		// 设置链接的属性
		urlconn.setRequestMethod("POST");
		urlconn.setDoOutput(true);
		urlconn.setDoInput(true);
		urlconn.setUseCaches(false);
		urlconn.setAllowUserInteraction(false);
		urlconn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		// 实例一个输出流对象
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlconn.getOutputStream()));
		int len = 4096;
		char[] buff = new char[len];
		while ((len = in.read(buff)) != -1) {
			bw.write(buff, 0, len);
		}
		bw.close();
		// 获得请求的响应状态
		if (urlconn.getResponseCode() != 200) {
			throw new IOException(urlconn.getResponseMessage());
		}
		// 实例一个Buffer读取和字符串Builder
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(urlconn.getInputStream()));
		StringBuilder stringBuilder = new StringBuilder();
		String line;
		// 将读取到的数据装载到line当中
		while ((line = bufferedReader.readLine()) != null) {
			stringBuilder.append(line);
		}
		bufferedReader.close();
		urlconn.disconnect();
		return stringBuilder.toString();
	}	
	
	/**
	 * REST请求POST方法
	 * 
	 * @param url
	 *            请求的资源地址
	 * @param value
	 *            请求的POST数据
	 * @return string
	 * @throws IOException
	 */
	public static String restPost(String url, byte[] value) throws IOException {
	    if (null == url || url.isEmpty()) {
	        throw new IllegalArgumentException("Invalid Url [empty or null]");
	    }
		// 实例一个http资源链接
		HttpURLConnection urlconn = (HttpURLConnection) new URL(url).openConnection();
		// 设置链接的属性
		urlconn.setRequestMethod("POST");
		urlconn.setDoOutput(true);
		urlconn.setDoInput(true);
		urlconn.setUseCaches(false);
		urlconn.setAllowUserInteraction(false);
		urlconn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		// 实例一个输出流对象
		OutputStream outs = urlconn.getOutputStream();
		outs.write(value);
		outs.close();
		// 获得请求的响应状态
		if (urlconn.getResponseCode() != 200) {
			throw new IOException(urlconn.getResponseMessage());
		}
		// 实例一个Buffer读取和字符串Builder
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(urlconn.getInputStream()));
		StringBuilder stringBuilder = new StringBuilder();
		String line;
		// 将读取到的数据装载到line当中
		while ((line = bufferedReader.readLine()) != null) {
			stringBuilder.append(line);
		}
		bufferedReader.close();
		urlconn.disconnect();
		return stringBuilder.toString();
	}

	/**
	 * 实现对REST服务的请求
	 * 
	 * @param urlStr
	 * @param urlParam
	 * @return
	 * @throws java.io.IOException
	 */
	public static String restGet(String urlStr, Map<String, String> urlParam)
			throws IOException {
		String urlParamStr = "?";
		if (!urlParam.isEmpty()) {
			// 定义一个迭代器，并将MAP值的集合赋值
			Iterator<Entry<String, String>> ups = urlParam.entrySet().iterator();
			while (ups.hasNext()) {
				Entry<String, String> MUPS = ups.next();
				urlParamStr += MUPS.getKey() + "="
						+ MUPS.getValue().toString().trim() + "&";
			}
			urlParamStr = urlParamStr.substring(0, urlParamStr.length() - 1);
		}
		// 实例一个URL资源
		URL url = new URL(urlStr + urlParamStr);
		// 实例一个HTTP CONNECT
		HttpURLConnection connet = (HttpURLConnection) url.openConnection();
		if (connet.getResponseCode() != 200) {
			throw new IOException(connet.getResponseMessage());
		}
		// 将返回的值存入到String中
		BufferedReader brd = new BufferedReader(new InputStreamReader(
				connet.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line;

		while ((line = brd.readLine()) != null) {
			sb.append(line);
		}
		brd.close();
		connet.disconnect();

		return sb.toString();
	}
}