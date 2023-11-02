package com.ddtsdk.common.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ddtsdk.utils.ToastUtils;

/**
 * Created by CZG on 2020/4/17.
 */
public abstract class BaseFragment extends Fragment {

    protected boolean mIsInit = false;
    protected View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(resourceId(layoutName(),"layout"), null);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!mIsInit) {
            mIsInit = true;
            onViewInit(savedInstanceState, view);
        } else if (null != view) {
            view.requestLayout();
        }
    }

    protected abstract void onViewInit(Bundle savedInstanceState, View view);

    protected abstract String layoutName();

    public int resourceId(String name, String type) {
        return getResources().getIdentifier(name, type, getActivity().getPackageName());
    }

    protected <T extends View> T findViewById(int id){
        return mView.findViewById(id);
    }

    /**
     * toast 提示信息
     *
     * @param msg
     */
    public void showToastMsg(String msg) {
        ToastUtils.showShort(getActivity(),msg);
    }
}
