package com.ddtsdk.ui.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ddtsdk.common.base.BaseMvpFragment;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.model.protocol.bean.LoginMessage;
import com.ddtsdk.ui.activity.KLFirstQuickLoginActivity;
import com.ddtsdk.ui.contract.LoginContract;
import com.ddtsdk.ui.presenter.LoginPresenter;
import com.ddtsdk.ui.view.LoadingDialog;
import com.ddtsdk.utils.QuickLoginUtils;

/**
 * Created by CZG on 2021/1/28
 */
public class KLWYQuickLoginFragment extends BaseMvpFragment<LoginContract.View, LoginPresenter> {

    private Button kl_login;
    private ImageView kl_fra_logo;
    private LoadingDialog mLoadingDialog;

    @Override
    protected void initView() {
        kl_login = findViewById(resourceId("kl_login","id"));
        kl_fra_logo = findViewById(resourceId("kl_fra_logo","id"));
        if (AppConstants.pack_model == 1){
            kl_fra_logo.setImageDrawable(getResources().getDrawable(resourceId("ic_app_hb","drawable")));
        }
        mLoadingDialog = new LoadingDialog(getActivity());
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        kl_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuickLoginUtils.getInstance().prefetchNumber(getActivity());
                mLoadingDialog.show();
            }
        });
        QuickLoginUtils.getInstance().setQuickLoginListener(new QuickLoginUtils.QuickLoginListener() {
            @Override
            public void quickLoginSuccess(LoginMessage loginMessage) {
                ((KLFirstQuickLoginActivity)getActivity()).loginSuccess(loginMessage);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mLoadingDialog != null){
            mLoadingDialog.dismiss();
        }
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected String layoutName() {
        return Constants.fragment_wy_quick_login;
    }
}
