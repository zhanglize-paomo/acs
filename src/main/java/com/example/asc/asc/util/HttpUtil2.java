package com.example.asc.asc.util;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtil2 {


	private static final CloseableHttpClient httpClient;

	public static final String CHARSET = "UTF-8";

	private static final Logger logger = LoggerFactory.getLogger(HttpUtil2.class);

	private static final String APPLICATION_JSON = "application/json";

	private static final String CONTENT_TYPE_JSON = "text/json";

	private static final String APPLICATION_FORM = "application/x-www-form-urlencoded";

	@SuppressWarnings("unused")
	private static final String MULTIPART_FORM_DATA = "multipart/form-data";

	static {
		RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(60000).setConnectTimeout(60000).setSocketTimeout(60000).setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
		PoolingHttpClientConnectionManager pcm = new PoolingHttpClientConnectionManager();
		pcm.setMaxTotal(500);
		pcm.setDefaultMaxPerRoute(100);

		httpClient = HttpClientBuilder.create().setConnectionManager(pcm).setDefaultRequestConfig(config)
//				.setRetryHandler(new HttpExceptionRetryHandler(3, false, Collections.emptyList())) // 设置失败重试
				.build();
	}


	/**
	 * HTTP Post 获取内容
	 *
	 * @param url     请求的url地址 ?之前的地址
	 * @param params  请求的参数
	 * @param charset 编码格式
	 * @return 页面内容
	 */
	public static String doPost(String url, Map<String,Object> params, String charset) {
		if (StringUtils.isEmpty(url)) {
			return null;
		}
		try {
			List<NameValuePair> pairs = null;
			if (params != null && !params.isEmpty()) {
				pairs = new ArrayList<NameValuePair>(params.size());
				for (Map.Entry<String, Object> entry : params.entrySet()) {
					String value = entry.getValue().toString();
					if (value != null) {
						pairs.add(new BasicNameValuePair(entry.getKey(), value));
					}
				}
			}
			HttpPost httpPost = new HttpPost(url);
			HttpHost httpHost = new HttpHost(url);
			httpPost.addHeader("Content-Type", APPLICATION_FORM);
			if (pairs != null && pairs.size() > 0) {
				httpPost.setEntity(new UrlEncodedFormEntity(pairs, CHARSET));
			}
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != 200) {
					httpPost.abort();
					logger.error("HTTP连接异常    HttpClient,error status code :" + statusCode);
				}
				HttpEntity entity = response.getEntity();
				String result = null;
				if (entity != null) {
					result = EntityUtils.toString(entity, "utf-8");
				}
				EntityUtils.consume(entity);
				return result;
			} finally {
				response.close();
			}
		} catch (Exception e) {
			logger.error("http do post error");
			e.printStackTrace();
			return null;
		}
	}

}
