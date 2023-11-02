package com.ddt.h5game.network;

import android.content.Context;
import android.util.Log;

import com.ddt.h5game.AppConfig;
import com.ddt.h5game.MainActivity;
import com.ddtsdk.KLSDK;
import com.ddtsdk.common.network.SSLSocketFactoryCompat;

import com.ddtsdk.utils.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.cert.CertificateException;

import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetH5UrlFromService {

    /**
     *  okhttp 网络请求 将登录结果返回的链接在此请求服务器得到h5游戏真正的入口地址
     *
     */

    // 自定义一个信任所有证书的TrustManager，添加SSLSocketFactory的时候要用到
    private static X509TrustManager trustAllCert = new X509TrustManager() {
        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
        }
    };

    public static void getFinalUrl(String url, final UrlCallBack back, final Context context){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().sslSocketFactory(new SSLSocketFactoryCompat(trustAllCert),trustAllCert).build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String url = "";
                    int type = 1;
                    if (jsonObject.has("dologinurl")){
                        AppConfig.loginUrlType=0;
                        url = Base64.decode(jsonObject.getString("dologinurl"));
                    }else {
                        type = 0;
                        StringBuilder sb = new StringBuilder();
                        AppConfig.loginUrlType=1;
                        sb.append(jsonObject.getString("data"))
                                .append("&")
                                .append("package=")
                                .append(context.getPackageName());
                        url = sb.toString();
                    }
                    back.getUrl(url, type);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
