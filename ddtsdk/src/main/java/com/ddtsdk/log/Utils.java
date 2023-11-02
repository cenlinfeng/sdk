package com.ddtsdk.log;

import com.ddtsdk.utils.GsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Provides convenient methods to some common operations
 */
public class Utils {

    public static String obj2String(Object... args){
        if (args.length < 1) return "it has not parmas!!!";
        StringBuilder builder = new StringBuilder();
        for (Object o : args){
            builder.append(PrinterFormat.DOUBLE_SPACE).append(Object2String(o)).append("\n");
        }
        return builder.toString();
    }


    private static String Object2String(Object object){
        StringBuilder builder = new StringBuilder();
        if (object instanceof String){
            return object.toString();
        }
        return jsonFormat(GsonUtils.getInstance().toJson(object));
    }

    private static String jsonFormat(String json){
//        if (com.ddtsdk.log.Utils.isEmpty(json)) {
//            d("Empty/Null json content");
//            return;
//        }
        String message = "";
        try {
            json = json.trim();
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                message = jsonObject.toString(2);
            }
            if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                message = jsonArray.toString(2);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }

}