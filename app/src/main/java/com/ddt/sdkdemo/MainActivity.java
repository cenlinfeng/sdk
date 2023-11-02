package com.ddt.sdkdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.ddt.sbkcq2.dsyg21d.R;
import com.ddtsdk.KLSDK;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.model.data.RoleData;
import com.ddtsdk.utils.CheckSimulator;
import com.ddtsdk.listener.ApiListenerInfo;
import com.ddtsdk.listener.IDdtListener;
import com.ddtsdk.listener.IKLExitListener;
import com.ddtsdk.listener.InitListener;
import com.ddtsdk.listener.UserApiListenerInfo;
import com.ddtsdk.model.data.LoginMessageInfo;
import com.ddtsdk.model.data.PaymentInfo;
import com.ddtsdk.model.protocol.bean.ResCertificate;
import com.ddtsdk.utils.FloatUtlis;
import com.ddtsdk.utils.Utils;
import com.sczbl.baibaoqg.R;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.internal.Util;

public class MainActivity extends Activity implements View.OnClickListener{
    public final static String TAG = "ddtsdk";

    public Button mBtninit;
    public Button mBtnlogin;
    public Button mBtninfo;
    public Button mBtnpay;
    public Button mBtnperson;
    public Button mBtnexit;
    public Button mBtnlogout;
    public Button mBtnswa;
    public Button mBtnplatform;
    public TextView tv;
    private EditText amount;

    private int appid = 1;
    private String appkey = "f33835e09026f478888e28045e8ecb7b";

    Timer timer = new Timer();

    @Override
    protected void onStart() {
        super.onStart();
        KLSDK.getInstance().onStart(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        KLSDK.getInstance().onCreate(this);
        initView();
        KLSDK.getInstance().setUserListener(new UserApiListenerInfo() {
            @Override
            public void onLogout(Object obj) {
                // 登出的逻辑.
                String result = (String) obj;
                Toast.makeText(MainActivity.this, "登出类型" + result,
                        Toast.LENGTH_SHORT).show();
            }
        });
        Log.e("GSADJD", "public_key:"+Constants.public_key);
        Log.e("GSADJD", "private_key:"+Constants.private_key);
    }

    private void initView(){
        // TODO Auto-generated method stub
        mBtninit = (Button) findViewById(R.id.initbt);
        mBtnlogin = (Button) findViewById(R.id.loginbt);
        mBtninfo = (Button) findViewById(R.id.info);
        mBtnpay = (Button) findViewById(R.id.paybt);
        mBtnperson = (Button) findViewById(R.id.personbt);
        mBtnexit = (Button) findViewById(R.id.exitbt);
        mBtnlogout = (Button) findViewById(R.id.logoutbt);
        mBtnswa = (Button) findViewById(R.id.swabt);
        mBtnplatform = findViewById(R.id.platformbt);
        amount = (EditText) findViewById(R.id.amount);
        tv = (TextView) findViewById(R.id.tv);
//      tv.setText(CheckSimulator.whereAdd.toString());

        mBtninit.setOnClickListener(this);
        mBtnlogin.setOnClickListener(this);
        mBtninfo.setOnClickListener(this);
        mBtnpay.setOnClickListener(this);
        mBtnperson.setOnClickListener(this);
        mBtnexit.setOnClickListener(this);
        mBtnlogout.setOnClickListener(this);
        mBtnswa.setOnClickListener(this);
        mBtnplatform.setOnClickListener(this);
        amount.setOnClickListener(this);
    }

    public void workTask(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.this.onClick(mBtninit);
                    }
                });
                workTask();
            }
        },2000);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.initbt) {
            // 初始化
            KLSDK.getInstance().initInterface(MainActivity.this, appid, appkey,
                    new InitListener() {

                        @Override
                        public void fail(String msg) {
                            // TODO Auto-generated method stub
                            //
//                            Toast.makeText(MainActivity.this, "初始化失败！", Toast.LENGTH_SHORT).show();
                            Log.i(TAG,msg+",请求失败,cp请注意");
                        }


                        @Override
                        public void Success(String msg) {
                            // TODO Auto-generated method stub
                            Toast.makeText(MainActivity.this, "初始化成功！", Toast.LENGTH_SHORT).show();
                            Log.i(TAG,"初始化成功"+msg);
//                            KLSDK.login(MainActivity.this, appid, appkey, new ApiListenerInfo() {
//                                @Override
//                                public void onSuccess(Object obj) {
//                                    // TODO Auto-generated method stub
//                                    Log.i(TAG,"---------登录的---------");
//                                    if (obj != null) {
//                                        LoginMessageinfo data = (LoginMessageinfo) obj;
//                                        String result = data.getResult();
//                                        String msg = data.getMsg();
//                                        String gametoken = data.getGametoken();
//                                        String time = data.getTime();
//                                        String uid = data.getUid();
//                                        String sessid = data.getSessid();
//                                        Log.i(TAG, "登录结果:" + result + "|msg" + msg
//                                                + "|gametoken" + gametoken + "" + "|time"
//                                                + time + "|uid" + uid + "|sessid" + sessid);
//                                    }
//                                }
//                            });
                        }
                    });

        } else if (v.getId() == R.id.loginbt) {
            // 登录
            KLSDK.getInstance().login(MainActivity.this, appid, appkey, new IDdtListener<LoginMessageInfo>() {

                @Override
                public void onSuccess(LoginMessageInfo data) {
                    Log.i(TAG,"---------登录的---------");
                    if (data != null) {
                        String result = data.getResult();
                        String msg = data.getMsg();
                        String gametoken = data.getGametoken();
                        String time = data.getTime();
                        String uid = data.getUid();
                        String sessid = data.getSessid();
                        //如果支持切换正式服和测试服，请以此字段为准，0为正式服，1为测试服
                        int isTest = data.getIs_test();
//                        FloatUtlis.getInstance().setFloatView(MainActivity.this);
                        Log.i(TAG, "登录结果:" + result + "|msg" + msg
                                + "|gametoken" + gametoken + "" + "|time"
                                + time + "|uid" + uid + "|sessid" + sessid+"|isTest "+ isTest);
                    }
                }
            });

        } else if (v.getId() == R.id.info) {
            /**
             * 额外信息
             *
             * @param context
             *            上下文
             * @param scene_Id
             *            场景 分别为进入服务器(enterServer)、玩家创建用户角色(createRole)、玩家升级(levelUp)
             * @param roleId
             *            角色id
             * @param roleName
             *            角色名
             * @param roleLevel
             *            角色等级
             * @param zoneId
             *            当前登录的游戏区服id
             * @param zoneName
             *            当前游戏区服名称
             * @param balance
             *            游戏币余额
             * @param Vip
             *            当前用户vip等级
             * @param partyName
             *            当前用户所属帮派
             * @param roleCTime
             *            单位为秒，创建角色的时间
             * @param roleLevelMTime
             *            单位为秒，角色等级变化时间
             */
            RoleData roleData = new RoleData();
            roleData.setScene_Id("enterServer");
            roleData.setRoleid("11");
            roleData.setRolename("王者");
            roleData.setRolelevel("99");
            roleData.setZoneid("1");
            roleData.setZonename("1区");
            roleData.setBalance("80");
            roleData.setVip("8");
            roleData.setPartyname("逍遥");
            roleData.setRolectime("21322222");
            roleData.setRolelevelmtime("54456588");
            KLSDK.getInstance().setExtData(this,roleData);

        } else if (v.getId() == R.id.paybt) {
            PaymentInfo paymentInfo = new PaymentInfo();
            paymentInfo.setAppid(appid);
            paymentInfo.setAppKey(appkey);
            paymentInfo.setAgent("");  //渠道id
            String money = amount.getText().toString();
            paymentInfo.setAmount(TextUtils.isEmpty(money) ? "1" : money); //金额
            paymentInfo.setBillno(System.currentTimeMillis() + "");  //订单id
            paymentInfo.setExtrainfo(System.currentTimeMillis() + ""); //扩展信息
            paymentInfo.setProductId("1"); //商品id
            paymentInfo.setSubject("钻石"); //支付描述
            paymentInfo.setIstest("1");//是否是test的参数
            paymentInfo.setRoleid("1111");//角色id
            paymentInfo.setRolename("爱家居");//角色名
            paymentInfo.setRolelevel("100");//角色等级
            paymentInfo.setServerid("8888");  //区服id
            paymentInfo.setUid("");// uid辨别用户是否只接入充值，如果只接入充值uid传"",相反传对方平台的用户名
            KLSDK.getInstance().payment(this, paymentInfo, new ApiListenerInfo() {

                @Override
                public void onSuccess(Object obj) {
                    // TODO Auto-generated method stub
                    super.onSuccess(obj);
                    Log.i(TAG, "---------充值的--------------");
                    if (obj != null) {
                        // LoginMessageInfo login=(LoginMessageInfo) obj;
                        Log.i(TAG, "充值界面关闭" + obj.toString());
                    }

                }

            });

        } else if (v.getId() == R.id.personbt) {
            KLSDK.getInstance().getCertificateData(this, new IDdtListener<ResCertificate>() {
                @Override
                public void onSuccess(ResCertificate data) {
                    String result = data.isResult()+"";
                    String msg = data.getMsg();
                    String isautonym = data.getIsautonym()+"";
                    String isnonage = data.getIsnonage()+"";
                    String type = data.getType()+"";
                    Log.i(TAG, "实名信息：result=" + result
                            + ", msg=" + msg
                            + ", isautonym=" + isautonym
                            + ", isnonage=" + isnonage
                            + ", type=" + type);
                }
            });

        } else if (v.getId() == R.id.exitbt) {
            KLSDK.getInstance().exit(this, new IKLExitListener() {

                @Override
                public void fail(String msg) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void exitSuccess(String msg) {
                    // TODO Auto-generated method stub
                    System.exit(0);
                    MainActivity.this.finish();
                }
            });
        }else if(v.getId() == R.id.logoutbt){
            KLSDK.getInstance().switchAccount();
        }else if(v.getId() == R.id.swabt){
            KLSDK.getInstance().switchAccount();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        KLSDK.getInstance().onActivityResult(MainActivity.this, requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        KLSDK.getInstance().onDestroy(this);
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
            KLSDK.getInstance().exit(MainActivity.this, new IKLExitListener() {

                @Override
                public void fail(String arg0) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void exitSuccess(String arg0) {
                    // TODO Auto-generated method stub

                    MainActivity.this.finish();
                    System.exit(0);
                }
            });

            KLSDK.getInstance().switchAccount();
            return true;
        }
        KLSDK.getInstance().onKeyDown(keyCode,event);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        KLSDK.getInstance().onConfigurationChanged(newConfig);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        KLSDK.getInstance().onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    public void reFreshGame(){
        Log.e("reFreshGame", "====");
    }
}
