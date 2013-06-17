package org.elasticsearch.plugin.analysis.jieba;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

public class Utility {

	public static final char[] STRING_CHAR_ARRAY = new String("未##串")
			.toCharArray();

	public static final char[] NUMBER_CHAR_ARRAY = new String("未##数")
			.toCharArray();

	public static final char[] START_CHAR_ARRAY = new String("始##始")
			.toCharArray();

	public static final char[] END_CHAR_ARRAY = new String("末##末")
			.toCharArray();

	/**
	 * Delimiters will be filtered to this character by {@link SegTokenFilter}
	 */
	public static final char[] COMMON_DELIMITER = new char[] { ',' };

	/**
	 * Space-like characters that need to be skipped: such as space, tab,
	 * newline, carriage return.
	 */
	public static final String SPACES = " 　\t\r\n";

	/**
	 * Maximum bigram frequency (used in the smoothing function).
	 */
	public static final int MAX_FREQUENCE = 2079997 + 80000;

	/**
	 * compare two arrays starting at the specified offsets.
	 * 
	 * @param larray
	 *            left array
	 * @param lstartIndex
	 *            start offset into larray
	 * @param rarray
	 *            right array
	 * @param rstartIndex
	 *            start offset into rarray
	 * @return 0 if the arrays are equal，1 if larray > rarray, -1 if larray <
	 *         rarray
	 */
	public static int compareArray(char[] larray, int lstartIndex,
			char[] rarray, int rstartIndex) {

		if (larray == null) {
			if (rarray == null || rstartIndex >= rarray.length)
				return 0;
			else
				return -1;
		} else {
			// larray != null
			if (rarray == null) {
				if (lstartIndex >= larray.length)
					return 0;
				else
					return 1;
			}
		}

		int li = lstartIndex, ri = rstartIndex;
		while (li < larray.length && ri < rarray.length
				&& larray[li] == rarray[ri]) {
			li++;
			ri++;
		}
		if (li == larray.length) {
			if (ri == rarray.length) {
				// Both arrays are equivalent, return 0.
				return 0;
			} else {
				// larray < rarray because larray has ended first.
				return -1;
			}
		} else {
			// differing lengths
			if (ri == rarray.length) {
				// larray > rarray because rarray has ended first.
				return 1;
			} else {
				// determine by comparison
				if (larray[li] > rarray[ri])
					return 1;
				else
					return -1;
			}
		}
	}

	/**
	 * Compare two arrays, starting at the specified offsets, but treating
	 * shortArray as a prefix to longArray. As long as shortArray is a prefix of
	 * longArray, return 0. Otherwise, behave as
	 * {@link Utility#compareArray(char[], int, char[], int)}
	 * 
	 * @param shortArray
	 *            prefix array
	 * @param shortIndex
	 *            offset into shortArray
	 * @param longArray
	 *            long array (word)
	 * @param longIndex
	 *            offset into longArray
	 * @return 0 if shortArray is a prefix of longArray, otherwise act as
	 *         {@link Utility#compareArray(char[], int, char[], int)}
	 */
	public static int compareArrayByPrefix(char[] shortArray, int shortIndex,
			char[] longArray, int longIndex) {

		// a null prefix is a prefix of longArray
		if (shortArray == null)
			return 0;
		else if (longArray == null)
			return (shortIndex < shortArray.length) ? 1 : 0;

		int si = shortIndex, li = longIndex;
		while (si < shortArray.length && li < longArray.length
				&& shortArray[si] == longArray[li]) {
			si++;
			li++;
		}
		if (si == shortArray.length) {
			// shortArray is a prefix of longArray
			return 0;
		} else {
			// shortArray > longArray because longArray ended first.
			if (li == longArray.length)
				return 1;
			else
				// determine by comparison
				return (shortArray[si] > longArray[li]) ? 1 : -1;
		}
	}

	/**
	 * REST请求POST方法
	 * 
	 * @param strUrl
	 *            请求的资源地址
	 * @param value
	 *            请求的POST数据
	 * @return string
	 * @throws IOException
	 */
	public static String restPost(String strUrl, byte[] value) throws IOException {
		if ("".equals(strUrl))
			return "0";
		String paramStr = "";
		URL url = new URL(strUrl);
		// 实例一个http资源链接
		HttpURLConnection urlconn = (HttpURLConnection) url.openConnection();
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
	public static String httpGet(String urlStr, Map urlParam)
			throws IOException {
		String urlParamStr = "?";
		if (!urlParam.isEmpty()) {
			// 定义一个迭代器，并将MAP值的集合赋值
			Iterator ups = urlParam.entrySet().iterator();
			while (ups.hasNext()) {
				Map.Entry MUPS = (Map.Entry) ups.next();
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