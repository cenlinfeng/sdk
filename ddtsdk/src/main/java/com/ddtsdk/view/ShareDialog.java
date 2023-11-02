package com.ddtsdk.view;


import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
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
import com.ddtsdk.common.network.request.HttpRequestClient;
import com.ddtsdk.constants.ApiConstants;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.model.protocol.bean.Share;
import com.ddtsdk.model.protocol.params.ShareParams;
import com.ddtsdk.utils.ToastUtils;

import java.util.HashMap;


/**
 * 浮点分享功能对话框   2020/10/13
 */
public class ShareDialog extends Dialog{
	private Context mContext;
	private View mView;
	private Button dl_btn_sahre_down;
	private ImageView dl_im_share_close;
	private LinearLayout share_ll;
	private final int GET_DATA_SUCCESSS = 1;
	private String gameUrl = "";
	private boolean isH5Game = true;

	public Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case GET_DATA_SUCCESSS:
					break;
			}
		}
	};

	public ShareDialog(Context context) {
		super(context, AppConfig.resourceId(context,
				"kl_MyDialog", "style"));
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mView = LayoutInflater.from(context).inflate(
				AppConfig.resourceId(context, "kl_dialog_share", "layout"),
				null);
		setCanceledOnTouchOutside(false);
		if (TextUtils.isEmpty(gameUrl)) getData();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(mView);
		share_ll = (LinearLayout) mView.findViewById(AppConfig.resourceId(mContext,"share_ll", "id"));
		LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)share_ll.getLayoutParams();
		if (mContext.getResources().getConfiguration().orientation == 2){
			layoutParams.width = getContext().getResources().getDisplayMetrics().heightPixels - 100;
		}
		dl_im_share_close = (ImageView) mView.findViewById(AppConfig.resourceId(mContext,"dl_im_share_close", "id"));
		dl_btn_sahre_down = (Button) mView.findViewById(AppConfig.resourceId(mContext,"dl_btn_sahre_down", "id"));
		dl_btn_sahre_down.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(gameUrl)){
					ToastUtils.showShort(getContext(),"复制失败");
				}
				else {
					ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
					clipboardManager.setPrimaryClip(ClipData.newPlainText(null, gameUrl));
					if (isH5Game) ToastUtils.showShort(getContext(), "复制成功，粘贴到浏览器即可体验游戏");
					else ToastUtils.showShort(getContext(), "复制成功，粘贴到浏览器即可下载游戏");
				}
			}
		});

		dl_im_share_close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	public void getData(){
		HttpRequestClient.sendPostRequest(ApiConstants.ACTION_ShARE, new ShareParams(), Share.class, new HttpRequestClient.ResultHandler<Share>(mContext) {
			@Override
			public void onSuccess(Share share) {
				gameUrl = share.getUrl();
				isH5Game = "0".equals(share.getIs_wake_up());
				show();
			}

			@Override
			public void onFailure(Throwable t) {
				dismiss();
			}
		});
	}
}
