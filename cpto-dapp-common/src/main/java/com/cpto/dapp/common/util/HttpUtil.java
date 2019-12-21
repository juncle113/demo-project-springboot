package com.cpto.dapp.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * httputil
 *
 * @author lvchaohua
 */
@Slf4j
public class HttpUtil {

    private static final String CHARSET = "UTF-8";
    private static CloseableHttpClient httpClient;

    /**
     * http get请求
     *
     * @param url 请求地址
     * @param
     * @return
     */
    public static String get(String url) {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String str = EntityUtils.toString(entity, CHARSET);
                    return str;
                }
            } finally {
                response.close();
                httpClient.close();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    /**
     * http get请求
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return
     */
    public static String get(String url, Map<String, Object> params) {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            url = url + "?";
            for (Iterator<String> iterator = params.keySet().iterator(); iterator.hasNext(); ) {
                String key = iterator.next();
                String temp = key + "=" + params.get(key) + "&";
                url = url + temp;
            }
            url = url.substring(0, url.length() - 1);
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String str = EntityUtils.toString(entity, CHARSET);
                    return str;
                }
            } finally {
                response.close();
                httpClient.close();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    /**
     * http post请求
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return
     */
    public static String post(String url, Map<String, Object> params) {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            for (Iterator<String> iterator = params.keySet().iterator(); iterator.hasNext(); ) {
                String key = iterator.next();
                parameters.add(new BasicNameValuePair(key, params.get(key).toString()));
            }
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(parameters, CHARSET);
            httpPost.setEntity(uefEntity);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String str = EntityUtils.toString(entity, CHARSET);
                    return str;
                }
            } finally {
                response.close();
                httpClient.close();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    /**
     * http post请求
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return
     */
    public static String post(String url, String params) {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            StringEntity sEntity = new StringEntity(params, CHARSET);
            httpPost.setEntity(sEntity);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity, CHARSET);
                }
            } finally {
                response.close();
                httpClient.close();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    public static String post(String url, String jsonString, String authorization) {
        CloseableHttpResponse response = null;
        BufferedReader in = null;
        String result = "";
        try {
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).setConnectionRequestTimeout(30000).setSocketTimeout(30000).build();
            httpPost.setConfig(requestConfig);
            httpPost.setConfig(requestConfig);
            httpPost.addHeader("Content-type", "application/json; charset=utf-8");
            httpPost.addHeader("Authorization", authorization);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setEntity(new StringEntity(jsonString, Charset.forName("UTF-8")));
            response = httpClient.execute(httpPost);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String nl = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + nl);
            }
            in.close();
            result = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != response) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}