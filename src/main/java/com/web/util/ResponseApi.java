package com.web.util;

import com.alibaba.fastjson.JSON;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.Map;

public class ResponseApi {
    //打印URL以及参数
    private static void LogOut(String url, String params, Map<String,String> headers, Map<String,String> cookies) {
        System.out.println("current url is: " + url);
        System.out.println("current param is: " + params);
        System.out.println("current headers is: " + headers);
        System.out.println("current cookies is: " + cookies);

    }

    //传参时json的post请求
    public static String Post(String url, String params, Map<String,String> headers,Map<String,String> cookies){
        if(null == cookies){
            String userCookie = "{}";
            cookies = (Map) JSON.parse(userCookie);
        }
        if(null == headers){
            String headerStr = "{}";
            headers = (Map) JSON.parse(headerStr);
        }
        headers.put("Content-Type","application/json;charset=UTF-8");
        LogOut(url, params, headers, cookies);
        Response Response = RestAssured
                .given()
                .cookies(cookies)
                .headers(headers)
                .body(params)
                .post(url);
        System.out.println("current response is:");
        Response.body().print();
        return Response.body().prettyPrint();
    }

    //传参是form的post请求
    public static String Post(String url,Map<String,String> params, Map<String,String> headers,Map<String,String> cookies){
        if(null == cookies){
            String userCookie = "{}";
            cookies = (Map) JSON.parse(userCookie);

        }
        LogOut(url, params.toString(), headers, cookies);
        Response Response = RestAssured
                .given()
                .cookies(cookies)
                .contentType("application/x-www-form-urlencoded;charset=utf-8")
                .headers(headers)
                .formParams(params)
                .post(url);
        System.out.println("current response is:");
        Response.body().print();
        return Response.body().prettyPrint();
    }
    //封装接口需要的参数
    public static String Get(String url, Map<String,String> params, Map<String,String> headers, Map<String,String> userCookies ){
        if(null == headers){
            String header ="{}";
            headers = (Map) JSON.parse(header);
        }
        if(null == params){
            String param ="{}";
            params = (Map) JSON.parse(param);
        }
        if(null == userCookies){
            String userCookie = "{}";
            userCookies = (Map) JSON.parse(userCookie);

        }
       // LogOut(url, params.toString(), headers, userCookies);
        Response Response = RestAssured
                .given()
                .cookies(userCookies)
                .headers(headers)
                .params(params)
                .get(url);
//        System.out.println("current response is:");
        return Response.body().prettyPrint();
    }
}
