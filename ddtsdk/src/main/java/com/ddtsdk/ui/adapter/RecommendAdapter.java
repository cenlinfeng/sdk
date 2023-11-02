package com.ddtsdk.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.model.data.PayData;
import com.ddtsdk.model.protocol.bean.ExitMsg;
import com.ddtsdk.platform.PlatformWebViewActivity;
import com.ddtsdk.ui.view.OvalImageView;
import com.ddtsdk.utils.ImageUtils;
import com.ddtsdk.utils.PayPointReport;

import java.util.List;

/**
 * Created by c on 2020/9/1.
 */
public class RecommendAdapter extends BaseAdapter {
    private Context context;
    private List <ExitMsg.ExitData> list;
    public RecommendAdapter(Context context, List <ExitMsg.ExitData> list){
        this.context=context;
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
        Item item;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(AppConfig.resourceId(context, "kl_item_recommend", "layout"), null);
            item = new Item();
            item.item_icon = (OvalImageView) convertView.findViewById(AppConfig.resourceId(context, "item_icon", "id"));
            item.item_title = (TextView)convertView.findViewById(AppConfig.resourceId(context, "item_title", "id"));
            item.item_start = (Button)convertView.findViewById(AppConfig.resourceId(context, "item_start", "id"));
            convertView.setTag(item);
        }else{
            item = (Item) convertView.getTag();
        }
        item.setData(list.get(position));
        return convertView;
    }
    private class Item{
        private OvalImageView item_icon;
        private TextView item_title;
        private Button item_start;

        public void setData(final ExitMsg.ExitData exitData){
            ImageUtils.loadImage(item_icon, exitData.getImage_url());
            item_title.setText(exitData.getName());
            item_start.setText(TextUtils.isEmpty(exitData.getH5_url()) ? "下载" : "开始");
            item_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(exitData.getH5_url())){
                        PlatformWebViewActivity.startThisActivity(context, exitData.getH5_url(), true, exitData.getScreen().equals("1") ? 1 : 0, true);
                        PayPointReport.getInstance().pushPoint(31);
                    }
                    else if (!TextUtils.isEmpty(exitData.getAndroid_url())){
                        Intent viewIntent = new Intent("android.intent.action.VIEW",
                                Uri.parse(exitData.getAndroid_url()));
                        context.startActivity(viewIntent);
                        PayPointReport.getInstance().pushPoint(32);
                    }
                }
            });
        }
    }
}
