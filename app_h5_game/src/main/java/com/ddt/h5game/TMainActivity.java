package com.ddt.h5game;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.ImageView;


import com.ddt.h5game.network.GetH5UrlFromService;
import com.ddt.h5game.network.UrlCallBack;
import com.ddtsdk.KLSDK;
import com.ddtsdk.listener.ApiListenerInfo;
import com.ddtsdk.listener.IDdtListener;
import com.ddtsdk.listener.IKLExitListener;
import com.ddtsdk.listener.InitListener;
import com.ddtsdk.model.data.DeviceInfo;
import com.ddtsdk.model.data.LoginMessageInfo;
import com.ddtsdk.model.data.PaymentInfo;
import com.ddtsdk.model.data.RoleData;
import com.ddtsdk.ui.view.LoadingDialog;
import com.ddtsdk.utils.GsonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import com.gr.xxsy22d.sdb.nearme.gamecenter.R;
import com.tencent.smtt.sdk.ValueCallback;

import static com.ddt.h5game.Constant.MHTAG;


public class TMainActivity extends Activity implements JSInterface.H5PayListener, H5JSInterface {

    private ImageView img;
    //TODO 记得改包名
    //安卓配置
    private static int appid = 7; //平台
    private static String appkey = "c62264f262e3938a5c3528cfb9d7e586";

    private String h5url = "";
    private FrameLayout web_fy;
    private H5WebFragment mH5WebFragment;
    private LoadingDialog mLoadingDialog;

    H5WebFragment.onWebCallBack onWebCallBack = new H5WebFragment.onWebCallBack() {
        @Override
        public void onPageFinished() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    img.setVisibility(View.GONE);
                    web_fy.setVisibility(View.VISIBLE);
                }
            }, 500);
            mLoadingDialog.dismiss();
        }

        @Override
        public void onReceivedError() {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        if (Build.VERSION.SDK_INT > 28){
//            WindowManager.LayoutParams lp = this.getWindow().getAttributes();
//            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
//            this.getWindow().setAttributes(lp);
//        }
//        KLSDK.getInstance().onCreate(this);
        initView();
//        initSDK();
    }

    private void initView() {
        mLoadingDialog = new LoadingDialog(this);
        mH5WebFragment = new H5WebFragment();
        mH5WebFragment.setOnWebCallBack(onWebCallBack);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.web_fy, mH5WebFragment);
        transaction.commit();
//        mWebview = (WebView) findViewById(R.id.webview);
        web_fy = findViewById(R.id.web_fy);
        img = (ImageView) findViewById(R.id.temp_img);
        img.setVisibility(View.GONE);
        //TODO 设置背景图
//        ImageUtil.getInstance().displayFromDrawable(R.drawable.login,img);
//        img.setImageResource(R.drawable.login);

//        KLSDK.getInstance().setUserListener(new UserApiListenerInfo() {
//            @Override
//            public void onLogout(Object obj) {
//                Log.e(MHTAG, "onLogout: 切换账号---");
//                //webview消失，显示背景图，重新拉起登录界面
//                if (web_fy != null) {
//                    web_fy.setVisibility(View.GONE);
//                }
//                img.setVisibility(View.VISIBLE);
//                doSDKLogin();
//            }
//        });
        mLoadingDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mH5WebFragment.setJsInterface(null, "");
                mH5WebFragment.loadUrl("https://sjbasdk.vipkunli.com/inner2/login.php");
            }
        }, 1500);
    }

    private void initSDK() {
        JSInterface.setH5PayListener(this);
        KLSDK.getInstance().initInterface(TMainActivity.this, appid, appkey,
                new InitListener() {
                    @Override
                    public void fail(String msg) {
                        // TODO Auto-generated method stub
                        Log.i(MHTAG, msg + ",请求失败,cp请注意");
                    }

                    @Override
                    public void Success(String msg) {
                        // TODO Auto-generated method stub
                        Log.i(MHTAG, "初始化成功,进行登录" + msg);
                        dosdkloginwait();
                    }
                });
    }

    private void dosdkloginwait() {
        TMainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doSDKLogin();
                    }
                }, 2000);
            }
        });
    }

    UrlCallBack mUrlBack = new UrlCallBack() {
        @Override
        public void getUrl(final String url, final int type) {
            TMainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!TextUtils.isEmpty(url)) {
                        if (type == 1) {
                            mH5WebFragment.setJsInterface(null, "");
                        } else {
                            mH5WebFragment.setJsInterface(null, "mhsdk");
                        }
                        loadUrl(url);
                        Log.i(MHTAG, "h5登录成功==========");
                    }
                }
            });
        }
    };

    private void doSDKLogin() {
        KLSDK.getInstance().login(TMainActivity.this, appid, appkey, new IDdtListener<LoginMessageInfo>() {
            @Override
            public void onSuccess(LoginMessageInfo data) {
                // TODO Auto-generated method stub
                Log.i(MHTAG, "---------登录的---------");
                if (data != null) {
                    String result = data.getResult();
                    String msg = data.getMsg();
                    String gametoken = data.getGametoken();
                    String time = data.getTime();
                    String uid = data.getUid();
                    String sessid = data.getSessid();
                    String dologin_h5 = data.getDologin_h5();

                    Log.d("mhLoginBack", "原生登录结果:" + result + "|msg" + msg
                            + "|gametoken" + gametoken + "" + "|time"
                            + time + "|uid" + uid + "|sessid" + sessid + "|dologin_h5" + dologin_h5);
                }
            }

            @Override
            public void onLoadH5Url(String dologin_h5, String h5url) {
                Log.e("dologin_h5", "dologin_h5=" + dologin_h5 + " h5url=" + h5url);
                if (!TextUtils.isEmpty(h5url)) {
                    mH5WebFragment.setJsInterface(TMainActivity.this, "android");
                    loadUrl(h5url);
                } else {
                    GetH5UrlFromService.getFinalUrl(dologin_h5, mUrlBack, TMainActivity.this);
                }
            }
        });
    }

    private void loadUrl(final String url) {
        TMainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //AndroidtoJS类对象映射到js的test对象
                if (mH5WebFragment != null) {
                    mLoadingDialog.show();
                    Log.e("mH5WebFragment", "is load");
                    h5url = url;
                    mH5WebFragment.loadUrl(url);
                } else {
                    Log.e("mH5WebFragment", "is null");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        KLSDK.getInstance().onActivityResult(TMainActivity.this, requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        KLSDK.getInstance().onDestroy(this);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        KLSDK.getInstance().onPause(this);
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        KLSDK.getInstance().onRestart(this);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        KLSDK.getInstance().onResume(this);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        KLSDK.getInstance().onStop(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        KLSDK.getInstance().onNewIntent(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            KLSDK.getInstance().exit(TMainActivity.this, new IKLExitListener() {

                @Override
                public void fail(String arg0) {
                }

                @Override
                public void exitSuccess(String arg0) {
                    TMainActivity.this.finish();
                    System.exit(0);
                }
            });

            return true;
        }
        KLSDK.getInstance().onKeyDown(keyCode, event);
        return super.onKeyDown(keyCode, event);
    }

    private void h5Pay(String json) {
        if (AppConfig.loginUrlType==0) {
                    H5PayBean.DataBean dataBean = null;
        try {
            dataBean = GsonUtils.getInstance().fromJson(json, H5PayBean.DataBean.class);
        } catch (JsonSyntaxException e) {
            Log.e(MHTAG, "h5Pay: 出现异常,json不对。json=" + json
                    + ",e=" + e.getCause() + "," + e.getMessage());
        }

        if (dataBean == null) {
            Log.e(MHTAG, "toPay:Data空，参数错误");
            return;
        }
        if (isEmpty(dataBean.getUserid() + "") || isEmpty(dataBean.getOrderid()) ||
                isEmpty(dataBean.getAmount() + "") || isEmpty(dataBean.getRolename()) ||
                isEmpty(dataBean.getServerid()) || isEmpty(dataBean.getExtrainfo())) {
            Log.e(MHTAG, "toPay:参数错误,dataBean=" + dataBean.toString());
            return;
        }

            PaymentInfo paymentInfo = new PaymentInfo();
            paymentInfo.setAppid(appid);
            paymentInfo.setAppKey(appkey);
            String agent = KLSDK.getInstance().getAgent(TMainActivity.this);
            paymentInfo.setAgent(agent);
            paymentInfo.setBillno(dataBean.getOrderid());
            paymentInfo.setExtrainfo(dataBean.getExtrainfo());//加密串
            paymentInfo.setSubject(dataBean.getSubject());
            paymentInfo.setIstest("0");//是否是test的参数
            paymentInfo.setRoleid("");//h5不传、20190130
            paymentInfo.setRolename(dataBean.getRolename());
            paymentInfo.setAmount(dataBean.getAmount() + "");
            paymentInfo.setServerid(dataBean.getServerid());
            paymentInfo.setUid(dataBean.getUserid() + "");

            KLSDK.getInstance().payment(TMainActivity.this, paymentInfo, new ApiListenerInfo() {
                @Override
                public void onSuccess(Object obj) {
                    // TODO Auto-generated method stub
                    super.onSuccess(obj);
                    Log.i(MHTAG, "---------充值的--------------");
                    if (obj != null) {
                        // LoginMessageInfo login=(LoginMessageInfo) obj;
                        Log.i(MHTAG, "充值界面关闭" + obj.toString());
                    }
                }
            });


        }
        if (AppConfig.loginUrlType==1){
            Gson gson = new Gson();
            H5PayBean bean = null;
            try {
                bean = gson.fromJson(json, H5PayBean.class);
            } catch (JsonSyntaxException e) {
                Log.e(MHTAG, "h5Pay: 出现异常,json不对。json=" + json
                        + ",e=" + e.getCause() + "," + e.getMessage());
            }

        /*if (bean == null) {
            Log.e(MHTAG, "toPay:bean空，参数错误");
            return;
        }*/

            if (bean == null) {
                Log.e(MHTAG, "toPay:Data空，参数错误");
                return;
            }
            H5PayBean.DataBean dataBean = bean.getData();

            if (isEmpty(dataBean.getUserid()) || isEmpty(dataBean.getOrderid()) ||
                    isEmpty(dataBean.getAmount() + "") || isEmpty(dataBean.getRolename()) ||
                    isEmpty(dataBean.getServerid()) || isEmpty(dataBean.getExtrainfo())) {
                Log.e(MHTAG, "toPay:参数错误,dataBean=" + dataBean.toString());
                return;
            }
//        if(!verifi(dataBean)){
////            return;
//        }
            PaymentInfo paymentInfo = new PaymentInfo();
            paymentInfo.setAppid(appid);
            paymentInfo.setAppKey(appkey);
            String agent = KLSDK.getInstance().getAgent(TMainActivity.this);
            paymentInfo.setAgent(agent);
            paymentInfo.setBillno(dataBean.getOrderid());
            paymentInfo.setExtrainfo(dataBean.getExtrainfo());//加密串
            paymentInfo.setSubject(dataBean.getSubject());
            paymentInfo.setIstest("0");//是否是test的参数
            paymentInfo.setRoleid("");//h5不传、20190130
            paymentInfo.setRolename(dataBean.getRolename());
            paymentInfo.setAmount(dataBean.getAmount() + "");
            paymentInfo.setServerid(dataBean.getServerid());
            paymentInfo.setUid(dataBean.getUserid());

            KLSDK.getInstance().payment(TMainActivity.this, paymentInfo, new ApiListenerInfo() {
                @Override
                public void onSuccess(Object obj) {
                    // TODO Auto-generated method stub
                    super.onSuccess(obj);
                    Log.i(MHTAG, "---------充值的--------------");
                    if (obj != null) {
                        // LoginMessageInfo login=(LoginMessageInfo) obj;
                        Log.i(MHTAG, "充值界面关闭" + obj.toString());
                    }
                }
            });
        }
    }

    private boolean isEmpty(String msg) {
        return TextUtils.isEmpty(msg);
    }

    @Override
    public void onPay(String json) {
        Log.d(MHTAG, "onPay: 监听器生效，触发支付");
        h5Pay(json);
    }

    @Override
    public void toSubmitInfo(String json) {
        try {
            RoleData roleData = GsonUtils.getInstance().fromJson(json, RoleData.class);
            if (roleData != null) {
                KLSDK.getInstance().setExtData(this, roleData);
            }
        } catch (Exception e) {
            Log.e("数据上报失败", "----json----" + json);
        }
    }

    @Override
    public void h5Init(String json) {
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        KLSDK.getInstance().onConfigurationChanged(newConfig);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        KLSDK.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @JavascriptInterface
    @Override
    public void submitRoleData(String json) {
        Log.e("H5JSInterface", "submitRoleData---json=" + json);
        try {
            RoleData roleData = GsonUtils.getInstance().fromJson(json, RoleData.class);
            if (roleData != null) {
                KLSDK.getInstance().setExtData(this, roleData);
            }
        } catch (Exception e) {
            Log.e("数据上报失败", "----json----" + json);
        }
    }

    @JavascriptInterface
    @Override
    public void payment(String json) {
        Log.d("H5JSInterface", "payment---json=" + json);
        try {
            PaymentInfo paymentInfo = GsonUtils.getInstance().fromJson(json, PaymentInfo.class);
            paymentInfo.setIstest("0");
            paymentInfo.setAppid(appid);
            paymentInfo.setAppKey(appkey);

            KLSDK.getInstance().payment(TMainActivity.this, paymentInfo, new ApiListenerInfo() {
                @Override
                public void onSuccess(Object obj) {
                    // TODO Auto-generated method stub
                    super.onSuccess(obj);
                    Log.i(MHTAG, "---------充值的--------------");
                    if (obj != null) {
                        // LoginMessageInfo login=(LoginMessageInfo) obj;
                        Log.i(MHTAG, "充值界面关闭" + obj.toString());
                    }
                }
            });
        } catch (JsonSyntaxException e) {
            Log.d(MHTAG, "payment: 出现异常,json不对。json=" + json
                    + ",e=" + e.getCause() + "," + e.getMessage());
        }
    }

    @JavascriptInterface
    @Override
    public void exit(String json) {
        Log.d("H5JSInterface", "exit---json=" + json);
        KLSDK.getInstance().exit(TMainActivity.this, new IKLExitListener() {

            @Override
            public void fail(String arg0) {
            }

            @Override
            public void exitSuccess(String arg0) {
                TMainActivity.this.finish();
                System.exit(0);
            }
        });
    }

    @JavascriptInterface
    @Override
    public void getInformation(String json) {
        Log.e("getInformation", "json=" + json);
        InformationBean informationBean = GsonUtils.getInstance().fromJson(json,InformationBean.class);
        if (TextUtils.equals(informationBean.getType(),"ddth5sdk")){
            DeviceInfo deviceInfo = new DeviceInfo(this);
            informationBean.setAction(deviceInfo.getUuid());
            final String backJson = GsonUtils.getInstance().toJson(informationBean);
            mH5WebFragment.getWebView().post(new Runnable() {
                @Override
                public void run() {
                    mH5WebFragment.getWebView().evaluateJavascript("window.DDTGameSDK.postMsg("+backJson+")", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {
                        }
                    });
                }
            });
        }
    }

    public void reFreshGame(){
        if (!TextUtils.isEmpty(h5url)){
            loadUrl(h5url);
        }
    }
}
