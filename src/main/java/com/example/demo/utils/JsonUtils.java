package com.example.demo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.ValueFilter;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonUtils {


    /**
     * 将List<T> 封装为JSONArray
     * 
     * @param list
     * @return
     */
    public static <T> JSONArray listBeanToJson(List<T> list) {
        ValueFilter filter = new ValueFilter() {
            @Override
            public Object process(Object obj, String s, Object v) {
                if (v == null)
                    return "";
                return v;
            }
        };
        String jsonText = JSON.toJSONString(list, filter);
        return JSONArray.parseArray(jsonText);
    }

    /**
     * 将字符串解析为JSONObject
     * 
     * @param jsonStr
     * @return
     */

    public static JSONObject getJsonObj(String jsonStr) {
        return JSONObject.parseObject(jsonStr);
    };

    /**
     * 将字符串解析为JSON
     * 
     * @param jsonStr
     * @return
     */

    public static JSON getJson(String jsonStr) {
        return JSON.parseObject(jsonStr);
    };

    /**
     * 将字符串解析为JSONArray
     *
     * @param jsonStr
     * @return
     */

    public static JSONArray getJsonArray(String jsonStr) {
        return JSONArray.parseArray(jsonStr);
    };

    /**
     * 将Json 类封装
     * 
     * @param obj
     * @return
     */

    public static JSONObject objectToJsonObject(Object obj) {
        ValueFilter filter = new ValueFilter() {
            @Override
            public Object process(Object obj, String s, Object v) {
                if (v == null)
                    return "";
                return v;
            }
        };
        String string = JSON.toJSONString(obj, filter);
        return JSONObject.parseObject(string);

    }

    /**
     * json 转map
     * 
     * @param json
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> jsonObjectToMapObject(JSONObject json) {
        String jsonStr = json.toString();
        Map<String, Object> map = JSON.parseObject(jsonStr, Map.class);
        return map;
    }

    /**
     * json 转JSONObject
     * 
     * @param json
     * @return
     */
    public static JSONObject jsonToJsonObject(JSON json) {
        return (JSONObject) json;
    }

    /**
     * json 转JSONArray
     * 
     * @param json
     * @return
     */
    public static JSONArray jsonToJsonArray(JSON json) {
        return (JSONArray) json;
    }





    // map转bean
    public static <T> T mapToBean(T t, Map<String, Object> map) {
        try {
            BeanUtils.populate(t, map);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return t;
    }

    // ListMap转ListBean
    public static <T> List<T> listMapToListBean(T t, List<Map<String, Object>> listMap) {
        List<T> resultAll = new ArrayList<>();
        listMap.forEach((e) -> {
            try {
                @SuppressWarnings("unchecked")
                T object = (T) t.getClass().newInstance();
                BeanUtils.populate(object, e);
                resultAll.add(object);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        return resultAll;
    }
}
