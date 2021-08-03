package com.yuanzhy.tools.commons.httpclient;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultRedirectStrategy;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.HttpVersion;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.util.Timeout;

public class V5 {

    public void t5() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://test.com/");
        httpGet.setHeader("Content-Type", "application/json");
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(2))
                .setConnectionRequestTimeout(Timeout.ofSeconds(2))
                .setResponseTimeout(Timeout.ofSeconds(2))
                .build();
        httpGet.setConfig(requestConfig);
        httpGet.setVersion(HttpVersion.HTTP_2); // 支持http2
        try (CloseableHttpResponse res = httpClient.execute(httpGet)) {
            if (res.getCode() == HttpStatus.SC_OK) {
                HttpEntity entity = res.getEntity();
                System.out.println(EntityUtils.toString(entity));
            } else {
                System.err.println("请求失败，状态码：" + res.getCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpGet.reset();
        }
    }
    // 一种用于确定是否应将 HTTP 请求重定向到新位置以响应从目标服务器收到的 HTTP 响应的策略。
    // 该接口的实现必须是线程安全的。对共享数据的访问必须同步，因为该接口的方法可以从多个线程执行。
    public void redirect() {
        CloseableHttpClient httpClient = HttpClients.custom()
                .disableAutomaticRetries() //关闭自动重试
                .setRedirectStrategy(DefaultRedirectStrategy.INSTANCE)
                .build();
        // ... ...
    }

    public void async() {
        CloseableHttpAsyncClient httpClient = HttpAsyncClients.createHttp2Default();
        // TODO
    }
}
