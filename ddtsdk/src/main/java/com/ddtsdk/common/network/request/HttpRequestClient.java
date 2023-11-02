package com.ddtsdk.common.network.request;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.ddtsdk.common.jsonadapter.ParameterizedTypeImpl;
import com.ddtsdk.common.network.BaseRetrofitClient;
import com.ddtsdk.common.network.GetRequest;
import com.ddtsdk.common.network.PostRequest;
import com.ddtsdk.common.network.result.BaseBean;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.ui.dialog.KLCommonAffirmDialog;
import com.ddtsdk.utils.Base64;
import com.ddtsdk.utils.DataUtils;
import com.ddtsdk.utils.GsonUtils;
import com.ddtsdk.utils.LifeCycleUtils;
import com.ddtsdk.utils.LogUtil;
import com.ddtsdk.utils.RSAEncrypt;
import com.ddtsdk.utils.ToastUtils;
import com.ddtsdk.model.data.DeviceInfo;
import com.ddtsdk.common.base.BaseParams;
import com.ddtsdk.network.ApiException;
import com.ddtsdk.network.HttpStatusCode;
import com.ddtsdk.utils.FastClickUtils;
import com.ddtsdk.utils.NetworkUtils;
import com.ddtsdk.utils.UrlParse;
import com.ddtsdk.utils.Utils;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Retrofit请求类 （get和post）
 */
public class HttpRequestClient {
    private static final String TAG = "HttpRequestClient";

    private static Retrofit retrofit = BaseRetrofitClient.getInstance().getDefaultRetrofit();

    /**
     * 发送GET网络请求
     * @param url 请求地址
     * @param clazz 返回的数据类型
     * @param resultHandler 回调
     * @param <T> 泛型
     */
    public static <T> void sendGetRequest(String url, final Class<T> clazz, final ResultHandler<T> resultHandler) {
        // 判断网络连接状况
        if (resultHandler.isNetDisconnected()) {
            resultHandler.onFailure(new ApiException("", HttpStatusCode.NO_NET_WORK));
            return;
        }

        GetRequest getRequest = retrofit.create(GetRequest.class);

        // 构建请求
        Call<ResponseBody> call = getRequest.getUrl(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    ResponseBody body = response.body();
                    if (body == null) {
                        resultHandler.onServerError();
                        resultHandler.onFailure(new ApiException("", HttpStatusCode.RESPONSE_BODY_NULL));
                        return;
                    }
                    String string = body.string();
                    T t = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().fromJson(string, clazz);

                    resultHandler.onSuccess(t);
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

    /**
     * 发送post网络请求
     * @param url 请求地址
     * @param params 参数对象
     * @param dataType 返回的数据类型
     * @param resultHandler 回调
     * @param <T> 泛型
     */
    public static <T> void sendPostRequest(String url, Object params, final Type dataType, final ResultHandler<T> resultHandler) {
        // 判断网络连接状况
        if (resultHandler.isNetDisconnected()) {
            resultHandler.onFailure(new ApiException("", HttpStatusCode.NO_NET_WORK));
            return;
        }

        if(!AppConstants.isInit && Utils.isWifiProxy(resultHandler.context)){
            LogUtil.e("PROXY","====================3");

            if(AppConstants.banpackage){
                LogUtil.e("1111188888","===============2222222");
                KLCommonAffirmDialog.Builder(1,false).setContent("网络异常，请重新进入游戏！").setIAffirmDialogClick(new KLCommonAffirmDialog.IAffirmDialogClick() {
                @Override
                public void onOkClick() {
                    System.exit(0);
                }

                @Override
                public void onCancelClick() {

                }
            }).show(((Activity)resultHandler.context).getFragmentManager(),"");
                return;
            }
        }

        LogUtil.e("PROXY","====================2");

        PostRequest postRequest = retrofit.create(PostRequest.class);
        DeviceInfo deviceInfo = new DeviceInfo(resultHandler.context);
        // 参数
        BaseParams baseParams = new BaseParams<>(deviceInfo,params);
        // 构建请求
        Call<ResponseBody> call = postRequest.postMap(url, baseParams.getParams());
//        Call<ResponseBody> call = postRequest.postMap(url, GsonUtils.getInstance().hashMapToJson(baseParams.getParams()));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!resultHandler.checkLifeCycle()){
                    // context，activity或者fragment已销毁，不回调直接返回
                    Log.d("sendPostRequest", "onResponse, but context or fragment is not active");
                    return;
                }
                try {
                    ResponseBody body = response.body();
                    if (body == null) {
                        resultHandler.onServerError();
                        resultHandler.onFailure(new ApiException("", HttpStatusCode.RESPONSE_BODY_NULL));
                        return;
                    }
                    String string = body.string();
                    Log.e("GDSAFJDGH","========="+string);
                    BaseBean baseBean = GsonUtils.getInstance().fromJson(string, BaseBean.class);
                    if (baseBean.isResult()){
                        //基于ParameterizedType实现泛型类类型参数化
                        LogUtil.e("GDSAFJDGH","===="+baseBean.isResult());
                        try {

                            String str = baseBean.getData().toString();
                            String encode = new String(DataUtils.base64Decode(str));
                            String urlData = RSAEncrypt.decryptByPrivateKey(encode,Constants.private_key);
                            LogUtil.e("dddd", urlData+ "*urlData*");
                            String json = UrlParse.getURLDecoderString(urlData);
                            LogUtil.e("dddd", json+ "*json*");
                            T t = GsonUtils.getInstance().fromJson(json, dataType);
                            resultHandler.onSuccess(t);
                        } catch (Exception e){
                            String str = GsonUtils.getInstance().toJson(baseBean.getData());
                            T t = GsonUtils.getInstance().fromJson(str, dataType);
                            resultHandler.onSuccess(t);
                        }
                    } else {
                        ApiException apiException = new ApiException(baseBean.getMsg(), HttpStatusCode.RESPONSE_FAIL);
                        resultHandler.onFailure(apiException);
                        LogUtil.e("baseBean is false",baseBean.getMsg()+"=================================");
                    }
                    resultHandler.onResponse(baseBean);

                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.e("1111111","2555555555555555555" + e.getMessage());
                    resultHandler.onFailure(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(!resultHandler.checkLifeCycle()){
                    // context，activity或者fragment已销毁，不回调直接返回
                    LogUtil.d("sendPostRequest", "onFailure, but context or fragment is not active");
                    return;
                }

                if (t instanceof SocketTimeoutException || t instanceof ConnectException || t instanceof UnknownHostException) {
                    LogUtil.d("sendPostRequest", "onFailure, but context or fragment is not active11");
                    // 连接异常
                    if (resultHandler.isNetDisconnected()) {
                        // 服务器连接出错(连接请求失败（超时）)
                        LogUtil.d("sendPostRequest", "onFailure, but context or fragment is not active22");
                        resultHandler.onFailure(new ApiException("", HttpStatusCode.CONNECT_FAIL));
                    } else {
                        // 手机网络不通
                        LogUtil.d("sendPostRequest", "onFailure, but context or fragment is not active33");
                        resultHandler.onFailure(new ApiException("", HttpStatusCode.NO_NET_WORK));
                    }
                }else {
                    // 其它异常
                    LogUtil.d("sendPostRequest", "onFailure, but context or fragment is not active44");
                    resultHandler.onFailure(t);
                }
            }
        });
    }

    /**
     * 网络请求结果处理类
     * @param <T> 请求结果封装对象
     */
    public static abstract class ResultHandler<T> {
        Context context;
        Fragment fragment;
        android.support.v4.app.Fragment v4fragment;

        public ResultHandler(Context context) {
            this.context = context;
        }

        public ResultHandler(Fragment fragment) {
            this.fragment = fragment;
        }

        public ResultHandler( android.support.v4.app.Fragment fragment) {
            this.v4fragment = fragment;
        }

        /**
         * 判断网络是否未连接
         *
         * @return
         */
        public boolean isNetDisconnected() {
            return !NetworkUtils.isNetworkConnected(context);
        }


        /**
         * 请求成功时
         *
         * @param t 结果数据
         */
        public abstract void onSuccess(T t);

        public void onResponse(BaseBean baseBean){

        }

        /**
         * 服务器出错
         */
        public void onServerError() {
            // 服务器处理出错
            ToastUtils.showShort(context,"服务器处理请求失败，请稍后重试");
        }

        /**
         * 请求失败时的默认处理
         * 如不需要请不调用super.onFailure(t)即可
         * @param t
         */
        public void onFailure(Throwable t) {
            if (t instanceof ApiException) {
                ApiException e = (ApiException) t;
                if (e.getStatus() == HttpStatusCode.NO_NET_WORK){
                    // 手机网络不通
                    showConnectErrorToast("网络连接失败，请检查您的网络连接");
                }else if(e.getStatus() == HttpStatusCode.CONNECT_FAIL){
                    // 连接请求失败（超时）
                    showConnectErrorToast("网络连接失败，请检查您的网络连接");
                }else {
                    if(TextUtils.isEmpty(e.getMessage())){
                        LogUtil.e(TAG, "onFailure, e.getMessage() is null");
                        return;
                    }
                    ToastUtils.showShort(context,e.getMessage());
                }
            }else if (t instanceof Exception){
                // 其它未知异常
                //ToastUtils.showShort(R.string.net_unknown_error);
                t.printStackTrace();
            }
        }

        // 检测context和fragment的生命周期
        private boolean checkLifeCycle(){
            if(this.fragment != null ){
                return LifeCycleUtils.checkFragmentLifeCycle(fragment);
            }else if(this.v4fragment != null){
                return LifeCycleUtils.checkV4FragmentLifeCycle(v4fragment);
            }else {
                return LifeCycleUtils.checkContextLifeCycle(context);
            }
        }

        private void showConnectErrorToast(String msg){
            if(FastClickUtils.isFastClick()){
                // 防止连续显示多次
                return;
            }
            // 网络问题统一隐藏当前页面多个请求的loading
            /*if(context!=null && context instanceof QLActivity){
                QLActivity qlActivity = (QLActivity) context;
                qlActivity.hideLoadingDialog();
            }else if(v4fragment!=null && v4fragment instanceof BaseFragment){
                BaseFragment baseFragment = (BaseFragment) v4fragment;
                baseFragment.hideLoadingDialog();
            }*/
            ToastUtils.showShort(context,msg);
        }
    }
}
