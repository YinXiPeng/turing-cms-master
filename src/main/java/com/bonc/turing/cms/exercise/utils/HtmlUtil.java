package com.bonc.turing.cms.exercise.utils;


import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author yh
 * @Description 用于处理html文本的工具类
 * @Date 12:16 2020/3/28
 * @Param
 * @return
 **/
public class HtmlUtil {

    /**
     * 用jsoup默认白名单过滤html
     * @author yh
     * @param text
     * @return
     * @date 2020.04.03
     */
    public static String  cleanHtml(String text){
        Whitelist whitelist = Whitelist.relaxed();
        return  Jsoup.clean(text,whitelist);
    }

    /**
     * 用jsoup过滤html标签
     * @author yh
     * @param text
     * @return
     * @date 2020.04.03
     */
    public static String  cleanAllHtmlTag(String text){
        Whitelist whitelist = Whitelist.none();
        return  Jsoup.clean(text,whitelist);
    }

}
