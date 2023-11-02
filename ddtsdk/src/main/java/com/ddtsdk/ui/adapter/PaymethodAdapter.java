package com.ddtsdk.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.model.data.PayData;
import com.ddtsdk.utils.ImageUtils;

import java.util.List;


/**
 * Created by  on 2015/12/20.
 */
public class PaymethodAdapter extends BaseAdapter {
    private Context context;
    private List <PayData>list;
    public PaymethodAdapter(Context context, List <PayData> list){
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
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(AppConfig.resourceId(context, "kl_payitem", "layout"), null);
            item = new Item();
            item.klpayimge = (ImageView) convertView.findViewById(AppConfig.resourceId(context, "klpayimage", "id"));
            item.klpayname = (TextView)convertView.findViewById(AppConfig.resourceId(context, "klpayname", "id"));
            convertView.setTag(item);
        }else{
            item=  (Item) convertView.getTag();
        }
        PayData payData = list.get(position);
        item.klpayname.setText(payData.getPayname());
//        setPayIcon(payData.getPayicon(), item.klpayimge);
        ImageUtils.loadImage(item.klpayimge, payData.getPayicon());
        return convertView;
    }
    class Item{
        private ImageView klpayimge;
        private TextView klpayname;
    }
}
