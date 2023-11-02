package com.ddtsdk.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddtsdk.model.protocol.bean.BuyPlatformBean;
import com.ddtsdk.mylibrary.R;
import com.ddtsdk.utils.LogUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * sea 2023年2月25日
 * gv的数据view适配器.list+1是为了最后一个显示自定义的金额
 */
public class BuyPlatformAdapter extends BaseAdapter {

    private Activity mContext;
    private List<BuyPlatformBean.MoneyConfig> mList;
    private PaymentOnClickListener mBuyPlatformAdapterOnClickListener;
    private int pos = -1;

    public BuyPlatformAdapter(Activity context, List<BuyPlatformBean.MoneyConfig> list) {
        this.mContext = context;
        this.mList = list;
    }

    public void setBuyPlatformAdapterOnClickListener(PaymentOnClickListener paymentOnClickListener) {
        this.mBuyPlatformAdapterOnClickListener = paymentOnClickListener;
    }

    public void setSelection(int position) {
        this.pos = position;
    }

    @Override
    public int getCount() {
        return mList.size() + 1;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final Item item;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.kl_item_buy_platform_amount, viewGroup, false);
            item = new Item();
            item.mAmount = view.findViewById(R.id.tv_item_pay_amount);
            item.mPlatform = view.findViewById(R.id.tv_item_pay_platform);
            item.mLLAmount = view.findViewById(R.id.ll_item_pay_amount);
            item.mLLMyselfAmount = view.findViewById(R.id.ll_pay_myself_amount);
            item.mRLItemPayAmount = view.findViewById(R.id.rl_item_pay_amount);
            item.mMyselfAmount = view.findViewById(R.id.et_item_myself_amount);
            item.mMyselfPlatform = view.findViewById(R.id.tv_item_myself_platform);
            item.mGiftCoin = view.findViewById(R.id.tv_item_pay_gift_coin);
            view.setTag(item);
        } else {
            item = (Item) view.getTag();
        }
        //填充数据
        if (mList.size() == i) {
            item.mLLAmount.setVisibility(View.GONE);
            item.mLLMyselfAmount.setVisibility(View.VISIBLE);
        } else {
            item.mAmount.setText(mList.get(i).getPay_money_name());
            item.mPlatform.setText(mList.get(i).getMoney_name());
            item.mGiftCoin.setText(mList.get(i).getGive_money_name());
            item.mLLAmount.setVisibility(View.VISIBLE);
            item.mLLMyselfAmount.setVisibility(View.GONE);
        }

        //改变选中的状态
        if (pos == i) {
            item.mRLItemPayAmount.setBackgroundResource(R.drawable.shape_pay_item_bg_checked_on);
            item.mAmount.setTextColor(mContext.getResources().getColor(R.color.color_e8aa49));
            //判断是否是其他金额
            if (mList.size() == i) {
                item.mMyselfAmount.setVisibility(View.VISIBLE);
                item.mMyselfAmount.setFocusable(false);
                showSoftInput(mContext, item.mMyselfAmount);
            }
        } else {
            item.mRLItemPayAmount.setBackgroundResource(R.drawable.shape_pay_item_bg_default);
            item.mAmount.setTextColor(mContext.getResources().getColor(R.color.klfont_black));
            item.mMyselfAmount.setVisibility(View.GONE);
            hideSoftInput(mContext);
        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBuyPlatformAdapterOnClickListener != null) {
                    if (mList.size() == i) {
                        String data = item.mMyselfAmount.getText().toString();
                        data = data.isEmpty() ? "0" : data;
                        mBuyPlatformAdapterOnClickListener.onItemClick(view, i, data, false);
                    } else {
                        mBuyPlatformAdapterOnClickListener.onItemClick(view, i, mList.get(i).getId(), true);
                    }
                }
            }
        });


        item.mMyselfAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence data, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence data, int i, int i1, int i2) {
                if (!data.toString().isEmpty()) {
                    mBuyPlatformAdapterOnClickListener.onChangeValue(data.toString());
                    item.mMyselfPlatform.setText((Integer.valueOf(data.toString()) * 100) + "平台币");
                } else {
                    item.mMyselfPlatform.setText("请输入金额");
                    mBuyPlatformAdapterOnClickListener.onChangeValue("0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;
    }


    class Item {
        TextView mAmount;
        TextView mPlatform;
        LinearLayout mLLAmount;
        LinearLayout mLLMyselfAmount;
        RelativeLayout mRLItemPayAmount;
        EditText mMyselfAmount;
        TextView mMyselfPlatform;
        TextView mGiftCoin;
    }

    public interface PaymentOnClickListener {
        void onItemClick(View view, int position, String data, boolean isMyself);

        void onChangeValue(String data);
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInput(final Activity activity, final EditText editText) {
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
                editText.requestFocus();
                editText.setCursorVisible(true);
                editText.setSelection(editText.getText().toString().length()); //移动光标到最后
            }
        }, 300);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 100);
    }

    /**
     * 隐藏输入法
     *
     * @param activity 当前页面
     */
    public static void hideSoftInput(Activity activity) {
        if (activity == null || activity.getCurrentFocus() == null) {
            return;
        }
        int times = 0;
        boolean isClosed = false;
        InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        while (!isClosed && times <= 5) {
            times++;
            isClosed = manager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }


}
