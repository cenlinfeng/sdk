package com.ddtsdk.utils;

import android.text.TextUtils;

import com.ddtsdk.common.jsonadapter.DoubleDefault0Adapter;
import com.ddtsdk.common.jsonadapter.IntegerDefault0Adapter;
import com.ddtsdk.common.jsonadapter.LongDefault0Adapter;
import com.ddtsdk.common.jsonadapter.MyObjectTypeAdapter;
import com.ddtsdk.common.jsonadapter.StringDefault0Adapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.ObjectTypeAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by CZG on 2020/3/31
 */

public class GsonUtils {

    private Gson mGson;
    private static GsonUtils mInstance;

    public GsonUtils() {
        mGson = buildGson();
    }

    public static GsonUtils getInstance() {
        if (mInstance == null) {
            synchronized (GsonUtils.class) {
                if (mInstance == null) {
                    mInstance = new GsonUtils();
                }
            }
        }
        return mInstance;
    }

    public static Gson getGson() {
        return getInstance().mGson;
    }

    public String toJson(Object src) {
        if (src == null) {
            return mGson.toJson(JsonNull.INSTANCE);
        }
        return mGson.toJson(src);
    }

    public <T> T fromJson(String json, Class<T> classOfT) {
        return mGson.fromJson(json, classOfT);
    }

    public <T> T fromJson(String json, Type typeOfT, T t) {
        T u = mGson.fromJson(json, typeOfT);
        return u == null ? t : u;
    }

    public <T> T fromJson(String json, Type typeOfT) throws JsonSyntaxException {
        return mGson.fromJson(json,typeOfT);
    }

    public <T> List<T> fromJsonArray(String json, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for(final JsonElement elem : array){
            list.add(new Gson().fromJson(elem, cls));
        }
        return list;
    }

    public static HashMap<String, String> createObj(String str) {
        HashMap<String, String> map = new HashMap<>();

        if (TextUtils.isEmpty(str)) {
            return new HashMap<>();
        }

        try {
            JSONObject obj = new JSONObject(str);

            /*jsonobject遍历*/
            Iterator<?> strKeys = obj.keys();
            while (strKeys.hasNext()) {//遍历JSONObject
                String strKey = strKeys.next().toString();
                String strValue = obj.optString(strKey);
                if (strValue == null || strValue.equals("null")) {
                    strValue = "";
                }
                map.put(strKey, strValue);
            }
            return map;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new HashMap<>();
    }

    public String hashMapToJson(HashMap map) {
        String string = "{";
        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
            Map.Entry e = (Map.Entry) it.next();
            string += "\"" + e.getKey() + "\":";
            string += "\"" + e.getValue() + "\",";
        }
        string = string.substring(0, string.lastIndexOf(","));
        string += "}";
        return string;
    }

    private Gson buildGson() {
        // 容错数字类型返回null
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
                .registerTypeAdapter(int.class, new IntegerDefault0Adapter())
                .registerTypeAdapter(Double.class, new DoubleDefault0Adapter())
                .registerTypeAdapter(double.class, new DoubleDefault0Adapter())
                .registerTypeAdapter(Long.class, new LongDefault0Adapter())
                .registerTypeAdapter(long.class, new LongDefault0Adapter())
                .registerTypeAdapter(String.class, new StringDefault0Adapter())
                .create();
        try {
            Field factories = Gson.class.getDeclaredField("factories");
            factories.setAccessible(true);
            Object o = factories.get(gson);
            Class<?>[] declaredClasses = Collections.class.getDeclaredClasses();
            for (Class c : declaredClasses) {
                if ("java.util.Collections$UnmodifiableList".equals(c.getName())) {
                    Field listField = c.getDeclaredField("list");
                    listField.setAccessible(true);
                    List<TypeAdapterFactory> list = (List<TypeAdapterFactory>) listField.get(o);
                    int i = list.indexOf(ObjectTypeAdapter.FACTORY);
                    list.set(i, MyObjectTypeAdapter.FACTORY);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gson;
    }
}
