package com.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.web.pojo.User;
import com.web.service.DiffService;
import com.web.service.UserService;
import com.web.util.ResponseApi;
import com.web.utils.DiffTools;
import io.restassured.response.Response;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;



import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class UserController {
    ResponseApi responseApi= new ResponseApi();
    @Autowired
    private UserService userService;
    @Autowired
    private DiffService diffService;


    @RequestMapping("/user")
    @ResponseBody
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @RequestMapping("/check1")
    public String result() {
        //Response resp = ResponseApi.Get("localhost:8333/user", null, null, null);
       // resp.body().prettyPrint();
        return "msg";
    }


    /**
     * 根据页面提交的两条url请求，对比响应结果是否相同。diff
     * 还差的工作：前端制作一个表格，提交两个URL，后段将返回结果封装到Map中，通过map比较是否相同。
     *
     * @param requestUrl
     * @return
     * @RequestParam暂时使用两个，分别表示两个已经拼接好的URL请求,若有参数拼接还需要进行分析
     */
    @RequestMapping("/check11")
    @ResponseBody
    public JSONObject getXpath(

            @RequestParam("requestUrl") String requestUrl,
            @RequestParam("requestUrl2") String requestUrl2,
            Model model) {
        Date date = new Date();
        // System.out.println(requestUrl);
        //调用业务层分析，返回响应的结果
       // String test1 = "{\"errno\":0,\"errmsg\":null,\"data\":{\"0\":[{\"name\":\"初始状态\",\"id\":0},{\"name\":\"待预约\",\"id\":10},{\"name\":\"待看车\",\"id\":20},{\"name\":\"待确认资金\",\"id\":35},{\"name\":\"待交车\",\"id\":40},{\"name\":\"车辆已绑定\",\"id\":50},{\"name\":\"待保单审核\",\"id\":55},{\"name\":\"租赁中\",\"id\":60},{\"name\":\"还款中\",\"id\":70},{\"name\":\"已完成\",\"id\":100},{\"name\":\"已取消\",\"id\":120},{\"name\":\"下单失败\",\"id\":130}],\"1\":[{\"name\":\"经租（快车）\",\"id\":1},{\"name\":\"融租（快车）\",\"id\":2},{\"name\":\"买车（快车）\",\"id\":3},{\"name\":\"先租后买（快车）\",\"id\":4},{\"name\":\"经租（专车）\",\"id\":33},{\"name\":\"融租（专车）\",\"id\":34},{\"name\":\"租售（专车）\",\"id\":38},{\"name\":\"经租（乘用车）\",\"id\":65},{\"name\":\"融租（乘用车）\",\"id\":66},{\"name\":\"购车（乘用车）\",\"id\":67},{\"name\":\"先租后买（乘用车）\",\"id\":68}],\"2\":[{\"name\":\"无法联系到司机\",\"id\":1},{\"name\":\"司机到店后放弃\",\"id\":2},{\"name\":\"司机希望更换商品\",\"id\":3},{\"name\":\"副班司机重复下单\",\"id\":4},{\"name\":\"当前商品无法提供\",\"id\":5},{\"name\":\"其他\",\"id\":6}],\"3\":[{\"name\":\"全款\",\"id\":1},{\"name\":\"分期\",\"id\":2}],\"4\":[{\"name\":\"正常结束\",\"id\":0},{\"name\":\"提前全款购车\",\"id\":1},{\"name\":\"退车解约\",\"id\":2},{\"name\":\"转为普通租赁\",\"id\":3}],\"5\":[{\"name\":\"NOT_CONTACT\",\"id\":1},{\"name\":\"NOT_ARRIVAL\",\"id\":2}],\"6\":[{\"name\":\"待交车\",\"id\":3},{\"name\":\"履约中\",\"id\":4},{\"name\":\"已结束\",\"id\":5},{\"name\":\"即将过期\",\"id\":6}],\"7\":[{\"name\":\"审核通过\",\"id\":1},{\"name\":\"审核驳回\",\"id\":2},{\"name\":\"不可用\",\"id\":3}],\"8\":[{\"name\":\"系统\",\"id\":0},{\"name\":\"租赁公司\",\"id\":1},{\"name\":\"司机\",\"id\":2}],\"9\":[{\"name\":\"已上传\",\"id\":0},{\"name\":\"审核中\",\"id\":1},{\"name\":\"审核失败\",\"id\":2},{\"name\":\"审核通过\",\"id\":3},{\"name\":\"待CP签约\",\"id\":10},{\"name\":\"待第三方签约\",\"id\":15},{\"name\":\"待司机签约\",\"id\":20},{\"name\":\"已签约\",\"id\":30},{\"name\":\"已撤销\",\"id\":40},{\"name\":\"已拒签\",\"id\":41},{\"name\":\"已失效\",\"id\":50},{\"name\":\"已归档\",\"id\":60}],\"10\":[{\"name\":\"主班\",\"id\":1},{\"name\":\"副班\",\"id\":2}],\"11\":[{\"name\":\"租赁公司\",\"id\":1},{\"name\":\"司机\",\"id\":2}],\"13\":[{\"name\":\"经租（快车）\",\"id\":1},{\"name\":\"融租（快车）\",\"id\":2},{\"name\":\"先租后买（快车）\",\"id\":3},{\"name\":\"全款购车（快车）\",\"id\":4},{\"name\":\"分期购车（快车）\",\"id\":5},{\"name\":\"经租（专车）\",\"id\":33},{\"name\":\"融租（专车）\",\"id\":34}],\"14\":[{\"name\":\"待预约\",\"id\":1},{\"name\":\"待看车\",\"id\":2},{\"name\":\"待交车\",\"id\":3},{\"name\":\"租赁中\",\"id\":4},{\"name\":\"还款中\",\"id\":5},{\"name\":\"已完成\",\"id\":6},{\"name\":\"已取消\",\"id\":7}],\"15\":[{\"name\":\"无效\",\"id\":0},{\"name\":\"有效\",\"id\":1}],\"16\":[{\"name\":\"租金返现\",\"id\":1},{\"name\":\"司机担保收入\",\"id\":2}],\"17\":[{\"name\":\"已上传\",\"id\":0},{\"name\":\"审核中\",\"id\":1},{\"name\":\"审核失败\",\"id\":2},{\"name\":\"审核通过\",\"id\":3},{\"name\":\"待CP签约\",\"id\":10},{\"name\":\"待第三方签约\",\"id\":15},{\"name\":\"待司机签约\",\"id\":20},{\"name\":\"已签约\",\"id\":30},{\"name\":\"已撤销\",\"id\":40},{\"name\":\"已拒签\",\"id\":41},{\"name\":\"已失效\",\"id\":50},{\"name\":\"已归档\",\"id\":60}],\"18\":[{\"name\":\"快车\",\"id\":1},{\"name\":\"专车\",\"id\":2},{\"name\":\"乘用车\",\"id\":3},{\"name\":\"优享\",\"id\":4}],\"19\":[{\"name\":\"未上传\",\"id\":0},{\"name\":\"待审核\",\"id\":1},{\"name\":\"审核通过\",\"id\":2},{\"name\":\"审核失败\",\"id\":3}],\"20\":[{\"name\":\"已上传\",\"id\":0},{\"name\":\"审核中\",\"id\":1},{\"name\":\"审核成功\",\"id\":2},{\"name\":\"审核失败\",\"id\":3}],\"21\":[{\"name\":\"司机照片\",\"id\":1},{\"name\":\"副班司机照片\",\"id\":2},{\"name\":\"车辆照片\",\"id\":3}],\"22\":[{\"name\":\"用户身份证正面\",\"id\":1010},{\"name\":\"用户身份证反面\",\"id\":1011},{\"name\":\"用户驾驶证正面\",\"id\":1020},{\"name\":\"用户驾驶证反面\",\"id\":1021},{\"name\":\"人车合影\",\"id\":1030},{\"name\":\"网约车驾驶员证\",\"id\":1040},{\"name\":\"行驶证正本正面\",\"id\":2010},{\"name\":\"行驶证正本反面\",\"id\":2011},{\"name\":\"行驶证副本\",\"id\":2012},{\"name\":\"网约车运输资格证\",\"id\":2020}],\"23\":[{\"name\":\"待交车\",\"id\":3},{\"name\":\"履约中\",\"id\":4},{\"name\":\"已结束\",\"id\":5}],\"24\":[{\"name\":\"未审核\",\"id\":0},{\"name\":\"待审核\",\"id\":1},{\"name\":\"审核通过\",\"id\":2},{\"name\":\"审核失败\",\"id\":3}],\"25\":[{\"name\":\"已确认，生成新的续租订单\",\"id\":1},{\"name\":\"已与司机沟通，不再续租\",\"id\":2},{\"name\":\"不确定，暂不处理\",\"id\":0}],\"26\":[{\"name\":\"经租\",\"id\":1},{\"name\":\"融租\",\"id\":2},{\"name\":\"先租后买\",\"id\":4},{\"name\":\"经租（专车）\",\"id\":33},{\"name\":\"融租（专车）\",\"id\":34},{\"name\":\"租售（专车）\",\"id\":38},{\"name\":\"经租（乘用车）\",\"id\":65},{\"name\":\"融租（乘用车）\",\"id\":66},{\"name\":\"购车（乘用车）\",\"id\":67},{\"name\":\"先租后买（乘用车）\",\"id\":68}],\"27\":[{\"name\":\"无返利\",\"id\":1},{\"name\":\"车主介绍人线索返利\",\"id\":10},{\"name\":\"车主介绍人销售返利\",\"id\":11},{\"name\":\"小桔经纪人线索返利\",\"id\":20},{\"name\":\"小桔经纪人销售返利\",\"id\":21}],\"28\":[{\"name\":\"平台\",\"id\":0},{\"name\":\"车胜\",\"id\":1},{\"name\":\"AMC\",\"id\":2}],\"29\":[{\"name\":\"天府银行\",\"id\":1},{\"name\":\"微众银行\",\"id\":2},{\"name\":\"浙商银行\",\"id\":3},{\"name\":\"自有资金\",\"id\":4}]}}\n";
        //String test2 = "{\"errno\":0,\"errmsg\":null,\"data\":{\"0\":[{\"nam\":\"初始状态\",\"id\":0},{\"name\":\"待约\",\"id\":10},{\"name\":\"待看车\",\"id\":90},{\"name\":\"待确认资金\",\"id\":35},{\"name\":\"待交车\",\"id\":40},{\"name\":\"车辆已绑定\",\"id\":50},{\"name\":\"待保单审核\",\"id\":55},{\"name\":\"租赁中\",\"id\":60},{\"name\":\"还款中\",\"id\":70},{\"name\":\"已完成\",\"id\":100},{\"name\":\"已取消\",\"id\":120},{\"name\":\"下单失败\",\"id\":130}],\"1\":[{\"name\":\"经租（快车）\",\"id\":1},{\"name\":\"融租（快车）\",\"id\":2},{\"name\":\"买车（快车）\",\"id\":3},{\"name\":\"先租后买（快车）\",\"id\":4},{\"name\":\"经租（专车）\",\"id\":33},{\"name\":\"融租（专车）\",\"id\":34},{\"name\":\"租售（专车）\",\"id\":38},{\"name\":\"经租（乘用车）\",\"id\":65},{\"name\":\"融租（乘用车）\",\"id\":66},{\"name\":\"购车（乘用车）\",\"id\":67},{\"name\":\"先租后买（乘用车）\",\"id\":68}],\"2\":[{\"name\":\"无法联系到司机\",\"id\":1},{\"name\":\"司机到店后放弃\",\"id\":2},{\"name\":\"司机希望更换商品\",\"id\":3},{\"name\":\"副班司机重复下单\",\"id\":4},{\"name\":\"当前商品无法提供\",\"id\":5},{\"name\":\"其他\",\"id\":6}],\"3\":[{\"name\":\"全款\",\"id\":1},{\"name\":\"分期\",\"id\":2}],\"4\":[{\"name\":\"正常结束\",\"id\":0},{\"name\":\"提前全款购车\",\"id\":1},{\"name\":\"退车解约\",\"id\":2},{\"name\":\"转为普通租赁\",\"id\":3}],\"5\":[{\"name\":\"NOT_CONTACT\",\"id\":1},{\"name\":\"NOT_ARRIVAL\",\"id\":2}],\"6\":[{\"name\":\"待交车\",\"id\":3},{\"name\":\"履约中\",\"id\":4},{\"name\":\"已结束\",\"id\":5},{\"name\":\"即将过期\",\"id\":6}],\"7\":[{\"name\":\"审核通过\",\"id\":1},{\"name\":\"审核驳回\",\"id\":2},{\"name\":\"不可用\",\"id\":3}],\"8\":[{\"name\":\"系统\",\"id\":0},{\"name\":\"租赁公司\",\"id\":1},{\"name\":\"司机\",\"id\":2}],\"9\":[{\"name\":\"已上传\",\"id\":0},{\"name\":\"审核中\",\"id\":1},{\"name\":\"审核失败\",\"id\":2},{\"name\":\"审核通过\",\"id\":3},{\"name\":\"待CP签约\",\"id\":10},{\"name\":\"待第三方签约\",\"id\":15},{\"name\":\"待司机签约\",\"id\":20},{\"name\":\"已签约\",\"id\":30},{\"name\":\"已撤销\",\"id\":40},{\"name\":\"已拒签\",\"id\":41},{\"name\":\"已失效\",\"id\":50},{\"name\":\"已归档\",\"id\":60}],\"10\":[{\"name\":\"主班\",\"id\":1},{\"name\":\"副班\",\"id\":2}],\"11\":[{\"name\":\"租赁公司\",\"id\":1},{\"name\":\"司机\",\"id\":2}],\"13\":[{\"name\":\"经租（快车）\",\"id\":1},{\"name\":\"融租（快车）\",\"id\":2},{\"name\":\"先租后买（快车）\",\"id\":3},{\"name\":\"全款购车（快车）\",\"id\":4},{\"name\":\"分期购车（快车）\",\"id\":5},{\"name\":\"经租（专车）\",\"id\":33},{\"name\":\"融租（专车）\",\"id\":34}],\"14\":[{\"name\":\"待预约\",\"id\":1},{\"name\":\"待看车\",\"id\":2},{\"name\":\"待交车\",\"id\":3},{\"name\":\"租赁中\",\"id\":4},{\"name\":\"还款中\",\"id\":5},{\"name\":\"已完成\",\"id\":6},{\"name\":\"已取消\",\"id\":7}],\"15\":[{\"name\":\"无效\",\"id\":0},{\"name\":\"有效\",\"id\":1}],\"16\":[{\"name\":\"租金返现\",\"id\":1},{\"name\":\"司机担保收入\",\"id\":2}],\"17\":[{\"name\":\"已上传\",\"id\":0},{\"name\":\"审核中\",\"id\":1},{\"name\":\"审核失败\",\"id\":2},{\"name\":\"审核通过\",\"id\":3},{\"name\":\"待CP签约\",\"id\":10},{\"name\":\"待第三方签约\",\"id\":15},{\"name\":\"待司机签约\",\"id\":20},{\"name\":\"已签约\",\"id\":30},{\"name\":\"已撤销\",\"id\":40},{\"name\":\"已拒签\",\"id\":41},{\"name\":\"已失效\",\"id\":50},{\"name\":\"已归档\",\"id\":60}],\"18\":[{\"name\":\"快车\",\"id\":1},{\"name\":\"专车\",\"id\":2},{\"name\":\"乘用车\",\"id\":3},{\"name\":\"优享\",\"id\":4}],\"19\":[{\"name\":\"未上传\",\"id\":0},{\"name\":\"待审核\",\"id\":1},{\"name\":\"审核通过\",\"id\":2},{\"name\":\"审核失败\",\"id\":3}],\"20\":[{\"name\":\"已上传\",\"id\":0},{\"name\":\"审核中\",\"id\":1},{\"name\":\"审核成功\",\"id\":2},{\"name\":\"审核失败\",\"id\":3}],\"21\":[{\"name\":\"司机照片\",\"id\":1},{\"name\":\"副班司机照片\",\"id\":2},{\"name\":\"车辆照片\",\"id\":3}],\"22\":[{\"name\":\"用户身份证正面\",\"id\":1010},{\"name\":\"用户身份证反面\",\"id\":1011},{\"name\":\"用户驾驶证正面\",\"id\":1020},{\"name\":\"用户驾驶证反面\",\"id\":1021},{\"name\":\"人车合影\",\"id\":1030},{\"name\":\"网约车驾驶员证\",\"id\":1040},{\"name\":\"行驶证正本正面\",\"id\":2010},{\"name\":\"行驶证正本反面\",\"id\":2011},{\"name\":\"行驶证副本\",\"id\":2012},{\"name\":\"网约车运输资格证\",\"id\":2020}],\"23\":[{\"name\":\"待交车\",\"id\":3},{\"name\":\"履约中\",\"id\":4},{\"name\":\"已结束\",\"id\":5}],\"24\":[{\"name\":\"未审核\",\"id\":0},{\"name\":\"待审核\",\"id\":1},{\"name\":\"审核通过\",\"id\":2},{\"name\":\"审核失败\",\"id\":3}],\"25\":[{\"name\":\"已确认，生成新的续租订单\",\"id\":1},{\"name\":\"已与司机沟通，不再续租\",\"id\":2},{\"name\":\"不确定，暂不处理\",\"id\":0}],\"26\":[{\"name\":\"经租\",\"id\":1},{\"name\":\"融租\",\"id\":2},{\"name\":\"先租后买\",\"id\":4},{\"name\":\"经租（专车）\",\"id\":33},{\"name\":\"融租（专车）\",\"id\":34},{\"name\":\"租售（专车）\",\"id\":38},{\"name\":\"经租（乘用车）\",\"id\":65},{\"name\":\"融租（乘用车）\",\"id\":66},{\"name\":\"购车（乘用车）\",\"id\":67},{\"name\":\"先租后买（乘用车）\",\"id\":68}],\"27\":[{\"name\":\"无返利\",\"id\":1},{\"name\":\"车主介绍人线索返利\",\"id\":10},{\"name\":\"车主介绍人销售返利\",\"id\":11},{\"name\":\"小桔经纪人线索返利\",\"id\":20},{\"name\":\"小桔经纪人销售返利\",\"id\":21}],\"28\":[{\"name\":\"平台\",\"id\":0},{\"name\":\"车胜\",\"id\":1},{\"name\":\"AMC\",\"id\":2}],\"29\":[{\"name\":\"天府银行\",\"id\":1},{\"name\":\"微众银行\",\"id\":2},{\"name\":\"浙商银行\",\"id\":3},{\"name\":\"自有资金\",\"id\":4}]}}\n";

        String json_string1 = diffService.getJson_String(requestUrl);
        JSONObject object1 = JSONObject.parseObject(json_string1);
        String json_string2 = diffService.getJson_String(requestUrl2);
        // JSONObject jsonObject = new JSONObject();
        //将检查结果封装到map集合中
        for(int i=0;i<1000;i++){
            Map<String, String> resultByJson = diffService.getResultByJson(json_string1, json_string2);
            resultByJson.put("1","");
        }
        Map<String, String> resultByJson = diffService.getResultByJson(json_string1, json_string2);
       /* Map<String, Object> map1 = JSONObject.parseObject(json_string1, Map.class);
        System.out.println(json_string1);
        Map<String, Object> map2 = JSONObject.parseObject(json_string2, Map.class);
        //遍历其中的元素，进行判断。
        for (String key : map1.keySet()) {
            // 根据每次得到的map1集合中的key，map2.contains（）方法判断是否含有key，
            if (map2.keySet().contains(key)) {
//                如果含有key，调用工具判断key对应类型
//                int i = diffTools.verificationBykey(key);
                Object o = map1.get(key);
                System.out.println("完整结果" + o);
                String ac = o == null ? "null" : o.toString();
                char a = ac.charAt(0);
                System.out.println("首字母为：--" + a);
                // System.out.println(o);
                boolean b = o instanceof String;
                System.out.println("reault:" + b);
                if (o == null) {
                    System.out.println("jieguoweikong,bucanwyuyanzheng");
                }
                System.out.println(map1.get(key));
//                如果含有则利用equals方法判断value的值是否相等
                /*if(map1.get(key).equals(map2.get(key))){
//                    两个json对应的key-value都相等
                    System.out.println("");

                }else{//添加错误的返回值
                    //model.addAttribute("msg","error");
                    String s1 = "url1中的key："+key+"对应的值："+map1.get(key)+"和在url2不相等！";
                    model.addAttribute("msg",s1);
                    System.out.println("不相同的地方为值不想等：url1中的key："+key+"对应的值："+map1.get(key)+"和在url2返回结果不想等！");
                }*/
           /* } else {

                String s2 = "url1中的key：" + map1.get(key) + "在url2不存在";
                model.addAttribute("msg1", s2);
                System.out.println("不相同的地方为：key不想等。url1中的key：" + map1.get(key) + "在url2不存在");
            }
//            System.out.println("json的key为："+key+"对应的value为："+map1.get(key));

        }
        //String json1 = "{'id':1,'name':'JAVAEE-1703','stus':[{'id':101,'name':'刘铭','age':16}]}";
        /*try{
            URL url = new URL(requestUrl);
            HttpURLConnection urlCon= (HttpURLConnection)url.openConnection();
            if(200==urlCon.getResponseCode()){
                InputStream is = urlCon.getInputStream();
                InputStreamReader isr = new InputStreamReader(is,"utf-8");
                BufferedReader br = new BufferedReader(isr);

                String str = null;
                while((str = br.readLine())!=null){
                    buffer.append(str);

                    System.out.println("--------");

                }
                br.close();
                isr.close();
                is.close();
                res = buffer.toString();
                JSONObject jsonObj = new JSONObject();
                //将String数据转换为JsonObject类型
                object = jsonObj.getJSONObject(res);
                //将数据封装到Map集合中 适合key_value这种情况
                Map<String,Object> map = JSON.parseObject(json1, Map.class);
                for (String a:map.keySet()) {
                    Object o = map.get(a);
                    System.out.println("key的值为："+a);
                    System.out.println("key对应的value值为："+o.toString());
                }
                //返回JsonObject
                // object = jsonObj.getJSONObject(res);
                //将String转换成数组类型比如[{},{},{}]类型
               // List<String> list= JSON.parseArray(res, String.class);
               // System.out.println("------list---");
                //for (String a:list) {
                  //  System.out.println(a);
                //}

            }
        }catch(IOException e){
            e.printStackTrace();
        }*/
//      将两个url结果返回到前端
        model.addAttribute("json1", json_string1);
        model.addAttribute("json2", json_string2);
        model.addAttribute("result",resultByJson);
        JSONObject result = new JSONObject();
        result.put("msg", "ok");
        result.put("method", "@ResponseBody");
        result.put("data", resultByJson);
        result.put("data1",object1);
        Date date1 = new Date();
        long  l = date1.getTime() - date.getTime();
        System.out.println("时间为"+l);
        return result;



    }

    //  页面请求跳转
    @RequestMapping("/diffc")
    public String requestForward() {

        return "test";
    }



}
