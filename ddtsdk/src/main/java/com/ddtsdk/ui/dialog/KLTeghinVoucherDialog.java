package com.ddtsdk.ui.dialog;

import android.app.FragmentManager;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ddtsdk.common.base.BaseDialogFragment;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.log.LogUtils;
import com.ddtsdk.model.data.NotReceive;
import com.ddtsdk.model.data.VoucherData;
import com.ddtsdk.model.protocol.bean.PayConfig;
import com.ddtsdk.ui.KLViewControl;
import com.ddtsdk.ui.activity.KLPaymentActivity;
import com.ddtsdk.utils.LogUtil;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class KLTeghinVoucherDialog extends BaseDialogFragment {

    private ListView mListViewOn;
    private ListView mListViewOff;
    private ListView mListViewUnreceived;
    private LinearLayout mLlTeghinVoucherOn;
    private LinearLayout mLlTeghinVoucherOff;
    private LinearLayout mLlTeghinVoucherUnreceived;
    private VoucherAdapter1 adapter;
    private VoucherAdapter2 adapter2;
    private VoucherAdapter3 adapter3;
    private List<VoucherData> list1;
    private List<VoucherData> list2;


    @Override
    protected void initDialog(View view) {

        mListViewOn = view.findViewById(resourceId("kl_lv_teghin_voucher_on", "id"));
        mListViewOff = view.findViewById(resourceId("kl_lv_teghin_voucher_off", "id"));
        mListViewUnreceived = view.findViewById(resourceId("kl_lv_teghin_voucher_unreceived", "id"));
        mLlTeghinVoucherOn = view.findViewById(resourceId("kl_ll_teghin_voucher_on", "id"));
        mLlTeghinVoucherOff = view.findViewById(resourceId("kl_ll_teghin_voucher_off", "id"));
        mLlTeghinVoucherUnreceived = view.findViewById(resourceId("kl_ll_teghin_voucher_unreceived", "id"));

        initView();

        mListViewOn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VoucherData voucherData = list1.get(position);
                AppConfig.coupon_id = voucherData.getId();
                LogUtil.e("test" + AppConfig.coupon_id);
                dismiss();
            }
        });

        setListViewHeight(mListViewOn);
        setListViewHeight(mListViewOff);
        setListViewHeight(mListViewUnreceived);
    }

    public void initView() {
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        for (int i = 0; i < AppConfig.pyyConfig.getVoucherList().size(); i++) {
            if (AppConfig.pyyConfig.getVoucherList().get(i).getState() == 1) {
                list1.add(AppConfig.pyyConfig.getVoucherList().get(i));
            } else if (AppConfig.pyyConfig.getVoucherList().get(i).getState() == 0) {
                list2.add(AppConfig.pyyConfig.getVoucherList().get(i));
            }
        }

        LogUtil.e("off:" + list2.size() + "on:" + list1.size());
        if (list1.size() > 0) {
            adapter = new VoucherAdapter1(list1);
            mListViewOn.setAdapter(adapter);
        } else {
            mLlTeghinVoucherOn.setVisibility(View.GONE);
        }

        if (list2.size() > 0) {
            adapter2 = new VoucherAdapter2(list2);
            mListViewOff.setAdapter(adapter2);
        } else {
            mLlTeghinVoucherOff.setVisibility(View.GONE);
        }

        if (AppConfig.pyyConfig.getNotReceive().size() > 0) {
            adapter3 = new VoucherAdapter3();
            mListViewUnreceived.setAdapter(adapter3);
        } else {
            mLlTeghinVoucherUnreceived.setVisibility(View.GONE);
        }

    }

    @Override
    protected String getLayoutId() {
        return Constants.kl_dialog_teghin_voucher;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    //适配满足条件的代金卷
    public class VoucherAdapter1 extends BaseAdapter {
        private List<VoucherData> list;

        public VoucherAdapter1(List<VoucherData> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(AppConfig.resourceId(mContext, "kl_item_pay_voucher", "layout"), null);
                holder.llOnPayVolume = convertView.findViewById(AppConfig.resourceId(mContext, "kl_ll_pay_volume_off", "id"));
                holder.tvOnName = (TextView) convertView.findViewById(AppConfig.resourceId(mContext, "kl_pay_voucher_on_tv", "id"));
                holder.tvOnExpiredTime = convertView.findViewById(AppConfig.resourceId(mContext, "kl_pay_expired_time_on_tv", "id"));
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            VoucherData voucherData = list.get(position);
            if (voucherData.getState() == 1) {
                holder.llOnPayVolume.setVisibility(View.GONE);
                String name = voucherData.getCoupon_name() + " * " + voucherData.getTotal() + "张";
                holder.tvOnName.setText(name);
                long expiredTime = Long.parseLong(voucherData.getExpired_time());
                holder.tvOnExpiredTime.setText("有效期至：" + getDateToString(expiredTime));
            }
            return convertView;
        }

        class ViewHolder {
            LinearLayout llOnPayVolume;
            TextView tvOnName;
            TextView tvOnExpiredTime;
        }
    }

    //适配没满足条件的代金卷
    public class VoucherAdapter2 extends BaseAdapter {
        private List<VoucherData> list;

        public VoucherAdapter2(List<VoucherData> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(AppConfig.resourceId(mContext, "kl_item_pay_voucher", "layout"), null);
                holder.llOffPayVolume = convertView.findViewById(AppConfig.resourceId(mContext, "kl_ll_pay_volume_on", "id"));
                holder.tvOffName = (TextView) convertView.findViewById(AppConfig.resourceId(mContext, "kl_pay_voucher_off_tv", "id"));
                holder.tvOffExpiredTime = convertView.findViewById(AppConfig.resourceId(mContext, "kl_pay_expired_time_off_tv", "id"));
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            VoucherData voucherData = list.get(position);
            if (voucherData.getState() == 0) {
                holder.llOffPayVolume.setVisibility(View.GONE);
                String name = voucherData.getCoupon_name() + " * " + voucherData.getTotal() + "张";
                holder.tvOffName.setText(name);
                long expiredTime = Long.parseLong(voucherData.getExpired_time());
                holder.tvOffExpiredTime.setText("有效期至：" + getDateToString(expiredTime));
            }
            return convertView;
        }

        class ViewHolder {
            LinearLayout llOffPayVolume;
            TextView tvOffName;
            TextView tvOffExpiredTime;
        }
    }

    private class VoucherAdapter3 extends BaseAdapter {

        @Override
        public int getCount() {
            return AppConfig.pyyConfig.getNotReceive().size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final VoucherAdapter3.ViewHolder holder;
            if (convertView == null) {
                holder = new VoucherAdapter3.ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(AppConfig.resourceId(mContext, "kl_item_pay_voucher", "layout"), null);
                holder.llOffPayVolume = convertView.findViewById(AppConfig.resourceId(mContext, "kl_ll_pay_volume_on", "id"));
                holder.tvOffName = (TextView) convertView.findViewById(AppConfig.resourceId(mContext, "kl_pay_voucher_off_tv", "id"));
                holder.tvOffExpiredTime = convertView.findViewById(AppConfig.resourceId(mContext, "kl_pay_expired_time_off_tv", "id"));
                convertView.setTag(holder);
            } else {
                holder = (VoucherAdapter3.ViewHolder) convertView.getTag();
            }
            NotReceive notReceive = AppConfig.pyyConfig.getNotReceive().get(position);

            holder.llOffPayVolume.setVisibility(View.GONE);
            String name = notReceive.getCoupon_name();
            holder.tvOffName.setText(name);
            long expiredTime = Long.parseLong(notReceive.getEnd_time());
            holder.tvOffExpiredTime.setText("活动结束时间：" + getDateToString(expiredTime));

            return convertView;
        }

        class ViewHolder {
            LinearLayout llOffPayVolume;
            TextView tvOffName;
            TextView tvOffExpiredTime;
        }
    }

    public static String getDateToString(long time) {
        Date d = new Date(time * 1000);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return sf.format(d);
    }


    /**
     * 重新计算ListView的高度，解决ScrollView和ListView两个View都有滚动的效果，在嵌套使用时起冲突的问题
     *
     * @param listView
     */
    public void setListViewHeight(ListView listView) {

        // 获取ListView对应的Adapter

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight();
            // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


}
