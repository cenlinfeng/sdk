package com.ddtsdk.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ddtsdk.common.base.BaseActivity;
import com.ddtsdk.common.base.BaseContract;
import com.ddtsdk.common.base.BaseMvpActivity;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.model.protocol.bean.BuyPlatformBean;
import com.ddtsdk.model.protocol.bean.PayMsg;
import com.ddtsdk.mylibrary.R;
import com.ddtsdk.ui.adapter.BuyPlatformAdapter;
import com.ddtsdk.ui.contract.BuyPlatformContract;
import com.ddtsdk.ui.presenter.BuyPlatformPresenter;
import com.ddtsdk.ui.view.KLButtonView;
import com.ddtsdk.utils.LogUtil;
import com.ddtsdk.utils.ToastUtils;
import com.ddtsdk.view.ResultDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class KLPlatFormBuyActivity extends BaseMvpActivity<BuyPlatformContract.View,
        BuyPlatformPresenter> implements BuyPlatformContract.View, View.OnClickListener {

    private GridView mGvPlatformConfig;
    private RadioGroup mRgBuyPlatformType;
    private KLButtonView mBtnBuyPlatform;
    private ImageView mIvClose;

    private BuyPlatformAdapter mAdapter;
    private List<BuyPlatformBean.MoneyConfig> mList = new ArrayList<>();
    private Boolean ispay = true;// 控制支付页面的调用次数
    private ResultDialog nklresultdialog;
    private String payPlatformAmount = "";
    private String payPlatformOrderId = "";
    private final static String PAYWECHAT = "wechath5";
    private final static String PAYALIPAY = "alipaywap";
    private String isPayType = "wechath5";

    @Override
    protected String layoutName() {
        return Constants.kl_buy_platfrom;
    }

    private Map<String, String> addPrams() {
//        payPlatformAmount = payPlatformAmount.isEmpty() ? "null" : Integer.valueOf(payPlatformAmount) * 100;
        Map<String, String> map = new HashMap<>();
        map.put("amount", payPlatformAmount == "" ? "null" : String.valueOf(Integer.valueOf(payPlatformAmount) * 100));
        map.put("roleid", "1");
        map.put("paychar", isPayType);
        map.put("subject", "平台币");
        map.put("rolelevel", "1");
        map.put("rolename", "12");
        map.put("serverid", "1");
        map.put("paytarget", "1");
        map.put("istest", "0");
        map.put("money_id", payPlatformOrderId + "");
        return map;
    }

    @Override
    public void onClick(View v) {
        if (v == mIvClose) {
            finish();
            return;
        }

    }

    @Override
    protected void initView() {
        mGvPlatformConfig = findViewById(resourceId("kl_gv_pay_amount_list", "id"));
        mRgBuyPlatformType = findViewById(resourceId("kl_rg_buy_platform_type", "id"));
        mBtnBuyPlatform = findViewById(resourceId("kl_btn_buy_platform", "id"));
        mIvClose = findViewById(resourceId("kl_closeiv", "id"));
        mIvClose.setOnClickListener(this);
        mAdapter = new BuyPlatformAdapter(this, mList);
    }

    @Override
    protected void initListener() {

        mBtnBuyPlatform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payPlatformAmount.isEmpty() && payPlatformOrderId.isEmpty()) {
                    ToastUtils.showShort(KLPlatFormBuyActivity.this, "请选择支付额度");
                    return;
                }
                mPresenter.PlatformToPay(KLPlatFormBuyActivity.this, addPrams());
            }
        });

        mAdapter.setBuyPlatformAdapterOnClickListener(new BuyPlatformAdapter.PaymentOnClickListener() {
            @Override
            public void onItemClick(View view, int position, String data, boolean isMyself) {
                if (isMyself) {
                    payPlatformOrderId = data;
                } else {
                    payPlatformOrderId = "";
                    payPlatformAmount = data;
                }

                mAdapter.setSelection(position);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChangeValue(String data) {
                payPlatformOrderId = "";
                payPlatformAmount = data;
            }
        });

        mRgBuyPlatformType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) findViewById(group.getCheckedRadioButtonId());
                isPayType = TextUtils.equals(rb.getText().toString(), PAYWECHAT) ? PAYWECHAT : PAYALIPAY;

            }
        });
    }

    @Override
    protected void initData() {
        mPresenter.getPlatformConfig(this);
    }

    @Override
    protected BuyPlatformPresenter createPresenter() {
        return new BuyPlatformPresenter();
    }

    @Override
    public void SetPlatformConfig(BuyPlatformBean initConfig) {
        mList.clear();
        if (initConfig == null) {
            ToastUtils.showShort(this, "数据加载失败！");
            return;
        }
        mList.addAll(initConfig.getMoneyconfig());
        mGvPlatformConfig.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void platformToPaySuccess(PayMsg payMsg) {
        if (ispay) {
            Intent intent = new Intent();
            intent.putExtra("url", payMsg.getPayUrl());
            intent.putExtra("type", AppConfig.WEB_PAY_SUCCESS);
            intent.setClass(this, KLpayWebActivity.class);
            startActivityForResult(intent, AppConfig.WEB_PAY_SUCCESS);
            ispay = false;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        switch (requestCode) {
            case AppConfig.WEB_PAY_SUCCESS:
                ispay = true;
                showResult(data.getStringExtra("result"));
                break;

        }
    }

    /**
     * 显示结果, 通过ResultDialog的定时器关闭 ,符合"close"才会关闭,下个版本注意修改.这个有点不合理
     */
    public void showResult(final String text) {
        if (nklresultdialog == null) {
            nklresultdialog = new ResultDialog(this, getResources()
                    .getIdentifier("kl_MyDialog", "style", getPackageName()),
                    text, new ResultDialog.ResultListener() {

                @Override
                public void onClick(String v) {
                    LogUtil.e("callBack: show");
                    // TODO Auto-generated method stub
                    if (v.equals("close")) {
                        nklresultdialog.dismiss();
                        KLPlatFormBuyActivity.this.finish();
                    }

                }
            });
            nklresultdialog.setCancelable(false);
            nklresultdialog.show();
        }
    }
}
