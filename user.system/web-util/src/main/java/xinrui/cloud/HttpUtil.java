package xinrui.cloud;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.net.MediaType;

/**
 * <p>
 * Http工具类
 * 
 * <p>
 * Http工具类，为系统提供通用Http访问操作方法：
 * 
 * <p>
 * 1、发送GET请求；
 * <p>
 * 2、发送POST请求。
 * 
 */
@SuppressWarnings("rawtypes")
public class HttpUtil {

	private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

	public static final String POST = "POST";

	public static final String GET = "GET";

	public static final String UTF8 = "UTF-8";

	public static final String GBK = "GBK";

	public static final String GB2312 = "GB2312";

	public static final String HEAD_CONTENT_TYPE_JSON = "application/json";

	public static final String HEAD_CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";

	/**
	 * <p>
	 * 发送GET请求
	 * 
	 * @param url GET请求地址
	 * 
	 * @return 与当前请求对应的响应内容字节数组
	 * 
	 */
	public static byte[] doGet(String url) {
		
		return HttpUtil.doGet(url, null, null, 0);
	}

	/**
	 * <p>
	 * 发送GET请求
	 * 
	 * @param url       GET请求地址
	 * @param headerMap GET请求头参数容器
	 * 
	 * @return 与当前请求对应的响应内容字节数组
	 * 
	 */
	public static byte[] doGet(String url, Map headerMap) {

		return HttpUtil.doGet(url, headerMap, null, 0);
	}

	/**
	 * <p>
	 * 发送GET请求
	 * 
	 * @param url       GET请求地址
	 * @param proxyUrl  代理服务器地址
	 * @param proxyPort 代理服务器端口号
	 * 
	 * @return 与当前请求对应的响应内容字节数组
	 * 
	 * @modify 窦海宁, 2012-03-19
	 */
	public static byte[] doGet(String url, String proxyUrl, int proxyPort) {

		return HttpUtil.doGet(url, null, proxyUrl, proxyPort);
	}

	/**
	 * <p>
	 * 发送GET请求
	 * 
	 * @param url       GET请求地址
	 * @param headerMap GET请求头参数容器
	 * @param proxyUrl  代理服务器地址
	 * @param proxyPort 代理服务器端口号
	 * 
	 * @return 与当前请求对应的响应内容字节数组
	 * 
	 * @modify 窦海宁, 2012-03-19
	 */
	public static synchronized  byte[] doGet(String url, Map headerMap, String proxyUrl, int proxyPort) {

		byte[] content = null;
		HttpClient httpClient = new HttpClient();
		httpClient.setTimeout(5000000);
		GetMethod getMethod = new GetMethod(url);
//		getMethod.setRequestHeader("Authorization","cd216cfc-3a2e-4160-8d72-37b4328084d2");
		if (headerMap != null) {

			// 头部请求信息
			if (headerMap != null) {

				Iterator iterator = headerMap.entrySet().iterator();
				while (iterator.hasNext()) {

					Entry entry = (Entry) iterator.next();
					getMethod.addRequestHeader(entry.getKey().toString(), entry.getValue().toString());
				}
			}
		}

		if (StringUtils.isNotBlank(proxyUrl)) {

			httpClient.getHostConfiguration().setProxy(proxyUrl, proxyPort);
		}

		// 设置成了默认的恢复策略，在发生异常时候将自动重试3次，在这里你也可以设置成自定义的恢复策略
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000000);
		// postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER , new
		// DefaultHttpMethodRetryHandler());
		InputStream inputStream = null;
		try {

			if (httpClient.executeMethod(getMethod) == HttpStatus.SC_OK) {

				// 读取内容
				inputStream = getMethod.getResponseBodyAsStream();
				content = IOUtils.toByteArray(inputStream);
			} else {
				System.err.println("Method failed: " + getMethod.getStatusLine());
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			IOUtils.closeQuietly(inputStream);
			getMethod.releaseConnection();
		}
		return content;
	}

	/**
	 * <p>
	 * 发送POST请求
	 * 
	 * @param url          POST请求地址
	 * @param parameterMap POST请求参数容器
	 * 
	 * @return 与当前请求对应的响应内容字节数组
	 * 
	 */
	public static byte[] doPost(String url, Map parameterMap) {

		return HttpUtil.doPost(url, null, parameterMap, null, null, 0);
	}

	/**
	 * <p>
	 * 发送POST请求
	 * 
	 * @param url          POST请求地址
	 * @param parameterMap POST请求参数容器
	 * @param paramCharset 参数字符集名称
	 * 
	 * @return 与当前请求对应的响应内容字节数组
	 * 
	 * @modify 窦海宁, 2012-05-21
	 */
	public static byte[] doPost(String url, Map parameterMap, String paramCharset) {

		return HttpUtil.doPost(url, null, parameterMap, paramCharset, null, 0);
	}

	/**
	 * <p>
	 * 发送POST请求
	 * 
	 * @param url          POST请求地址
	 * @param headerMap    POST请求头参数容器
	 * @param parameterMap POST请求参数容器
	 * @param paramCharset 参数字符集名称
	 * 
	 * @return 与当前请求对应的响应内容字节数组
	 * 
	 * @modify 窦海宁, 2012-05-21
	 */
	public static byte[] doPost(String url, Map headerMap, Map parameterMap, String paramCharset) {

		return HttpUtil.doPost(url, headerMap, parameterMap, paramCharset, null, 0);
	}

	/**
	 * <p>
	 * 发送POST请求
	 * 
	 * @param url          POST请求地址
	 * @param parameterMap POST请求参数容器
	 * @param paramCharset 参数字符集名称
	 * @param proxyUrl     代理服务器地址
	 * @param proxyPort    代理服务器端口号
	 * 
	 * @return 与当前请求对应的响应内容字节数组
	 * 
	 */
	public static byte[] doPost(String url, Map parameterMap, String paramCharset, String proxyUrl, int proxyPort) {

		return HttpUtil.doPost(url, null, parameterMap, paramCharset, proxyUrl, proxyPort);
	}

	/**
	 * <p>
	 * 发送POST请求
	 * 
	 * @param url          POST请求地址
	 * @param headerMap    POST请求头参数容器
	 * @param parameterMap POST请求参数容器
	 * @param paramCharset 参数字符集名称
	 * @param proxyUrl     代理服务器地址
	 * @param proxyPort    代理服务器端口号
	 * 
	 * @return 与当前请求对应的响应内容字节数组
	 * 
	 * @modify 窦海宁, 2012-05-21
	 */
	public static byte[] doPost(String url, Map headerMap, Map parameterMap, String paramCharset, String proxyUrl,
			int proxyPort) {

		byte[] content = null;
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		if (StringUtils.isNotBlank(paramCharset)) {
			postMethod.getParams().setContentCharset(paramCharset);
			postMethod.getParams().setHttpElementCharset(paramCharset);
		}
		if (headerMap != null) {

			// 头部请求信息
			if (headerMap != null) {

				Iterator iterator = headerMap.entrySet().iterator();
				while (iterator.hasNext()) {

					Entry entry = (Entry) iterator.next();
					postMethod.addRequestHeader(entry.getKey().toString(), entry.getValue().toString());
				}
			}
		}

		Iterator iterator = parameterMap.keySet().iterator();
		while (iterator.hasNext()) {

			String key = (String) iterator.next();
			postMethod.addParameter(key, (String) parameterMap.get(key));
		}

		if (StringUtils.isNotBlank(proxyUrl)) {

			httpClient.getHostConfiguration().setProxy(proxyUrl, proxyPort);
		}

		// 设置成了默认的恢复策略，在发生异常时候将自动重试3次，在这里你也可以设置成自定义的恢复策略
		postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 10000);
		// postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER , new
		// DefaultHttpMethodRetryHandler());
		InputStream inputStream = null;
		try {

			if (httpClient.executeMethod(postMethod) == HttpStatus.SC_OK) {

				// 读取内容
				inputStream = postMethod.getResponseBodyAsStream();
				content = IOUtils.toByteArray(inputStream);
			} else {

				System.err.println("Method failed: " + postMethod.getStatusLine());
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			IOUtils.closeQuietly(inputStream);
			postMethod.releaseConnection();
		}
		return content;
	}

	public static HttpResult getResponse(String url, String method, String encoding, String body, Integer timeout) {
		OutputStream out = null;
		InputStream in = null;
		try {
			URL postUrl = new URL(url);
			
			HttpURLConnection conn = (HttpURLConnection) postUrl.openConnection();
//			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//			conn.setRequestProperty("Authorization", "cd216cfc-3a2e-4160-8d72-37b4328084d2");

			if (!GET.equals(method)) {
				conn.setDoOutput(true);
			}
			conn.setDoInput(true);
			conn.setRequestMethod(method);
			conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(true);
			if (timeout != null) {
				conn.setReadTimeout(timeout);
			}
			conn.connect();
			if (!GET.equals(method)) {
				if (!StringUtils.isEmpty(body)) {
					out = conn.getOutputStream();
					out.write(body.getBytes());
					out.flush();
				}
			}
			HttpResult httpResult = new HttpResult();
			httpResult.setResponseCode(conn.getResponseCode());
			httpResult.setResponseMessage(conn.getResponseMessage());
			if (HttpURLConnection.HTTP_OK == httpResult.getResponseCode()) {
				in = conn.getInputStream();
				String result = read(in, encoding);
				httpResult.setResult(result);
			} else {
				in = conn.getErrorStream();
				String result = read(in, encoding);
				logger.info("请求不成功, url:{} body:{} responsecode:{} result:{}",
						new Object[] { url, body, httpResult.getResponseCode(), result });
			}
			return httpResult;
		} catch (MalformedURLException e) {
			logger.error("请求地址有误, url:" + url, e);
			throw new RuntimeException(e.getMessage());
		} catch (Exception e) {
			logger.error("IO错误,url:" + url + ",body:" + body, e);
			throw new RuntimeException(e.getMessage());
		} finally {
			close(in);
			close(out);
		}
	}

	public static String read(InputStream in, String encoding) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, encoding));
			StringBuilder builder = new StringBuilder();
			String line = null;
			while (null != (line = reader.readLine())) {
				builder.append(line);
			}
			return builder.toString();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			close(in);
		}
	}

	private static void close(InputStream in) {
		if (null != in) {
			try {
				in.close();
			} catch (Exception e) {
			}
		}
	}

	private static void close(OutputStream out) {
		if (null != out) {
			try {
				out.close();
			} catch (Exception e) {

			}
		}
	}

}