package com.ddtsdk.ui.dialog;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ddtsdk.common.base.BaseDialogFragment;
import com.ddtsdk.common.network.request.HttpRequestClient;
import com.ddtsdk.constants.ApiConstants;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.ui.KLViewControl;
import com.ddtsdk.ui.view.KLCommonTitleBar;
import com.ddtsdk.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

public class KLKeyCodeDialog extends BaseDialogFragment {

    private KLCommonTitleBar klTitlebar;
    private EditText edKeyCode;
    private Button btnKeyCodeSubmit;


    @Override
    protected void initDialog(View view) {
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        edKeyCode = view.findViewById(AppConfig.resourceId(mContext,"kl_ed_code_key","id"));
        btnKeyCodeSubmit = view.findViewById(AppConfig.resourceId(mContext,"kl_btn_code_key_submit","id"));
        klTitlebar = view.findViewById(AppConfig.resourceId(mContext,"kl_titlebar","id"));



       btnKeyCodeSubmit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String cdkey = edKeyCode.getText().toString().trim();
               if (cdkey.isEmpty()){
                   ToastUtils.showShort(mContext,"激活码不能为空！");
                   return;
               }

               submitCodeKey(cdkey);
               authenticateBottomSetting(false);
           }
       });

        klTitlebar.setLeftButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLViewControl.getInstance().back();
            }
        });

    }

    /**
     * 验证防抖重复提交
     */
    private void authenticateBottomSetting(boolean enable) {
        btnKeyCodeSubmit.setEnabled(enable);
        if (enable) {
            btnKeyCodeSubmit.setText("提交激活码");
        } else {
            btnKeyCodeSubmit.setText("提交中。。。");
        }
    }

    /**
     *  提交
     * @return
     */

    public void submitCodeKey(String cdkey){
        Map<String,Object> map = new HashMap<>();
        map.put("code",cdkey);
        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_CODE_KEY, map, Object.class, new HttpRequestClient.ResultHandler<Object>(mContext) {
            @Override
            public void onSuccess(Object o) {
                ToastUtils.showShort(mContext,"奖励已发送，请进入游戏使用吧！");
                authenticateBottomSetting(true);
            }

            @Override
            public void onFailure(Throwable t) {
                super.onFailure(t);
                authenticateBottomSetting(true);
            }
        });
    }

    @Override
    protected String getLayoutId() {
        return Constants.kl_dialog_key_code;
    }
}
