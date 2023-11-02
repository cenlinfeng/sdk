//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.netease.nis.quicklogin.helper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.cmic.sso.wy.AuthThemeConfig;
import com.cmic.sso.wy.auth.AuthnHelper;
import com.cmic.sso.wy.auth.LoginClickListener;
import com.netease.nis.quicklogin.listener.ActivityLifecycleCallbacks;
import com.netease.nis.quicklogin.listener.ClickEventListener;
import com.netease.nis.quicklogin.listener.LoginListener;
import com.netease.nis.quicklogin.listener.MaskNumberListener;
import com.netease.nis.quicklogin.utils.g;
import com.netease.nis.quicklogin.utils.LoginUiHelper.CustomViewListener;
import com.netease.nis.quicklogin.utils.LoginUiHelper.a;
import java.util.ArrayList;
import org.json.JSONObject;

public class UnifyUiConfig {
    public static final int CLICK_PRIVACY = 1;
    public static final int CLICK_CHECKBOX = 2;
    public static final int CLICK_TOP_LEFT_BACK_BUTTON = 3;
    public static final int CLICK_LOGIN_BUTTON = 4;
    public static final int CHECKBOX_UNCHECKED = 0;
    public static final int CHECKBOX_CHECKED = 1;
    public static final int POSITION_IN_BODY = 0;
    public static final int POSITION_IN_TITLE_BAR = 1;
    private int statusBarColor;
    private boolean isStatusBarDarkColor;
    private String navBackIcon;
    private Drawable navBackIconDrawable;
    private int navBackIconWidth;
    private int navBackIconHeight;
    private boolean isHideBackIcon;
    private int navBackgroundColor;
    private int navHeight;
    private String navTitle;
    private int navTitleColor;
    private int navTitleSize;
    private int navTitleDpSize;
    private boolean isHideNav;
    private boolean isNavTitleBold;
    private String logoIconName;
    private Drawable logoIconDrawable;
    private int logoWidth;
    private int logoHeight;
    private int logoTopYOffset;
    private int logoBottomYOffset;
    private int logoXOffset;
    private boolean isHideLogo;
    private int maskNumberColor;
    private int maskNumberSize;
    private int maskNumberDpSize;
    private int maskNumberTopYOffset;
    private int maskNumberBottomYOffset;
    private int maskNumberXOffset;
    private int sloganSize;
    private int sloganDpSize;
    private int sloganColor;
    private int sloganTopYOffset;
    private int sloganBottomYOffset;
    private int sloganXOffset;
    private String loginBtnText;
    private int loginBtnTextSize;
    private int loginBtnTextDpSize;
    private int loginBtnTextColor;
    private int loginBtnWidth;
    private int loginBtnHeight;
    private int loginBtnTopYOffset;
    private int loginBtnBottomYOffset;
    private int loginBtnXOffset;
    private String loginBtnBackgroundRes;
    private Drawable loginBtnBackgroundDrawable;
    private int privacyTextColor;
    private int privacyProtocolColor;
    private int privacySize;
    private int privacyDpSize;
    private int privacyTopYOffset;
    private int privacyBottomYOffset;
    private int privacyXOffset;
    private int privacyMarginRight;
    private boolean privacyState;
    private boolean isHidePrivacySmh;
    private boolean isHidePrivacyCheckBox;
    private boolean isPrivacyTextGravityCenter;
    private int checkBoxGravity;
    private String checkedImageName;
    private Drawable checkedImageDrawable;
    private String unCheckedImageName;
    private Drawable unCheckedImageNameDrawable;
    private String privacyTextStart;
    private String protocolText;
    private String protocolLink;
    private String protocol2Text;
    private String protocol2Link;
    private String privacyTextEnd;
    private String protocolNavTitle;
    private String cmProtocolNavTitle;
    private String ctProtocolNavTitle;
    private String cuProtocolNavTitle;
    private String customProtocolNavTitle;
    private String customProtocol2NavTitle;
    private String protocolNavBackIcon;
    private Drawable protocolNavBackIconDrawable;
    private int protocolNavColor;
    private int protocolNavHeight;
    private int protocolNavTitleColor;
    private int protocolNavTitleSize;
    private int protocolNavTitleDpSize;
    private int protocolNavBackIconWidth;
    private int protocolNavBackIconHeight;
    private boolean isDialogMode;
    private int dialogWidth;
    private int dialogHeight;
    private int dialogX;
    private int dialogY;
    private boolean isBottomDialog;
    private String backgroundImage;
    private Drawable backgroundImageDrawable;
    private String backgroundGif;
    private Drawable backgroundGifDrawable;
    private String backgroundVideo;
    private String backgroundVideoImage;
    private Drawable backgroundVideoImageDrawable;
    private String activityEnterAnimation;
    private String activityExitAnimation;
    private boolean isLandscape;
    private ArrayList<a> customViewHolders;
    private AuthThemeConfig cmAuthThemeConfig;
    private Context context;
    private MaskNumberListener maskNumberListener;
    private LoginListener loginListener;
    private ClickEventListener clickEventListener;
    private ActivityLifecycleCallbacks activityLifecycleCallbacks;

    private UnifyUiConfig(UnifyUiConfig.Builder builder, Context context) {
        this.protocolNavBackIconWidth = 25;
        this.protocolNavBackIconHeight = 25;
        this.statusBarColor = builder.a;
        this.isStatusBarDarkColor = builder.b;
        this.navBackIcon = builder.c;
        this.navBackIconDrawable = builder.d;
        this.navBackIconWidth = builder.f;
        this.navBackIconHeight = builder.g;
        this.isHideBackIcon = builder.e;
        this.navBackgroundColor = builder.h;
        this.navTitle = builder.i;
        this.navHeight = builder.j;
        this.navTitleColor = builder.k;
        this.navTitleSize = builder.l;
        this.navTitleDpSize = builder.m;
        this.isHideNav = builder.n;
        this.isNavTitleBold = builder.o;
        this.logoIconName = builder.p;
        this.logoIconDrawable = builder.q;
        this.logoIconDrawable = builder.q;
        this.logoWidth = builder.r;
        this.logoHeight = builder.s;
        this.logoTopYOffset = builder.t;
        this.logoBottomYOffset = builder.u;
        this.logoXOffset = builder.v;
        this.isHideLogo = builder.w;
        this.maskNumberColor = builder.x;
        this.maskNumberSize = builder.y;
        this.maskNumberDpSize = builder.z;
        this.maskNumberTopYOffset = builder.A;
        this.maskNumberBottomYOffset = builder.B;
        this.maskNumberXOffset = builder.C;
        this.sloganSize = builder.D;
        this.sloganDpSize = builder.E;
        this.sloganColor = builder.F;
        this.sloganTopYOffset = builder.G;
        this.sloganBottomYOffset = builder.H;
        this.sloganXOffset = builder.I;
        this.loginBtnText = builder.J;
        this.loginBtnTextSize = builder.K;
        this.loginBtnTextDpSize = builder.L;
        this.loginBtnTextColor = builder.M;
        this.loginBtnWidth = builder.N;
        this.loginBtnHeight = builder.O;
        this.loginBtnBackgroundRes = builder.P;
        this.loginBtnBackgroundDrawable = builder.Q;
        this.loginBtnTopYOffset = builder.R;
        this.loginBtnBottomYOffset = builder.S;
        this.loginBtnXOffset = builder.T;
        this.privacyTextColor = builder.U;
        this.privacyProtocolColor = builder.V;
        this.privacySize = builder.W;
        this.privacyDpSize = builder.X;
        this.privacyTopYOffset = builder.Y;
        this.privacyBottomYOffset = builder.Z;
        this.privacyXOffset = builder.aa;
        this.privacyMarginRight = builder.ab;
        this.privacyState = builder.ac;
        this.isHidePrivacySmh = builder.ae;
        this.isHidePrivacyCheckBox = builder.ad;
        this.isPrivacyTextGravityCenter = builder.af;
        this.checkBoxGravity = builder.ag;
        this.checkedImageName = builder.ah;
        this.checkedImageDrawable = builder.ai;
        this.unCheckedImageName = builder.aj;
        this.unCheckedImageNameDrawable = builder.ak;
        this.privacyTextStart = builder.al;
        this.protocolText = builder.am;
        this.protocolLink = builder.an;
        this.protocol2Text = builder.ao;
        this.protocol2Link = builder.ap;
        this.privacyTextEnd = builder.aq;
        this.customViewHolders = builder.aY;
        this.backgroundImage = builder.aM;
        this.backgroundImageDrawable = builder.aN;
        this.backgroundGif = builder.aO;
        this.backgroundGifDrawable = builder.aP;
        this.backgroundVideo = builder.aQ;
        this.backgroundVideoImage = builder.aR;
        this.backgroundVideoImageDrawable = builder.aS;
        this.activityEnterAnimation = builder.aT;
        this.activityExitAnimation = builder.aU;
        this.protocolNavTitle = builder.ar;
        this.cmProtocolNavTitle = builder.as;
        this.ctProtocolNavTitle = builder.at;
        this.cuProtocolNavTitle = builder.au;
        this.customProtocolNavTitle = builder.av;
        this.customProtocol2NavTitle = builder.aw;
        this.protocolNavBackIcon = builder.aD;
        this.protocolNavBackIconDrawable = builder.aE;
        this.protocolNavColor = builder.aF;
        this.protocolNavHeight = builder.ax;
        this.protocolNavTitleColor = builder.ay;
        this.protocolNavTitleSize = builder.az;
        this.protocolNavTitleDpSize = builder.aA;
        this.protocolNavBackIconWidth = builder.aB;
        this.protocolNavBackIconHeight = builder.aC;
        this.isDialogMode = builder.aG;
        this.dialogWidth = builder.aH;
        this.dialogHeight = builder.aI;
        this.dialogX = builder.aJ;
        this.dialogY = builder.aK;
        this.isBottomDialog = builder.aL;
        this.isLandscape = builder.isLandscape;
        this.context = context;
        this.maskNumberListener = builder.aV;
        this.loginListener = builder.aW;
        this.clickEventListener = builder.aX;
        this.activityLifecycleCallbacks = builder.aZ;
        this.createCmAuthUiBuilder();
    }

    public int getStatusBarColor() {
        return this.statusBarColor;
    }

    public boolean isStatusBarDarkColor() {
        return this.isStatusBarDarkColor;
    }

    public String getNavBackIcon() {
        return this.navBackIcon;
    }

    public Drawable getNavBackIconDrawable() {
        return this.navBackIconDrawable;
    }

    public int getNavBackIconWidth() {
        return this.navBackIconWidth;
    }

    public int getNavBackIconHeight() {
        return this.navBackIconHeight;
    }

    public boolean isHideBackIcon() {
        return this.isHideBackIcon;
    }

    public int getNavBackgroundColor() {
        return this.navBackgroundColor;
    }

    public String getNavTitle() {
        return this.navTitle;
    }

    public int getNavHeight() {
        return this.navHeight;
    }

    public int getNavTitleColor() {
        return this.navTitleColor;
    }

    public int getNavTitleSize() {
        return this.navTitleSize;
    }

    public int getNavTitleDpSize() {
        return this.navTitleDpSize;
    }

    public boolean isHideNav() {
        return this.isHideNav;
    }

    public boolean isNavTitleBold() {
        return this.isNavTitleBold;
    }

    public String getLogoIconName() {
        return this.logoIconName;
    }

    public int getLogoWidth() {
        return this.logoWidth;
    }

    public int getLogoHeight() {
        return this.logoHeight;
    }

    public int getLogoTopYOffset() {
        return this.logoTopYOffset;
    }

    public int getLogoBottomYOffset() {
        return this.logoBottomYOffset;
    }

    public int getLogoXOffset() {
        return this.logoXOffset;
    }

    public boolean isHideLogo() {
        return this.isHideLogo;
    }

    public int getMaskNumberColor() {
        return this.maskNumberColor;
    }

    public int getMaskNumberSize() {
        return this.maskNumberSize;
    }

    public int getMaskNumberTopYOffset() {
        return this.maskNumberTopYOffset;
    }

    public int getMaskNumberBottomYOffset() {
        return this.maskNumberBottomYOffset;
    }

    public int getMaskNumberXOffset() {
        return this.maskNumberXOffset;
    }

    public int getSloganSize() {
        return this.sloganSize;
    }

    public int getSloganColor() {
        return this.sloganColor;
    }

    public int getSloganTopYOffset() {
        return this.sloganTopYOffset;
    }

    public int getSloganBottomYOffset() {
        return this.sloganBottomYOffset;
    }

    public int getSloganXOffset() {
        return this.sloganXOffset;
    }

    public String getLoginBtnText() {
        return this.loginBtnText;
    }

    public int getLoginBtnTextSize() {
        return this.loginBtnTextSize;
    }

    public int getLoginBtnTextColor() {
        return this.loginBtnTextColor;
    }

    public int getLoginBtnWidth() {
        return this.loginBtnWidth;
    }

    public int getLoginBtnHeight() {
        return this.loginBtnHeight;
    }

    public int getLoginBtnTopYOffset() {
        return this.loginBtnTopYOffset;
    }

    public int getLoginBtnBottomYOffset() {
        return this.loginBtnBottomYOffset;
    }

    public int getLoginBtnXOffset() {
        return this.loginBtnXOffset;
    }

    public String getLoginBtnBackgroundRes() {
        return this.loginBtnBackgroundRes;
    }

    public int getPrivacyTextColor() {
        return this.privacyTextColor;
    }

    public int getPrivacyProtocolColor() {
        return this.privacyProtocolColor;
    }

    public int getPrivacySize() {
        return this.privacySize;
    }

    public int getPrivacyTopYOffset() {
        return this.privacyTopYOffset;
    }

    public int getPrivacyBottomYOffset() {
        return this.privacyBottomYOffset;
    }

    public boolean isPrivacyState() {
        return this.privacyState;
    }

    public boolean isPrivacyTextGravityCenter() {
        return this.isPrivacyTextGravityCenter;
    }

    public int getCheckBoxGravity() {
        return this.checkBoxGravity;
    }

    public String getCheckedImageName() {
        return this.checkedImageName;
    }

    public String getUnCheckedImageName() {
        return this.unCheckedImageName;
    }

    public String getPrivacyTextStart() {
        return this.privacyTextStart;
    }

    public String getProtocolText() {
        return this.protocolText;
    }

    public String getProtocolLink() {
        return this.protocolLink;
    }

    public String getProtocol2Text() {
        return this.protocol2Text;
    }

    public String getProtocol2Link() {
        return this.protocol2Link;
    }

    public String getPrivacyTextEnd() {
        return this.privacyTextEnd;
    }

    public int getPrivacyXOffset() {
        return this.privacyXOffset;
    }

    public int getPrivacyMarginRight() {
        return this.privacyMarginRight;
    }

    public String getProtocolNavTitle() {
        return this.protocolNavTitle;
    }

    public String getCmProtocolNavTitle() {
        return this.cmProtocolNavTitle;
    }

    public String getCtProtocolNavTitle() {
        return this.ctProtocolNavTitle;
    }

    public String getCuProtocolNavTitle() {
        return this.cuProtocolNavTitle;
    }

    public String getCustomProtocolNavTitle() {
        return this.customProtocolNavTitle;
    }

    public String getCustomProtocol2NavTitle() {
        return this.customProtocol2NavTitle;
    }

    public String getProtocolNavBackIcon() {
        return this.protocolNavBackIcon;
    }

    public int getProtocolNavColor() {
        return this.protocolNavColor;
    }

    public int getProtocolNavHeight() {
        return this.protocolNavHeight;
    }

    public int getProtocolNavTitleColor() {
        return this.protocolNavTitleColor;
    }

    public int getProtocolNavTitleSize() {
        return this.protocolNavTitleSize;
    }

    public int getProtocolNavTitleDpSize() {
        return this.protocolNavTitleDpSize;
    }

    public int getProtocolNavBackIconWidth() {
        return this.protocolNavBackIconWidth;
    }

    public int getProtocolNavBackIconHeight() {
        return this.protocolNavBackIconHeight;
    }

    public boolean isDialogMode() {
        return this.isDialogMode;
    }

    public int getDialogWidth() {
        return this.dialogWidth;
    }

    public int getDialogHeight() {
        return this.dialogHeight;
    }

    public int getDialogX() {
        return this.dialogX;
    }

    public int getDialogY() {
        return this.dialogY;
    }

    public boolean isBottomDialog() {
        return this.isBottomDialog;
    }

    public boolean isHidePrivacyCheckBox() {
        return this.isHidePrivacyCheckBox;
    }

    public boolean isHidePrivacySmh() {
        return this.isHidePrivacySmh;
    }

    public String getBackgroundImage() {
        return this.backgroundImage;
    }

    public String getBackgroundGif() {
        return this.backgroundGif;
    }

    public String getBackgroundVideo() {
        return this.backgroundVideo;
    }

    public String getBackgroundVideoImage() {
        return this.backgroundVideoImage;
    }

    public String getActivityEnterAnimation() {
        return this.activityEnterAnimation;
    }

    public String getActivityExitAnimation() {
        return this.activityExitAnimation;
    }

    public boolean isLandscape() {
        return this.isLandscape;
    }

    public MaskNumberListener getMaskNumberListener() {
        return this.maskNumberListener;
    }

    public LoginListener getLoginListener() {
        return this.loginListener;
    }

    public ClickEventListener getClickEventListener() {
        return this.clickEventListener;
    }

    public ActivityLifecycleCallbacks getActivityLifecycleCallbacks() {
        return this.activityLifecycleCallbacks;
    }

    public int getMaskNumberDpSize() {
        return this.maskNumberDpSize;
    }

    public int getSloganDpSize() {
        return this.sloganDpSize;
    }

    public int getLoginBtnTextDpSize() {
        return this.loginBtnTextDpSize;
    }

    public int getPrivacyDpSize() {
        return this.privacyDpSize;
    }

    public AuthThemeConfig getCmAuthThemeConfig() {
        return this.cmAuthThemeConfig == null ? (new com.cmic.sso.wy.AuthThemeConfig.Builder()).build() : this.cmAuthThemeConfig;
    }

    public ArrayList<a> getCustomViewHolders() {
        return this.customViewHolders;
    }

    public Drawable getLogoIconDrawable() {
        return this.logoIconDrawable;
    }

    public Drawable getLoginBtnBackgroundDrawable() {
        return this.loginBtnBackgroundDrawable;
    }

    public Drawable getCheckedImageDrawable() {
        return this.checkedImageDrawable;
    }

    public Drawable getUnCheckedImageNameDrawable() {
        return this.unCheckedImageNameDrawable;
    }

    public Drawable getProtocolNavBackIconDrawable() {
        return this.protocolNavBackIconDrawable;
    }

    public Drawable getBackgroundImageDrawable() {
        return this.backgroundImageDrawable;
    }

    public Drawable getBackgroundGifDrawable() {
        return this.backgroundGifDrawable;
    }

    public Drawable getBackgroundVideoImageDrawable() {
        return this.backgroundVideoImageDrawable;
    }

    private void createCmAuthUiBuilder() {
        com.cmic.sso.wy.AuthThemeConfig.Builder var1 = new com.cmic.sso.wy.AuthThemeConfig.Builder();
        int var2 = this.navHeight == 0 ? g.b(this.context) : this.navHeight;
        if (this.maskNumberSize != 0) {
            var1.setNumberSize(this.maskNumberSize);
        }

        if (this.maskNumberColor != 0) {
            var1.setNumberColor(this.maskNumberColor);
        }

        if (this.maskNumberTopYOffset != 0) {
            var1.setNumFieldOffsetY(this.maskNumberTopYOffset + var2);
        }

        if (this.maskNumberBottomYOffset != 0) {
            var1.setNumFieldOffsetY_B(this.maskNumberBottomYOffset);
        }

        var1.setLogBtn(this.loginBtnWidth, this.loginBtnHeight).setLogBtnMargin(0, 0).setLogBtnText(this.loginBtnText, this.loginBtnTextColor, this.loginBtnTextSize);
        if (this.loginBtnTopYOffset != 0) {
            var1.setLogBtnOffsetY(this.loginBtnTopYOffset + var2);
        }

        if (this.loginBtnBottomYOffset != 0) {
            var1.setLogBtnOffsetY_B(this.loginBtnBottomYOffset);
        }

        var1.setLogBtnClickListener(new LoginClickListener() {
            public void onLoginClickStart(Context context, JSONObject jsonObject) {
            }

            public void onLoginClickComplete(Context context, JSONObject jsonObject) {
            }
        });
        var1.setAuthPageActIn(this.activityEnterAnimation, this.activityEnterAnimation);
        var1.setAuthPageActOut(this.activityExitAnimation, this.activityExitAnimation);
        var1.setAuthLayoutResID(this.context.getResources().getIdentifier("activity_quick_login_cm", "layout", this.context.getPackageName()));
        this.cmAuthThemeConfig = var1.build();
        AuthnHelper.getInstance(this.context).setAuthThemeConfig(this.cmAuthThemeConfig);
    }

    public static class Builder {
        private int a = -1;
        private boolean b = false;
        private String c;
        private Drawable d;
        private boolean e;
        private int f = 25;
        private int g = 25;
        private int h;
        private String i;
        private int j;
        private int k;
        private int l;
        private int m;
        private boolean n = false;
        private boolean o;
        private String p;
        private Drawable q;
        private int r;
        private int s;
        private int t;
        private int u;
        private int v;
        private boolean w = false;
        private int x;
        private int y;
        private int z;
        private int A;
        private int B;
        private int C;
        private int D = 10;
        private int E;
        private int F = -16776961;
        private int G;
        private int H;
        private int I;
        private String J = "本机号码一键登录";
        private int K;
        private int L;
        private int M = -1;
        private int N;
        private int O;
        private String P;
        private Drawable Q;
        private int R;
        private int S;
        private int T;
        private int U = -16777216;
        private int V = -7829368;
        private int W;
        private int X;
        private int Y;
        private int Z;
        private int aa;
        private int ab;
        private boolean ac = true;
        private boolean ad = false;
        private boolean ae = false;
        private boolean af = false;
        private int ag;
        private String ah = "yd_checkbox_checked";
        private Drawable ai;
        private String aj = "yd_checkbox_unchecked";
        private Drawable ak;
        private String al = "登录即同意";
        private String am;
        private String an;
        private String ao;
        private String ap;
        private String aq = "且授权使用本机号码登录";
        private String ar;
        private String as;
        private String at;
        private String au;
        private String av;
        private String aw;
        private int ax;
        private int ay;
        private int az;
        private int aA;
        private int aB = 25;
        private int aC = 25;
        private String aD;
        private Drawable aE;
        private int aF;
        private boolean aG;
        private int aH;
        private int aI;
        private int aJ;
        private int aK;
        private boolean aL;
        public boolean isLandscape;
        private String aM;
        private Drawable aN;
        private String aO;
        private Drawable aP;
        private String aQ;
        private String aR;
        private Drawable aS;
        private String aT;
        private String aU;
        private MaskNumberListener aV;
        private LoginListener aW;
        private ClickEventListener aX;
        private ArrayList<a> aY;
        private ActivityLifecycleCallbacks aZ;

        public Builder() {
        }

        public UnifyUiConfig.Builder setStatusBarColor(int statusBarColor) {
            this.a = statusBarColor;
            return this;
        }

        public UnifyUiConfig.Builder setStatusBarDarkColor(boolean statusBarDarkColor) {
            this.b = statusBarDarkColor;
            return this;
        }

        public UnifyUiConfig.Builder setNavigationIcon(String backIcon) {
            this.c = backIcon;
            return this;
        }

        public UnifyUiConfig.Builder setNavigationIconDrawable(Drawable navBackIconDrawable) {
            this.d = navBackIconDrawable;
            return this;
        }

        public UnifyUiConfig.Builder setNavigationBackIconWidth(int backIconWidth) {
            this.f = backIconWidth;
            return this;
        }

        public UnifyUiConfig.Builder setNavigationBackIconHeight(int backIconHeight) {
            this.g = backIconHeight;
            return this;
        }

        public UnifyUiConfig.Builder setHideNavigationBackIcon(boolean isHideBackIcon) {
            this.e = isHideBackIcon;
            return this;
        }

        public UnifyUiConfig.Builder setNavigationBackgroundColor(int backgroundColor) {
            this.h = backgroundColor;
            return this;
        }

        public UnifyUiConfig.Builder setNavigationHeight(int navHeight) {
            this.j = navHeight;
            return this;
        }

        public UnifyUiConfig.Builder setNavigationTitle(String title) {
            this.i = title;
            return this;
        }

        public UnifyUiConfig.Builder setNavigationTitleColor(int titleColor) {
            this.k = titleColor;
            return this;
        }

        public UnifyUiConfig.Builder setNavTitleSize(int navTitleSize) {
            this.l = navTitleSize;
            return this;
        }

        public UnifyUiConfig.Builder setNavTitleDpSize(int navTitleDpSize) {
            this.m = navTitleDpSize;
            return this;
        }

        public UnifyUiConfig.Builder setHideNavigation(boolean isHideNavigation) {
            this.n = isHideNavigation;
            return this;
        }

        public UnifyUiConfig.Builder setNavTitleBold(boolean navTitleBold) {
            this.o = navTitleBold;
            return this;
        }

        public UnifyUiConfig.Builder setLogoIconName(String logoIconName) {
            this.p = logoIconName;
            return this;
        }

        public UnifyUiConfig.Builder setLogoIconDrawable(Drawable logoIconDrawable) {
            this.q = logoIconDrawable;
            return this;
        }

        public UnifyUiConfig.Builder setLogoWidth(int logoWidth) {
            this.r = logoWidth;
            return this;
        }

        public UnifyUiConfig.Builder setLogoHeight(int logoHeight) {
            this.s = logoHeight;
            return this;
        }

        public UnifyUiConfig.Builder setLogoTopYOffset(int logoTopYOffset) {
            this.t = logoTopYOffset;
            return this;
        }

        public UnifyUiConfig.Builder setLogoBottomYOffset(int logoBottomYOffset) {
            this.u = logoBottomYOffset;
            return this;
        }

        public UnifyUiConfig.Builder setLogoXOffset(int logoXOffset) {
            this.v = logoXOffset;
            return this;
        }

        public UnifyUiConfig.Builder setHideLogo(boolean hideLogo) {
            this.w = hideLogo;
            return this;
        }

        public UnifyUiConfig.Builder setMaskNumberColor(int maskNumberColor) {
            this.x = maskNumberColor;
            return this;
        }

        public UnifyUiConfig.Builder setMaskNumberSize(int maskNumberSize) {
            this.y = maskNumberSize;
            return this;
        }

        public UnifyUiConfig.Builder setMaskNumberDpSize(int maskNumberDpSize) {
            this.z = maskNumberDpSize;
            return this;
        }

        public UnifyUiConfig.Builder setMaskNumberTopYOffset(int maskNumberTopYOffset) {
            this.A = maskNumberTopYOffset;
            return this;
        }

        public UnifyUiConfig.Builder setMaskNumberBottomYOffset(int maskNumberBottomYOffset) {
            this.B = maskNumberBottomYOffset;
            return this;
        }

        public UnifyUiConfig.Builder setMaskNumberXOffset(int maskNumberXOffset) {
            this.C = maskNumberXOffset;
            return this;
        }

        public UnifyUiConfig.Builder setSloganSize(int sloganSize) {
            this.D = sloganSize;
            return this;
        }

        public UnifyUiConfig.Builder setSloganDpSize(int sloganDpSize) {
            this.E = sloganDpSize;
            return this;
        }

        public UnifyUiConfig.Builder setSloganColor(int sloganColor) {
            this.F = sloganColor;
            return this;
        }

        public UnifyUiConfig.Builder setSloganTopYOffset(int sloganTopYOffset) {
            this.G = sloganTopYOffset;
            return this;
        }

        public UnifyUiConfig.Builder setSloganBottomYOffset(int sloganBottomYOffset) {
            this.H = sloganBottomYOffset;
            return this;
        }

        public UnifyUiConfig.Builder setSloganXOffset(int sloganXOffset) {
            this.I = sloganXOffset;
            return this;
        }

        public UnifyUiConfig.Builder setLoginBtnText(String loginBtnText) {
            this.J = loginBtnText;
            return this;
        }

        public UnifyUiConfig.Builder setLoginBtnTextSize(int loginBtnTextSize) {
            this.K = loginBtnTextSize;
            return this;
        }

        public UnifyUiConfig.Builder setLoginBtnTextDpSize(int loginBtnTextDpSize) {
            this.L = loginBtnTextDpSize;
            return this;
        }

        public UnifyUiConfig.Builder setLoginBtnTextColor(int loginBtnTextColor) {
            this.M = loginBtnTextColor;
            return this;
        }

        public UnifyUiConfig.Builder setLoginBtnWidth(int loginBtnWidth) {
            this.N = loginBtnWidth;
            return this;
        }

        public UnifyUiConfig.Builder setLoginBtnHeight(int loginBtnHeight) {
            this.O = loginBtnHeight;
            return this;
        }

        public UnifyUiConfig.Builder setLoginBtnBackgroundRes(String loginBtnBackgroundRes) {
            this.P = loginBtnBackgroundRes;
            return this;
        }

        public UnifyUiConfig.Builder setLoginBtnBackgroundDrawable(Drawable loginBtnBackgroundDrawable) {
            this.Q = loginBtnBackgroundDrawable;
            return this;
        }

        public UnifyUiConfig.Builder setLoginBtnTopYOffset(int loginBtnTopYOffset) {
            this.R = loginBtnTopYOffset;
            return this;
        }

        public UnifyUiConfig.Builder setLoginBtnBottomYOffset(int loginBtnBottomYOffset) {
            this.S = loginBtnBottomYOffset;
            return this;
        }

        public UnifyUiConfig.Builder setLoginBtnXOffset(int loginBtnXOffset) {
            this.T = loginBtnXOffset;
            return this;
        }

        public UnifyUiConfig.Builder setPrivacyTextColor(int privacyTextColor) {
            this.U = privacyTextColor;
            return this;
        }

        public UnifyUiConfig.Builder setPrivacyProtocolColor(int privacyProtocolColor) {
            this.V = privacyProtocolColor;
            return this;
        }

        public UnifyUiConfig.Builder setPrivacySize(int privacySize) {
            this.W = privacySize;
            return this;
        }

        public UnifyUiConfig.Builder setPrivacyDpSize(int privacyDpSize) {
            this.X = privacyDpSize;
            return this;
        }

        public UnifyUiConfig.Builder setPrivacyTopYOffset(int privacyTopYOffset) {
            this.Y = privacyTopYOffset;
            return this;
        }

        public UnifyUiConfig.Builder setPrivacyBottomYOffset(int privacyBottomYOffset) {
            this.Z = privacyBottomYOffset;
            return this;
        }

        public UnifyUiConfig.Builder setPrivacyXOffset(int privacyXOffset) {
            this.aa = privacyXOffset;
            return this;
        }

        public UnifyUiConfig.Builder setPrivacyMarginRight(int privacyMarginRight) {
            this.ab = privacyMarginRight;
            return this;
        }

        public UnifyUiConfig.Builder setPrivacyState(boolean privacyState) {
            this.ac = privacyState;
            return this;
        }

        public UnifyUiConfig.Builder setHidePrivacyCheckBox(boolean hidePrivacyCheckBox) {
            this.ad = hidePrivacyCheckBox;
            return this;
        }

        public UnifyUiConfig.Builder setPrivacyTextGravityCenter(boolean privacyTextGravityCenter) {
            this.af = privacyTextGravityCenter;
            return this;
        }

        public UnifyUiConfig.Builder setHidePrivacySmh(boolean hidePrivacySmh) {
            this.ae = hidePrivacySmh;
            return this;
        }

        public UnifyUiConfig.Builder setCheckBoxGravity(int checkBoxGravity) {
            this.ag = checkBoxGravity;
            return this;
        }

        public UnifyUiConfig.Builder setCheckedImageName(String checkedImageName) {
            this.ah = checkedImageName;
            return this;
        }

        public UnifyUiConfig.Builder setCheckedImageDrawable(Drawable checkedImageDrawable) {
            this.ai = checkedImageDrawable;
            return this;
        }

        public UnifyUiConfig.Builder setUnCheckedImageName(String unCheckedImageName) {
            this.aj = unCheckedImageName;
            return this;
        }

        public UnifyUiConfig.Builder setUnCheckedImageDrawable(Drawable unCheckedImageNameDrawable) {
            this.ak = unCheckedImageNameDrawable;
            return this;
        }

        public UnifyUiConfig.Builder setPrivacyTextStart(String privacyTextStart) {
            this.al = privacyTextStart;
            return this;
        }

        public UnifyUiConfig.Builder setProtocolText(String protocolText) {
            this.am = protocolText;
            return this;
        }

        public UnifyUiConfig.Builder setProtocolLink(String protocolLink) {
            this.an = protocolLink;
            return this;
        }

        public UnifyUiConfig.Builder setProtocol2Text(String protocol2Text) {
            this.ao = protocol2Text;
            return this;
        }

        public UnifyUiConfig.Builder setProtocol2Link(String protocol2Link) {
            this.ap = protocol2Link;
            return this;
        }

        public UnifyUiConfig.Builder setPrivacyTextEnd(String privacyTextEnd) {
            this.aq = privacyTextEnd;
            return this;
        }

        /** @deprecated */
        @Deprecated
        public UnifyUiConfig.Builder setProtocolPageNavTitle(String protocolNavTitle) {
            this.ar = protocolNavTitle;
            return this;
        }

        public UnifyUiConfig.Builder setProtocolPageNavTitle(String cmProtocolNavTitle, String cuProtocolNavTitle, String ctProtocolNavTitle) {
            this.as = cmProtocolNavTitle;
            this.au = cuProtocolNavTitle;
            this.at = ctProtocolNavTitle;
            return this;
        }

        public UnifyUiConfig.Builder setProtocolPageNavTitle(String cmProtocolNavTitle, String cuProtocolNavTitle, String ctProtocolNavTitle, String protocolTextNavTitle, String protocol2TextNavTitle) {
            this.as = cmProtocolNavTitle;
            this.au = cuProtocolNavTitle;
            this.at = ctProtocolNavTitle;
            this.av = protocolTextNavTitle;
            this.aw = protocol2TextNavTitle;
            return this;
        }

        public UnifyUiConfig.Builder setProtocolPageNavBackIcon(String protocolNavBackIcon) {
            this.aD = protocolNavBackIcon;
            return this;
        }

        public UnifyUiConfig.Builder setProtocolPageNavBackIconDrawable(Drawable protocolNavBackIconDrawable) {
            this.aE = protocolNavBackIconDrawable;
            return this;
        }

        public UnifyUiConfig.Builder setProtocolPageNavColor(int protocolNavColor) {
            this.aF = protocolNavColor;
            return this;
        }

        public UnifyUiConfig.Builder setProtocolPageNavHeight(int protocolNavHeight) {
            this.ax = protocolNavHeight;
            return this;
        }

        public UnifyUiConfig.Builder setProtocolPageNavTitleColor(int protocolNavTitleColor) {
            this.ay = protocolNavTitleColor;
            return this;
        }

        public UnifyUiConfig.Builder setProtocolPageNavTitleSize(int protocolNavTitleSize) {
            this.az = protocolNavTitleSize;
            return this;
        }

        public UnifyUiConfig.Builder setProtocolPageNavTitleDpSize(int protocolNavTitleDpSize) {
            this.aA = protocolNavTitleDpSize;
            return this;
        }

        public UnifyUiConfig.Builder setProtocolPageNavBackIconWidth(int protocolNavBackIconWidth) {
            this.aB = protocolNavBackIconWidth;
            return this;
        }

        public UnifyUiConfig.Builder setProtocolPageNavBackIconHeight(int protocolNavBackIconHeight) {
            this.aC = protocolNavBackIconHeight;
            return this;
        }

        public UnifyUiConfig.Builder setDialogMode(boolean isDialogMode) {
            this.aG = isDialogMode;
            return this;
        }

        public UnifyUiConfig.Builder setDialogMode(boolean isDialogMode, int dialogWidth, int dialogHeight, int dialogX, int dialogY, boolean isBottomDialog) {
            this.aG = isDialogMode;
            this.aH = dialogWidth;
            this.aI = dialogHeight;
            this.aJ = dialogX;
            this.aK = dialogY;
            this.aL = isBottomDialog;
            return this;
        }

        public UnifyUiConfig.Builder setDialogWidth(int dialogWidth) {
            this.aH = dialogWidth;
            return this;
        }

        public UnifyUiConfig.Builder setDialogHeight(int dialogHeight) {
            this.aI = dialogHeight;
            return this;
        }

        public UnifyUiConfig.Builder setDialogX(int dialogX) {
            this.aJ = dialogX;
            return this;
        }

        public UnifyUiConfig.Builder setDialogY(int dialogY) {
            this.aK = dialogY;
            return this;
        }

        public UnifyUiConfig.Builder setBottomDialog(boolean bottomDialog) {
            this.aL = bottomDialog;
            return this;
        }

        public UnifyUiConfig.Builder setLandscape(boolean landscape) {
            this.isLandscape = landscape;
            return this;
        }

        public UnifyUiConfig.Builder setBackgroundImage(String backgroundImage) {
            this.aM = backgroundImage;
            return this;
        }

        public UnifyUiConfig.Builder setBackgroundImageDrawable(Drawable backgroundImageDrawable) {
            this.aN = backgroundImageDrawable;
            return this;
        }

        public UnifyUiConfig.Builder setBackgroundGif(String backgroundGif) {
            this.aO = backgroundGif;
            return this;
        }

        public UnifyUiConfig.Builder setBackgroundGifDrawable(Drawable backgroundGifDrawable) {
            this.aP = backgroundGifDrawable;
            return this;
        }

        public UnifyUiConfig.Builder setBackgroundVideo(String videoPath, String videoImage) {
            this.aQ = videoPath;
            this.aR = videoImage;
            return this;
        }

        public UnifyUiConfig.Builder setBackgroundVideo(String videoPath, Drawable videoImageDrawable) {
            this.aQ = videoPath;
            this.aS = videoImageDrawable;
            return this;
        }

        public UnifyUiConfig.Builder setActivityTranslateAnimation(String enterAnimation, String exitAnimation) {
            this.aT = enterAnimation;
            this.aU = exitAnimation;
            return this;
        }

        public UnifyUiConfig.Builder setMaskNumberListener(MaskNumberListener maskNumberListener) {
            this.aV = maskNumberListener;
            return this;
        }

        public UnifyUiConfig.Builder setLoginListener(LoginListener loginListener) {
            this.aW = loginListener;
            return this;
        }

        public UnifyUiConfig.Builder setClickEventListener(ClickEventListener clickEventListener) {
            this.aX = clickEventListener;
            return this;
        }

        public UnifyUiConfig.Builder addCustomView(View customView, String viewId, int positionType, CustomViewListener listener) {
            if (customView == null) {
                return this;
            } else {
                if (this.aY == null) {
                    this.aY = new ArrayList();
                }

                a var5 = new a();
                var5.a = customView;
                var5.b = viewId;
                var5.c = positionType;
                var5.d = listener;
                this.aY.add(var5);
                return this;
            }
        }

        public UnifyUiConfig.Builder setActivityLifecycleCallbacks(ActivityLifecycleCallbacks Callbacks) {
            this.aZ = Callbacks;
            return this;
        }

        public UnifyUiConfig build(Context context) {
            return new UnifyUiConfig(this, context);
        }
    }
}
