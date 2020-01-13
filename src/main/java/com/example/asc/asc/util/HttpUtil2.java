package com.example.asc.asc.util;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtil2 {

    public static final String CHARSET = "UTF-8";
    private static final CloseableHttpClient httpClient;
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

    public static String doPost(String geoconvUrl, Map<String, Object> params) {
        return doPost(geoconvUrl, params, CHARSET);
    }

    public static String doPut(String geoconvUrl, Map<String, String> params) {
        return doPut(geoconvUrl, params, CHARSET);
    }

    public static String doGet(String geoconvUrl, Map<String,Object> params) {
        return doGetObj(geoconvUrl, params, CHARSET);
    }

    public static String doGetCommon(String geoconvUrl, Map<String, String> params) {
        return doGetObjCommon(geoconvUrl, params, CHARSET);
    }

    /**
     * HTTP Get 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @param charset 编码格式
     * @return 页面内容
     */
    public static String doGetForIp(String url, Map<String, String> params, String charset, String ip) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        try {
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), value));
                    }
                }
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
            }
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("x-forwarded-for", ip);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    httpGet.abort();
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
            logger.error("http do doGetForIp error");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * HTTP Get 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @param charset 编码格式
     * @return 页面内容
     */
    public static String doGet(String url, Map<String, String> params, String charset) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        try {
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String value = entry.getValue().toString();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), value));
                    }
                }
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
            }
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    httpGet.abort();
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
            logger.error("http do doGet error");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * HTTP Delete 删除内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @param charset 编码格式
     * @return 页面内容
     */
    public static String doDelete(String url, Map<String, String> params, String charset) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        try {
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), value));
                    }
                }
                url += "&" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
            }
            HttpDelete httpDelete = new HttpDelete(url);
            CloseableHttpResponse response = httpClient.execute(httpDelete);
            try {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    httpDelete.abort();
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
            logger.error("http do doDelete error");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * HTTP Post 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param josnStr 请求的参数
     * @return 页面内容
     */
    public static String doPostJSON(String url, String josnStr) {
        if (StringUtils.isEmpty(url) || StringUtils.isEmpty(josnStr)) {
            return null;
        }
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
            StringEntity se = new StringEntity(josnStr, "utf-8");
            se.setContentType(CONTENT_TYPE_JSON);
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
            httpPost.setEntity(se);
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
            logger.error("http do doPostJSON error");
            e.printStackTrace();
            return null;
        }
    }

//	/***
//	 * POST提交文件
//	 *
//	 * @param url   请求的url地址 ?之前的地址
//	 * @param params
//	 * @param fileName
//	 * @return
//	 * @throws IOException
//	 * @throws ClientProtocolException
//	 */
//	public static String postFile(String url, Map<String, Object> params, String fileName) {
//		HttpPost post = new HttpPost(url);
//		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//		Set<String> pNames = params.keySet();
//		// 构建参数信息
//		for (String key : pNames) {
//			Object obj = params.get(key);
//			if (obj instanceof String) {// 普通文本
//				builder.addTextBody(key, obj.toString(), ContentType.TEXT_PLAIN);
//			}
//			if (obj instanceof Integer) {// 普通文本
//				builder.addTextBody(key, obj.toString(), ContentType.TEXT_PLAIN);
//			}
//			if (obj instanceof Long) {// 普通文本
//				builder.addTextBody(key, obj.toString(), ContentType.TEXT_PLAIN);
//			}
//			if (obj instanceof byte[]) {// byte数组的处理
//				builder.addBinaryBody(key, (byte[]) obj, ContentType.DEFAULT_BINARY, fileName);
//			}
//		}
//		HttpEntity entity = builder.build();
//		post.setEntity(entity);
//		try {
//			CloseableHttpResponse response = httpClient.execute(post);
//			try {
//				int statusCode = response.getStatusLine().getStatusCode();
//				if (statusCode != 200) {
//					post.abort();
//					logger.error("HTTP连接异常    HttpClient,error status code :" + statusCode);
//				}
//				HttpEntity entity2 = response.getEntity();
//				String result = null;
//				if (entity2 != null) {
//					result = EntityUtils.toString(entity2, "utf-8");
//				}
//				EntityUtils.consume(entity2);
//				return result;
//			} finally {
//				response.close();
//			}
//		} catch (Exception e) {
//			logger.error("http do postFile error");
//			e.printStackTrace();
//			return null;
//		}
//	}

//	/***
//	 * POST提交文件
//	 *
//	 * @param url
//	 * @param params
//	 * @param imgName
//	 * @param bytes
//	 * @param charset
//	 * @param ip
//	 * @return
//	 * @throws IOException
//	 * @throws ClientProtocolException
//	 */
//	public static String doPostFile(String url, byte[] content) {
//		HttpPost post = new HttpPost(url);
//		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//		builder.addBinaryBody("file", content, ContentType.create("image/png"), "1.png");
//		HttpEntity entity = builder.build();
//		post.setEntity(entity);
//		try {
//			CloseableHttpResponse response = httpClient.execute(post);
//			try {
//				int statusCode = response.getStatusLine().getStatusCode();
//				if (statusCode != 200) {
//					post.abort();
//					logger.error("HTTP连接异常    HttpClient,error status code :" + statusCode);
//				}
//				HttpEntity entity2 = response.getEntity();
//				String result = null;
//				if (entity2 != null) {
//					result = EntityUtils.toString(entity2, "utf-8");
//				}
//				EntityUtils.consume(entity2);
//				return result;
//			} finally {
//				response.close();
//			}
//
//		} catch (ClientProtocolException e) {
//			logger.error("上传文件异常: ",e);
//		} catch (IOException e) {
//			logger.error("上传文件异常: ",e);
//		}
//		return null;
//	}

    /**
     * HTTP Get 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @param charset 编码格式
     * @return 页面内容
     */
    public static String doGetObj(String url, Map<String, Object> params, String charset) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        try {

            if (params != null && !params.isEmpty()) {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    Object value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), String.valueOf(value)));
                    }
                }
                url += "&" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
            }
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    httpGet.abort();
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
            logger.error("http do doGetObj error");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * HTTP Post 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @param charset 编码格式
     * @return 页面内容
     */
    public static String doPostObj(String url, Map<String, Object> params, String charset) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        try {
            List<NameValuePair> pairs = null;
            if (params != null && !params.isEmpty()) {
                pairs = new ArrayList<NameValuePair>(params.size());
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    Object value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), String.valueOf(value)));
                    }
                }
            }
            HttpPost httpPost = new HttpPost(url);
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
            logger.error("http do doPostObj error");
            e.printStackTrace();
            return null;
        }
    }

    public static String doPut(String url, Map<String, String> params, String charset) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        try {
            List<NameValuePair> pairs = null;
            if (params != null && !params.isEmpty()) {
                pairs = new ArrayList<NameValuePair>(params.size());
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), value));
                    }
                }
            }
            HttpPut httpPut = new HttpPut(url);
            httpPut.addHeader("Content-Type", APPLICATION_FORM);
            if (pairs != null && pairs.size() > 0) {
                httpPut.setEntity(new UrlEncodedFormEntity(pairs, CHARSET));
            }
            CloseableHttpResponse response = httpClient.execute(httpPut);
            try {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    httpPut.abort();
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
            logger.error("http do doPut error");
            e.printStackTrace();
            return null;
        }
    }


    public static String doGetObjCommon(String url, Map<String, String> params, String charset) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        try {
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), value));
                    }
                }
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
            }
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    httpGet.abort();
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
            logger.error("http do doGetObjCommon error");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据url下载图片
     *
     * @param url
     * @return
     */
    public static byte[] httpGetRes(String url) {
        logger.info("根据url下载文件：url={}", url);
        HttpGet get = new HttpGet(url);
        byte[] response = null;
        CloseableHttpResponse res = null;
        try {
            get.setHeader("Accept", "application/octet-stream");
            res = httpClient.execute(get);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                response = EntityUtils.toByteArray(res.getEntity());
            }
            logger.info("根据url下载文件成功：url={}", url);
        } catch (Exception e) {
            //抛出连接异常
            logger.error("http do httpGetRes error");
            e.printStackTrace();
            return null;
        } finally {
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                }
            }
        }
        return response;
    }


    /**
     * HTTP Get 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @param charset 编码格式
     * @return 页面内容
     */
    public static String getAuthorization(String url, String authorization, Map<String, String> params, String charset) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        try {
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    Object value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), String.valueOf(value)));
                    }
                }
                url += "&" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
            }
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Content-Type", APPLICATION_FORM);
            httpGet.addHeader("Authorization", "Bearer " + authorization);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    httpGet.abort();
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
            logger.error("http do getAuthorization error");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * HTTP Post 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @param charset 编码格式
     * @return 页面内容
     */
    public static String doPost(String url, Map<String, Object> params, String charset) {
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
