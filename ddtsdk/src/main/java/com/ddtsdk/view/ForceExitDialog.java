package com.ddtsdk.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ddtsdk.constants.AppConfig;

import java.util.Timer;
import java.util.TimerTask;

public class ForceExitDialog extends Dialog {

    private View exitview;
    private Button force_exit;
    private TextView force_msg;
    private TextView force_time;
    private Activity mactivity = null;
    private String msg = "禁止模拟器打开此游戏，\n请您使用手机畅玩!";  // 强制提示语

    private int times = 5; // 倒计时秒数，默认5秒
    private Timer onTimer;

    /**
     * 强制退出窗体，使用规则参考下面参数说明
     * @param context
     * @param msg 如果msg为空，则默认提示禁止模拟器使用，参考msg定义。
     * @param times 如果times < 0，则会显示“退出游戏”按钮，并且会点击按钮会强制本app，否则会按times来倒计时关闭窗体（按钮会被隐藏），结合listener可以在外部做后续动作。
     * @param listener 当times > 0时才会生效。
     * @return
     */
    public static ForceExitDialog showDialog(@NonNull Context context, String msg, int times, onTimesListener listener) {
        ForceExitDialog exitDialog = new ForceExitDialog((Activity)context, AppConfig.resourceId(context, "kl_MyDialog", "style"), msg, times, listener);
        exitDialog.setCancelable(false);
        exitDialog.show();

        return exitDialog;
    }

    public ForceExitDialog(@NonNull Activity activity, int themeResId, String msg, int times, onTimesListener listener) {
        super(activity, themeResId);
        mactivity = activity;
        exitview = LayoutInflater.from(mactivity).inflate(
                AppConfig.resourceId(mactivity, "kl_force_exitdialog", "layout"), null);
        this.msg = msg == null ? this.msg : msg;
        this.times = times;
        this.timesListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(exitview);
        force_msg = (TextView) exitview.findViewById(AppConfig.resourceId(mactivity,
                "force_msg", "id"));
        force_msg.setText(msg);

        force_exit = (Button) exitview.findViewById(AppConfig.resourceId(mactivity,
                "force_exit", "id"));
        force_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mactivity.finish();
                System.exit(0);
            }
        });

        force_time = (TextView) exitview.findViewById(AppConfig.resourceId(mactivity,
                "force_time", "id"));

        toTimes();
    }

    /// 倒计时回调

    Handler timeHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (onTimer != null) {
                        if (times < 0) {
                            onTimer.cancel();
                            dismiss();

                            if (timesListener!=null) {
                                timesListener.OnSuccess();
                            }
                        }
                        force_time.setText(times + "秒");
                        times--;
                    }
                    break;
            }
            return false;
        }
    });

    private void sendData(int what) {
        Message msg = new Message();
        msg.what = what;
        timeHandler.sendMessage(msg);
    }

    private void toTimes() {
        if (times <= 0) {
            return;
        }

        force_exit.setVisibility(View.GONE);
        force_time.setVisibility(View.VISIBLE);

        onTimer = new Timer();
        onTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendData(1);
            }
        }, 100, 1000);
    }

    /// 倒计时结束回调

    /**
     * 定义一个接口
     */
    public interface  onTimesListener{
        void OnSuccess();
    }
    /**
     *定义一个变量储存数据
     */
    private onTimesListener timesListener;
    /**
     *提供公共的方法,并且初始化接口类型的数据
     */
    public void setTimesListener( onTimesListener listener){
        this.timesListener = listener;
    }
}
