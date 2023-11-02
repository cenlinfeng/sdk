package com.ddt.h5game;

import android.content.Context;

import com.ddtsdk.common.jsonadapter.ParameterizedTypeImpl;
import com.ddtsdk.utils.GsonUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Scanner;

/**
 * Created by CZG55 on 2020/9/11.
 * 兵王本地资源帮助类
 */
public class BWResourceHelper {

    public static String getResourceName(Context context,String url){
        List<String> resourceList = getResourceNameList(context);
        for (int i = 0; i < resourceList.size(); i++) {
            if (url.contains(resourceList.get(i))){
                return resourceList.get(i);
            }
        }
        return null;
    }

    private static List<String> getResourceNameList(Context context){
        List<String> resourceList = null;
        try {
            InputStream input = context.getAssets().open("bw.json");
            String json = convertStreamToString(input);
            Type gameListType = new ParameterizedTypeImpl(List.class, new Type[]{String.class}, List.class);
            resourceList= GsonUtils.getInstance().fromJson(json,gameListType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resourceList;
    }

    /**
     * input 流转换为字符串
     *
     * @param is
     * @return
     */
    private static String convertStreamToString(InputStream is) {
        String s = null;
        try {
            //格式转换
            Scanner scanner = new Scanner(is, "UTF-8").useDelimiter("\\A");
            if (scanner.hasNext()) {
                s = scanner.next();
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }
}
