package com.web.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public interface DiffService {
    /**
     * 通用方法抽取-将url请求的结果变为String字符串返回
     * @param Url
     * @return
     */
    public  String getJson_String(String Url);


    public Map<String,String> getResultByJson(String json1,String json2);
    public JSONObject getResultByJson(JSONObject json1,JSONObject json2);

    /**
     * 迭代遍历判断
     * @param o1
     * @param o2
     * @param i
     * @param resultMap
     * @return
     */
    public Map<String,String> generalMethod(Object o1,Object o2,int i,Map resultMap);

    public JSONObject generalMethod(Object o1, Object o2, int i, int j,JSONObject resultJson, String  original_key);
}
