package com.xosmos.utils;

import org.json.JSONObject;

import java.util.Map;

public class JSONUtils {
    public static String getJSONString(int code, String msg, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if (map != null)
            for (String key : map.keySet())
                json.put(key, map.get(key));
        return json.toString();
    }

    public static String getJSONString(int code, String msg) {
        return getJSONString(code, msg, null);
    }

    public static String getJSONString(int code) {
        return getJSONString(code, null, null);
    }
}
