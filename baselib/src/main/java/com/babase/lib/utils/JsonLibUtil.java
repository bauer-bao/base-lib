package com.babase.lib.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author bauer on 17/5/5.
 */

public class JsonLibUtil {

    /**
     * 将返回结果包装成对应实体类
     *
     * @param str
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T parseObject(String str, Class<T> cls) {
        return JSON.parseObject(str, cls);
    }

    /**
     * 获取string
     *
     * @param object
     * @param key
     * @return
     */
    public static String getStringValue(JSONObject object, String key) {
        return object.getString(key);
    }

    /**
     * 获取int
     *
     * @param object
     * @param key
     * @return
     */
    public static int getIntValue(JSONObject object, String key) {
        return object.getIntValue(key);
    }

    /**
     * 获取boolean
     *
     * @param object
     * @param key
     * @return
     */
    public static boolean getBooleanValue(JSONObject object, String key) {
        return object.getBooleanValue(key);
    }

    /**
     * 获取double
     *
     * @param object
     * @param requestedAmount
     * @return
     */
    public static double getDoubleValue(JSONObject object, String requestedAmount) {
        return object.getDoubleValue(requestedAmount);
    }

    /**
     * 获取long
     *
     * @param object
     * @param key
     * @return
     */
    public static long getLongValue(JSONObject object, String key) {
        return object.getLongValue(key);
    }

    /**
     * 获取jsonobject
     *
     * @param object
     * @param key
     * @return
     */
    public static JSONObject getJsonObject(JSONObject object, String key) {
        return object.getJSONObject(key);
    }

    /**
     * 获取jsonarray
     *
     * @param object
     * @param key
     * @return
     */
    public static JSONArray getJsonArray(JSONObject object, String key) {
        return object.getJSONArray(key);
    }

    /**
     * 从服务器返回的json中获取到错误信息，因为错误信息是个数组，可能有多个错误信息，这里支取第一个错误
     *
     * @param object
     * @return
     */
    public static String getErrorMsg(JSONObject object) {
        if (!object.isEmpty()) {
            JSONArray jsonArray = object.getJSONArray("errors");
            if (jsonArray != null && jsonArray.size() > 0) {
                JSONObject error = jsonArray.getJSONObject(0);
                if (!error.isEmpty()) {
                    return getStringValue(error, "errorMessage");
                }
            } else {//没有error，需要取message
                return getStringValue(object, "message");
            }
        }
        return "";
    }
}
