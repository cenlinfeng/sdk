package com.ddtsdk.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;


import com.ddtsdk.mylibrary.BuildConfig;
import com.ddtsdk.othersdk.manager.AbnormalAdManager;
import com.ddtsdk.othersdk.manager.AdManager;
import com.ddtsdk.othersdk.manager.paymanager.PayManager;
import com.ddtsdk.utils.PayPointReport;

import com.ddtsdk.view.UpdataDialog.UpdataListener;
import com.ddtsdk.constants.ApiConstants;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.listener.InitListener;
import com.ddtsdk.model.data.DeviceInfo;
import com.ddtsdk.model.protocol.bean.InitMsg;
import com.ddtsdk.model.protocol.bean.UpdateApp;
import com.ddtsdk.model.protocol.params.InitParams;
import com.ddtsdk.model.protocol.params.NoDataParams;
import com.ddtsdk.common.network.request.HttpRequestClient;
import com.ddtsdk.utils.CheckSimulator;
import com.ddtsdk.utils.FloatUtlis;
import com.ddtsdk.utils.LogUtil;
import com.ddtsdk.utils.Utils;
import com.ddtsdk.view.UpdataDialog;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class InitData {

    private InitListener listener;
    private Context context;
    private String ddt_ver_id;
    private String local_ver = "";
    private final static int UPDATE_SUCCESS = 32;
    private final static int UPDATE_FAILED = 33;
    private final static int CACHE_APK = 36;
    private final static int WAIT = 37;
    private final static int INIT_SUCCESS = 38;
    private final static int FLAG_PUSH = 103;

    private boolean isOpen = true; // 是否打开浮点
    private boolean isUpdate = false; // 是否在线更新
    private boolean allSend = true; // 浮点是开启，但是服务端关闭所有的选项
    private DeviceInfo deviceInfo;

    public static boolean clientIsTimeDisplay = false; // 时间影响端的显示,默认不显示
    public UpdataDialog data, coerce;

    public InitData(Context context, String ddt_ver_id, Boolean point,
                    InitListener listener) {
        this.context = context;
        this.ddt_ver_id = ddt_ver_id;
        this.listener = listener;
        this.isOpen = point;
        deviceInfo = new DeviceInfo(context);
        Init();
    }

    public void Init() {
        initHttp();
    }

    /**
     * 安装安全支付服务，安装assets文件夹下的apk
     *
     * @param context  上下文环境
     * @param fileName apk名称
     * @param path     安装路径
     * @return
     */
    public boolean retrieveApkFromAssets(Context context, String fileName,
                                         String path) {
        boolean bRet = false;
        try {
            File file = new File(path);
            InputStream is = context.getAssets().open(fileName);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);

            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }

            fos.close();
            is.close();

            bRet = true;

        } catch (IOException e) {
            // disDialog();
            Toast.makeText(context, "assets目录下缺失文件", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return bRet;
    }

    /**
     * http请求，初始化接口
     */
    public void initHttp() {
        AppConstants.isInit = true;
        DeviceInfo deviceInfo = new DeviceInfo(context);
        InitParams initParams = new InitParams(deviceInfo);
        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_INIT, initParams, InitMsg.class, new HttpRequestClient.ResultHandler<InitMsg>(context) {
            @Override
            public void onSuccess(InitMsg initMsg) {
                if (!initMsg.getBannlogging()) {
                    CheckSimulator.showExitDialog(context);
                    return;
                }

                AppConstants.isInit = false;
                AppConstants.init_type = initMsg.getControl_type();
                //广告sdk先初始化
                AbnormalAdManager.abAdInit();
//                AdManager.getInstance().init(AppConfig.mApplication);
//                if (!AppConstants.init_type.equals("decrement") && AppConfig.adType == 1){
//                    AppConstants.adopen = true;
//                    AppConfig.mApplication.setInit(true);
//                    GdtAdUtils.getInstance().cApp(AppConfig.mApplication);
//                }
                AppConstants.pack_model = initMsg.getPack_model();
                if (AppConstants.pack_model == 1) {
                    AppConstants.colorPrimary = "#ee483d";
                }
                AppConfig.simulatorCanLogin = initMsg.getBannlogging();
                AppConstants.platform_pay = initMsg.getPlatform_cash();

                AppConstants.init_is_adv = initMsg.getAdv_info().getIs_adv();
                AppConstants.init_adv_url = initMsg.getAdv_info().getAdv_url();
                AppConstants.init_adv_img = initMsg.getAdv_info().getAdv_img();

                AppConstants.small_game_url = initMsg.getMini_game_url();
                AppConstants.isOpen_smallgame = initMsg.getShow_time();
                AppConstants.banpackage = initMsg.getBanpackage();
                AppConstants.is_test = initMsg.getIs_test();
                AppConstants.banres = initMsg.isBanreg();


                PayPointReport.getInstance().cache();
                sendData(INIT_SUCCESS, initMsg, handler);
            }

            @Override
            public void onFailure(Throwable t) {
                if (t instanceof ApiException && ((ApiException) t).getStatus() == HttpStatusCode.RESPONSE_FAIL) {
                    sendData(AppConfig.FLAG_FAIL, t.getMessage(),
                            handler);
                } else {
                    sendData(AppConfig.FLAG_FAIL, "网络连接失败，请检查您的网络连接",
                            handler);
                }
            }
        });
    }

    /**
     * 更新接口
     */
    public void updateVersion() {
        Properties properties = new Properties();
        try {

            local_ver = Utils.getVersion(context);
            String agent = AppConstants.ddt_ver_id;
            if ("".equals(ddt_ver_id)) {
                updateHttp(agent);
            } else {
                updateHttp(ddt_ver_id);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * 更新http请求
     */
    public void updateHttp(String ver) {
        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_UPDATE,
                new NoDataParams(), UpdateApp.class,
                new HttpRequestClient.ResultHandler<UpdateApp>(context) {
                    @Override
                    public void onSuccess(UpdateApp updateApp) {
                        sendData(UPDATE_SUCCESS, updateApp, handler);
                    }
                });
    }

    /**
     * 接口返回数据处理
     */
    public void sendData(int num, Object data, Handler callback) {
        Message msg = callback.obtainMessage();
        msg.what = num;
        msg.obj = data;
        msg.sendToTarget();
    }

    /**
     * 把assets下的apk写入到cache里面
     */
    public void toCache() {
        try {
            File cacheDir = context.getCacheDir();
            final String cachePath = cacheDir.getAbsolutePath() + "/temp.apk";
            AppConfig.isApkCacheExit = retrieveApkFromAssets(context,
                    "HeepayService.apk", cachePath);
            handler.sendEmptyMessage(WAIT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_SUCCESS:
                    UpdateApp result = (UpdateApp) msg.obj;
                    contrastUpdate(result.getNewversion(), result.getUpdatetype(),
                            result.getVersionurl(), result.getUpdatecontent());
                    AppConstants.is_update = result.getNewversion();
                    break;
                case AppConfig.FLAG_REQUEST_ERROR:
                    // disDialog();
                    Toast.makeText(context, (String) msg.obj, Toast.LENGTH_SHORT)
                            .show();
                    break;
                case CACHE_APK:
                    toCache();
                    break;
                case WAIT:
                    if (AppConfig.isApkCacheExit) {
                        handler.sendEmptyMessage(FLAG_PUSH);
                    } else {
                        handler.sendEmptyMessage(WAIT);
                    }
                    break;
                case INIT_SUCCESS:
                    if (AppConfig.adSdkInitSuccess) {
                        PayManager.getInstance().cacheOrderid();
                    }
                    AdManager.getInstance().setUserUniqueID(context,
                            (TextUtils.isEmpty(deviceInfo.getImei()) ? AppConstants.Oaid
                                    : deviceInfo.getImei()));
                    initGG = false;
                    InitMsg init = (InitMsg) msg.obj;
                    setInit(init);
                    break;
                case AppConfig.FLAG_FAIL:
                    initGG = true; //联网GG
                    initRetry();
                    break;
                case FLAG_PUSH:
                    FloatUtlis.getInstance().initView(context);
                    updateVersion();// 更新逻辑还没实现先注销
                    break;
            }
        }
    };

    private boolean isfirstretry = true;
    private boolean initGG = true;

    private void initRetry() {
        if (!isfirstretry) return;
        LogUtil.e("initRetry: 初始化失败后,重试机制开启.");
        isfirstretry = false;
        retryThread.start();
    }

    private Thread retryThread = new Thread(new Runnable() {
        @Override
        public void run() {
            int count = 0;
//			网络差,但是有网,也要重试.  test -- >pass
            if (Utils.isNetworkAvailable(context)) {
//				LogUtil.i("---有网(弱网)进来---" );
                while (initGG && count < 3) { //初始化GG 100
                    Init();//重新请求
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count++;

                }
                if (count > 2) {  //99
//					LogUtil.i("重试已超100次");
                    if (initGG) {
                        listener.fail("fail");
                    }
                }
            } else {
//				LogUtil.i("---断网进来---" );
                //断网情况的重试会进去.有网会退出,或者count满200退出.
                while (initGG && !Utils.isNetworkAvailable(context) && count < 3) {  //200  pass
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count++;
                }
                //不断网会进来
                //有网或者count超出最大值,跳出循环.
                if (Utils.isNetworkAvailable(context)) {
//					LogUtil.i("等到网络了,重试,\n" +
//							"并将 isfirstretry 赋值为true,在有网状态,若其失败,可以继续循环");
                    //不急,等500ms
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    isfirstretry = true;
                    Init();//重新请求
                }
                if (count > 2) { //3 pass
//					LogUtil.i("重试已超3次,等不到网络,再见");
                    if (initGG) {
//						LogUtil.i("回调给游戏  初始化GG");
                        listener.fail("fail");
                    }
                }
            }
        }
    });

    /**
     * 赋值初始化信息
     */
    public void setInit(InitMsg result) {

        try {
            AppConstants.qq = result.getQq();
            AppConstants.phone = result.getPhone();
            AppConstants.Token = result.getToken();
            AppConstants.Sessid = result.getSessid();
            AppConstants.agree = result.getAgree();
            AppConstants.logo_img = result.getLogo_img();
            AppConstants.float_img = result.getFloat_img();
            AppConstants.minutetime = result.getMinutetime();
            AppConstants.FPWD = result.getFpwd();
            AppConstants.Strategy_site = result.getStrategy_site();
            AppConstants.Sessid = result.getSessid();
            AppConstants.Token = result.getToken();
            AppConstants.wx_name = result.getWx_name();
            AppConstants.wx_qrcode = result.getWx_qrcode();
            AppConstants.customer_qq = result.getCustomer_qq();
            AppConstants.H5LoginLink = result.getH5LoginLink();
            AppConstants.openillustrate = result.getOpenillustrate();
            AppConstants.login_config = result.getLogin_config();
            AppConstants.sdk_track = result.getSdk_track();
            AppConstants.more_game = result.getMore_game();
            AppConstants.is_test = result.getIs_test();
            handler.sendEmptyMessage(FLAG_PUSH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 对比更新
     * @param newversion
     *            "newversion":true, 【是否有新版本，false的时候versionurl字段为空】
     * @param updatetype
     *            "updatetype":, 【更新类别，1强制，2选择】
     * @param url
     * @param updatecontent
     */

    public void contrastUpdate(Boolean newversion, String updatetype,
                               String url, String updatecontent) {
        //klSDK.assitiveTouch(context);
        if (newversion) {
            if (updatetype.equals("2")) {
                SelectUpdata(updatecontent, url);
                listener.Success("success");
            } else {
                CoerceUpdata(updatecontent, url);
                listener.Success("update");
            }
        } else {
            if (isOpen && allSend)
                listener.Success("success");
        }
        if (BuildConfig.DEBUG) {
            Toast.makeText(context, "初始化成功", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 调用浏览器下载
     */
    public void Downloadwebview(String url) {
        Intent viewIntent = new Intent("android.intent.action.VIEW",
                Uri.parse(url));
        context.startActivity(viewIntent);
    }

    public void SelectUpdata(String text, final String app_url) {
        data = new UpdataDialog(context, AppConfig.resourceId(context,
                "kl_MyDialog", "style"), text, false, new UpdataListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (v.getId() == AppConfig.resourceId(context, "button_updata",
                        "id")) {
                    Downloadwebview(app_url);
                    data.dismiss();
                } else if (v.getId() == AppConfig.resourceId(context,
                        "next_button_updata", "id")) {
                    AppConstants.is_update = false;
                    data.dismiss();
                    // callBack("close2");
                    // PaymentActivity.this.finish();
                }
            }
        });
        data.setCancelable(false);
        data.show();
    }

    /**
     * 强制更新 text 内容 app_url 下载地址
     */
    public void CoerceUpdata(String text, final String app_url) {
        coerce = new UpdataDialog(context, AppConfig.resourceId(context,
                "kl_MyDialog", "style"), text, true, new UpdataListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (v.getId() == AppConfig.resourceId(context, "button_updata",
                        "id")) {

                    Downloadwebview(app_url);
//                    coerce.dismiss();

                } else if (v.getId() == AppConfig.resourceId(context,
                        "next_button_updata", "id")) {
                    coerce.dismiss();

                }
            }
        });
        coerce.setCancelable(false);
        coerce.show();
    }
}
