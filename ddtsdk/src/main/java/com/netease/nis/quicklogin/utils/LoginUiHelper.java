//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.netease.nis.quicklogin.utils;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout.LayoutParams;
import com.cmic.sso.wy.activity.LoginAuthActivity;
import com.netease.nis.quicklogin.b.b;
import com.netease.nis.quicklogin.helper.UnifyUiConfig;
import com.netease.nis.quicklogin.listener.ClickEventListener;
import com.netease.nis.quicklogin.listener.LoginListener;
import com.netease.nis.quicklogin.listener.QuickLoginTokenListener;
import com.netease.nis.quicklogin.ui.ProtocolDetailActivity;
import com.netease.nis.quicklogin.ui.YDQuickLoginActivity;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LoginUiHelper {
    private ActivityLifecycleCallbacks a;
    private Context b;
    private UnifyUiConfig c;
    private f d;
    private WeakReference<CheckBox> e;
    private WeakReference<CheckBox> f;
    private WeakReference<RelativeLayout> g;
    private WeakReference<RelativeLayout> h;
    private boolean i = true;
    private WeakReference<QuickLoginTokenListener> j;
    private WeakReference<Activity> k;
    private b l;
    private String m;

    public LoginUiHelper(Context context) {
        if (context != null) {
            this.b = context.getApplicationContext();
            this.d = com.netease.nis.quicklogin.utils.f.a(this.b);
        }

    }

    public void a(UnifyUiConfig var1, String var2) {
        this.c = var1;
        this.m = var2;
        if (this.a == null) {
            this.b();
        } else {
            ((Application)this.b).unregisterActivityLifecycleCallbacks(this.a);
        }

        ((Application)this.b).registerActivityLifecycleCallbacks(this.a);
    }

    public void a(QuickLoginTokenListener var1) {
        this.j = new WeakReference(var1);
    }

    public void a() {
        if (com.netease.nis.quicklogin.utils.a.a(this.k)) {
            ((Activity)this.k.get()).finish();
        }

    }

    private void a(Activity var1, boolean var2) {
        this.b(var1);
        this.c(var1);
        this.d(var1);
        this.e(var1);
        this.f(var1);
        if (var2) {
            this.a(var1, 1);
        } else {
            this.a(var1, 2);
        }

    }

    private void a(Activity var1) {
        com.netease.nis.quicklogin.utils.g.a(var1, this.c.getStatusBarColor());
        com.netease.nis.quicklogin.utils.g.a(var1, this.c.isStatusBarDarkColor());
    }

    private void b(final Activity var1) {
        RelativeLayout var2 = (RelativeLayout)var1.findViewById(resourceId(var1, "yd_navigation_rl", "id"));
        if (var2 != null) {
            if (this.c.getNavBackgroundColor() != 0) {
                var2.setBackgroundColor(this.c.getNavBackgroundColor());
            }

            if (this.c.isHideNav()) {
                var2.setVisibility(View.INVISIBLE);
            }

            if (this.c.getNavHeight() != 0) {
                LayoutParams var3 = (LayoutParams)var2.getLayoutParams();
                var3.height = com.netease.nis.quicklogin.utils.g.a(this.b, (float)this.c.getNavHeight());
                var2.setLayoutParams(var3);
            }
        }

        ImageView var5 = (ImageView)var1.findViewById(resourceId(var1, "yd_navigation_back", "id"));
        if (var5 != null) {
            if (this.c.isHideBackIcon()) {
                var5.setVisibility(View.INVISIBLE);
            }

            if (this.c.getNavBackIconDrawable() != null) {
                var5.setImageDrawable(this.c.getNavBackIconDrawable());
            } else if (!TextUtils.isEmpty(this.c.getNavBackIcon())) {
                var5.setImageResource(this.d.b(this.c.getNavBackIcon()));
            }

            android.view.ViewGroup.LayoutParams var4 = var5.getLayoutParams();
            var4.width = com.netease.nis.quicklogin.utils.g.a(this.b, (float)this.c.getNavBackIconWidth());
            var4.height = com.netease.nis.quicklogin.utils.g.a(this.b, (float)this.c.getNavBackIconHeight());
            var5.setLayoutParams(var4);
            var5.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    LoginUiHelper.this.a(3, 0);
                    var1.finish();
                    if (com.netease.nis.quicklogin.utils.a.a(LoginUiHelper.this.j)) {
                        ((QuickLoginTokenListener)LoginUiHelper.this.j.get()).onCancelGetToken();
                    }

                }
            });
        }

        TextView var6 = (TextView)var1.findViewById(resourceId(var1, "yd_navigation_title", "id"));
        if (var6 != null) {
            if (!TextUtils.isEmpty(this.c.getNavTitle())) {
                var6.setText(this.c.getNavTitle());
            }

            if (this.c.getNavTitleColor() != 0) {
                var6.setTextColor(this.c.getNavTitleColor());
            }

            if (this.c.getNavTitleSize() != 0) {
                var6.setTextSize((float)this.c.getNavTitleSize());
            } else if (this.c.getNavTitleDpSize() != 0) {
                var6.setTextSize(1, (float)this.c.getNavTitleDpSize());
            }

            if (this.c.isNavTitleBold()) {
                var6.setTypeface(Typeface.defaultFromStyle(1));
            }
        }

    }

    private void c(Activity var1) {
        ImageView var2 = (ImageView)var1.findViewById(resourceId(var1, "oauth_logo", "id"));
        if (var2 != null) {
            int var3 = this.c.getLogoWidth();
            int var4 = this.c.getLogoHeight();
            if (var3 != 0 || var4 != 0) {
                LayoutParams var5;
                if (var3 == 0) {
                    var5 = new LayoutParams(com.netease.nis.quicklogin.utils.g.a(this.b, 70.0F), com.netease.nis.quicklogin.utils.g.a(this.b, (float)var4));
                } else if (var4 == 0) {
                    var5 = new LayoutParams(com.netease.nis.quicklogin.utils.g.a(this.b, (float)var3), com.netease.nis.quicklogin.utils.g.a(this.b, 70.0F));
                } else {
                    var5 = new LayoutParams(com.netease.nis.quicklogin.utils.g.a(this.b, (float)var3), com.netease.nis.quicklogin.utils.g.a(this.b, (float)var4));
                }

                var2.setLayoutParams(var5);
            }

            if (this.c.getLogoXOffset() != 0) {
                com.netease.nis.quicklogin.utils.g.b(var2, this.c.getLogoXOffset());
            } else {
                com.netease.nis.quicklogin.utils.g.a(var2);
            }

            if (this.c.getLogoTopYOffset() != 0) {
                com.netease.nis.quicklogin.utils.g.d(var2, this.c.getLogoTopYOffset());
            }

            if (this.c.getLogoBottomYOffset() != 0) {
                com.netease.nis.quicklogin.utils.g.c(var2, this.c.getLogoBottomYOffset());
            }

            if (this.c.getLogoIconDrawable() != null) {
                var2.setImageDrawable(this.c.getLogoIconDrawable());
            } else if (!TextUtils.isEmpty(this.c.getLogoIconName())) {
                int var6 = this.d.b(this.c.getLogoIconName());
                var2.setImageResource(var6);
            }

            if (this.c.isHideLogo()) {
                var2.setVisibility(View.INVISIBLE);
            }
        }

    }

    private void d(Activity var1) {
        EditText var2 = (EditText)var1.findViewById(resourceId(var1, "oauth_mobile_et", "id"));
        if (var2 != null) {
            if (this.c.getMaskNumberSize() != 0) {
                var2.setTextSize((float)this.c.getMaskNumberSize());
            } else if (this.c.getMaskNumberDpSize() != 0) {
                var2.setTextSize(1, (float)this.c.getMaskNumberDpSize());
            }

            if (this.c.getMaskNumberColor() != 0) {
                var2.setTextColor(this.c.getMaskNumberColor());
            }

            if (this.c.getMaskNumberXOffset() != 0) {
                com.netease.nis.quicklogin.utils.g.b(var2, this.c.getMaskNumberXOffset());
            } else {
                com.netease.nis.quicklogin.utils.g.a(var2);
            }

            if (this.c.getMaskNumberTopYOffset() != 0) {
                com.netease.nis.quicklogin.utils.g.d(var2, this.c.getMaskNumberTopYOffset());
            }

            if (this.c.getMaskNumberBottomYOffset() != 0) {
                com.netease.nis.quicklogin.utils.g.c(var2, this.c.getMaskNumberBottomYOffset());
            }

            if (this.c.getMaskNumberListener() != null) {
                this.c.getMaskNumberListener().onGetMaskNumber(var2, var2.getText().toString());
            }
        }

    }

    private void e(Activity var1) {
        TextView var2 = (TextView)var1.findViewById(resourceId(var1, "brand", "id"));
        if (var2 != null) {
            if (this.c.getSloganSize() != 0) {
                var2.setTextSize((float)this.c.getSloganSize());
            } else if (this.c.getSloganDpSize() != 0) {
                var2.setTextSize(1, (float)this.c.getSloganDpSize());
            }

            if (this.c.getSloganColor() != 0) {
                var2.setTextColor(this.c.getSloganColor());
            }

            if (this.c.getSloganXOffset() != 0) {
                com.netease.nis.quicklogin.utils.g.b(var2, this.c.getSloganXOffset());
            } else {
                com.netease.nis.quicklogin.utils.g.a(var2);
            }

            if (this.c.getSloganTopYOffset() != 0) {
                com.netease.nis.quicklogin.utils.g.d(var2, this.c.getSloganTopYOffset());
            }

            if (this.c.getSloganBottomYOffset() != 0) {
                com.netease.nis.quicklogin.utils.g.c(var2, this.c.getSloganBottomYOffset());
            }
        }

    }

    private void f(Activity var1) {
        Button var2 = (Button)var1.findViewById(resourceId(var1, "oauth_login", "id"));
        if (var2 != null) {
            Context var3 = var1.getApplicationContext();
            if (this.c.getLoginBtnWidth() != 0) {
                var2.getLayoutParams().width = com.netease.nis.quicklogin.utils.g.a(var3, (float)this.c.getLoginBtnWidth());
            }

            if (this.c.getLoginBtnHeight() != 0) {
                var2.getLayoutParams().height = com.netease.nis.quicklogin.utils.g.a(var3, (float)this.c.getLoginBtnHeight());
            }

            if (!TextUtils.isEmpty(this.c.getLoginBtnText())) {
                var2.setText(this.c.getLoginBtnText());
            }

            if (this.c.getLoginBtnTextColor() != 0) {
                var2.setTextColor(this.c.getLoginBtnTextColor());
            }

            if (this.c.getLoginBtnTextSize() != 0) {
                var2.setTextSize((float)this.c.getLoginBtnTextSize());
            } else if (this.c.getLoginBtnTextDpSize() != 0) {
                var2.setTextSize(1, (float)this.c.getLoginBtnTextDpSize());
            }

            if (this.c.getLoginBtnXOffset() != 0) {
                com.netease.nis.quicklogin.utils.g.b(var2, this.c.getLoginBtnXOffset());
            } else {
                com.netease.nis.quicklogin.utils.g.a(var2);
            }

            if (this.c.getLoginBtnTopYOffset() != 0) {
                com.netease.nis.quicklogin.utils.g.d(var2, this.c.getLoginBtnTopYOffset());
            }

            if (this.c.getLoginBtnBottomYOffset() != 0) {
                com.netease.nis.quicklogin.utils.g.c(var2, this.c.getLoginBtnBottomYOffset());
            }

            if (this.c.getLoginBtnBackgroundDrawable() != null) {
                var2.setBackground(this.c.getLoginBtnBackgroundDrawable());
            } else if (!TextUtils.isEmpty(this.c.getLoginBtnBackgroundRes())) {
                var2.setBackground(com.netease.nis.quicklogin.utils.f.a(var3).a(this.c.getLoginBtnBackgroundRes()));
            }
        }

    }

    private void a(View var1) {
        var1.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 1) {
                    if (com.netease.nis.quicklogin.utils.a.a(LoginUiHelper.this.e) && ((CheckBox)LoginUiHelper.this.e.get()).isChecked()) {
                        LoginUiHelper.this.a(4, 1);
                        return false;
                    } else {
                        LoginUiHelper.this.a(4, 0);
                        LoginListener var3 = LoginUiHelper.this.c.getLoginListener();
                        if (var3 != null) {
                            if (!var3.onDisagreePrivacy()) {
                                Toast.makeText(LoginUiHelper.this.b, resourceId(LoginUiHelper.this.b, "yd_privacy_agree", "string"), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(LoginUiHelper.this.b, resourceId(LoginUiHelper.this.b, "yd_privacy_agree", "string"), Toast.LENGTH_LONG).show();
                        }

                        return true;
                    }
                } else {
                    return false;
                }
            }
        });
    }

    private void a(int var1, int var2) {
        ClickEventListener var3 = this.c.getClickEventListener();
        if (var3 != null) {
            var3.onClick(var1, var2);
        }

    }

    public void a(Activity var1, int var2) {
        LinearLayout var3 = (LinearLayout)var1.findViewById(resourceId(var1, "protocol_ll", "id"));
        if (var3 != null) {
            CheckBox var4 = (CheckBox)var3.findViewById(resourceId(var1, "yd_quick_login_privacy_checkbox", "id"));
            this.e = new WeakReference(var4);
            RelativeLayout var5 = (RelativeLayout)var3.findViewById(resourceId(var1, "yd_quick_login_privacy_rl", "id"));
            if (this.c.isHidePrivacyCheckBox()) {
                var5.setVisibility(View.GONE);
            } else if (this.c.getCheckBoxGravity() != 0) {
                android.widget.LinearLayout.LayoutParams var6 = (android.widget.LinearLayout.LayoutParams)var5.getLayoutParams();
                var6.gravity = this.c.getCheckBoxGravity();
                var5.setLayoutParams(var6);
            }

            if (com.netease.nis.quicklogin.utils.a.a(this.f)) {
                ((CheckBox)this.f.get()).setChecked(true);
            }

            if (com.netease.nis.quicklogin.utils.a.a(this.e)) {
                if (this.c.isPrivacyState()) {
                    ((CheckBox)this.e.get()).setChecked(true);
                    if (this.c.getCheckedImageDrawable() != null) {
                        ((CheckBox)this.e.get()).setBackground(this.c.getCheckedImageDrawable());
                    } else if (!TextUtils.isEmpty(this.c.getCheckedImageName())) {
                        ((CheckBox)this.e.get()).setBackgroundResource(this.d.b(this.c.getCheckedImageName()));
                    }
                } else {
                    ((CheckBox)this.e.get()).setChecked(false);
                    if (this.c.getUnCheckedImageNameDrawable() != null) {
                        ((CheckBox)this.e.get()).setBackground(this.c.getUnCheckedImageNameDrawable());
                    } else if (!TextUtils.isEmpty(this.c.getUnCheckedImageName())) {
                        ((CheckBox)this.e.get()).setBackgroundResource(this.d.b(this.c.getUnCheckedImageName()));
                    }
                }

                ((CheckBox)this.e.get()).setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            LoginUiHelper.this.a(2, 1);
                            if (LoginUiHelper.this.c.getCheckedImageDrawable() != null) {
                                ((CheckBox)LoginUiHelper.this.e.get()).setBackground(LoginUiHelper.this.c.getCheckedImageDrawable());
                            } else if (!TextUtils.isEmpty(LoginUiHelper.this.c.getCheckedImageName())) {
                                ((CheckBox)LoginUiHelper.this.e.get()).setBackgroundResource(LoginUiHelper.this.d.b(LoginUiHelper.this.c.getCheckedImageName()));
                            }
                        } else {
                            LoginUiHelper.this.a(2, 0);
                            if (LoginUiHelper.this.c.getUnCheckedImageNameDrawable() != null) {
                                ((CheckBox)LoginUiHelper.this.e.get()).setBackground(LoginUiHelper.this.c.getUnCheckedImageNameDrawable());
                            } else if (!TextUtils.isEmpty(LoginUiHelper.this.c.getUnCheckedImageName())) {
                                ((CheckBox)LoginUiHelper.this.e.get()).setBackgroundResource(LoginUiHelper.this.d.b(LoginUiHelper.this.c.getUnCheckedImageName()));
                            }
                        }

                    }
                });
            }

            TextView var8 = (TextView)var3.findViewById(resourceId(var1, "yd_quick_login_privacy_text", "id"));
            var8.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    LoginUiHelper.this.a(1, 0);
                }
            });
            com.netease.nis.quicklogin.utils.a.a(var2, this.c, var8);
            if (this.c.getPrivacySize() != 0) {
                var8.setTextSize((float)this.c.getPrivacySize());
            } else if (this.c.getPrivacyDpSize() != 0) {
                var8.setTextSize(1, (float)this.c.getPrivacyDpSize());
            }

            if (this.c.getPrivacyXOffset() != 0) {
                com.netease.nis.quicklogin.utils.g.b(var3, this.c.getPrivacyXOffset());
            } else {
                com.netease.nis.quicklogin.utils.g.b(var3);
            }

            if (this.c.getPrivacyMarginRight() != 0) {
                TextView var7 = (TextView)var3.findViewById(resourceId(var1, "yd_quick_login_privacy_text", "id"));
                com.netease.nis.quicklogin.utils.g.a(var7, this.c.getPrivacyMarginRight());
            }

            if (this.c.getPrivacyTopYOffset() != 0 && this.c.getPrivacyBottomYOffset() == 0) {
                com.netease.nis.quicklogin.utils.g.a(var3, this.c.getPrivacyTopYOffset() + com.netease.nis.quicklogin.utils.g.b(this.b), this.c.getPrivacyXOffset());
            }

            if (this.c.getPrivacyBottomYOffset() != 0) {
                com.netease.nis.quicklogin.utils.g.c(var3, this.c.getPrivacyBottomYOffset());
            }

            if (this.c.isPrivacyTextGravityCenter()) {
                var8.setGravity(17);
            }
        }

    }

    private void g(Activity var1) {
        View var2 = var1.getWindow().getDecorView();
        ViewGroup var3 = (ViewGroup)var2.findViewById(16908290);
        ViewGroup var4 = (ViewGroup)var3.getChildAt(0);
        this.b(var1);
        this.c(var1);
        this.e(var1);
        List var5 = com.netease.nis.quicklogin.utils.g.c(var4);
        Iterator var6 = var5.iterator();

        while(var6.hasNext()) {
            View var7 = (View)var6.next();
            if (var7 instanceof TextView) {
                String var8 = ((TextView)var7).getText().toString();
                if (!TextUtils.isEmpty(var8) && var8.contains("****")) {
                    if (this.c.getMaskNumberListener() != null) {
                        this.c.getMaskNumberListener().onGetMaskNumber((TextView)var7, var8);
                    }

                    if (this.c.getMaskNumberXOffset() != 0) {
                        com.netease.nis.quicklogin.utils.g.b(var7, this.c.getMaskNumberXOffset());
                    }

                    if (this.c.getMaskNumberSize() == 0 && this.c.getMaskNumberDpSize() != 0) {
                        ((TextView)var7).setTextSize(1, (float)this.c.getMaskNumberDpSize());
                    }
                }
            }

            if (var7 instanceof CheckBox && var7.getId() != resourceId(var1, "yd_quick_login_privacy_checkbox", "id")) {
                CheckBox var12 = (CheckBox)var7;
                ViewGroup var9 = (ViewGroup)var12.getParent().getParent();
                var9.setVisibility(View.GONE);
                this.f = new WeakReference(var12);
            }
        }

        ViewGroup var10 = (ViewGroup)var4.getChildAt(2);
        if (var10 instanceof RelativeLayout && ((RelativeLayout)var10).getChildCount() == 1) {
            if (this.c.getLoginBtnXOffset() != 0) {
                com.netease.nis.quicklogin.utils.g.b(var10, this.c.getLoginBtnXOffset());
            } else {
                com.netease.nis.quicklogin.utils.g.a(var10, this.c.isLandscape(), this.c.isDialogMode());
            }

            if (this.c.getLoginBtnBottomYOffset() != 0) {
                com.netease.nis.quicklogin.utils.g.c(var10, this.c.getLoginBtnBottomYOffset());
            }

            if (this.c.getLoginBtnTextSize() == 0 && this.c.getLoginBtnTextDpSize() != 0) {
                TextView var11 = (TextView)var10.getChildAt(0);
                var11.setTextSize(1, (float)this.c.getLoginBtnTextDpSize());
            }

            if (this.c.getLoginBtnBackgroundDrawable() != null) {
                var10.setBackground(this.c.getLoginBtnBackgroundDrawable());
            } else if (!TextUtils.isEmpty(this.c.getLoginBtnBackgroundRes())) {
                var10.setBackground(com.netease.nis.quicklogin.utils.f.a(this.b).a(this.c.getLoginBtnBackgroundRes()));
            }

            this.a((View)var10);
        }

        this.a(var1, 0);
    }

    private void b() {
        this.a = new ActivityLifecycleCallbacks() {
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                LoginUiHelper.this.a(activity, "onActivityCreated");
                if (LoginUiHelper.this.h(activity) && LoginUiHelper.this.c != null && LoginUiHelper.this.c.getActivityLifecycleCallbacks() != null) {
                    LoginUiHelper.this.c.getActivityLifecycleCallbacks().onCreate(activity);
                }

            }

            public void onActivityStarted(Activity activity) {
                LoginUiHelper.this.a(activity, "onActivityStarted");
                if (LoginUiHelper.this.h(activity) && LoginUiHelper.this.c != null && LoginUiHelper.this.c.getActivityLifecycleCallbacks() != null) {
                    LoginUiHelper.this.c.getActivityLifecycleCallbacks().onStart(activity);
                }

            }

            public void onActivityResumed(Activity activity) {
                LoginUiHelper.this.a(activity, "onActivityResumed");
                if (LoginUiHelper.this.c != null) {
                    RelativeLayout var2;
                    if (LoginUiHelper.this.h(activity)) {
                        if (LoginUiHelper.this.c.getActivityLifecycleCallbacks() != null) {
                            LoginUiHelper.this.c.getActivityLifecycleCallbacks().onResume(activity);
                        }

                        if (LoginUiHelper.this.i) {
                            LoginUiHelper.this.k = new WeakReference(activity);
                            if (LoginUiHelper.this.c.isDialogMode()) {
                                com.netease.nis.quicklogin.utils.g.a((Activity)LoginUiHelper.this.k.get(), LoginUiHelper.this.c.getDialogWidth(), LoginUiHelper.this.c.getDialogHeight(), LoginUiHelper.this.c.getDialogX(), LoginUiHelper.this.c.getDialogY(), LoginUiHelper.this.c.isBottomDialog());
                            } else if (VERSION.SDK_INT == 26) {
                                if (LoginUiHelper.this.c.isLandscape()) {
                                    activity.setRequestedOrientation(3);
                                }
                            } else if (LoginUiHelper.this.c.isLandscape()) {
                                activity.setRequestedOrientation(0);
                            } else {
                                activity.setRequestedOrientation(1);
                            }

                            if (activity instanceof LoginAuthActivity) {
                                var2 = (RelativeLayout)activity.findViewById(resourceId(activity, "rl_quick_login_root", "id"));
                                if (var2 == null) {
                                    String var9 = "移动接口添加易盾布局文件失败";
                                    if (com.netease.nis.quicklogin.utils.a.a(LoginUiHelper.this.j)) {
                                        ((QuickLoginTokenListener)LoginUiHelper.this.j.get()).onGetMobileNumberError(LoginUiHelper.this.m, var9);
                                    }

                                    com.netease.nis.quicklogin.utils.e.a().a(com.netease.nis.quicklogin.utils.e.b.c, com.netease.nis.quicklogin.a.a.h.ordinal(), LoginUiHelper.this.m, 2, 0, 0, var9, System.currentTimeMillis());
                                    com.netease.nis.quicklogin.utils.e.a().b();
                                    activity.finish();
                                    return;
                                }
                            }

                            if ((!TextUtils.isEmpty(LoginUiHelper.this.c.getBackgroundImage()) || LoginUiHelper.this.c.getBackgroundImageDrawable() != null) && TextUtils.isEmpty(LoginUiHelper.this.c.getBackgroundGif()) && LoginUiHelper.this.c.getBackgroundGifDrawable() == null) {
                                Object var6 = (RelativeLayout)activity.findViewById(resourceId(activity, "rl_quick_login_root", "id"));
                                if (activity instanceof LoginAuthActivity) {
                                    ((View)var6).setBackgroundColor(0);
                                    var6 = (View)((View)var6).getParent();
                                }

                                if (LoginUiHelper.this.c.getBackgroundImageDrawable() != null) {
                                    ((View)var6).setBackground(LoginUiHelper.this.c.getBackgroundImageDrawable());
                                } else {
                                    ((View)var6).setBackground(LoginUiHelper.this.d.a(LoginUiHelper.this.c.getBackgroundImage()));
                                }
                            }

                            if (TextUtils.isEmpty(LoginUiHelper.this.c.getBackgroundGif()) && LoginUiHelper.this.c.getBackgroundGifDrawable() == null) {
                                if (!TextUtils.isEmpty(LoginUiHelper.this.c.getBackgroundVideo()) && (!TextUtils.isEmpty(LoginUiHelper.this.c.getBackgroundVideoImage()) || LoginUiHelper.this.c.getBackgroundVideoImageDrawable() != null)) {
                                    var2 = (RelativeLayout)activity.findViewById(resourceId(activity, "rl_quick_login_root", "id"));
                                    var2.setFitsSystemWindows(false);
                                    LoginUiHelper.this.l = new b(LoginUiHelper.this.b);
                                    LoginUiHelper.this.l.setVideoURI(Uri.parse(LoginUiHelper.this.c.getBackgroundVideo()));
                                    if (LoginUiHelper.this.c.getBackgroundVideoImageDrawable() != null) {
                                        LoginUiHelper.this.l.setLoadingImageResId(LoginUiHelper.this.c.getBackgroundVideoImageDrawable());
                                    } else {
                                        LoginUiHelper.this.l.setLoadingImageResId(LoginUiHelper.this.d.b(LoginUiHelper.this.c.getBackgroundVideoImage()));
                                    }

                                    LoginUiHelper.this.l.setLayoutParams(new LayoutParams(-1, -1));
                                    var2.addView(LoginUiHelper.this.l, 0);
                                }
                            } else {
                                var2 = (RelativeLayout)activity.findViewById(resourceId(activity, "rl_quick_login_root", "id"));
                                var2.setFitsSystemWindows(false);
                                com.netease.nis.quicklogin.b.a var3 = new com.netease.nis.quicklogin.b.a(LoginUiHelper.this.b);
                                if (LoginUiHelper.this.c.getBackgroundGifDrawable() != null) {
                                    var3.setGifDrawable(LoginUiHelper.this.c.getBackgroundGifDrawable());
                                } else {
                                    var3.setGifResId(LoginUiHelper.this.d.b(LoginUiHelper.this.c.getBackgroundGif()));
                                }

                                var3.setLayoutParams(new LayoutParams(-1, -1));
                                var2.addView(var3, 0);
                            }

                            LoginUiHelper.this.a(activity);
                            if (activity instanceof LoginAuthActivity) {
                                LoginUiHelper.this.g(activity);
                            }

                            if (activity instanceof YDQuickLoginActivity) {
                                LoginUiHelper.this.i(activity);
                                ((YDQuickLoginActivity)activity).a(LoginUiHelper.this.c);
                                ((YDQuickLoginActivity)activity).a(LoginUiHelper.this.c.getLoginListener());
                                LoginUiHelper.this.a(activity, ((YDQuickLoginActivity)activity).b);
                            }

                            LoginUiHelper.this.j((Activity)LoginUiHelper.this.k.get());
                            LoginUiHelper.this.i = false;
                        }

                        if (LoginUiHelper.this.l != null) {
                            LoginUiHelper.this.l.a();
                            LoginUiHelper.this.l.start();
                        }
                    }

                    if (activity instanceof ProtocolDetailActivity) {
                        var2 = (RelativeLayout)activity.findViewById(resourceId(activity, "yd_navigation_rl", "id"));
                        if (var2 != null) {
                            if (LoginUiHelper.this.c.getProtocolNavColor() != 0) {
                                var2.setBackgroundColor(LoginUiHelper.this.c.getProtocolNavColor());
                            }

                            if (LoginUiHelper.this.c.getProtocolNavHeight() != 0) {
                                android.widget.LinearLayout.LayoutParams var7 = (android.widget.LinearLayout.LayoutParams)var2.getLayoutParams();
                                var7.height = com.netease.nis.quicklogin.utils.g.a(LoginUiHelper.this.b, (float)LoginUiHelper.this.c.getProtocolNavHeight());
                                var2.setLayoutParams(var7);
                            }
                        }

                        TextView var8 = (TextView)activity.findViewById(resourceId(activity, "yd_navigation_title", "id"));
                        if (var8 != null) {
                            if (LoginUiHelper.this.c.getProtocolNavTitleSize() != 0) {
                                var8.setTextSize((float)LoginUiHelper.this.c.getProtocolNavTitleSize());
                            } else if (LoginUiHelper.this.c.getProtocolNavTitleDpSize() != 0) {
                                var8.setTextSize(1, (float)LoginUiHelper.this.c.getProtocolNavTitleDpSize());
                            }

                            if (LoginUiHelper.this.c.getProtocolNavTitleColor() != 0) {
                                var8.setTextColor(LoginUiHelper.this.c.getProtocolNavTitleColor());
                            }
                        }

                        ImageView var4 = (ImageView)activity.findViewById(resourceId(activity, "yd_navigation_back", "id"));
                        if (var4 != null) {
                            if (LoginUiHelper.this.c.getProtocolNavBackIconDrawable() != null) {
                                var4.setImageDrawable(LoginUiHelper.this.c.getProtocolNavBackIconDrawable());
                            } else if (!TextUtils.isEmpty(LoginUiHelper.this.c.getProtocolNavBackIcon())) {
                                var4.setImageDrawable(LoginUiHelper.this.d.a(LoginUiHelper.this.c.getProtocolNavBackIcon()));
                            }

                            android.view.ViewGroup.LayoutParams var5 = var4.getLayoutParams();
                            var5.width = com.netease.nis.quicklogin.utils.g.a(LoginUiHelper.this.b, (float)LoginUiHelper.this.c.getProtocolNavBackIconWidth());
                            var5.height = com.netease.nis.quicklogin.utils.g.a(LoginUiHelper.this.b, (float)LoginUiHelper.this.c.getProtocolNavBackIconHeight());
                            var4.setLayoutParams(var5);
                        }
                    }
                }

            }

            public void onActivityPaused(Activity activity) {
                LoginUiHelper.this.a(activity, "onActivityPaused");
                if (LoginUiHelper.this.h(activity) && LoginUiHelper.this.c != null && LoginUiHelper.this.c.getActivityLifecycleCallbacks() != null) {
                    LoginUiHelper.this.c.getActivityLifecycleCallbacks().onPause(activity);
                }

            }

            public void onActivityStopped(Activity activity) {
                LoginUiHelper.this.a(activity, "onActivityStopped");
                if (LoginUiHelper.this.h(activity) && LoginUiHelper.this.c != null && LoginUiHelper.this.c.getActivityLifecycleCallbacks() != null) {
                    LoginUiHelper.this.c.getActivityLifecycleCallbacks().onStop(activity);
                }

            }

            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            public void onActivityDestroyed(Activity activity) {
                if (LoginUiHelper.this.h(activity)) {
                    if (LoginUiHelper.this.c != null && LoginUiHelper.this.c.getActivityLifecycleCallbacks() != null) {
                        LoginUiHelper.this.c.getActivityLifecycleCallbacks().onDestroy(activity);
                    }

                    LoginUiHelper.this.i = true;
                    if (com.netease.nis.quicklogin.utils.a.a(LoginUiHelper.this.g)) {
                        ((RelativeLayout)LoginUiHelper.this.g.get()).removeAllViews();
                    }

                    if (com.netease.nis.quicklogin.utils.a.a(LoginUiHelper.this.h)) {
                        ((RelativeLayout)LoginUiHelper.this.h.get()).removeAllViews();
                    }

                    if (LoginUiHelper.this.l != null) {
                        LoginUiHelper.this.l = null;
                    }
                }

                LoginUiHelper.this.a(activity, "onActivityDestroyed");
            }
        };
    }

    private boolean h(Activity var1) {
        return var1 instanceof LoginAuthActivity || var1 instanceof YDQuickLoginActivity;
    }

    private void i(Activity var1) {
        if (!TextUtils.isEmpty(this.c.getActivityEnterAnimation()) || !TextUtils.isEmpty(this.c.getActivityExitAnimation())) {
            int var2 = 0;
            if (!TextUtils.isEmpty(this.c.getActivityEnterAnimation())) {
                var2 = this.d.c(this.c.getActivityEnterAnimation());
            }

            int var3 = 0;
            if (!TextUtils.isEmpty(this.c.getActivityExitAnimation())) {
                var3 = this.d.c(this.c.getActivityExitAnimation());
            }

            var1.overridePendingTransition(var2, var3);
        }

    }

    private void j(Activity var1) {
        ArrayList var2 = this.c.getCustomViewHolders();
        if (var2 != null) {
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
                LoginUiHelper.a var4 = (LoginUiHelper.a)var3.next();
                if (var4.a != null) {
                    this.a(var1, var4);
                }
            }

        }
    }

    private void a(Activity var1, final LoginUiHelper.a var2) {
        if (var2.a.getParent() == null) {
            RelativeLayout var3;
            if (var2.c == 1) {
                var3 = (RelativeLayout)var1.findViewById(resourceId(var1, "yd_navigation_rl", "id"));
                var3.addView(var2.a);
                this.g = new WeakReference(var3);
            } else if (var2.c == 0) {
                var3 = (RelativeLayout)var1.findViewById(resourceId(var1, "yd_quick_login_body", "id"));
                var3.addView(var2.a);
                this.h = new WeakReference(var3);
            }
        }

        var2.a.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (var2.d != null) {
                    var2.d.onClick(v.getContext(), var2.a);
                }

            }
        });
    }

    private void a(Activity var1, String var2) {
        if (var1 instanceof LoginAuthActivity || var1 instanceof YDQuickLoginActivity || var1 instanceof ProtocolDetailActivity) {
            if (!"onActivityResumed".equals(var2) && !"onActivityDestroyed".equals(var2)) {
                com.netease.nis.quicklogin.utils.a.b("[ActivityLifecycle] " + var2 + " ---> " + var1.getLocalClassName());
            } else {
                com.netease.nis.quicklogin.utils.a.b("[ActivityLifecycle] " + var2 + " ---> " + var1.getLocalClassName() + " isNotSetLoginUi=" + this.i);
            }
        }

    }

    public interface CustomViewListener {
        void onClick(Context var1, View var2);
    }

    public static class a {
        public View a;
        public String b;
        public int c;
        public LoginUiHelper.CustomViewListener d;

        public a() {
        }
    }

    public int resourceId(Context context,String name, String type) {
        return context.getResources().getIdentifier(name, type, context.getPackageName());
    }
}
