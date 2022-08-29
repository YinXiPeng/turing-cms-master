package com.bonc.turing.cms.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static RestTemplate client;

    static {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        //60s
        requestFactory.setConnectTimeout(60*1000);
        requestFactory.setReadTimeout(60*1000);
        client = new RestTemplate(requestFactory);
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.
                setSupportedMediaTypes(Arrays.asList(MediaType.ALL));
        client.getMessageConverters().add(mappingJackson2HttpMessageConverter);
    }


    public static JSONObject sendPostRequestByFormData(String url, MultiValueMap<String, Object> param){
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.POST;
        //以json方式提交
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        //将请求头部和参数合成一个请求
        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<>(param, headers);
        //执行http请求，将返回的结果封装
        ResponseEntity<JSONObject> response = client.exchange(url, method, requestEntity, JSONObject.class);
        return response.getBody();
    }

    public static JSONObject sendGetRequest(String url){
        ResponseEntity<JSONObject> response = client.getForEntity(url, JSONObject.class);
        return response.getBody();
    }

    public static JSONObject sendPostRequestByJson(String url, JSONObject params){
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.POST;
        //以json方式提交
        headers.setContentType(MediaType.APPLICATION_JSON);
        //将请求头部和参数合成一个请求
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(params, headers);
        //执行http请求，将返回的结果封装
        ResponseEntity<JSONObject> response = client.exchange(url, method, requestEntity, JSONObject.class);
        return response.getBody();
    }

    public static void delete(String url){
        client.delete(url);
    }

}
