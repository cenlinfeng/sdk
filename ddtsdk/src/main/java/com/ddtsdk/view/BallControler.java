package com.ddtsdk.view;

import android.support.annotation.NonNull;
import android.util.Log;

import com.ddtsdk.utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 悬浮球的条目控制。
 * <p>
 * Created by a5706 on 2018/8/3.
 */

public class BallControler {

    //使用map 存储键值对
    private static HashMap<String, Boolean> map;

    /**
     * 接收数组.
     *
     * @param json 菜单功能控制开关
     */
    public static void setCtr(@NonNull String json) {
        handler(json);
    }

    private static void handler(String json) {

        LogUtil.d("jsonArray--->" + json);
        JSONArray jsons = null;
        map = new HashMap<>();
        String k;
        boolean v;
        try {
            jsons = new JSONArray(json);
            for (int i = 0; i < jsons.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsons.get(i);
                k = jsonObject.optString("name");
                v = jsonObject.optInt("is_open") == 1;
                map.put(k, v);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

// 后台返回   "float_menu":[{"name":"userfloat","is_open":1},{"name":"servicefloat","is_open":1},
//    {"name":"gongluefloat","is_open":1},{"name":"hidefloat","is_open":1}]
    // 若隐藏,对应的字段将消失 ,所以需要判断是否为空

    /**
     * 是否展示该坐标对应的图标.
     *
     * @param key 悬浮球的item名称:userfloat,servicefloat,gongluefloat,hidefloat
     */
    public static boolean canShow(@NonNull String key) {
        if (map == null) {
            return true;
        }
        //自检
        if (map.isEmpty()) {  //小于等于0 或者 参数异常,默认显示.
            return true;
        }
        Log.d("|", "key:" + key + ",,value:" + map.get(key));
        if (map.get(key) == null) {
            return false;  //若无该key,默认隐藏
        } else {
            return map.get(key);
        }
    }

}
