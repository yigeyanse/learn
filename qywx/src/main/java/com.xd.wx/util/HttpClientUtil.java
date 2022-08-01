package com.xd.wx.util;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {

    public static String doPost(String url, String body) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = null;
        String result = null;
        try {
            httpPost = new HttpPost(url);
            //设置参数
            StringEntity entity = new StringEntity(body, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            //HttpResponse response = httpClient.execute(httpPost);
            ClassicHttpResponse response = (ClassicHttpResponse) httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static String doPost(String url, File file) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = null;
        String result = null;
        try {
            httpPost = new HttpPost(url);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addPart("file", new FileBody(file, ContentType.DEFAULT_BINARY));
            httpPost.setEntity(builder.build());
            //HttpResponse response = httpClient.execute(httpPost);
            ClassicHttpResponse response = (ClassicHttpResponse) httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static String doPostFile(String url, File file) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = null;
        String result = null;
        try {
            httpPost = new HttpPost(url);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addPart("file", new FileBody(file, ContentType.MULTIPART_FORM_DATA));
            httpPost.setEntity(builder.build());
            //HttpResponse response = httpClient.execute(httpPost);
            ClassicHttpResponse response = (ClassicHttpResponse) httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static String doPost(String url, Map<String, String> map, String charset) {
        //HttpClient httpClient = null;
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = null;
        String result = null;
        try {
            //httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            //设置参数
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
            }
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, Charset.forName(charset));
                httpPost.setEntity(entity);
            }
            //HttpResponse response = httpClient.execute(httpPost);
            ClassicHttpResponse response = (ClassicHttpResponse) httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static String doGet(String url) {
        return doGet(url, null);
    }

    public static String doGet(String url, String charset) {
        if (null == charset) {
            charset = "utf-8";
        }
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = null;
        String result = null;

        try {
            //httpClient = new SSLClient();
            httpGet = new HttpGet(url);
            ClassicHttpResponse response = (ClassicHttpResponse) httpClient.execute(httpGet);
            //HttpResponse response = httpClient.execute(httpGet);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 根据 url 下载文件
     *
     * @param url 目标文件 url
     * @param file 要保存入的文件
     */
    public static boolean doGetDownload(String url, File file) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet;
        try {
            httpGet = new HttpGet(url);
            //HttpResponse response = httpClient.execute(httpGet);
            ClassicHttpResponse response = (ClassicHttpResponse) httpClient.execute(httpGet);
            if (response != null) {
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();
                return FileUtils.saveFileByInput(file, is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
