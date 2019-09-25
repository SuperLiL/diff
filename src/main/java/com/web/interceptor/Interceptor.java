package com.web.interceptor;


import com.web.controller.DiffController;
import com.web.util.SSRFChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ResourceBundle;


@Component
public class Interceptor implements HandlerInterceptor {
    SSRFChecker ssrfChecker = new SSRFChecker();

    @Autowired
    private DiffController diffController;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        HandlerMethod handlerMethod = (HandlerMethod) handler;
        //HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
//        // 获取方法上的注解
        Diffintercept requiredStatistics = handlerMethod.getMethod().getAnnotation(Diffintercept.class);
        //如果不为空，说明有方法请求
        if(requiredStatistics!=null){
            //获取参数
            String requestUrl=null;
            String requestUrl2=null;
            requestUrl = request.getParameter("requestUrl");
            requestUrl2 = request.getParameter("requestUrl2");
            //判断参数是否为空，若不为空进行下一步的验证。
            if(requestUrl!=null&&requestUrl2!=null){
                //解码
                requestUrl = URLDecoder.decode(requestUrl, "UTF-8");
                requestUrl2 = URLDecoder.decode(requestUrl2, "UTF-8");
                //判断是否为白名单账号，防止请求非法数据。
                boolean b1 = ssrfChecker.checkURL(requestUrl);
                boolean b2 = ssrfChecker.checkURL(requestUrl2);
                //如果两个请求都为真返回true，否则返回false
                if(b1&&b2){
                    request.setAttribute("error",0);
                    return true;
                }else {
                    request.setAttribute("error",1);
                    response.sendError(400);
                    return false;
                }
            }
               /* response.reset();
                 //设置编码格式
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter pw = response.getWriter();
                pw.write("非法访问地址，请先注册后在使用diff工具");
                pw.flush();
                pw.close();*/
        }

        //System.out.println("jinlaibu");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        String userId= request.getParameter("userId");
//        // 将handler强转为HandlerMethod, 前面已经证实这个handler就是HandlerMethod
//        HandlerMethod handlerMethod = (HandlerMethod) handler;
//        //HandlerMethod handlerMethod = (HandlerMethod) handler;
//        Method method = handlerMethod.getMethod();
//        // 获取方法上的注解
//        RequiredStatistics requiredStatistics = handlerMethod.getMethod().getAnnotation(RequiredStatistics.class);
//        // 如果方法上的注解为空 则获取类的注解
//        if (requiredStatistics == null) {
//            requiredStatistics = handlerMethod.getMethod().getDeclaringClass().getAnnotation(RequiredStatistics.class);
//        }
//
//        if (requiredStatistics != null && StringUtils.isNotBlank(requiredStatistics.value())) {
//            logger.info("注解值："+requiredStatistics.value()+"name："+requiredStatistics.name());
//            Timestamp time = new Timestamp(new Date().getTime());
//            statisticsService.insert(requiredStatistics.value(), requiredStatistics.name(), method.getName(), userId, "", time);
//
//        }
        //response.getHeader();

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //System.out.println("afterCompletion");
    }
}

