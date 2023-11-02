package com.ddtsdk.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;

import com.ddtsdk.common.network.request.HttpRequestClient;
import com.ddtsdk.common.network.result.BaseBean;
import com.ddtsdk.constants.ApiConstants;
import com.ddtsdk.model.protocol.params.NoDataParams;
import com.ddtsdk.model.protocol.params.WeChatParams;
import com.ddtsdk.utils.WeChatUtils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信授权结果回调Activity
 */
public class BaseWXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
    public static final String TAG = "WXEntryActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        //隐藏状态栏并获取wxapi
//        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //接收到分享以及登录的intent传递handleIntent方法，处理结果
        api = WXAPIFactory.createWXAPI(this, WeChatUtils.wc_appid,false);
        api.handleIntent(getIntent(),this);
    }


    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    //请求回调结果处理
    public void onResp(BaseResp baseResp) {
        //登录回调
        switch (baseResp.errCode){
            case BaseResp.ErrCode.ERR_OK:
                Log.e("WXEntryActivity", "ERR_OK");
                String code = ((SendAuth.Resp) baseResp).code;
                //获取accesstoken
                Log.e("WXEntryActivity", "code=" + code);
                getAccessToken(code);
                break;
            //用户拒绝授权
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Log.e("WXEntryActivity", "ERR_AUTH_DENIED");
                finish();
                break;
            //用户取消授权
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Log.e("WXEntryActivity", "ERR_USER_CANCEL");
                finish();
                break;
        }
    }

    private void getAccessToken(String code) {

//        NetReqCore.get().startWeChat(code, new ApiRequestListener() {
//            @Override
//            public void onSuccess(Object obj) {
//                final Msg msg = (Msg)obj;
//                if (msg != null && !msg.getResult()){
//                    BaseWXEntryActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(BaseWXEntryActivity.this, msg.getMsg(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//                finish();
//            }
//
//            @Override
//            public void onError(int statusCode) {
//                finish();
//            }
//        });

        WeChatParams weChatParams = new WeChatParams(code, WeChatUtils.wc_appid);
        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_WECHAT_CODE, weChatParams, NoDataParams.class, new HttpRequestClient.ResultHandler<NoDataParams>(this) {
            @Override
            public void onSuccess(NoDataParams initMsg) {
                finish();
            }

            @Override
            public void onResponse(BaseBean baseBean) {
                super.onResponse(baseBean);
                finish();
            }

            @Override
            public void onFailure(Throwable t) {
                finish();
            }
        });

        //新建一个progressDialog，避免长时间白屏（因为在进行多次网络请求）造成卡死的假象
//        createProgressDialog();
/**
 *        access_token:接口调用凭证
 *        appid：应用唯一标识，在微信开放平台提交应用审核通过后获得。
 *        secret：应用密钥AppSecret，在微信开放平台提交应用审核通过后获得。
 *        code：填写第一步获取的code参数。
 *        grant_type：填authorization_code。
 */
//        String url = "https://api.weixin.qq.com/sns/oauth2/access_token"
//                + "?appid=" + AppConfig.weChat_appId
//                + "&secret=" + AppConfig.weChat_appSecret
//                + "&code=" + code
//                + "&grant_type=authorization_code";
//        OkHttpClient okHttpClient = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url(url)
//                .get()
//                .build();
//        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e(TAG, "onFailure: Fail");
//                MHSDK.mApiLoginListener.onError(4040);
//                finish();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String responseInfo = response.body().string();
//                String access = null;
//                String openId = null;
//                ResLogin login = new ResLogin();
//                //用json去解析返回来的access和token值
//                try {
//                    JSONObject jsonObject = new JSONObject(responseInfo);
//                    if (jsonObject.has("errcode")){
//                        MHSDK.mApiLoginListener.onError(jsonObject.getInt("errcode"));
//                    }
//                    else {
//                        login.setAccessToken(jsonObject.getString("access_token"));
//                        login.setOpenId(jsonObject.getString("openid"));
//                        login.setExpiresIn(jsonObject.getString("expires_in")) ;
//                        login.setRefreshToken(jsonObject.getString("refresh_token"));
//                        login.setScope(jsonObject.getString("scope"));
//                        login.setUnionid(jsonObject.getString("unionid"));
//                        MHSDK.mApiLoginListener.onSuccess(login);
//                        UMSDKUtils.onUserSignIn(openId);
//                    }
//                    finish();
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
////                getUserInfo(access, openId);
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
