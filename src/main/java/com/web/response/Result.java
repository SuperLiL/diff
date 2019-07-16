package com.web.response;

import com.alibaba.fastjson.JSON;

public class Result {
    /**
     * 状态码
     */
    private final Integer code;
    /**
     * 消息
     */
    private final String msg;
    /**
     * 数据内容，比如列表，实体
     */
    private final Object data;
   // private final Object data_api;
    //private final Object data_api2;

    private Result(final Builder builder) {
        this.code = builder.code;
        this.msg = builder.msg;
        this.data = builder.data;
       // this.data_api = builder.data_api;
        //this.data_api2 = builder.data_api2;
    }

    public static class Builder {
        private final Integer code;
        private String msg;
        private Object data;
        private Object data_api;
        private Object data_api2;

        public Builder(final Integer code) {
            this.code = code;
        }

        public Builder msg(final String msg) {
            this.msg = msg;
            return this;
        }
        public Builder data(final Object data) {
            this.data = data;
            return this;
        }
        public Builder data_api(final Object data_api) {
            this.data_api = data_api;
            return this;
        }
        public Builder data_api2(final Object data_api2) {
            this.data_api2 = data_api2;
            return this;
        }

        public Result build() {
            return new Result(this);
        }
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public Object getData() {
        return this.data;
    }



    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
