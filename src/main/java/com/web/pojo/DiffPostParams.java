package com.web.pojo;

import com.alibaba.fastjson.JSONObject;

public class DiffPostParams {
    private JSONObject requestBody1;
    private JSONObject requestBody2;

    public JSONObject getRequestBody1() {
        return requestBody1;
    }

    public void setRequestBody1(JSONObject requestBody1) {
        this.requestBody1 = requestBody1;
    }

    public JSONObject getRequestBody2() {
        return requestBody2;
    }

    public void setRequestBody2(JSONObject requestBody2) {
        this.requestBody2 = requestBody2;
    }
}
