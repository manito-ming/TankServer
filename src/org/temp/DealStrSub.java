package org.temp;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class DealStrSub {
    /**
     * 正则表达式匹配两个指定字符串中间的内容
     * @param soap
     * @return
     */

    public static int fire;
    public static int path;


    /**
     * 返回单个字符串，若匹配到多个的话就返回第一个，方法与getSubUtil一样

     */
    public static String getSubUtilSimple(String soap,String rgex){
        Pattern pattern = Pattern.compile(rgex);// 匹配的模式
        Matcher m = pattern.matcher(soap);
        while(m.find()){
            return m.group(1);
        }
        return "";
    }
    public void fire(String code)
    {
        String rgex = "var fire_choose=(.*?);";
        String str = getSubUtilSimple(code,rgex);
        int fire_value=parseInt(str);
        this.fire=fire_value;
    }
    public void path(String code) {
        String rgex = "var path_choose=(.*?);";
        String str = getSubUtilSimple(code,rgex);
        int path_value=parseInt(str);
        this.path=path_value;
    }

}
