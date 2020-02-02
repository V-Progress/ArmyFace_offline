package com.yunbiao.armyface_offline.query;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.yunbiao.armyface_offline.R;
import com.yunbiao.armyface_offline.act.BaseActivity;
import com.yunbiao.armyface_offline.utils.SpUtils;

public class QueryResultActivity extends BaseActivity {
    private WebView webView;
    private String url;
    private FrameLayout webViewRoot;
    private WebView newWebView;

    @Override
    protected int getLayout_h() {
        return R.layout.activity_query_result;
    }

    @Override
    protected int getLayout_s() {
        return R.layout.activity_query_result;
    }

    private static final String TAG = "QueryResultActivity";

    @Override
    protected void initView() {
        fv(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        webViewRoot = fv(R.id.fl_webview_root);
        webView = fv(R.id.web_view);
        initWebView(webView);

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {//html中调用window.open()，会回调此函数
                //新创建一个webview
                newWebView = new WebView(QueryResultActivity.this);

                initWebView(newWebView);//初始化webview

                webViewRoot.addView(newWebView);//把webview加载到activity界面上
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;//以下的操作应该就是让新的webview去加载对应的url等操作。
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();

                newWebView.setWebChromeClient(new WebChromeClient() {
                    @Override
                    public void onCloseWindow(WebView window) {
                        super.onCloseWindow(window);
                        Log.e(TAG, "onCloseWindow: 子页面关闭");
                        if (newWebView != null) {
                            webViewRoot.removeView(newWebView);
                        }
                    }
                });
                return true;
            }
        });
    }

    @Override
    protected void initData() {
        String userCode = getIntent().getStringExtra("userCode");

        //"http://39.107.125.63:8081/getTrainList?userCode="
        url = SpUtils.getStr(SpUtils.WEB_URL, "http://192.168.43.226:8081/getTrainList?userCode=");
        url += userCode;

        webView.loadUrl(url);
    }

    private void initWebView(WebView webView) {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings settings = webView.getSettings();
//        settings.setUseWideViewPort(true);//自适应屏幕
//        settings.setLoadWithOverviewMode(true);//自适应屏幕
        settings.setSupportMultipleWindows(true);//允许多窗口
        settings.setJavaScriptEnabled(true);//支持JS
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);//在File域下，能够执行任意的JavaScript代码，同源策略跨域访问能够对私有目录文件进行访问等
        settings.setAllowContentAccess(true);//是否允许在WebView中访问内容URL（Content Url），默认允许

        int screenDensity = getResources().getDisplayMetrics().densityDpi;
        WebSettings.ZoomDensity zoomDensity;
        switch (screenDensity) {
            case DisplayMetrics.DENSITY_LOW:
                zoomDensity = WebSettings.ZoomDensity.CLOSE;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                zoomDensity = WebSettings.ZoomDensity.MEDIUM;
                break;
            case DisplayMetrics.DENSITY_HIGH:
            case DisplayMetrics.DENSITY_XHIGH:
            case DisplayMetrics.DENSITY_XXHIGH:
            default:
                zoomDensity = WebSettings.ZoomDensity.FAR;
                break;
        }
        settings.setDefaultZoom(zoomDensity);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (newWebView != null) {
            newWebView.onPause();
        }
        webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (newWebView != null) {
            newWebView.onResume();
        }
        webView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (newWebView != null) {
            newWebView.destroy();
            try {
                webViewRoot.removeView(newWebView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        webView.destroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (TextUtils.equals(url, webView.getUrl())) {
            return super.onKeyDown(keyCode, event);/**/
        } else {
            webView.goBack();
        }
        return true;
    }
}
