package com.bonc.turing.cms.manage.constant;


import com.alibaba.fastjson.JSONObject;

/**
 * @author liuyunkai
 * @date 2018-11-26 16:40
 */
public class ResponseResult {

    public static JSONObject getResult(){
        //初始化结果
        JSONObject result = new JSONObject();
        result.put("code", SystemConstants.CODE_FAILURE);
        result.put("msg", SystemConstants.MSG_ERROR);
        return result;
    }

    public static JSONObject getParamsErrorResult(){
        //初始化结果
        JSONObject result = new JSONObject();
        result.put("data", new JSONObject());
        result.put("code", SystemConstants.CODE_407);
        result.put("msg", SystemConstants.MSG_407);
        return result;
    }

    public static void successResult(JSONObject result){
        result.put("code", SystemConstants.CODE_SUCCESS);
        result.put("msg", SystemConstants.MSG_200);
    }

    /**
     * 内部错误
     * @param result 结果
     */
    public static void internalErrorResult(JSONObject result){
        result.put("code", SystemConstants.CODE_ERROR);
        result.put("msg", SystemConstants.MSG_ERROR);
        result.put("data", new JSONObject());
    }

    /**
     * 参数错误
     * @param result 结果
     */
    public static void paramErrorResult(JSONObject result){
        result.put("code", SystemConstants.CODE_ERROR);
        result.put("msg", SystemConstants.MSG_407);
        result.put("data", new JSONObject());
    }
    /**
     * 用户名不可用
     * @param result 结果
     */
    public static void userNameErrorResult(JSONObject result){
        result.put("code", SystemConstants.CODE_402);
        result.put("msg", SystemConstants.MSG_402);
        result.put("data", new JSONObject());
    }
    /**
     * 用户名不可用
     * @param result 结果
     */
    public static void userNameNullResult(JSONObject result){
        result.put("code", SystemConstants.CODE_405);
        result.put("msg", SystemConstants.MSG_405);
        result.put("data", new JSONObject());
    }
    /**
     * 手机号已有账号
     * @param result 结果
     */
    public static void phoneErrorResult(JSONObject result){
        result.put("code", SystemConstants.CODE_403);
        result.put("msg", SystemConstants.MSG_403);
        result.put("data", new JSONObject());
    }
    /**
     * 手机号未绑定账号
     * @param result 结果
     */
    public static void phoneNullResult(JSONObject result){
        result.put("code", SystemConstants.CODE_406);
        result.put("msg", SystemConstants.MSG_406);
        result.put("data", new JSONObject());
    }
    /**
     * 未生成验证码
     * @param result 结果
     */
    public static void codeErrorResult(JSONObject result){
        result.put("code", SystemConstants.CODE_404);
        result.put("msg", SystemConstants.MSG_404);
        result.put("data", new JSONObject());
    }
    /**
     * 未生成验证码
     * @param result 结果
     */
    public static void codeErrorMsgResult(JSONObject result){
        result.put("code", SystemConstants.CODE_404);
        result.put("msg", "发送短信失败");
        result.put("data", new JSONObject());
    }

    /**
     * 通过二维码参加比赛
     * @param result 结果
     */
    public static void applyedResult(JSONObject result){
        result.put("code", SystemConstants.CODE_410);
        result.put("msg", SystemConstants.MSG_410);
    }

    public static void passWordIsNull(JSONObject result){
        result.put("code", SystemConstants.CODE_411);
        result.put("msg", SystemConstants.MSG_411);
    }

    public static void noPeople(JSONObject result){
        result.put("code", SystemConstants.CODE_412);
        result.put("msg", SystemConstants.MSG_412);
    }

    public static void phoneIsNull(JSONObject result){
        result.put("code", SystemConstants.CODE_413);
        result.put("msg", SystemConstants.MSG_413);
    }

}
