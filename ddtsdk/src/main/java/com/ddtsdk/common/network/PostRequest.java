package com.ddtsdk.common.network;

import com.ddtsdk.mylibrary.BuildConfig;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Post请求封装
 */
public interface PostRequest{

    /**
     * 发送Post请求
     * @param url URL路径
     * @param requestMap 请求参数
     * @return
     */
    @FormUrlEncoded
    @POST
    @Headers("apiversion:"+ BuildConfig.API_VERSION)
    Call<ResponseBody> postMap(@Url String url, @FieldMap Map<String, String> requestMap);

//    /**
//     * 发送Post请求
//     * @param url URL路径
//     * @param baseParams 请求参基类 BaseParams
//     * @return
//     */
//    @POST
//    @Headers("apiversion:"+ BuildConfig.API_VERSION)
//    Call<ResponseBody> postMap(@Url String url, @FieldMap Object baseParams);

}
