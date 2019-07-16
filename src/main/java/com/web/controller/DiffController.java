package com.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.web.response.Result;
import com.web.response.ResultGenerator;
import com.web.service.DiffService;
import com.web.util.ResponseApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Map;


@Controller
@RequestMapping("/")
public class DiffController {
    ResponseApi responseApi= new ResponseApi();
    @Autowired
    private DiffService diffService;
    //  页面请求跳转
    @RequestMapping("/diff")
    public String requestForward() {

        return "test";
    }

    /**
     * 调用service服务对比结果，将结果标准化输出
     *
     * @param requestUrl
     * @return
     * @RequestParam暂时使用两个，分别表示两个已经拼接好的URL请求,若有参数拼接还需要进行分析

    @RequestMapping(value = "/check2",method = RequestMethod.GET)
    @ResponseBody
    public Result byJsonMethod(
            @RequestParam("requestUrl") String requestUrl,
            @RequestParam("requestUrl2") String requestUrl2) {
        String response_body1 = responseApi.Get(requestUrl, null, null, null);
        String response_body2 = responseApi.Get(requestUrl2, null, null, null);

        return resultByString(response_body1,response_body2);
    }*/
    /**
     * 调用service服务对比结果，将结果标准化输出
     *
     * @param requestUrl
     * @return
     * @RequestParam暂时使用两个，分别表示两个已经拼接好的URL请求,若有参数拼接还需要进行分析
     */
    @RequestMapping(value = "/check2",method = RequestMethod.GET)
    @ResponseBody
    public Result byJsonMethod(
            @RequestParam("requestUrl") String requestUrl,
            @RequestParam("requestUrl2") String requestUrl2,
            @RequestParam(value = "params1",defaultValue="null") Map params1,
            @RequestParam(value = "params2",defaultValue="null") Map params2,
            @RequestParam(value = "headers1",defaultValue="null") Map headers1,
            @RequestParam(value = "headers2",defaultValue="null") Map headers2,
            @RequestParam(value = "userCookies1",defaultValue="null") Map userCookies1,
            @RequestParam(value = "userCookies2",defaultValue="null") Map userCookies2) {
        String response_body1 = responseApi.Get(requestUrl, params1, headers1, userCookies1);
        String response_body2 = responseApi.Get(requestUrl2, params2, headers2, userCookies2);
        return resultByString(response_body1,response_body2);
    }

    public Result resultByString(String response_body1,String response_body2){
       // String json_string1 = diffService.getJson_String(requestUrl);
        JSONObject jsonObject1 = JSONObject.parseObject(response_body1);
       // String json_string2 = diffService.getJson_String(requestUrl2);
        JSONObject jsonObject2 = JSONObject.parseObject(response_body2);
        // JSONObject jsonObject = new JSONObject();
        //将检查结果封装到map集合中
        JSONObject resultByJson = diffService.getResultByJson(jsonObject1, jsonObject2);
//      将两个url结果返回到前端
        if(resultByJson.getJSONObject("diff").size()>=1){

            return ResultGenerator.genOkResult(resultByJson,"两个接口返回值不一致");
        }else {
            return ResultGenerator.genOkResult(resultByJson);
        }
    }
    /**
     * 调用service服务对比结果，将结果标准化输出
     *
     * @param requestUrl
     * @return
     * @RequestParam暂时使用两个，分别表示两个已经拼接好的URL请求,若有参数拼接还需要进行分析
     */
    @RequestMapping(value = "/check2",method =RequestMethod.POST)
    @ResponseBody
    public Result byJsonMethodPost(
            @RequestParam("requestUrl") String requestUrl,
            @RequestParam("requestUrl2") String requestUrl2) {
        String response_body1 = responseApi.Post(requestUrl,  "",null,null);
        String response_body2 = responseApi.Post(requestUrl2,"",null,null);
        return resultByString(response_body1,response_body2);
    }
}
//  String test1="{\"errno\":0,\"errmsg\":null,\"data\":{\"0\":[{\"name\":\"初始状态\",\"id\":0},{\"name\":\"待预约\",\"id\":10},{\"name\":\"待看车\",\"id\":20},{\"name\":\"待确认资金\",\"id\":35},{\"name\":\"待交车\",\"id\":40},{\"name\":\"车辆已绑定\",\"id\":50},{\"name\":\"待保审核\",\"id\":55},{\"name\":\"租赁中\",\"id\":60},{\"name\":\"还款中\",\"id\":70},{\"name\":\"已完成\",\"id\":100},{\"name\":\"已取消\",\"id\":120},{\"name\":\"下单失败\",\"id\":130}],\"1\":[{\"name\":\"经租（快车）\",\"id\":1},{\"name\":\"融租（快车）\",\"id\":2},{\"name\":\"买车（车）\",\"id\":3},{\"name\":\"先租后买（快车）\",\"id\":4},{\"name\":\"经租（专车）\",\"id\":33},{\"name\":\"融租（专车）\",\"id\":34},{\"name\":\"租售（专车）\",\"id\":38},{\"name\":\"经租（乘用车）\",\"id\":65},{\"name\":\"融租（乘用车）\",\"id\":66},{\"name\":\"购车（乘用车）\",\"id\":67},{\"name\":\"先租后买（乘用车）\",\"id\":68}],\"2\":[{\"name\":\"无法联系到司机\",\"id\":1},{\"name\":\"司机到店后放弃\",\"id\":2},{\"name\":\"司机希望更换商品\",\"id\":3},{\"name\":\"副班司机重复下单\",\"id\":4},{\"name\":\"当前商品无法提供\",\"id\":5},{\"name\":\"其他\",\"id\":6}],\"3\":[{\"name\":\"全款\",\"id\":1},{\"name\":\"分期\",\"id\":2}],\"4\":[{\"name\":\"正常结束\",\"id\":0},{\"name\":\"提前全款购车\",\"id\":1},{\"name\":\"退车解约\",\"id\":2},{\"name\":\"转为普通租赁\",\"id\":3}],\"5\":[{\"name\":\"NOT_CONTACT\",\"id\":1},{\"name\":\"NOT_ARRIVAL\",\"id\":2}],\"6\":[{\"name\":\"待交车\",\"id\":3},{\"name\":\"履约中\",\"id\":4},{\"name\":\"已结束\",\"id\":5},{\"name\":\"即将过期\",\"id\":6}],\"7\":[{\"name\":\"审核通过\",\"id\":1},{\"name\":\"审核驳回\",\"id\":2},{\"name\":\"不可用\",\"id\":3}],\"8\":[{\"name\":\"系统\",\"id\":0},{\"name\":\"租赁公司\",\"id\":1},{\"name\":\"司机\",\"id\":2}],\"9\":[{\"name\":\"已上传\",\"id\":0},{\"name\":\"审核中\",\"id\":1},{\"name\":\"审核失败\",\"id\":2},{\"name\":\"审核通过\",\"id\":3},{\"name\":\"待CP签约\",\"id\":10},{\"name\":\"待第三方签约\",\"id\":15},{\"name\":\"待司机签约\",\"id\":20},{\"name\":\"已签约\",\"id\":30},{\"name\":\"已撤销\",\"id\":40},{\"name\":\"已拒签\",\"id\":41},{\"name\":\"已失效\",\"id\":50},{\"name\":\"已归档\",\"id\":60}],\"10\":[{\"name\":\"主班\",\"id\":1},{\"name\":\"副班\",\"id\":2}],\"11\":[{\"name\":\"租赁公司\",\"id\":1},{\"name\":\"司机\",\"id\":2}],\"13\":[{\"name\":\"经租（快车）\",\"id\":1},{\"name\":\"融租（快车）\",\"id\":2},{\"name\":\"先租后买（快车）\",\"id\":3},{\"name\":\"全款购车（快车）\",\"id\":4},{\"name\":\"分期购车（快车）\",\"id\":5},{\"name\":\"经租（专车）\",\"id\":33},{\"name\":\"融租（专车）\",\"id\":34}],\"14\":[{\"name\":\"待预约\",\"id\":1},{\"name\":\"待看车\",\"id\":2},{\"name\":\"待交车\",\"id\":3},{\"name\":\"租赁中\",\"id\":4},{\"name\":\"还款中\",\"id\":5},{\"name\":\"已完成\",\"id\":6},{\"name\":\"已取消\",\"id\":7}],\"15\":[{\"name\":\"无效\",\"id\":0},{\"name\":\"有效\",\"id\":1}],\"16\":[{\"name\":\"租金返现\",\"id\":1},{\"name\":\"司机担保收入\",\"id\":2}],\"17\":[{\"name\":\"已上传\",\"id\":0},{\"name\":\"审核中\",\"id\":1},{\"name\":\"审核失败\",\"id\":2},{\"name\":\"审核通过\",\"id\":3},{\"name\":\"待CP签约\",\"id\":10},{\"name\":\"待第三方签约\",\"id\":15},{\"name\":\"待司机签约\",\"id\":20},{\"name\":\"已签约\",\"id\":30},{\"name\":\"已撤销\",\"id\":40},{\"name\":\"已拒签\",\"id\":41},{\"name\":\"已失效\",\"id\":50},{\"name\":\"已归档\",\"id\":60}],\"18\":[{\"name\":\"快车\",\"id\":1},{\"name\":\"专车\",\"id\":2},{\"name\":\"乘用车\",\"id\":3},{\"name\":\"优享\",\"id\":4}],\"19\":[{\"name\":\"未上传\",\"id\":0},{\"name\":\"待审核\",\"id\":1},{\"name\":\"审核通过\",\"id\":2},{\"name\":\"审核失败\",\"id\":3}],\"20\":[{\"name\":\"已上传\",\"id\":0},{\"name\":\"审核中\",\"id\":1},{\"name\":\"审核成功\",\"id\":2},{\"name\":\"审核失败\",\"id\":3}],\"21\":[{\"name\":\"司机照片\",\"id\":1},{\"name\":\"副班司机照片\",\"id\":2},{\"name\":\"车辆照片\",\"id\":3}],\"22\":[{\"name\":\"用户身份证正面\",\"id\":1010},{\"name\":\"用户身份证反面\",\"id\":1011},{\"name\":\"用户驾驶证正面\",\"id\":1020},{\"name\":\"用户驾驶证反面\",\"id\":1021},{\"name\":\"人车合影\",\"id\":1030},{\"name\":\"网约车驾驶员证\",\"id\":1040},{\"name\":\"行驶证正本正面\",\"id\":2010},{\"name\":\"行驶证正本反面\",\"id\":2011},{\"name\":\"行驶证副本\",\"id\":2012},{\"name\":\"网约车运输资格证\",\"id\":2020}],\"23\":[{\"name\":\"待交车\",\"id\":3},{\"name\":\"履约中\",\"id\":4},{\"name\":\"已结束\",\"id\":5}],\"24\":[{\"name\":\"未审核\",\"id\":0},{\"name\":\"待审核\",\"id\":1},{\"name\":\"审核通过\",\"id\":2},{\"name\":\"审核失败\",\"id\":3}],\"25\":[{\"name\":\"已确认，生成新的续租订单\",\"id\":1},{\"name\":\"已与司机沟通，不再续租\",\"id\":2},{\"name\":\"不确定，暂不处理\",\"id\":0}],\"26\":[{\"name\":\"经租\",\"id\":1},{\"name\":\"融租\",\"id\":2},{\"name\":\"先租后买\",\"id\":4},{\"name\":\"经租（专车）\",\"id\":33},{\"name\":\"融租（专车）\",\"id\":34},{\"name\":\"租售（专车）\",\"id\":38},{\"name\":\"经租（乘用车）\",\"id\":65},{\"name\":\"融租（乘用车）\",\"id\":66},{\"name\":\"购车（乘用车）\",\"id\":67},{\"name\":\"先租后买（乘用车）\",\"id\":68}],\"27\":[{\"name\":\"无返利\",\"id\":1},{\"name\":\"车主介绍人线索返利\",\"id\":10},{\"name\":\"车主介绍人销售返利\",\"id\":11},{\"name\":\"小桔经纪人线索返利\",\"id\":20},{\"name\":\"小桔经纪人销售返利\",\"id\":21}],\"28\":[{\"name\":\"平台\",\"id\":0},{\"name\":\"车胜\",\"id\":1},{\"name\":\"AMC\",\"id\":2}],\"29\":[{\"name\":\"天府银行\",\"id\":1},{\"name\":\"微众银行\",\"id\":2},{\"name\":\"浙商银行\",\"id\":3},{\"name\":\"自有资金\",\"id\":4}]}}";
// String test2="{\"errno\":0,\"errmsg\":null,\"data\":{\"0\":[{\"name\":\"初状态\",\"id\":0},{\"name\":\"待约\",\"id\":10},{\"name\":\"待车\",\"id\":20},{\"name\":\"待确认资金\",\"id\":35},{\"name\":\"待交车\",\"id\":40},{\"name\":\"车辆已绑定\",\"id\":50},{\"name\":\"待保单审核\",\"id\":55},{\"name\":\"租赁中\",\"id\":60},{\"name\":\"还款中\",\"id\":70},{\"name\":\"已完成\",\"id\":100},{\"name\":\"已取消\",\"id\":120},{\"name\":\"下单失败\",\"id\":130}],\"1\":[{\"name\":\"经租（快车）\",\"id\":1},{\"name\":\"融租（快车）\",\"id\":2},{\"name\":\"买车（快车）\",\"id\":3},{\"name\":\"先租后买（快车）\",\"id\":4},{\"name\":\"经租（专车）\",\"id\":33},{\"name\":\"融租（专车）\",\"id\":34},{\"name\":\"租售（专车）\",\"id\":38},{\"name\":\"经租（乘用车）\",\"id\":65},{\"name\":\"融租（乘用车）\",\"id\":66},{\"name\":\"购车（乘用车）\",\"id\":67},{\"name\":\"先租后买（乘用车）\",\"id\":68}],\"2\":[{\"name\":\"无法联系到司机\",\"id\":1},{\"name\":\"司机到店后放弃\",\"id\":2},{\"name\":\"司机希望更换商品\",\"id\":3},{\"name\":\"副班司机重复下单\",\"id\":4},{\"name\":\"当前商品无法提供\",\"id\":5},{\"name\":\"其他\",\"id\":6}],\"3\":[{\"name\":\"全款\",\"id\":1},{\"name\":\"分期\",\"id\":2}],\"4\":[{\"name\":\"正常结束\",\"id\":0},{\"name\":\"提前全款购车\",\"id\":1},{\"name\":\"退车解约\",\"id\":2},{\"name\":\"转为普通租赁\",\"id\":3}],\"5\":[{\"name\":\"NOT_CONTACT\",\"id\":1},{\"name\":\"NOT_ARRIVAL\",\"id\":2}],\"6\":[{\"name\":\"待交车\",\"id\":3},{\"name\":\"履约中\",\"id\":4},{\"name\":\"已结束\",\"id\":5},{\"name\":\"即将过期\",\"id\":6}],\"7\":[{\"name\":\"审核通过\",\"id\":1},{\"name\":\"审核驳回\",\"id\":2},{\"name\":\"不可用\",\"id\":3}],\"8\":[{\"name\":\"系统\",\"id\":0},{\"name\":\"租赁公司\",\"id\":1},{\"name\":\"司机\",\"id\":2}],\"9\":[{\"name\":\"已上传\",\"id\":0},{\"name\":\"审核中\",\"id\":1},{\"name\":\"审核失败\",\"id\":2},{\"name\":\"审核通过\",\"id\":3},{\"name\":\"待CP签约\",\"id\":10},{\"name\":\"待第三方签约\",\"id\":15},{\"name\":\"待司机签约\",\"id\":20},{\"name\":\"已签约\",\"id\":30},{\"name\":\"已撤销\",\"id\":40},{\"name\":\"已拒签\",\"id\":41},{\"name\":\"已失效\",\"id\":50},{\"name\":\"已归档\",\"id\":60}],\"10\":[{\"name\":\"主班\",\"id\":1},{\"name\":\"副班\",\"id\":2}],\"11\":[{\"name\":\"租赁公司\",\"id\":1},{\"name\":\"司机\",\"id\":2}],\"13\":[{\"name\":\"经租（快车）\",\"id\":1},{\"name\":\"融租（快车）\",\"id\":2},{\"name\":\"先租后买（快车）\",\"id\":3},{\"name\":\"全款购车（快车）\",\"id\":4},{\"name\":\"分期购车（快车）\",\"id\":5},{\"name\":\"经租（专车）\",\"id\":33},{\"name\":\"融租（专车）\",\"id\":34}],\"14\":[{\"name\":\"待预约\",\"id\":1},{\"name\":\"待看车\",\"id\":2},{\"name\":\"待交车\",\"id\":3},{\"name\":\"租赁中\",\"id\":4},{\"name\":\"还款中\",\"id\":5},{\"name\":\"已完成\",\"id\":6},{\"name\":\"已取消\",\"id\":7}],\"15\":[{\"name\":\"无效\",\"id\":0},{\"name\":\"有效\",\"id\":1}],\"16\":[{\"name\":\"租金返现\",\"id\":1},{\"name\":\"司机担保收入\",\"id\":2}],\"17\":[{\"name\":\"已上传\",\"id\":0},{\"name\":\"审核中\",\"id\":1},{\"name\":\"审核失败\",\"id\":2},{\"name\":\"审核通过\",\"id\":3},{\"name\":\"待CP签约\",\"id\":10},{\"name\":\"待第三方签约\",\"id\":15},{\"name\":\"待司机签约\",\"id\":20},{\"name\":\"已签约\",\"id\":30},{\"name\":\"已撤销\",\"id\":40},{\"name\":\"已拒签\",\"id\":41},{\"name\":\"已失效\",\"id\":50},{\"name\":\"已归档\",\"id\":60}],\"18\":[{\"name\":\"快车\",\"id\":1},{\"name\":\"专车\",\"id\":2},{\"name\":\"乘用车\",\"id\":3},{\"name\":\"优享\",\"id\":4}],\"19\":[{\"name\":\"未上传\",\"id\":0},{\"name\":\"待审核\",\"id\":1},{\"name\":\"审核通过\",\"id\":2},{\"name\":\"审核失败\",\"id\":3}],\"20\":[{\"name\":\"已上传\",\"id\":0},{\"name\":\"审核中\",\"id\":1},{\"name\":\"审核成功\",\"id\":2},{\"name\":\"审核失败\",\"id\":3}],\"21\":[{\"name\":\"司机照片\",\"id\":1},{\"name\":\"副班司机照片\",\"id\":2},{\"name\":\"车辆照片\",\"id\":3}],\"22\":[{\"name\":\"用户身份证正面\",\"id\":1010},{\"name\":\"用户身份证反面\",\"id\":1011},{\"name\":\"用户驾驶证正面\",\"id\":1020},{\"name\":\"用户驾驶证反面\",\"id\":1021},{\"name\":\"人车合影\",\"id\":1030},{\"name\":\"网约车驾驶员证\",\"id\":1040},{\"name\":\"行驶证正本正面\",\"id\":2010},{\"name\":\"行驶证正本反面\",\"id\":2011},{\"name\":\"行驶证副本\",\"id\":2012},{\"name\":\"网约车运输资格证\",\"id\":2020}],\"23\":[{\"name\":\"待交车\",\"id\":3},{\"name\":\"履约中\",\"id\":4},{\"name\":\"已结束\",\"id\":5}],\"24\":[{\"name\":\"未审核\",\"id\":0},{\"name\":\"待审核\",\"id\":1},{\"name\":\"审核通过\",\"id\":2},{\"name\":\"审核失败\",\"id\":3}],\"25\":[{\"name\":\"已确认，生成新的续租订单\",\"id\":1},{\"name\":\"已与司机沟通，不再续租\",\"id\":2},{\"name\":\"不确定，暂不处理\",\"id\":0}],\"26\":[{\"name\":\"经租\",\"id\":1},{\"name\":\"融租\",\"id\":2},{\"name\":\"先租后买\",\"id\":4},{\"name\":\"经租（专车）\",\"id\":33},{\"name\":\"融租（专车）\",\"id\":34},{\"name\":\"租售（专车）\",\"id\":38},{\"name\":\"经租（乘用车）\",\"id\":65},{\"name\":\"融租（乘用车）\",\"id\":66},{\"name\":\"购车（乘用车）\",\"id\":67},{\"name\":\"先租后买（乘用车）\",\"id\":68}],\"27\":[{\"name\":\"无返利\",\"id\":1},{\"name\":\"车主介绍人线索返利\",\"id\":10},{\"name\":\"车主介绍人销售返利\",\"id\":11},{\"name\":\"小桔经纪人线索返利\",\"id\":20},{\"name\":\"小桔经纪人销售返利\",\"id\":21}],\"28\":[{\"name\":\"平台\",\"id\":0},{\"name\":\"车胜\",\"id\":1},{\"name\":\"AMC\",\"id\":2}],\"29\":[{\"name\":\"天府银行\",\"id\":1},{\"name\":\"微众银行\",\"id\":2},{\"name\":\"浙商银行\",\"id\":3},{\"name\":\"自有资金\",\"id\":4}]}}";
//代优化问题：

//如果用户只发送了一个接口的情况

//当访问不正确情况

//当服务器内部出现错误的时候返回什么。

// 异常信息需要返回什么。
