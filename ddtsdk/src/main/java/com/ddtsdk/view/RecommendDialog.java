package com.ddtsdk.view;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ddtsdk.common.base.BaseParams;
import com.ddtsdk.common.network.request.HttpRequestClient;
import com.ddtsdk.constants.ApiConstants;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.manager.KLAppManager;
import com.ddtsdk.model.data.DeviceInfo;
import com.ddtsdk.model.protocol.bean.ExitMsg;
import com.ddtsdk.model.protocol.params.ExitParams;
import com.ddtsdk.network.ApiException;
import com.ddtsdk.network.HttpStatusCode;
import com.ddtsdk.platform.PlatformWebViewActivity;
import com.ddtsdk.ui.adapter.RecommendAdapter;
import com.ddtsdk.ui.view.DdtWebView;
import com.ddtsdk.utils.GsonUtils;
import com.ddtsdk.utils.ImageUtils;
import com.ddtsdk.utils.PayPointReport;
import com.tencent.smtt.sdk.WebView;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * 退出游戏功能对话框   2020/9/2
 */
public class RecommendDialog extends Dialog{
	private Context mContext;
	private View mView;
	private Button dialog_recommend_exit;
	private Button dialog_recommend_back;
	private GridView dialog_recommend_grid;
	public ImageView dialog_recommend_close;
	public TextView dialog_recommend_title;
	private LinearLayout recommend_ll;
	private com.ddtsdk.ui.view.CusImageView item_banner;
	public ExitMsg exitMsg = null;
	private String exit_url = "";
	private final int GET_DATA_SUCCESSS = 1;

	public Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case GET_DATA_SUCCESSS:
					handData();
					break;
			}
		}
	};

	public RecommendDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mView = LayoutInflater.from(context).inflate(
				AppConfig.resourceId(context, "kl_dialog_recommend", "layout"),
				null);
		setCanceledOnTouchOutside(false);
		getData();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(mView);
		recommend_ll = (LinearLayout) mView.findViewById(AppConfig.resourceId(mContext,"recommend_ll", "id"));
		if (mContext.getResources().getConfiguration().orientation == 2){
			LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)recommend_ll.getLayoutParams();
			layoutParams.width = getContext().getResources().getDisplayMetrics().heightPixels;
		}
		dialog_recommend_exit = (Button) mView.findViewById(AppConfig.resourceId(mContext,"dialog_recommend_exit", "id"));
		dialog_recommend_back = (Button)mView.findViewById(AppConfig.resourceId(mContext,"dialog_recommend_back", "id"));
		dialog_recommend_grid = (GridView) mView.findViewById(AppConfig.resourceId(mContext,"dialog_recommend_grid", "id"));
		item_banner = (com.ddtsdk.ui.view.CusImageView) mView.findViewById(AppConfig.resourceId(mContext,"item_banner", "id"));
		dialog_recommend_title = (TextView)mView.findViewById(AppConfig.resourceId(mContext,"dialog_recommend_title", "id"));
		dialog_recommend_close = (ImageView)mView.findViewById(AppConfig.resourceId(mContext,"dialog_recommend_close", "id"));
		dialog_recommend_exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(exit_url)){
					dismiss();
					((Activity)mContext).finish();
					KLAppManager.getInstance().exitApp();
				}
				else {
					PlatformWebViewActivity.startThisActivityWithNoFloat(getContext(),exit_url, true,0 );
					dismiss();
					((Activity)mContext).finish();
				}
			}
		});
		dialog_recommend_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				if (exitMsg.getModel() == 1){
					PayPointReport.getInstance().pushPoint(33);
				} else {
					PayPointReport.getInstance().pushPoint(36);
				}
			}
		});
		dialog_recommend_close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				if (exitMsg.getModel() == 1){
					PayPointReport.getInstance().pushPoint(34);
				} else {
					PayPointReport.getInstance().pushPoint(37);
				}
			}
		});
		item_banner.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(exitMsg.getContent().get(0).getH5_url()) && TextUtils.isEmpty(exitMsg.getContent().get(0).getAndroid_url())){
					return;
				}
				if (!TextUtils.isEmpty(exitMsg.getContent().get(0).getH5_url())){
					PlatformWebViewActivity.startThisActivity(getContext(), exitMsg.getContent().get(0).getH5_url(), true,
							exitMsg.getContent().get(0).getScreen().equals("1") ? 1 : 0, true);
				}
				else {
					Intent viewIntent = new Intent("android.intent.action.VIEW",
							Uri.parse(exitMsg.getContent().get(0).getAndroid_url()));
					getContext().startActivity(viewIntent);
					((Activity)mContext).finish();
					dismiss();
				}
				PayPointReport.getInstance().pushPoint(35);
			}
		});

		handData();
//		getData();
	}

	public String appenUrl(String url){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(url)
				.append("?");
		HashMap<String, String> hashMap = new BaseParams<>(new DeviceInfo((Activity)mContext), null).getParams();
		if (hashMap != null){
			int index = 0;
			for (String key : hashMap.keySet()){
				stringBuilder.append(key)
						.append("=")
						.append(hashMap.get(key));
				if (index != hashMap.size() - 1){
					stringBuilder.append("&");
				}
				index ++;
			}
		}
		Log.e("eeeeeeeee", stringBuilder.toString());
		return stringBuilder.toString();
	}

	public void handData(){
		if (exitMsg == null){
			dismiss();
			return;
		}
		switch (exitMsg.getModel()){
			case 1:
				dialog_recommend_title.setText("游戏推荐");
				dialog_recommend_grid.setVisibility(View.VISIBLE);
				dialog_recommend_grid.setAdapter(new RecommendAdapter(getContext(), exitMsg.getContent()));
				break;
			case 2:
			case 3:
				item_banner.setVisibility(View.VISIBLE);
				ImageUtils.loadImage(item_banner, exitMsg.getContent().get(0).getImage_url());
				dialog_recommend_title.setText(exitMsg.getContent().get(0).getTitle());
				break;
		}

	}

	public void getData(){
		HttpRequestClient.sendPostRequest(ApiConstants.ACTION_EXIT, new ExitParams(), ExitMsg.class, new HttpRequestClient.ResultHandler<ExitMsg>(mContext) {
			@Override
			public void onSuccess(ExitMsg exitdata) {
				show();
				exitMsg = exitdata;
				exit_url = exitdata.getJump_url();
				handler.sendEmptyMessage(GET_DATA_SUCCESSS);
			}

			@Override
			public void onFailure(Throwable t) {
				dismiss();
				KLAppManager.getInstance().exitApp();
			}
		});
	}
}
