package com.web.util;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component(value = "diffTools")
public class DiffTools {
    /**
     * 判断复杂的Json类型
     * @param value
     * @return
     */
    public int verificationBykey(Object value){
        ArrayList<Character> arr = new ArrayList<Character>();
        //获取第一个字符
        if(value==null){
            return 0;
        }

        //2019。8。20新增异常情况：字符串为空的情况
        String ac  = value==null?"null":value.toString();
        if(ac==""||ac.length()==0){
            return 9;
        }
        char a = ac.charAt(0);

        switch (a){
//          开头[，表示嵌套了一个数组
            case '[':
                return 1;
//          开头为{，表示嵌套的是一个JsonObject对象
            case '{':
                return 2;
                // 开头为]，表示嵌套的是一个JsonArray对象结束
            case ']':
                if(arr.get(arr.size()-1)==a){
                    arr.remove(arr.size()-1);
                    return 3;
                }else{
                    return 7;
                }
//          开头为}，表示嵌套的是一个JsonObject对象结束
            case '}':
                if(arr.get(arr.size()-1)==a){
                    arr.remove(arr.size()-1);
                    return 4;
                }else{
                    return 7;
                }
//          开头为"，表示普通值并没有嵌套过程
            default:
                return 5;

        }

    }
}
