//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.netease.nis.quicklogin.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.netease.nis.quicklogin.helper.UnifyUiConfig;
import com.netease.nis.quicklogin.listener.LoginListener;
import com.netease.nis.quicklogin.listener.QuickLoginTokenListener;
import com.sdk.cp.base.api.ToolUtils;
import org.json.JSONObject;

public class YDQuickLoginActivity extends Activity {
    ImageView a;
    private EditText c;
    private Button d;
    private CheckBox e;
    private static QuickLoginTokenListener f;
    private UnifyUiConfig g;
    private LoginListener h;
    private String i;
    private String j;
    private String k;
    public boolean b;

    public YDQuickLoginActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(resourceId("activity_quick_login","layout"));
        this.b();
        Intent var2 = this.getIntent();
        if (var2 != null) {
            this.a(var2);
        }

    }

    public static void a(QuickLoginTokenListener var0) {
        f = var0;
    }

    public void a(UnifyUiConfig var1) {
        this.g = var1;
    }

    public void a(LoginListener var1) {
        this.h = var1;
    }

    private void a(Intent var1) {
        if ("cu".equals(var1.getStringExtra("operatorType"))) {
            this.b = true;
        }

        if (this.b) {
            TextView var2 = (TextView)this.findViewById(resourceId("brand","id"));
            if (var2 != null) {
                var2.setText("中国联通提供认证服务");
            }
        }

        String var3 = var1.getStringExtra("maskNumber");
        this.c.setText(var3);
        this.i = var1.getStringExtra("accessToken");
        this.j = var1.getStringExtra("gwAuth");
        this.k = var1.getStringExtra("ydToken");
    }

    private void b() {
        this.a = (ImageView)this.findViewById(resourceId("yd_navigation_back","id"));
        this.a.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (YDQuickLoginActivity.f != null) {
                    YDQuickLoginActivity.f.onCancelGetToken();
                }

                YDQuickLoginActivity.this.finish();
            }
        });
        this.c = (EditText)this.findViewById(resourceId("oauth_mobile_et","id"));
        this.d = (Button)this.findViewById(resourceId("oauth_login","id"));
        this.d.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (YDQuickLoginActivity.this.e.isChecked()) {
                    YDQuickLoginActivity.this.a(4, 1);
                    YDQuickLoginActivity.this.c();
                } else {
                    YDQuickLoginActivity.this.a(4, 0);
                    if (YDQuickLoginActivity.this.h != null) {
                        if (!YDQuickLoginActivity.this.h.onDisagreePrivacy()) {
                            Toast.makeText(YDQuickLoginActivity.this.getApplicationContext(), resourceId("yd_privacy_agree","string"), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(YDQuickLoginActivity.this.getApplicationContext(), resourceId("yd_privacy_agree","string"), Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
        this.e = (CheckBox)this.findViewById(resourceId("yd_quick_login_privacy_checkbox","id"));
    }

    private void c() {
        JSONObject var1 = new JSONObject();

        try {
            if (this.b) {
                var1.put("accessToken", this.i);
                var1.put("version", "v2");
                var1.put("md5", ToolUtils.getAppMd5(this.getApplicationContext()));
            } else {
                var1.put("accessToken", this.i);
                var1.put("gwAuth", this.j);
            }

            if (f != null) {
                f.onGetTokenSuccess(this.k, com.netease.nis.quicklogin.utils.a.c(var1.toString()));
            }
        } catch (Exception var3) {
            var3.printStackTrace();
            if (f != null) {
                f.onGetTokenError(this.k, var3.toString());
            }

            this.a(this.k, com.netease.nis.quicklogin.a.a.g.ordinal(), 0, var3.toString());
        }

    }

    private void a(String var1, int var2, int var3, String var4) {
        com.netease.nis.quicklogin.utils.e.a().a(com.netease.nis.quicklogin.utils.e.b.b, var2, var1, 1, var3, 0, var4, System.currentTimeMillis());
        com.netease.nis.quicklogin.utils.e.a().b();
    }

    private void a(int var1, int var2) {
        if (this.g != null && this.g.getClickEventListener() != null) {
            this.g.getClickEventListener().onClick(var1, var2);
        }

    }

    public void onBackPressed() {
        if (f != null) {
            f.onCancelGetToken();
        }

        super.onBackPressed();
    }

    public void finish() {
        super.finish();
        if (this.g != null && (!TextUtils.isEmpty(this.g.getActivityEnterAnimation()) || !TextUtils.isEmpty(this.g.getActivityExitAnimation()))) {
            com.netease.nis.quicklogin.utils.f var1 = com.netease.nis.quicklogin.utils.f.a(this.getApplicationContext());
            int var2 = var1.c(this.g.getActivityEnterAnimation());
            int var3 = var1.c(this.g.getActivityExitAnimation());
            this.overridePendingTransition(var2, var3);
        }

        if (f != null) {
            f = null;
        }

    }
    public int resourceId(String name, String type) {
        return getResources().getIdentifier(name, type, getPackageName());
    }
}
