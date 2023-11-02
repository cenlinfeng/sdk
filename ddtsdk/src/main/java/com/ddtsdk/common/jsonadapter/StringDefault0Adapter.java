package com.ddtsdk.common.jsonadapter;

import android.text.TextUtils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * Created by CZG on 2018/12/27.
 * 对返回值为空处理
 */

public class StringDefault0Adapter implements JsonSerializer<String>, JsonDeserializer<String> {
    @Override
    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        try {
            if (TextUtils.equals(json.getAsString(),"null")) {//定义为String类型,如果后台返回""或者null,则返回0
                return null;
            }
        } catch (Exception ignore) {
        }
        try {
            return json.getAsString();
        } catch (NumberFormatException e) {
            throw new JsonSyntaxException(e);
        }
    }

    @Override
    public JsonElement serialize(String src, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(src);
    }
}

