package com.yuanzhy.tools.commons.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class V4 {

    public void t() {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("http://test.com/?name=zs&age=11");
        httpGet.setHeader("Content-Type", "application/json");
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(2000) // 连接超时
                .setConnectionRequestTimeout(2000) // 请求超时
                .setSocketTimeout(2000) // 响应超时
                .build();
        httpGet.setConfig(requestConfig);
        try (CloseableHttpResponse res = httpClient.execute(httpGet)) {
            int code = res.getStatusLine().getStatusCode();
            if (code == HttpStatus.SC_OK) {
                HttpEntity entity = res.getEntity();
                System.out.println(EntityUtils.toString(entity));
            } else {
                System.err.println("请求失败，状态码：" + code);
            }
        } catch (IOException e) {
            System.err.println("请求异常");
        } finally {
            httpGet.reset();
//            httpGet.releaseConnection(); // 等价于reset()
        }
    }
}
