package com.ddtsdk.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.model.data.BaseUserData;
import com.ddtsdk.utils.ImageUtils;
import com.ddtsdk.utils.TimeUtil;

import java.util.List;

/**
 * Created by CZG on 2020/5/25.
 */
public class AccountHitoryAdapter extends BaseAdapter {

    private Context mContext;

    private List<BaseUserData> mUserDataList;

    private IAccountOnClickListener mIAccountOnClickListener;

    public AccountHitoryAdapter(Context context, List<BaseUserData> userDatas) {
        mContext = context;
        this.mUserDataList = userDatas;
    }

    public void setIAccountOnClickListener(IAccountOnClickListener iAccountOnClickListener) {
        this.mIAccountOnClickListener = iAccountOnClickListener;
    }

    @Override
    public int getCount() {
        return mUserDataList != null ? mUserDataList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(AppConfig.resourceId(mContext,Constants.item_account_list, "layout"), null);
            holder = new ViewHolder();
            holder.kl_account_tv = convertView.findViewById(AppConfig.resourceId(mContext, "kl_account_tv", "id"));
            holder.kl_time_tv = convertView.findViewById(AppConfig.resourceId(mContext, "kl_time_tv", "id"));
            holder.kl_open_iv = convertView.findViewById(AppConfig.resourceId(mContext, "kl_open_iv", "id"));
            holder.kl_often_tv = convertView.findViewById(AppConfig.resourceId(mContext, "kl_often_tv", "id"));
            holder.kl_head_iv = convertView.findViewById(AppConfig.resourceId(mContext, "kl_head_iv", "id"));
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.kl_head_iv.setImageDrawable(ImageUtils.tintDrawable(mContext,"kl_icon_user_header", AppConstants.colorPrimary));
        final BaseUserData userData = getData(position);
        if (userData != null){
            if (!TextUtils.isEmpty(userData.getPhone()) && userData.getPhone().length() == 11){
                holder.kl_account_tv.setText(userData.getPhone());
            } else {
                holder.kl_account_tv.setText(userData.getUname());
            }
            holder.kl_time_tv.setText("上次登录 "+ TimeUtil.getDescriptionTimeFromTimestamp7(userData.getLastLoginTime()));
        }
        if (position == 0){
            holder.kl_open_iv.setVisibility(View.VISIBLE);
            holder.kl_often_tv.setVisibility(View.VISIBLE);
        }else {
            holder.kl_open_iv.setVisibility(View.GONE);
            holder.kl_often_tv.setVisibility(View.GONE);
        }
        if (getCount() == 1){
            holder.kl_open_iv.setImageDrawable(ImageUtils.tintDrawable(mContext,"kl_icon_open", AppConstants.colorPrimary));
        } else if (getCount() > 1){
            holder.kl_open_iv.setImageDrawable(ImageUtils.tintDrawable(mContext,"kl_icon_fold", AppConstants.colorPrimary));
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIAccountOnClickListener != null){
                    mIAccountOnClickListener.onItemClick(v,position,userData);
                }
            }
        });
        return convertView;
    }

    public BaseUserData getData(int position){
        return position < getCount() ? mUserDataList.get(position) : null;
    }

    public static class ViewHolder{
        TextView kl_account_tv;
        TextView kl_time_tv;
        TextView kl_often_tv;
        ImageView kl_open_iv;
        ImageView kl_head_iv;

    }

    public interface IAccountOnClickListener{
        void onItemClick(View view, int position, BaseUserData data);
    }
}
