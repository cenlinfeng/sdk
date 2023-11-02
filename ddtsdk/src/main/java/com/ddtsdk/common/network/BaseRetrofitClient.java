package com.ddtsdk.common.network;

import com.ddtsdk.common.network.converter.GsonConverterFactory;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.utils.GsonUtils;

import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by CZG on 2020/3/31
 */
public class BaseRetrofitClient {

    private static Retrofit retrofit;
    private static int TIME_OUT = 30; // 30秒超时断开连接

    private BaseRetrofitClient(){
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.baseHttp)
                    .client(newOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(GsonUtils.getGson()))
                    .build();
        }
    }

    // 静态内部类模式实现单例
    private static class SingleTonHoler{
        private static BaseRetrofitClient INSTANCE = new BaseRetrofitClient();
    }


    public static BaseRetrofitClient getInstance() {

        return SingleTonHoler.INSTANCE;
    }


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
    private static SSLSocketFactory sslSocketFactory = new SSLSocketFactoryCompat(trustAllCert);

    // httpclient
    public static OkHttpClient client = new OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCert)
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            .build();

    public Retrofit getDefaultRetrofit() {
        return retrofit;
    }

    private OkHttpClient newOkHttpClient(){
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.sslSocketFactory(sslSocketFactory, trustAllCert)
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS);

        //添加日志输出
        //if (BuildConfig.DEVELOPMENT_ENV)
        {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClientBuilder.addInterceptor(loggingInterceptor);
        }

        return okHttpClientBuilder.build();
    }


}
