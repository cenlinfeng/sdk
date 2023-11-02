package com.ddtsdk.utils;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.ddtsdk.KLSDK;
import com.ddtsdk.common.base.BaseParams;
import com.ddtsdk.common.network.BaseRetrofitClient;
import com.ddtsdk.common.network.PostRequest;
import com.ddtsdk.common.network.request.HttpRequestClient;
import com.ddtsdk.common.network.result.BaseBean;
import com.ddtsdk.constants.ApiConstants;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.model.data.DeviceInfo;
import com.ddtsdk.model.protocol.params.PayPointParams;
import com.ddtsdk.model.protocol.params.NoDataParams;
import com.ddtsdk.network.ApiException;
import com.ddtsdk.network.HttpStatusCode;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PayPointReport {
    private static PayPointReport payPointReport;
    private static SharedPreferences spu = null;
    private static String FILENAME = "ddt_pay_point";
    private static String payKey = "paykey";
    private static Retrofit retrofit = BaseRetrofitClient.getInstance().getDefaultRetrofit();
    private static Map<String, HashMap<String, String>> map = new HashMap<>();

    private void reportToServies(final PayPointParams p, final String uid){
        sendPostRequest(uid, ApiConstants.ACTION_PAY_POINT, p, PayPointParams.class, new HttpRequestClient.ResultHandler<Object>(KLSDK.getInstance().getContext()) {
            @Override
            public void onSuccess(Object o) {
            }

            @Override
            public void onFailure(Throwable t) {
                if (!(t instanceof ApiException)){
                    Log.e("PayPointReport", "请求失败=");
                    if (map.get(uid) != null){
                        saveData(map.get(uid));
                    }
                }
            }
        });
    }

    public static PayPointReport getInstance(){
        if (spu == null || payPointReport == null){
            synchronized (PayPointReport.class){
                if (spu == null){
                    spu = KLSDK.getInstance().getContext()
                            .getSharedPreferences(FILENAME, 0);
                }
                if (payPointReport == null){
                    payPointReport = new PayPointReport();
                }
            }
        }
        return payPointReport;
    }

    /**
     *
     * @param type
     *              1 => 'CP调安卓SDK支付接口',
     *             2 => 'SDK加载支付链接成功',
     *             3 => 'SDK加载支付链接失败',
     *             4 => '加载完成支付方式选择页面',
     *             5 => '点击[支付方式选择页面]关闭按钮',
     *             6 => '点击[支付方式选择页面]支付宝按钮',
     *             7 => '点击[支付方式选择页面]微信按钮',
     * @param ext
     * 扩展参数
     */
    public void payPoint(int type, String ext){
        synchronized (this){
            Log.e("PayPointReport", "type=" + type + "==ext" + ext);
            if (AppConstants.sdk_track == 1){
                PayPointParams payPoint = new PayPointParams(type, AppConstants.paramquery, Base64.encode(ext.getBytes()));
                reportToServies(payPoint, System.currentTimeMillis()+"");
            }
        }
    }

    public void pushPoint(int type){
        synchronized (this){
            Log.e("PayPointReport", "type=" + type);
            if (AppConstants.sdk_track == 1){
                PayPointParams payPoint = new PayPointParams(type, AppConstants.paramquery, "");
                reportToServies(payPoint, System.currentTimeMillis()+"");
            }
        }
    }

    public void cache(){
        if (AppConstants.sdk_track == 1){
            try {
                String temp = spu.getString(payKey, "");
                if (!TextUtils.isEmpty(temp)){
                    SharedPreferences.Editor edit = spu.edit();
                    edit.putString(payKey, "");
                    edit.apply();
                    toObj(temp);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void saveData(HashMap<String, String> hashm) {
        if (hashm != null){
            String json = GsonUtils.getInstance().hashMapToJson(hashm);
            SharedPreferences.Editor edit = spu.edit();
            edit.putString(payKey, json + "&&");
            edit.apply();
        }
    }

//    private void toObj(String json) {
//        if (!TextUtils.isEmpty(json)) {
//            for (String val : json.split("&&")) {
//                if (!TextUtils.isEmpty(val)) {
//                    try {
//                        PayPointParams ppp = (PayPointParams)GsonUtils.getGson().fromJson(val, PayPointParams.class);
//                        payCache(ppp);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }
    private void toObj(String json) {
        if (!TextUtils.isEmpty(json)) {
            for (String val : json.split("&&")) {
                if (!TextUtils.isEmpty(val)) {
                    try {
                        HashMap<String, String> hmp = new HashMap<>();
                        JSONObject jsonObject = new JSONObject(val);
                        for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
                            String tkey = it.next();
                            if (!TextUtils.isEmpty(tkey)){
                                hmp.put(tkey, jsonObject.getString(tkey));
                            }
                        }
                        payCache(hmp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * @param hs
     */
    public void payCache(final HashMap<String, String> hs) {
        Log.e("PayPointReport", "请求失败=payCache===");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000 * 5);
                    PostRequest postRequest = retrofit.create(PostRequest.class);
                    Call<ResponseBody> call = postRequest.postMap(ApiConstants.ACTION_PAY_POINT, hs);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            saveData(hs);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 发送post网络请求
     * @param url 请求地址
     * @param params 参数对象
     * @param dataType 返回的数据类型
     * @param resultHandler 回调
     * @param <T> 泛型
     */
    public static <T> void sendPostRequest(String uid, String url, Object params, final Type dataType, final HttpRequestClient.ResultHandler<T> resultHandler) {
        // 判断网络连接状况
        if (resultHandler.isNetDisconnected()) {
            resultHandler.onFailure(new ApiException("", HttpStatusCode.NO_NET_WORK));
            return;
        }
        PostRequest postRequest = retrofit.create(PostRequest.class);
        DeviceInfo deviceInfo = new DeviceInfo(KLSDK.getInstance().getContext());
        // 参数
        BaseParams baseParams = new BaseParams<>(deviceInfo, params);
        HashMap<String, String> hMap = baseParams.getParams();
        map.put(uid, hMap);
        // 构建请求
        Call<ResponseBody> call = postRequest.postMap(url, hMap);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    ResponseBody body = response.body();
                    if (body == null) {
                        resultHandler.onFailure(new ApiException("", HttpStatusCode.RESPONSE_BODY_NULL));
                        return;
                    }
                    String string = body.string();
                    BaseBean baseBean = GsonUtils.getInstance().fromJson(string, BaseBean.class);
                    if (baseBean.isResult()) {
                        //基于ParameterizedType实现泛型类类型参数化
                        try {
                            String str = baseBean.getData().toString();
                            String encode = new String(DataUtils.base64Decode(str));
                            String urlData = RSAEncrypt.decryptByPrivateKey(encode, Constants.private_key);
                            String json = UrlParse.getURLDecoderString(urlData);
                            T t = GsonUtils.getInstance().fromJson(json, dataType);
                            resultHandler.onSuccess(t);
                        } catch (Exception e) {
                            String str = GsonUtils.getInstance().toJson(baseBean.getData());
                            T t = GsonUtils.getInstance().fromJson(str, dataType);
                            resultHandler.onSuccess(t);
                        }
                    } else {
                        ApiException apiException = new ApiException(baseBean.getMsg(), HttpStatusCode.RESPONSE_FAIL);
                        resultHandler.onFailure(apiException);
                    }
                    resultHandler.onResponse(baseBean);

                } catch (Exception e) {
                    e.printStackTrace();
                    resultHandler.onFailure(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                resultHandler.onFailure(t);
            }
        });
    }
}
