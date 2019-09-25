package com.web.pojo;

/**
 * 响应实体类
 */
public class ResponseBean {

    /**
     * 状态码 0为成功
     */
    private Integer errno;

    /**
     * 返回值
     */
    private Object data;

    /**
     *
     */
    private String trace;

    /**
     * 如果服务端返回错误，将附带此消息
     */
    private String errmsg;

    public Integer getErrno() {
        return errno;
    }

    public void setErrno(Integer errno) {
        this.errno = errno;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getTrace() {
        return trace;
    }

    public void setTrace(String trace) {
        this.trace = trace;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    @Override
    public String toString() {
        return "ResponseBean{" +
                "errno=" + errno +
                ", data=" + data +
                ", trace='" + trace + '\'' +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }
}
