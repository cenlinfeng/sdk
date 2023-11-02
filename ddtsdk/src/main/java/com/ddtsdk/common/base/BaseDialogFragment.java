package com.ddtsdk.common.base;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.ddtsdk.KLSDK;
import com.ddtsdk.ui.KLViewControl;
import com.ddtsdk.utils.LogUtil;
import com.ddtsdk.utils.Utils;

public abstract class BaseDialogFragment extends DialogFragment {
    protected Context mContext;
    private MyDialogFragment_Listener myDialogFragment_Listener;

    // 回调接口，用于传递数据给Activity -------
    public interface MyDialogFragment_Listener {
        void getDataFrom_DialogFragment();
    }

    public BaseDialogFragment() {
        super();
        KLViewControl.getInstance().addView(this);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
        //避免mainactivity需要实现这个接口
        if (!(activity == KLSDK.getInstance().getContext())) {
            try {
                myDialogFragment_Listener = (MyDialogFragment_Listener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must implementon MyDialogFragment_Listener");
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        WindowManager.LayoutParams attributes = getDialog().getWindow().getAttributes();
        attributes.width = Utils.dp2px(350);
        attributes.height = Utils.dp2px(300);
        getDialog().getWindow().setAttributes(attributes);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(resourceId(getLayoutId(), "layout"), container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (savedInstanceState == null) {
            initDialog(view);
        }
        return view;
    }


    protected abstract void initDialog(View view);

    protected abstract String getLayoutId();

    public int resourceId(String name, String type) {
        return getResources().getIdentifier(name, type, mContext.getPackageName());
    }

    @Override
    public void dismiss() {
        super.dismiss();
        KLViewControl.getInstance().removeView(this);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onDestroy() {
        if (myDialogFragment_Listener != null) {
            myDialogFragment_Listener.getDataFrom_DialogFragment();
        }
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
//        super.show(manager, tag);
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
