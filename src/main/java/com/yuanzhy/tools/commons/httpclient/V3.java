package com.yuanzhy.tools.commons.httpclient;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;

// 发布于2007年， 已不在维护
public class V3 {
    public void t() {
        HttpClient httpClient = new HttpClient();
        // 设置连接超时 和 socket超时
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(2000);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(5000); // 响应超时
        HttpMethod getM = new GetMethod("http://test.com/");
        // 设置请求头
        getM.setRequestHeader("Content-Type", "application/json");
        NameValuePair p1 = new NameValuePair("name", "zs");
        NameValuePair p2 = new NameValuePair("age", "11");
        // 设置查询参数，相当于 ?name=zs&age=11
        getM.setQueryString(new NameValuePair[]{p1, p2});
        try {
            int code = httpClient.executeMethod(getM);
            if (code == HttpStatus.SC_OK) {
                // 获取结果字符串
                String res = getM.getResponseBodyAsString();
//            InputStream res = getM.getResponseBodyAsStream(); // 也可以转换为流
                System.out.println(res);
            } else {
                System.err.println("请求失败，状态码：" + code);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            getM.releaseConnection();
        }
    }
}
