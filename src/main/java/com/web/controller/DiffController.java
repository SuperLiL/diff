package com.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import com.web.interceptor.Diffintercept;
import com.web.pojo.DiffPostParams;
import com.web.response.ResultGenerator;
import com.web.service.DiffService;
import com.web.util.DiffTools;
import com.web.util.ResponseApi;
import com.web.response.Result;
import com.web.util.SSRFChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/diff")
public class DiffController {
    ResponseApi responseApi = new ResponseApi();
    SSRFChecker ssrfChecker = new SSRFChecker();
    @Autowired
    private DiffService diffService;
    @Autowired
    private DiffTools diffTools;


    //  页面请求跳转
    @RequestMapping("/diff")
    public String requestForward() {
        return "test";
    }

    @RequestMapping("/diffbody")
    public String requestForwardbody() {
        return "test1";
    }

    /**
     * Get请求- 调用service服务对比结果，将结果标准化输出
     *
     * @param requestUrl
     * @return
     * @RequestParam暂时使用两个，分别表示两个已经拼接好的URL请求,若有参数拼接还可以进行分析
     */
    @Diffintercept()
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    @ResponseBody
    public Result byJsonMethod(
            @RequestParam("requestUrl") String requestUrl,
            @RequestParam("requestUrl2") String requestUrl2,
            @RequestParam(required = false, defaultValue = "") String params1,
            @RequestParam(required = false, defaultValue = "") String params2,
            @RequestParam(required = false, defaultValue = "") String headers1,
            @RequestParam(required = false, defaultValue = "") String headers2,
            @RequestParam(required = false, defaultValue = "") String userCookies1,
            @RequestParam(required = false, defaultValue = "") String userCookies2,
            HttpServletRequest request) {
        /*int error = (int)request.getAttribute("error");
        if(error==1){
            return ResultGenerator.genFailedResult("疑似请求非法数据，请联系管理员核实");
        }*/
        Map<String, String> param1;
        Map<String, String> param2;
        Map<String, String> header1;
        Map<String, String> header2;
        Map<String, String> userCookie1;
        Map<String, String> userCookie2;
        try {
            param1 = parameterNormalization(params1);
            param2 = parameterNormalization(params2);
            header1 = parameterNormalization(headers1);
            header2 = parameterNormalization(headers2);
            userCookie1 = parameterNormalization(userCookies1);
            userCookie2 = parameterNormalization(userCookies2);
        } catch (Exception e) {
            System.out.println(e);
            return ResultGenerator.genFailedResult("请求参数数据不合法,请检查后输入");
        }
        //将前端的复合参数转为map集合封装到对应的request请求。
        try {
            requestUrl = URLDecoder.decode(requestUrl, "UTF-8");
            requestUrl2 = URLDecoder.decode(requestUrl2, "UTF-8");
            /*boolean b1 = ssrfChecker.checkURL(requestUrl);
            boolean b2 = ssrfChecker.checkURL(requestUrl2);
            if(!b1||!b2) {
                return ResultGenerator.genFailedResult("Bad IP~ Error");
            }*/
            String response_body1 = responseApi.Get(requestUrl, param1, header1, userCookie1);
            String response_body2 = responseApi.Get(requestUrl2, param2, header2, userCookie2);
            return resultByString(response_body1, response_body2);
        } catch (Exception e) {
            return ResultGenerator.genFailedResult("对比过程中出现错误，请重试");
        }

    }
    public Result illegalDataRequest(){
        return ResultGenerator.genFailedResult("疑似请求非法数据，请联系管理员核实");
    }

    /**
     * 接收前端传递的初始数据处理，将处理结果map集合返回。
     *
     * @param param
     * @return
     */

    public Map<String, String> parameterNormalization(String param) {
        Map<String, String> paramMap = new HashMap<>();
        //若为空，初始化为null
        if (param.isEmpty()) {
            paramMap = null;
        } else if (param.charAt(0) == '{') {  //在这里判断参数是如何传递到后端的--1、json-{"":"","":""}类型；2、字符串（页面展示key=value；这种类型）
            paramMap = JSONObject.parseObject(param, Map.class);
        } else {
            String[] keyValues = param.split(";");
            for (int i = 0; i < keyValues.length; i++) {
                String[] key_value = keyValues[i].split("=");
                String key = key_value[0];
                String value = key_value[1];
                paramMap.put(key, value);

            }
        }

        return paramMap;
    }

    /**
     * 传入string的转为JsonObject
     *
     * @param response_body1
     * @param response_body2
     * @return
     */
    public Result resultByString(String response_body1, String response_body2) {
        try {
            int status1 = diffTools.verificationBykey(response_body1);
            int status2 = diffTools.verificationBykey(response_body2);
            if (status1 != 1 && status1 != 2 || status1 != status2 || status2 != 1 && status2 != 2) {
                return ResultGenerator.genFailedResult("请求数据中含有不合法数据,请检查后输入");
            }
            if (status1 == 2) {
                /*JSONObject.parseObject(response_body1, SerializerFeature.WRITE_MAP_NULL_FEATURES);
                net.sf.json.JSONObject jsonObjectss = net.sf.json.JSONObject.fromObject(response_body2);*/
                JSONObject jsonObject1 = JSONObject.parseObject(response_body1);
                JSONObject jsonObject2 = JSONObject.parseObject(response_body2);
                //2019.8.12
                JSONObject resultByJson = diffService.getResultByJson(jsonObject1, jsonObject2);

                if (resultByJson.getJSONObject("diff").size() >= 1) {

                    return ResultGenerator.genOkResult(resultByJson, "两个接口返回值不一致");
                } else {
                    return ResultGenerator.genOkResult(resultByJson);
                }
            } else {
                JSONArray jsonArray1 = JSONArray.parseArray(response_body1);
                JSONArray jsonArray2 = JSONArray.parseArray(response_body2);
                JSONObject resultByJson = diffService.getResultByJson(jsonArray1, jsonArray2);
                if (resultByJson.getJSONObject("diff").size() >= 1) {

                    return ResultGenerator.genOkResult(resultByJson, "两个接口返回值不一致");
                } else {
                    return ResultGenerator.genOkResult(resultByJson);
                }

            }

        } catch (Exception e) {
            return ResultGenerator.genFailedResult("请求数据不合法,请检查后输入");
        }

    }

    /**
     * Post请求-调用service服务对比结果，将结果标准化输出
     *
     * @param requestUrl
     * @return
     * @RequestParam
     */
    @Diffintercept()
    @RequestMapping(value = "/check", method = RequestMethod.POST)
    @ResponseBody
    public Result byJsonMethodPost(
            @RequestParam("requestUrl") String requestUrl,
            @RequestParam("requestUrl2") String requestUrl2,
            @RequestParam(required = false, defaultValue = "") String params1,
            @RequestParam(required = false, defaultValue = "") String params2,
            @RequestParam(required = false, defaultValue = "") String headers1,
            @RequestParam(required = false, defaultValue = "") String headers2,
            @RequestParam(required = false, defaultValue = "") String userCookies1,
            @RequestParam(required = false, defaultValue = "") String userCookies2,
            HttpServletRequest request) {
        int error = (int)request.getAttribute("error");
        if(error==1){
            return ResultGenerator.genFailedResult("疑似请求非法数据，请联系管理员核实");
        }
        Map<String, String> param1;
        Map<String, String> param2;
        Map<String, String> header1;
        Map<String, String> header2;
        Map<String, String> userCookie1;
        Map<String, String> userCookie2;
        try {
            param1 = parameterNormalization(params1);
            param2 = parameterNormalization(params2);
            header1 = parameterNormalization(headers1);
            header2 = parameterNormalization(headers2);
            userCookie1 = parameterNormalization(userCookies1);
            userCookie2 = parameterNormalization(userCookies2);
        } catch (Exception e) {
            return ResultGenerator.genFailedResult("请求参数数据不合法,请检查后输入");
        }
        //将前端的复合参数转为map集合封装到对应的request请求。
        try {
            requestUrl = URLDecoder.decode(requestUrl, "UTF-8");
            requestUrl2 = URLDecoder.decode(requestUrl2, "UTF-8");
           /* boolean b1 = ssrfChecker.checkURL(requestUrl);
            boolean b2 = ssrfChecker.checkURL(requestUrl2);
            if(!b1||!b2) {
                return ResultGenerator.genFailedResult("Bad IP~ Error");
            }*/
            String response_body1 = responseApi.Post(requestUrl, param1, header1, userCookie1);
            String response_body2 = responseApi.Post(requestUrl2, param2, header2, userCookie2);
            return resultByString(response_body1, response_body2);
        } catch (Exception e) {
            return ResultGenerator.genFailedResult("请求参数数据不合法,请检查后输入");
        }
    }

    /**
     * 直接传入String的情况
     *  1、普通字符串""情况
     *  2、JsonObject转为String的情况 {"":""}
     *  3、JsonArrary转为String情况[ , , ,]。
     *
     * @param requestBody1
     * @return
     * @RequestParam
     */
    @Diffintercept()
    @RequestMapping(value = "/checkByString",method = RequestMethod.GET)
    @ResponseBody
    public Result byJsonMethodString(
            @RequestParam("requestBody1") String requestBody1,
            @RequestParam("requestBody2") String requestBody2) {
        try {
            return resultByString(requestBody1, requestBody2);
        } catch (Exception e) {
            return ResultGenerator.genFailedResult("请求参数数据不合法,请检查后输入");
        }

    }
    /**
     * 直接传入jsonObject的情况。
     *
     * @param
     * @return
     *
     */
    @RequestMapping(value = "/checkByStringTest",method = RequestMethod.POST)
    @ResponseBody
    public Result byJsonMethodString(
            @RequestBody DiffPostParams diffPostParams) {
        try {
            JSONObject resultByJson = diffService.getResultByJson(diffPostParams.getRequestBody1(), diffPostParams.getRequestBody2());
            if (resultByJson.getJSONObject("diff").size() >= 1) {

                return ResultGenerator.genOkResult(resultByJson, "两个接口返回值不一致");
            } else {
                return ResultGenerator.genOkResult(resultByJson);
            }
            //return resultByString(diffPostParams.getRequestBody1().toString(), diffPostParams.getRequestBody2().toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genFailedResult("请求参数数据不合法,请检查后输入");
        }

    }
    /**
     * 直接传入jsonObject的情况。
     * 在这里并没有过多的考虑传入是否为JsonObject、JsonArray、普通字符串，主要是考虑到diff主要目的。
     * @param
     * @return
     *
     */
    @RequestMapping(value = "/checkByStringPost",method = RequestMethod.POST)
    @ResponseBody
    public Result byJsonMethodStringss(
            @RequestBody DiffPostParams diffPostParams) {
        try{
           /* JSONObject requestBody2 = diffPostParams.getRequestBody2();
            String sq = requestBody2.toString(SerializerFeature.WriteMapNullValue);
            String s2 = diffPostParams.getRequestBody2().toString();
            System.out.println("s2"+s2+"---"+sq);
            System.out.println(diffPostParams.getRequestBody2());*/
            //2019.8.12解决了在Json转为String出现的忽视null字段的问题。SerializerFeature.WriteMapNullValue
            return resultByString(diffPostParams.getRequestBody1().toString(), diffPostParams.getRequestBody2().toString());
        } catch (Exception e) {
            return ResultGenerator.genFailedResult("请求参数数据不合法,请检查后输入");
        }
    }
    @RequestMapping(value = "/checkByStringPosttest",method = RequestMethod.POST)
    @ResponseBody
    public Result byJsonMethodStringss2(
            @RequestBody String requestBody1,
            @RequestBody String requestBody2) {
        try{
           /* JSONObject requestBody2 = diffPostParams.getRequestBody2();
            String sq = requestBody2.toString(SerializerFeature.WriteMapNullValue);
            String s2 = diffPostParams.getRequestBody2().toString();
            System.out.println("s2"+s2+"---"+sq);
            System.out.println(diffPostParams.getRequestBody2());*/
            //2019.8.12解决了在Json转为String出现的忽视null字段的问题。
            return resultByString(requestBody1,requestBody2);
        } catch (Exception e) {
            return ResultGenerator.genFailedResult("请求参数数据不合法,请检查后输入");
        }
    }

}

