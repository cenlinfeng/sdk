package com.ddtsdk.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.utils.ImageUtils;

@SuppressLint("AppCompatCustomView")
public class KLCheckBoxView extends CheckBox {

    public KLCheckBoxView(Context context) {
        super(context);
        initView();
    }

    public KLCheckBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public KLCheckBoxView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }

    @Override
    public void setOnCheckedChangeListener(@Nullable final OnCheckedChangeListener listener) {
        OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                    setButtonDrawable(ImageUtils.tintDrawable(getContext(),"kl_icon_select", AppConstants.colorPrimary));
                } else {
                    setButtonDrawable(ImageUtils.tintDrawable(getContext(),"kl_icon_normal", AppConstants.colorPrimary));
                }
                listener.onCheckedChanged(buttonView,isChecked);
            }
        };
        super.setOnCheckedChangeListener(onCheckedChangeListener);
    }
}
