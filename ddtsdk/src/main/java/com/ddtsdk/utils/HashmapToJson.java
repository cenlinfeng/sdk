package com.ddtsdk.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;


public class HashmapToJson {

    public static String hashMapToJson(HashMap map) {
        String string = "{";
        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
            Entry e = (Entry) it.next();
            string += "\"" + e.getKey() + "\":";
            string += "\"" + e.getValue() + "\",";
        }
        string = string.substring(0, string.lastIndexOf(","));
        string += "}";
        return string;
    }

    public static String toJson(HashMap<String, Object> map) {
        String jsonString = "[";

        jsonString += hashMapToJson(map);

        return jsonString += "]";

    }
}