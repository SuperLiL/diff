package com.web.serviceimp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.web.service.DiffService;
import com.web.utils.DiffTools;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("diffService")
public class DiffServiceImp implements DiffService {
    @Autowired
    private DiffTools diffTools;
    
    /**
     * 将请求的URL变成Json格式传入，输出<错误1：具体位置>这种map集合的格式
     * @param json
     * @return map
     */
    @Override
    public Map<String, String> getResultByJson(String json,String json2) {
//        0.方法返回结果
        Map<String, String> resultMap = new HashMap<String, String>();
//        1、分解第一层
        int i = 1;
        Map<String, Object> map1 = JSONObject.parseObject(json, Map.class);
        Object ss = JSONObject.parseObject(json);
        Map<String, Object> map2 = JSONObject.parseObject(json2, Map.class);
        for (String key : map1.keySet()) {
            // 根据每次得到的map1集合中的key，map2.contains（）方法判断是否含有key，
            if (map2.keySet().contains(key)) {
//                如果含有则利用equals方法判断value的值是否相等
//              如果有相同的key，判断是否有嵌套情况或者再次复杂的情况
//                (1)key对应的为数组，数组中嵌套json。
//                (2)key对应的为json。
//                (3)key对应的为json，json再次嵌套数组-json
                resultMap = generalMethod(map1.get(key), map2.get(key), i,resultMap);
                /*
                int status = diffTools.verificationBykey(map1.get(key));

                int status2 = diffTools.verificationBykey(map2.get(key));
//              两种URL虽然key相等，但是对应的value类型不想等，例如一个是数组，另一个是json
                if(status!= status2){
                    resultMap.put("第"+i+"处error","key对应的value类型不一致");
                    continue;
                }
//                两种URL类型相等，都是嵌套了一个数组，需要继续进行解析
                if (status==1){
//                    JSONArray jsonArray1 = JSONArray.parseArray(map1.get(key).toString());
//                    JSONArray jsonArray2 = JSONArray.parseArray(map1.get(key).toString());
//                    将数组结果封装到list<String>集合中
                    List<Object> listString1 = JSONArray.parseArray(map1.get(key).toString(), Object.class);
                    List<Object> listString2 = JSONArray.parseArray(map2.get(key).toString(), Object.class);

                    for (Object arrString1:listString1) {


                    }



                }
//                两种URL类型相等，都是嵌套了一个json，需要继续进行解析
                if(status==2){
                    Map<String, Object> map12 = JSONObject.parseObject(map1.get(key).toString(), Map.class);
                    Map<String, Object> map13 = JSONObject.parseObject(map2.get(key).toString(), Map.class);
                }
//                两种URL类型相等，都是普通的键值对，可以直接判断
                if (status==3){
                    boolean result_value = map1.get(key).equals(map2.get(key));
                    if(result_value==false){
                        resultMap.put("第"+i+"错误","key:"+key+"对应的value不同");
                    }

                }
/*
//                try {
//                    map1.get(key).toString();
//                } catch (Exception e) {
//
//                }
//                if (map1.get(key).equals(map2.get(key))) {
//                    两个json对应的key-value都相等
//                    System.out.println("");
//
//                } else {//添加错误的返回值
//
//                    String s1 = "url1中的key：" + key + "对应的值：" + map1.get(key) + "和在url2不相等！";
//
//                    System.out.println("不相同的地方为值不想等：url1中的key：" + key + "对应的值：" + map1.get(key) + "和在url2返回结果不想等！");
//                }*/
            } else {
                String s2 = "url1中的key：" + key + "在url2不存在";
                resultMap.put(key+"出现错误", s2);
            }
        }
        return resultMap;
    }

    /**
     * 迭代判断公用方法
     * @param o1
     * @param o2
     * @param i
     * @param resultMap
     * @return
     */
    public Map<String,String> generalMethod(Object o1,Object o2,int i,Map resultMap) {
        int status = diffTools.verificationBykey(o1);
        int status2 = diffTools.verificationBykey(o2);
//              两种URL虽然key相等，但是对应的value类型不想等，例如一个是数组，另一个是json
        if (status != status2) {
            resultMap.put("第" + i + "层error", "key对应的value类型不一致");
        }
//                两种URL类型相等，都是嵌套了一个数组，需要继续进行解析
        if (status == 1) {
//      JSONArray jsonArray1 = JSONArray.parseArray(map1.get(key).toString());
//      JSONArray jsonArray2 = JSONArray.parseArray(map1.get(key).toString());
//      将数组结果封装到list<String>集合中
            List<Object> listString1 = JSONArray.parseArray(o1.toString(), Object.class);
            List<Object> listString2 = JSONArray.parseArray(o2.toString(), Object.class);
            if (listString1.size() != listString2.size()) {
                resultMap.put("第" + i + "层error", "长度不一致");
                return resultMap;
            }
            for (int j = 0; j < listString1.size(); j++) {
                resultMap=generalMethod(listString1.get(j), listString2.get(j), i+1,resultMap);
            }
        }
//                两种URL类型相等，都是嵌套了一个json，需要继续进行解析
        if (status == 2) {
            Map<String, Object> map12 = JSONObject.parseObject(o1.toString(), Map.class);
            Map<String, Object> map13 = JSONObject.parseObject(o2.toString(), Map.class);
            if (map12.size() != map13.size()) {
                resultMap.put("第" + i + "层error", "长度不一致");
                return resultMap;
            }
            for (String key : map12.keySet()) {
                // 根据每次得到的map1集合中的key，map2.contains（）方法判断是否含有key，
                i++;
                if (map13.keySet().contains(key)) {
                    resultMap = generalMethod(map12.get(key), map13.get(key), i+1,resultMap);
                } else {
                    resultMap.put("", "key不包含");
                }
            }
        }
//      两种URL类型相等，都是普通的键值对，可以直接判断
        if (status == 3) {
            boolean result_value = o1.equals(o2);
            if (result_value == false) {
                resultMap.put("第" + i + "错误", "key对应的value不同");

            }

        }
        return resultMap;
    }



    /**
     * 使用json方法，不使用map--通用属性的抽取
     * @param o1
     * @param o2
     * @param i
     * @param resultJson
     * @return
     */
    @Override
    public JSONObject generalMethod(Object o1, Object o2, int i, int j,JSONObject resultJson,String original_key) {
        int status  = diffTools.verificationBykey(o1);
        int status2 = diffTools.verificationBykey(o2);
//              两种URL虽然key相等，但是对应的value类型不想等，例如一个是数组，另一个是json
        if (status != status2) {
            resultJson.put(++j+"", "在" + i + "层错误，"+original_key+"对应的值类型不一致。value1："+o1+";value2:"+o2);
        }
//                两种URL类型相等，都是嵌套了一个数组，需要继续进行解析
        if (status == 1) {
            JSONArray jsonArray1 = JSONArray.parseArray(o1.toString());
            JSONArray jsonArray2 = JSONArray.parseArray(o2.toString());
            if (jsonArray1.size() != jsonArray2.size()) {
                resultJson.put(++j+"", "在"+i+"层错误，结构不一致");
                return resultJson;
            }
            for (int k = 0; k < jsonArray1.size(); k++) {
                //数组不当做嵌套关系，平级关系，所以这里i不在+1。所以key也不需要传入复合状态。
                resultJson=generalMethod(jsonArray1.get(k), jsonArray2.get(k), i,j,resultJson,original_key);
            }
        }
//                两种URL类型相等，都是嵌套了一个json，需要继续进行解析
        if (status == 2) {
            JSONObject jsonObject1 = JSONObject.parseObject(o1.toString());
            JSONObject jsonObject2 = JSONObject.parseObject(o2.toString());

            if (jsonObject1.size() != jsonObject2.size()) {
                resultJson.put(++j+"", "在" + i + "层错误，结构不一致");
                return resultJson;
            }
            for (String key : jsonObject1.keySet()) {
                // 根据每次得到的map1集合中的key，map2.contains（）方法判断是否含有key，
                if (jsonObject2.keySet().contains(key)) {
                    resultJson = generalMethod(jsonObject1.get(key), jsonObject2.get(key), i+1,j,resultJson,original_key+":"+key);
                } else {
                    resultJson.put(++j+"", "第" + i + "层错误，接口1中的key：" + key + "  在接口2结果中不存在");
                }
            }
        }
//      两种URL类型相等，都是普通的键值对，可以直接判断
        if (status == 3) {
            boolean result_value = o1.equals(o2);
            if (result_value == false) {
                //考虑如何更友好的显示到前端
                resultJson.put(++j+"-"+original_key,"在" + i + "层错误,"+original_key+"对应的value不同。value1："+o1+"。value2:"+o2);

                //resultJson.put(original_key,"对应的值不一致");
            }
        }
        return resultJson;
    }

    /**
     *  Json方式-----遍历第一层
     * @param json1
     * @param json2
     * @return
     */
    @Override
    public JSONObject getResultByJson(JSONObject json1, JSONObject json2) {
        //        0.方法返回结果
        JSONObject resultJson = new JSONObject();
        JSONObject resultJson_final = new JSONObject();

        resultJson_final.put("data_api_1",json1);
        resultJson_final.put("data_api_2",json2);
//        1、分解第一层
        int i = 0;
        int j = 1;
        if(json1.size()!=json2.size()){
            resultJson.put(j+"","在"+i+"层错误,二者体系结构不同");
            resultJson_final.put("diff",resultJson);
            return resultJson_final;
        }
        for (String key : json1.keySet()) {
            // 根据每次得到的key，map2.contains（）方法判断是否含有key，
            if (json2.keySet().contains(key)) {
                resultJson = generalMethod(json1.get(key), json2.get(key), i+1,j,resultJson,key);
            } else {
                resultJson.put(j+"", "在"+i+"层错误，接口1中的key：" + key + "在接口2不存在");
            }
        }
         resultJson_final.put("diff",resultJson);
        return resultJson_final;
    }


    /**
     * 通用方法抽取-将url请求的结果变为String字符串返回
     * @param Url
     * @return
     */
    @Override
    public String getJson_String(String Url) {
        //返回结果
        String res="";
        StringBuffer buffer = new StringBuffer();
        try{
            //创建url，此处可以动态扩展，拼接
            URL url = new URL(Url);
            HttpURLConnection urlCon= (HttpURLConnection)url.openConnection();
            if(200==urlCon.getResponseCode()){
                InputStream is = urlCon.getInputStream();
                InputStreamReader isr = new InputStreamReader(is,"utf-8");
                BufferedReader br = new BufferedReader(isr);
                String str = null;
                while((str = br.readLine())!=null){
                    buffer.append(str);
                }
                br.close();
                isr.close();
                is.close();
                res = buffer.toString();
            }
        }catch(IOException e){
            System.out.println("访问不成功");
        }
        return res;
    }


}
