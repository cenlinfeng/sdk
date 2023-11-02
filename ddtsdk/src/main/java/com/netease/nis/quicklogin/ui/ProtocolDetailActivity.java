package com.netease.nis.quicklogin.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class ProtocolDetailActivity extends Activity {
    private WebView a;

    public ProtocolDetailActivity() {
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(resourceId("activity_protocol_detail", "layout"));
        this.a = (WebView)this.findViewById(resourceId("wv_protocol", "id"));
        this.a();
        Intent var2 = this.getIntent();
        this.a(var2);
    }

    private void a() {
        WebSettings var1 = this.a.getSettings();
        var1.setJavaScriptEnabled(true);
        var1.setSupportZoom(true);
        var1.setBuiltInZoomControls(true);
        var1.setSupportMultipleWindows(true);
        var1.setLoadWithOverviewMode(true);
        var1.setDomStorageEnabled(true);
        var1.setDatabaseEnabled(true);
        this.a.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
    }

    private void a(Intent var1) {
        String var2 = var1.getStringExtra("url");
        String var3 = var1.getStringExtra("title");
        if (!TextUtils.isEmpty(var3)) {
            TextView var4 = (TextView)this.findViewById(resourceId("yd_navigation_title", "id"));
            if (var4 != null) {
                var4.setText(var3);
            }
        }

        this.a.loadUrl(var2);
    }

    public void back(View view) {
        this.finish();
    }

    public int resourceId(String name, String type) {
        return getResources().getIdentifier(name, type, getPackageName());
    }
}