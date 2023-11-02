package com.ddtsdk.ui.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ddtsdk.KLSDK;
import com.ddtsdk.common.base.BaseActivity;
import com.ddtsdk.common.base.BaseDialogFragment;
import com.ddtsdk.common.network.request.HttpRequestClient;
import com.ddtsdk.constants.ApiConstants;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.listener.ApiListenerInfo;
import com.ddtsdk.model.data.PaymentInfo;
import com.ddtsdk.model.net.KLApi;
import com.ddtsdk.model.protocol.bean.ExclusiveVolumeBean;
import com.ddtsdk.model.protocol.bean.PayConfig;
import com.ddtsdk.model.protocol.params.ExclusiveVolumeParams;
import com.ddtsdk.mylibrary.R;
import com.ddtsdk.network.InitData;
import com.ddtsdk.ui.dialog.KLCommonAffirmDialog;
import com.ddtsdk.ui.view.HorizontalListView;
import com.ddtsdk.utils.LogUtil;
import com.ddtsdk.utils.ToastUtils;
import com.ddtsdk.utils.Utils;

import org.w3c.dom.Text;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class KLExclusiveVolumeActivity extends BaseDialogFragment {

    private HorizontalListView lvExclusive;
    private ExclusiveVolume exclusiveVolume;
    private ImageView ivArrowLeft;
    private ImageView ivArrowRight;
    private TextView tvSpecialDate, tvExclusiveHint, tvExclusiveRule;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestExclusiveVolume(mContext);
    }

    @Override
    protected void initDialog(View view) {
        lvExclusive = view.findViewById(AppConfig.resourceId(mContext, "kl_lv_exclusive_item", "id"));
        ivArrowLeft = view.findViewById(AppConfig.resourceId(mContext, "kl_iv_arrow_left", "id"));
        ivArrowRight = view.findViewById(AppConfig.resourceId(mContext, "kl_iv_arrow_right", "id"));
        tvSpecialDate = view.findViewById(AppConfig.resourceId(mContext, "kl_tv_special_time", "id"));
        tvExclusiveHint = view.findViewById(AppConfig.resourceId(mContext, "kl_tv_exclusive_hint", "id"));
        tvExclusiveRule = view.findViewById(AppConfig.resourceId(mContext, "kl_tv_exclusive_rule", "id"));
        initListener();

    }

    //初始化监听
    private void initListener() {
        lvExclusive.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ExclusiveVolumeBean.voucherList data = AppConfig.exclusiveConfig.getVoucherList().get(position);
                if (data.getStatus().equals("1")) {
                    openBuyView(data);
                } else {
                    Toast.makeText(mContext, "次数已购买完，等待下次吧!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //初始化数据
    private void initData(List<ExclusiveVolumeBean.voucherList> data) {
        long startTime = Long.parseLong(data.get(0).getStart_time());
        long endTime = Long.parseLong(data.get(0).getEnd_time());
        tvSpecialDate.setText("活动时间:" + getDateToString(startTime) + "至" + getDateToString(endTime));
        exclusiveVolume = new ExclusiveVolume();
        lvExclusive.setAdapter(exclusiveVolume);
        tvExclusiveHint.setVisibility(View.GONE);
        if (data.get(0).getRule() != null) {
            tvExclusiveRule.setText(data.get(0).getRule());
        }
    }

    /**
     * http请求，获取到所有直充卷
     */
    public void requestExclusiveVolume(Context context) {
        ExclusiveVolumeParams params = new ExclusiveVolumeParams();
        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_EXCLUSIVE_VOLUME, params, ExclusiveVolumeBean.class, new HttpRequestClient.ResultHandler<ExclusiveVolumeBean>(context) {

            @Override
            public void onSuccess(ExclusiveVolumeBean ev) {
                AppConfig.exclusiveConfig = ev;
                if (ev.getVoucherList().size() > 3) {
                    ivArrowLeft.setVisibility(View.VISIBLE);
                    ivArrowRight.setVisibility(View.VISIBLE);
                } else {
                    ivArrowLeft.setVisibility(View.GONE);
                    ivArrowRight.setVisibility(View.GONE);
                }
                if (ev.getVoucherList().size() > 0) {
                    initData(ev.getVoucherList());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                super.onFailure(t);
            }
        });
    }

    private void openBuyView(ExclusiveVolumeBean.voucherList data) {
        PaymentInfo info = new PaymentInfo();
        info.setAppid(AppConstants.appId);
        info.setAppKey(AppConstants.appKey);
        info.setAgent(AppConstants.ddt_ver_id);
        info.setAmount(data.getPrice());
        info.setBillno(System.currentTimeMillis() + "");
        info.setExtrainfo(System.currentTimeMillis() + "");
        info.setSubject(data.getCoupon_name());
        info.setIstest("0");
        info.setRolename("0");
        info.setRolelevel("0");
        info.setRoleid(AppConstants.uid);
        info.setServerid("0");
        info.setGoods_id(data.getId());
        info.setUid("");// uid辨别用户是否只接入充值，如果只接入充值uid传"",相反传对方平台的用户名
//        Log.e("PAYLOG", "非平台支付");
        AppConfig.isExclusive = "true";
        KLPaymentActivity.startPaymentActivity((Activity) mContext, info);
    }


    public static String getDateToString(long time) {
        Date d = new Date(time * 1000);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault());
        return sf.format(d);
    }

    @Override
    protected String getLayoutId() {
        return Constants.kl_dialog_exclusive_volume;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }


    public class ExclusiveVolume extends BaseAdapter {

        @Override
        public int getCount() {
            return AppConfig.exclusiveConfig.getVoucherList().size();
        }

        @Override
        public Object getItem(int position) {
            return AppConfig.exclusiveConfig.getVoucherList().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            ExclusiveVolumeBean.voucherList data = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(AppConfig.resourceId(mContext, "kl_exclusive_volume_item", "layout"), null);
                holder.llExclusiveToPay = convertView.findViewById(AppConfig.resourceId(mContext, "kl_ll_to_pay_exclusive_layout", "id"));
                holder.tvExclusiveTitle = convertView.findViewById(AppConfig.resourceId(mContext, "kl_tv_exclusive_title", "id"));
                holder.tvExclusivePrice = convertView.findViewById(AppConfig.resourceId(mContext, "kl_tv_exclusive_price", "id"));
                holder.tvExclusiveNum = convertView.findViewById(AppConfig.resourceId(mContext, "kl_tv_exclusive_num", "id"));
                holder.tvExclusiveDiscounts = convertView.findViewById(AppConfig.resourceId(mContext, "kl_tv_exclusive_discounts", "id"));
                holder.llExclusiveBC = convertView.findViewById(AppConfig.resourceId(mContext, "kl_ll_exclusive_layout", "id"));
                //已购代金卷
                holder.tvExclusivePriceOff = convertView.findViewById(AppConfig.resourceId(mContext, "kl_tv_exclusive_price_off", "id"));
                holder.tvExclusiveNumOff = convertView.findViewById(AppConfig.resourceId(mContext, "kl_tv_exclusive_num_off", "id"));
                holder.tvExclusiveDiscountsOff = convertView.findViewById(AppConfig.resourceId(mContext, "kl_tv_exclusive_discounts_off", "id"));
                holder.llExclusiveBCOff = convertView.findViewById(AppConfig.resourceId(mContext, "kl_ll_exclusive_off_layout", "id"));
                data = AppConfig.exclusiveConfig.getVoucherList().get(position);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //解决视图拖动时为空的问题
            if (data == null) {
//                arrow(position);
                data = AppConfig.exclusiveConfig.getVoucherList().get(position);
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(AppConfig.resourceId(mContext, "kl_exclusive_volume_item", "layout"), null);
                holder.llExclusiveToPay = convertView.findViewById(AppConfig.resourceId(mContext, "kl_ll_to_pay_exclusive_layout", "id"));
                holder.tvExclusiveTitle = convertView.findViewById(AppConfig.resourceId(mContext, "kl_tv_exclusive_title", "id"));
                holder.tvExclusivePrice = convertView.findViewById(AppConfig.resourceId(mContext, "kl_tv_exclusive_price", "id"));
                holder.tvExclusiveNum = convertView.findViewById(AppConfig.resourceId(mContext, "kl_tv_exclusive_num", "id"));
                holder.tvExclusiveDiscounts = convertView.findViewById(AppConfig.resourceId(mContext, "kl_tv_exclusive_discounts", "id"));
                holder.llExclusiveBC = convertView.findViewById(AppConfig.resourceId(mContext, "kl_ll_exclusive_layout", "id"));
                //已购代金卷
                holder.tvExclusivePriceOff = convertView.findViewById(AppConfig.resourceId(mContext, "kl_tv_exclusive_price_off", "id"));
                holder.tvExclusiveNumOff = convertView.findViewById(AppConfig.resourceId(mContext, "kl_tv_exclusive_num_off", "id"));
                holder.tvExclusiveDiscountsOff = convertView.findViewById(AppConfig.resourceId(mContext, "kl_tv_exclusive_discounts_off", "id"));
                holder.llExclusiveBCOff = convertView.findViewById(AppConfig.resourceId(mContext, "kl_ll_exclusive_off_layout", "id"));
            }
            if (data.getStatus().equals("1")) {
                holder.llExclusiveBCOff.setVisibility(View.GONE);
                holder.tvExclusivePrice.setText("￥" + Utils.price2String(data.getPrice()));
                holder.tvExclusiveNum.setText(cost(data.getCost(), data.getCoupon_num()));
                holder.tvExclusiveDiscounts.setText(discounts(data.getCost(), data.getPrice()));
            } else {
                holder.llExclusiveBC.setVisibility(View.GONE);
                holder.tvExclusivePriceOff.setText("￥" + Utils.price2String(data.getPrice()));
                holder.tvExclusiveNumOff.setText(cost(data.getCost(), data.getCoupon_num()));
                holder.tvExclusiveDiscountsOff.setText(discounts(data.getCost(), data.getPrice()));
            }

            return convertView;
        }

        class ViewHolder {
            //正常代金卷
            LinearLayout llExclusiveToPay;
            LinearLayout llExclusiveBC;
            TextView tvExclusiveTitle;
            TextView tvExclusivePrice;
            TextView tvExclusiveNum;
            TextView tvExclusiveDiscounts;
            //已购完
            LinearLayout llExclusiveBCOff;
            TextView tvExclusivePriceOff;
            TextView tvExclusiveNumOff;
            TextView tvExclusiveDiscountsOff;
        }

        void arrow(int position) {
            if (position > 1) {
                showArrow("RIGHT");
            } else if (position < 1) {
                showArrow("LEFT");
            } else {
                showArrow("");
            }
        }

    }

    private String cost(String cost, String num) {
        cost = Utils.price2String(cost);
        int costs = Integer.parseInt(cost) / Integer.parseInt(num);
        String ret = "可领" + costs + "/" + num + "张";
        return ret;
    }

    private String discounts(String str, String str1) {
        str = Utils.price2String(str);
        str1 = Utils.price2String(str1);
        int discounts = Integer.parseInt(str) - Integer.parseInt(str1);
        String ret = "(立减" + discounts + "元)";
        return ret;
    }

    public void showArrow(String orientation) {
        if (orientation.equals("LEFT")) {
            ivArrowLeft.setVisibility(View.GONE);
            ivArrowRight.setVisibility(View.VISIBLE);
        } else if (orientation.equals("RIGHT")) {
            ivArrowRight.setVisibility(View.GONE);
            ivArrowLeft.setVisibility(View.VISIBLE);
        } else {
            ivArrowRight.setVisibility(View.GONE);
            ivArrowLeft.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        LogUtil.e("KLExclusiveVolumeActivity-onPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        LogUtil.e("KLExclusiveVolumeActivity-onResume");
        requestExclusiveVolume(mContext);
        super.onResume();
    }

}
