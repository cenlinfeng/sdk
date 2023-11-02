package com.ddt.h5game.network;

import android.content.Context;
import android.util.Log;

import com.ddtsdk.KLSDK;
import com.ddtsdk.common.network.SSLSocketFactoryCompat;

import org.json.JSONObject;

import java.io.IOException;
import java.security.cert.CertificateException;

import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetH5UrlFromServiceOld {

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
        Log.e("DATAllllllllllllllllll",url);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().sslSocketFactory(new SSLSocketFactoryCompat(trustAllCert),trustAllCert).build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("DATAllllllllllll2222",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String url = jsonObject.getString("data");
                    Log.e("DATA",url);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(url)
                            .append("&")
                            .append("package=")
                            .append(context.getPackageName());
                    back.getUrl(stringBuilder.toString(), -1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //    public static void getStr(final String url, final MainActivity.UrlBack back) {
//        Executors.newCachedThreadPool().execute(new Runnable() {
//            @Override
//            public void run() {
//                StringBuffer h5game = new StringBuffer();
//                try {
//                    URL H5url = new URL(url);
//                    HttpsURLConnection httpConnection = (HttpsURLConnection) H5url.openConnection();
//                    InputStream is = httpConnection.getInputStream();
//                    BufferedReader bf = new BufferedReader(new InputStreamReader(is, "utf-8"));
//                    String s = null;
//                    while ((s = bf.readLine()) != null) {
//                        h5game.append(s);
//                    }
//                    Log.e("h5game", h5game + "======");
//                    bf.close();
//                    is.close();
//                    httpConnection.disconnect();
//                    JSONObject jsonObject = new JSONObject(h5game.toString());
//                    back.getUrl(jsonObject.getString("data"));
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
}
