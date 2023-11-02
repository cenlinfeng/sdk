package com.ddtsdk.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.ddtsdk.BaseKLSDK;
import com.ddtsdk.common.base.BaseDialogFragment;
import com.ddtsdk.common.base.BaseMvpActivity;
import com.ddtsdk.model.data.VoucherData;
import com.ddtsdk.mylibrary.R;
import com.ddtsdk.othersdk.manager.AdManager;
import com.ddtsdk.othersdk.manager.EventFlag;
import com.ddtsdk.othersdk.manager.paymanager.PayManager;
import com.ddtsdk.ui.KLViewControl;
import com.ddtsdk.utils.Utils;
import com.ddtsdk.view.ResultDialog.ResultListener;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.model.data.PayData;
import com.ddtsdk.model.data.PaymentInfo;
import com.ddtsdk.model.protocol.bean.PayConfig;
import com.ddtsdk.model.protocol.bean.PayMsg;
import com.ddtsdk.ui.adapter.PaymethodAdapter;
import com.ddtsdk.ui.contract.PaymentContract;
import com.ddtsdk.ui.presenter.PaymentPresenter;
import com.ddtsdk.utils.GetAppInfoUtil;
import com.ddtsdk.utils.LogUtil;
import com.ddtsdk.view.ResultDialog;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CZG on 2020/4/21.
 * 支付
 */
public class KLPaymentActivity extends BaseMvpActivity<PaymentContract.View, PaymentPresenter> implements PaymentContract.View, BaseDialogFragment.MyDialogFragment_Listener {
    private RelativeLayout mkPayView;
    private ImageView mklback;
    private TextView mklusername;
    private TextView mklprodutename;
    private TextView mklamount;
    private TextView qq_url;
    private LinearLayout mKlVolumeLL;
    //private TextView mklmoeny;
    //private TextView mklpingtai;
    private TextView mklqq;
    private TextView mJihpone;
    private GridView mklcardgrid;
    private TextView mTeghinVolume;
    private TextView mPlatform;
    public String agent = "", serverId = "", billNo = "",
            amount = "", extraInfo = "", subject = "", uid = "", isTest = "",
            oritation = "", pt = "", gudinjine = "", goods_id = "", platform = "0";
    private String rolename;// 角色名
    private String level;// 角色等级
    private String gameuid;// 游戏用户id
    private String roleid;// 角色id

    public PaymentInfo payInfo;
    private PaymethodAdapter mklPaymethodAdapter;
    private List<PayData> mklpaydataList;
    private ResultDialog nklresultdialog;
    private Boolean ispay = true;// 控制支付页面的调用次数
    private ImageView mIclose;
    private String serverOrderId = "";  //服务端返回的订单id
    private List<VoucherData> mKLVoucherDataList;
    private int count;


    public static void startPaymentActivity(final Activity activity, final PaymentInfo payInfo) {
        Intent intent = new Intent(activity, KLPaymentActivity.class);
        Bundle bundle = new Bundle();
        Log.i("TAG", "startPaymentActivity: " + activity);
        bundle.putParcelable("kl_pay_info", payInfo);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Override
    protected String layoutName() {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LogUtil.i("landscape");
            AppConfig.ORIENTATION_Land = true;
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            LogUtil.i("portrait");
            AppConfig.ORIENTATION_Land = false;
        }
        if (AppConfig.ORIENTATION_Land) {
            return Constants.pay;
        } else {
            return Constants.pay2;
        }
    }

    @Override
    protected void initView() {
//        mkPayView = findViewById(resourceId("kl_main_view", "id"));
        mklback = findViewById(resourceId("kl_backiv", "id"));
        mklusername = findViewById(resourceId("kl_usernametv", "id"));
        mklprodutename = findViewById(resourceId("kl_produtenametv", "id"));
        mklamount = findViewById(resourceId("kl_amouttv", "id"));
        qq_url = findViewById(resourceId("qq_url", "id"));
        mklqq = findViewById(resourceId("kl_qqtv", "id"));
        mJihpone = findViewById(resourceId("kl_iphoentv", "id"));
        mklcardgrid = findViewById(resourceId("kl_cardgrid", "id"));
        mIclose = findViewById(resourceId("kl_closeiv", "id"));
        mTeghinVolume = findViewById(resourceId("kl_teghin_voucher", "id"));
        mKlVolumeLL = findViewById(resourceId("kl_ll_pay_volume_layout", "id"));
        mPlatform = findViewById(resourceId("kl_platform", "id"));

//        mkPayView.setVisibility(View.GONE);//默认隐藏

        if (!TextUtils.isEmpty(subject))
            mklprodutename.setText(subject);
        if (!TextUtils.isEmpty(amount))
            mklamount.setText(amount);
        if (!TextUtils.isEmpty(AppConstants.customer_qq))
            qq_url.setText(AppConstants.customer_qq);
        if (!TextUtils.isEmpty(AppConfig.isExclusive))
            mKlVolumeLL.setVisibility(View.GONE);

        mklPaymethodAdapter = new PaymethodAdapter(this, mklpaydataList);
        mklcardgrid.setAdapter(mklPaymethodAdapter);

    }

    @Override
    protected void initListener() {
        qq_url.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkApkExist(KLPaymentActivity.this, "com.tencent.mobileqq")) {
                    final String qqNum = qq_url.getText().toString();
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + qqNum + "&version=1")));
                } else {
                    Toast.makeText(KLPaymentActivity.this, "未安装手机QQ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mklcardgrid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int i,
                                    long arg3) {
                LogUtil.e("选中：：：：" + AppConfig.coupon_id);
                final PayData paData = mklpaydataList.get(i);
//                String paychar = paData.getPaychar() + paData.getPayname();
                if (count > 0 && !AppConfig.coupon_id.equals("") && !paData.getPaytype().equals("platform")) {
                    toPay(paData);
                } else if (count == 0 && AppConfig.coupon_id.equals("")) {
                    toPay(paData);
                } else if (!paData.getPaytype().equals("platform")) {
                    //处理直充卷会弹出提示框的逻辑
                    if (TextUtils.isEmpty(AppConfig.isExclusive)) {
                        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(KLPaymentActivity.this);
                        normalDialog.setTitle("温馨提示");
                        normalDialog.setMessage("您还有代金卷未选择，请问您要使用代金卷么？");
                        normalDialog.setPositiveButton("去使用",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        KLViewControl.getInstance().showTeghinVolume(KLPaymentActivity.this);
                                    }
                                });
                        normalDialog.setNegativeButton("不使用", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                toPay(paData);
                            }
                        });
                        normalDialog.show();
                    } else {
                        toPay(paData);
                    }
                } else if (paData.getPaytype().equals("platform")) {
                    LogUtil.e(AppConfig.coupon_id);
                    if (!AppConfig.coupon_id.isEmpty()) {
                        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(KLPaymentActivity.this);
                        normalDialog.setTitle("温馨提示");
                        normalDialog.setMessage("代金券与平台币不可以联合使用，将恢复默认值!");
                        normalDialog.setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AppConfig.coupon_id = "";
                                        mklamount.setText(amount);
                                        toPay(paData);
                                    }
                                });
                        normalDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        normalDialog.show();
                    } else {
                        toPay(paData);
                    }
                }
            }
        });

        mklback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                callBack("close");
                finish();
            }
        });

        mIclose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                callBack("close");
                finish();
            }
        });

        mTeghinVolume.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                KLViewControl.getInstance().showTeghinVolume(KLPaymentActivity.this);
            }
        });


    }

    public void toPay(PayData paData) {
        if (paData.getPaychar().equals("platform")) {
            requestpayHttp("2", paData.getPaychar() + "");
        } else {
            requestpayHttp("5", paData.getPaychar() + "");
        }
    }


    @Override
    protected void initData() {
        if (getIntent() != null) {
            payInfo = getIntent().getParcelableExtra("kl_pay_info");
            agent = payInfo.getAgent();
            serverId = payInfo.getServerid();
            billNo = payInfo.getBillno();
            amount = payInfo.getAmount();
            extraInfo = payInfo.getExtrainfo();
            subject = payInfo.getSubject();
            uid = payInfo.getUid();
            goods_id = payInfo.getGoods_id() == null ? "" : payInfo.getGoods_id();
            this.pt = "0";
            if (TextUtils.isEmpty(uid)) {
                uid = AppConstants.uid;
            }
            isTest = payInfo.getIstest();

            rolename = payInfo.getRolename();
            level = payInfo.getRolelevel();
            LogUtil.e("商品id :::" + payInfo.getProductId());

            roleid = payInfo.getRoleid();// 角色id
            mklpaydataList = new ArrayList<PayData>();
        }
        requestHttp();
    }

    /**
     * http请求(每次进入游戏界面都需要获取配置接口，查看充值界面的配置信息)
     */
    private void requestHttp() {
        if (!TextUtils.isEmpty(uid)) {
            mPresenter.initRequest(this, amount);
        } else {
            showToastMsg("请先登录");
            finish();
        }
    }

    /**
     * 支付成功后，仅获取一次信息。
     */
    private void postAppNameList() {
        SharedPreferences sp = getSharedPreferences("kl_firstPay", Context.MODE_PRIVATE);
        Boolean isFirstPay = sp.getBoolean("firstPay", true);
        if (!isFirstPay) {
            LogUtil.d("非首次，略过");
            return;
        }
//		//将firstPay赋值为false，
        sp.edit().putBoolean("firstPay", false).apply();
        //提交信息
        GetAppInfoUtil.getInstance().startPostAppListToServer(getApplicationContext());
    }

    /**
     * @param paytarget 充值对象
     *                  : 直接充到游戏传5，充到平台币传1 ,平台币兑换游戏币传2 , 赠宝兑换游戏币传3
     * @param paychar   付标识
     *                  支付列表返回的支付标识
     */
    private void requestpayHttp(String paytarget, String paychar) {
        mPresenter.pay(this, billNo, amount, paytarget, paychar, serverId, extraInfo, subject,
                isTest, rolename, level, roleid, "", AppConfig.coupon_id, AppConfig.isExclusive, goods_id);
    }

    /**
     * 显示结果, 通过ResultDialog的定时器关闭 ,符合"close"才会关闭,下个版本注意修改.这个有点不合理
     */
    public void showResult(final String text) {
        if (nklresultdialog == null) {
            nklresultdialog = new ResultDialog(this, getResources()
                    .getIdentifier("kl_MyDialog", "style", getPackageName()),
                    text, new ResultListener() {

                @Override
                public void onClick(String v) {
                    LogUtil.e("callBack: show");
                    // TODO Auto-generated method stub
                    if (v.equals("close")) {
                        nklresultdialog.dismiss();
                        // if (!TextUtils.isEmpty(billNo) {
                        callBack("close");
                        KLPaymentActivity.this.finish();
                    }

                }
            });
            nklresultdialog.setCancelable(false);
            nklresultdialog.show();
        }
    }

    /**
     * 充值回调
     */
    public void callBack(String billo) {
        LogUtil.e("callBack: show");

        BaseKLSDK.getInstance().payCallback(billo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case AppConfig.WEB_PAY_SUCCESS:
                ispay = true;
//                adLogReport();
                showResult(data.getStringExtra("result"));
                if (!TextUtils.isEmpty(serverOrderId)) {
                    PayManager.getInstance().newestPay(serverOrderId, amount);
                }
                break;
            default:
                break;
            case RealNameActivity.REALNAME_REQUEST_CODE:
                String returnData = data.getStringExtra(RealNameActivity.RETURN_DATA);
                if (returnData != null && TextUtils.equals("success", returnData)) {
                    requestHttp();
                } else {
                    finish();
                }
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            callBack("close");
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        LogUtil.i("KLPaymentActivity onDestroy");
        AppConfig.isExclusive = "";
        super.onDestroy();
    }

    @Override
    protected PaymentPresenter createPresenter() {
        return new PaymentPresenter();
    }

    //检查app是否存在
    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    public void initSuccess(PayConfig payConfig) {
//        mkPayView.setVisibility(View.VISIBLE);//初始化成功再显示
        try {
            mklpaydataList = payConfig.getPaylist();
//            mKLVoucherDataList = payConfig.getVoucherList();
            AppConfig.pyyConfig = payConfig;


            mklPaymethodAdapter = new PaymethodAdapter(this, mklpaydataList);
            mklPaymethodAdapter.notifyDataSetChanged();
            mklcardgrid.setAdapter(mklPaymethodAdapter);

            mklqq.setText(AppConstants.qq);
            mJihpone.setText(AppConstants.phone);
            String name = AppConstants.userName;
            if (name == null || name.equals("")) {
                name = AppConstants.uid;
            }

            mklusername.setText(name + "");
            mklprodutename.setText(subject);
            //数据初始化显示代金卷可用数量
            if (getUsableVoucher(payConfig) != 0) {
                mTeghinVolume.setText(getUsableVoucher(payConfig) + "张可用");
                mTeghinVolume.setTextColor(Color.WHITE);
            } else {
                //处理融合打包时找不到资源
                mTeghinVolume.setBackgroundColor(Color.WHITE);
            }
            mPlatform.setText(String.valueOf(payConfig.getMoney()));

        } catch (Exception e) {
            LogUtil.vv("e.getMessage = " + e.getMessage());
        }
    }

    public int getUsableVoucher(PayConfig payConfig) {
        count = 0;
        for (int i = 0; i < payConfig.getVoucherList().size(); i++) {
            if (payConfig.getVoucherList().get(i).getState() == 1) {
                count += payConfig.getVoucherList().get(i).getTotal();
            }
        }
        return count;
    }

    @Override
    public void initFail(String msg) {
        /**
         * 在支付充值，一定要实名
         */
        if (AppConstants.isautonym == 1) {
            KLTipActivity.startThisActivity(this, msg);
//            Toast.makeText(KLPaymentActivity.this, msg, Toast.LENGTH_LONG).show();
            finish();
        } else {
//            showToastMsg(TextUtils.isEmpty(msg) ? "请先实名后再支付！" : msg);
            RealNameActivity.startThisActivity(this, 3);
            KLTipActivity.startThisActivity(this, msg);
        }
    }

    @Override
    public void showFailMsg(String msg) {
        showToastMsg(msg);
    }

    @Override
    public void paySuccess(PayMsg payMsg) {
        if (ispay) {
            serverOrderId = payMsg.getBillno();
            PayManager.getInstance().saveData(payMsg.getBillno(), amount);
//            Log.e("serverOrderId", "gg=" + serverOrderId);
            Intent intent = new Intent();
            intent.putExtra("url", payMsg.getPayUrl());
            intent.putExtra("type", AppConfig.WEB_PAY_SUCCESS);
            intent.setClass(this, KLpayWebActivity.class);
            startActivityForResult(intent, AppConfig.WEB_PAY_SUCCESS);
            ispay = false;

        }
    }


    @Override
    public void platformSuccess(String msg) {
        showResult(msg);
    }

    @Override
    public void payFail(String msg) {
        String amounts = mklamount.getText().toString();
        if (amounts.equals("0.00"))
            showResult("直充券充值完成，请查看游戏是否到账！");
        else
            showResult(msg);
    }

    /**
     * 通过回调来修改界面
     */
    @Override
    public void getDataFrom_DialogFragment() {
        initTeghinVolume();
    }

    private void initTeghinVolume() {
//        mklpaydataList = new ArrayList<>();
        if (AppConfig.coupon_id.equals(""))
            return;
        for (int i = 0; i < AppConfig.pyyConfig.getVoucherList().size(); i++) {
            if (AppConfig.pyyConfig.getVoucherList().get(i).getId() == AppConfig.coupon_id) {
                mTeghinVolume.setText(AppConfig.pyyConfig.getVoucherList().get(i).getCoupon_name());
                if (AppConfig.pyyConfig.getVoucherList().get(i).getType().equals("2")) {
                    mklamount.setText(Utils.multiply(amount, AppConfig.pyyConfig.getVoucherList().get(i).getDiscount_amount()));
                } else {
                    mklamount.setText(Utils.subtract(amount, AppConfig.pyyConfig.getVoucherList().get(i).getDiscount_amount()));
                }
            }
        }
    }

    @Override
    protected void onPause() {
        LogUtil.i("KLPaymentActivity onPause");
        AppConfig.isShow = true;
        super.onPause();
    }
}
