package com.ddtsdk.ui.activity;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ddtsdk.common.base.BaseMvpActivity;
import com.ddtsdk.common.network.result.BaseBean;
import com.ddtsdk.constants.Constants;
import com.ddtsdk.model.protocol.bean.PokerInitBean;
import com.ddtsdk.model.protocol.bean.PokerPlayBean;
import com.ddtsdk.mylibrary.R;
import com.ddtsdk.ui.contract.PokerContract;
import com.ddtsdk.ui.presenter.PokerPresenter;
import com.ddtsdk.ui.view.LuckPokerView;
import com.ddtsdk.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class KLPokerActivity extends BaseMvpActivity<PokerContract.View, PokerPresenter> implements PokerContract.View {

    private ImageView mIvPokerSpin;
    private LuckPokerView mLpvPokerView;
    private ImageButton mIbPokerSwitch;
    private TextView mTvPlatformCoin;
    private TextView mTvPlatformPresentCoin;
    private ImageView mIvPokerReduce;
    private ImageView mIvPokerAdd;
    private TextView mTvPokerJackpot;
    private ImageView mImPokerClose;
    private TextView mTvPokerRoyalFlush;
    private TextView mTvPokerStraightFlush;
    private TextView mTvPokerFour;
    private TextView mTvPokerFullHouse;
    private TextView mTvPokerFlush;
    private TextView mTvPokerStraight;
    private TextView mTvPokerThreeKind;
    private TextView mTvPokerTwoPair;
    private TextView mTvPokerOnePair;
    private TextView mTvPokerHigh;
    //切换代币模式1平台币0赠送币
    private int COIN_TYPE = 0;
    //奖池增加
    private final int JACKPOT_ADD = 1;
    //奖池减少
    private final int JACKPOT_Reduce = 2;
    //原始平台币
    private int PLATFORM_COIN = 0;
    //原始赠送币
    private int PLATFORM_PRESENT_COIN = 0;
    //最大奖池
    private final int MAX_JACKPOT_COIN = 10000;
    //最小奖池
    private final int MIN_JACKPOT_COIN = 0;
    //当前奖池
    private int CURRENT_JACKPOT_COIN = 0;
    //每次点击增加减少的步长
    private final int STEP_JACKPOT_COIN = 100;
    //当前view显示的平台币
    private int CURRENT_PLATFORM_COIN = 0;
    //当前view显示的赠送币
    private int CURRENT_PLATFORM_PRESENT_COIN = 0;
    private String showPlayResult;

    @Override
    protected void initView() {
        mIvPokerSpin = findViewById(resourceId("kl_ib_poker_spin", "id"));
        mLpvPokerView = findViewById(resourceId("kl_lpv_poker", "id"));
        mIbPokerSwitch = findViewById(resourceId("kl_btn_poker_switch", "id"));
        mTvPlatformCoin = findViewById(resourceId("kl_tv_poker_platform_coin", "id"));
        mTvPlatformPresentCoin = findViewById(resourceId("kl_tv_poker_present_coin", "id"));
        mIvPokerReduce = findViewById(resourceId("kl_iv_poker_reduce", "id"));
        mIvPokerAdd = findViewById(resourceId("kl_iv_poker_add", "id"));
        mTvPokerJackpot = findViewById(resourceId("kl_tv_poker_jackpot_coin", "id"));
        mImPokerClose = findViewById(resourceId("kl_iv_poker_close", "id"));
        mTvPokerRoyalFlush = findViewById(resourceId("kl_tv_poker_royal_flush", "id"));
        mTvPokerStraightFlush = findViewById(resourceId("kl_tv_poker_straight_flush", "id"));
        mTvPokerFour = findViewById(resourceId("kl_tv_poker_four", "id"));
        mTvPokerFullHouse = findViewById(resourceId("kl_tv_poker_full_house", "id"));
        mTvPokerFlush = findViewById(resourceId("kl_tv_poker_flush", "id"));
        mTvPokerStraight = findViewById(resourceId("kl_tv_poker_straight", "id"));
        mTvPokerThreeKind = findViewById(resourceId("kl_tv_poker_three_kind", "id"));
        mTvPokerTwoPair = findViewById(resourceId("kl_tv_poker_two_pair", "id"));
        mTvPokerOnePair = findViewById(resourceId("kl_tv_poker_one_pair", "id"));
        mTvPokerHigh = findViewById(resourceId("kl_tv_poker_high", "id"));

        if (mTvPokerJackpot != null)
            mTvPokerJackpot.setText(String.valueOf(CURRENT_JACKPOT_COIN));

        ChangeCoinType(COIN_TYPE);
    }

    private void updateUi() {
        CURRENT_PLATFORM_COIN = PLATFORM_COIN;
        CURRENT_PLATFORM_PRESENT_COIN = PLATFORM_PRESENT_COIN;
        CURRENT_JACKPOT_COIN = 0;
        mTvPlatformCoin.setText(getResources().getString(R.string.mks_tv_platform_coin,
                String.valueOf(CURRENT_PLATFORM_COIN)));
        mTvPlatformPresentCoin.setText(getResources().getString(R.string.mks_tv_platform_present_coin,
                String.valueOf(CURRENT_PLATFORM_PRESENT_COIN)));
        mTvPokerJackpot.setText(String.valueOf(CURRENT_JACKPOT_COIN));
    }

    @Override
    protected void initListener() {

        mIvPokerSpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CURRENT_JACKPOT_COIN == 0) {
                    ToastUtils.showShort(KLPokerActivity.this, "请下注！");
                    return;
                }
                initClick(true);
                mPresenter.toPlay(KLPokerActivity.this, String.valueOf(CURRENT_JACKPOT_COIN),
                        COIN_TYPE == 1 ? "money" : "give_money");
            }
        });

        mImPokerClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mIbPokerSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeCoinType(COIN_TYPE);
            }
        });

        mIvPokerReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jackpotType(JACKPOT_Reduce);
            }
        });

        mIvPokerAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                JackPotAdd();
            }
        });

        LuckPokerView.setOnStopEndListener(new LuckPokerView.onStopEndListener() {
            @Override
            public void onStopEnd() {
                initClick(false);
                updateUi();
                if (!showPlayResult.isEmpty())
                    ToastUtils.showShort(KLPokerActivity.this, showPlayResult);
//                mTvPokerJackpot.setText("0");
            }
        });
    }

    //游戏开始禁止其他按钮点击
    private void initClick(boolean isStart) {
        if (isStart) {
            mIvPokerSpin.setClickable(false);
            mIvPokerAdd.setClickable(false);
            mIvPokerReduce.setClickable(false);
            mIbPokerSwitch.setClickable(false);
            mIvPokerSpin.setBackgroundResource(R.mipmap.kl_poker_bet_off);
        } else {
            mIvPokerSpin.setClickable(true);
            mIvPokerAdd.setClickable(true);
            mIvPokerReduce.setClickable(true);
            mIbPokerSwitch.setClickable(true);
            mIvPokerSpin.setBackgroundResource(R.mipmap.kl_poker_bet_on);
        }
    }


    private void ChangeCoinType(int coinType) {
        switch (coinType) {
            case 0:
                mTvPlatformCoin.setBackgroundResource(R.drawable.kl_poker_coin_shape_bg);
                mTvPlatformPresentCoin.setBackgroundResource(R.color.transparent);
                updateUi();
                COIN_TYPE = 1;
                break;
            case 1:
                mTvPlatformCoin.setBackgroundResource(R.color.transparent);
                mTvPlatformPresentCoin.setBackgroundResource(R.drawable.kl_poker_coin_shape_bg);
                updateUi();
                COIN_TYPE = 0;
                break;
        }
    }


    private void jackpotType(int jackpotType) {
        switch (jackpotType) {
            case JACKPOT_ADD:
                JackPotAdd();
                break;
            case JACKPOT_Reduce:
                jackPotReduce();
                break;
        }
    }


    private void JackPotAdd() {
        if (CURRENT_JACKPOT_COIN == MAX_JACKPOT_COIN) {
            ToastUtils.showShort(this, "奖池最大为10000！");
            return;
        }

        if (COIN_TYPE == 1 && CURRENT_PLATFORM_COIN <= MIN_JACKPOT_COIN) {
            ToastUtils.showShort(this, "没有可下注的平台币！");
            return;
        }

        if (COIN_TYPE == 0 && CURRENT_PLATFORM_PRESENT_COIN <= MIN_JACKPOT_COIN) {
            ToastUtils.showShort(this, "没有可下注的赠送币！");
            return;
        }

        //根据选中代币状态来更新ui
        if (COIN_TYPE == 1) {
            CURRENT_PLATFORM_COIN = CURRENT_PLATFORM_COIN - STEP_JACKPOT_COIN;
            mTvPlatformCoin.setText(getResources().getString(R.string.mks_tv_platform_coin,
                    String.valueOf(CURRENT_PLATFORM_COIN)));
        } else {
            CURRENT_PLATFORM_PRESENT_COIN = CURRENT_PLATFORM_PRESENT_COIN - STEP_JACKPOT_COIN;
            mTvPlatformPresentCoin.setText(getResources().getString(R.string.mks_tv_platform_present_coin,
                    String.valueOf(CURRENT_PLATFORM_PRESENT_COIN)));
        }

        CURRENT_JACKPOT_COIN = CURRENT_JACKPOT_COIN + STEP_JACKPOT_COIN;
        mTvPokerJackpot.setText(String.valueOf(CURRENT_JACKPOT_COIN));
    }

    private void jackPotReduce() {
        if (CURRENT_JACKPOT_COIN == MIN_JACKPOT_COIN) {
            ToastUtils.showShort(this, "已经是最小的奖池了");
            return;
        }

        if (COIN_TYPE == 1) {
            CURRENT_PLATFORM_COIN = CURRENT_PLATFORM_COIN + STEP_JACKPOT_COIN;
            mTvPlatformCoin.setText(getResources().getString(R.string.mks_tv_platform_coin,
                    String.valueOf(CURRENT_PLATFORM_COIN)));
        } else {
            CURRENT_PLATFORM_PRESENT_COIN = CURRENT_PLATFORM_PRESENT_COIN + STEP_JACKPOT_COIN;
            mTvPlatformPresentCoin.setText(getResources().getString(R.string.mks_tv_platform_present_coin,
                    String.valueOf(CURRENT_PLATFORM_PRESENT_COIN)));
        }

        CURRENT_JACKPOT_COIN = CURRENT_JACKPOT_COIN - STEP_JACKPOT_COIN;
        mTvPokerJackpot.setText(String.valueOf(CURRENT_JACKPOT_COIN));
    }


    @Override
    protected void initData() {
        mPresenter.initPlay(this);
    }

    @Override
    protected PokerPresenter createPresenter() {
        return new PokerPresenter();
    }

    @Override
    protected String layoutName() {
        return Constants.kl_poker_game;
    }

    @Override
    public void setPlayConfig(PokerInitBean initBean) {
        PLATFORM_COIN = Integer.valueOf(initBean.getMoney());
        PLATFORM_PRESENT_COIN = Integer.valueOf(initBean.getGive_money());
        updateUi();
        mTvPokerRoyalFlush.setText(getResources().getString(R.string.mks_tv_royal_flush,
                initBean.getMultiple().getRoyalFlush()));
        mTvPokerStraightFlush.setText(getResources().getString(R.string.mks_tv_straight_flush,
                initBean.getMultiple().getStraightFlush()));
        mTvPokerFour.setText(getResources().getString(R.string.mks_tv_four,
                initBean.getMultiple().getFour()));
        mTvPokerFullHouse.setText(getResources().getString(R.string.mks_tv_full_house,
                initBean.getMultiple().getFullHouse()));
        mTvPokerFlush.setText(getResources().getString(R.string.mks_tv_flush,
                initBean.getMultiple().getFluse()));
        mTvPokerStraight.setText(getResources().getString(R.string.mks_tv_straight,
                initBean.getMultiple().getStraight()));
        mTvPokerThreeKind.setText(getResources().getString(R.string.mks_tv_three_kind,
                initBean.getMultiple().getThreeKind()));
        mTvPokerTwoPair.setText(getResources().getString(R.string.mks_tv_two_pair,
                initBean.getMultiple().getTwoPair()));
        mTvPokerOnePair.setText(getResources().getString(R.string.mks_tv_one_pair,
                initBean.getMultiple().getOnePair()));
        mTvPokerHigh.setText(getResources().getString(R.string.mks_tv_high,
                initBean.getMultiple().getHighCard()));
    }

    @Override
    public void setPlayResult(PokerPlayBean playResult) {
        mLpvPokerView.start();
        List<Integer> list = new ArrayList<>();
        list.addAll(playResult.getHand());
        mLpvPokerView.stop(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4));
        PLATFORM_COIN = Integer.valueOf(playResult.getMoney());
        PLATFORM_PRESENT_COIN = Integer.valueOf(playResult.getGive_money());
        showPlayResult = playResult.getSuitpattern();
    }

    @Override
    public void showError(BaseBean baseBean) {
        ToastUtils.showShort(this, baseBean.getMsg());
        initClick(false);
    }
}
