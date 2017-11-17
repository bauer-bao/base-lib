package com.babase.lib.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.babase.lib.utils.Logger;
import com.babase.lib.utils.Network;

/**
 * @author bauer on 17/6/8.
 */
@SuppressLint("SetJavaScriptEnabled")
public class BaWebView extends WebView {
    /**
     * webview的接口
     */
    private WebViewImp myWebviewImp;
    private WebSettings settings;

    /**
     * 是否支持javascript
     */
    private boolean isSupportJavaScript = true;

    /**
     * 水平滚动条隐藏
     */
    private boolean isShowHorizontalScrollBarEnabled = false;

    /**
     * 竖直滚动条隐藏
     */
    private boolean isShowVerticalScrollBarEnabled = false;
    private boolean loadFailed = false;

    public BaWebView(Context context) {
        super(context);
        init();
    }

    public BaWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        this.setHorizontalScrollBarEnabled(isShowHorizontalScrollBarEnabled);
        this.setVerticalScrollBarEnabled(isShowVerticalScrollBarEnabled);

        settings = getSettings();
        settings.setJavaScriptEnabled(isSupportJavaScript);
//        settings.setSavePassword(false);

//        settings.setAllowFileAccess(true);// 设置可以访问网络
//        settings.setSupportZoom(true);
//        settings.setBuiltInZoomControls(false);// 设置不支持缩放
//        settings.setJavaScriptCanOpenWindowsAutomatically(true);
//        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);//默认为normal
//
//        settings.setSaveFormData(false);
        // 开启 Application Caches 功能
        settings.setAppCacheEnabled(true);
//        // 设置 Application Caches 缓存目录
//        // webSettings.setAppCachePath(cacheDirPath);
        // 开启 DOM storage API 功能
        settings.setDomStorageEnabled(true);
        settings.setDefaultTextEncodingName("UTF-8");
//        // 开启 database storage API 功能
//        settings.setDatabaseEnabled(true);
//        // 设置数据库缓存路径
//        // webSettings.setDatabasePath(cacheDirPath);
//        settings.setPluginState(WebSettings.PluginState.ON);

//        setWebChromeClient();
        setWebViewClient();
    }

    public void load(String url) {
        if (Network.getNetWorkType(getContext()) == Network.NETWORKTYPE_INVALID) {
            //无网络的时候，使用缓存
            Logger.d("no network");
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        } else {//有网络的情况下，不使用缓存
            Logger.d("has network");
            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        }
        loadFailed = false;
        loadUrl(url);
    }

    /**
     * 设置是否使用javascriot
     */
    public BaWebView setSupportJavaScript(boolean isSupportJavaScript) {
        this.isSupportJavaScript = isSupportJavaScript;
        return this;
    }

    /**
     * 是否显示滚动条
     */
    public BaWebView setShowScrollBarEnabled(boolean isShow) {
        this.isShowHorizontalScrollBarEnabled = isShow;
        this.isShowVerticalScrollBarEnabled = isShow;
        return this;
    }

    private void setWebViewClient() {
        this.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Logger.d("load error------> ");
                loadFailed = true;
                if (myWebviewImp != null) {
                    myWebviewImp.loadFailed();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Logger.d("load finished---->");
                if (null != myWebviewImp) {
                    if (!loadFailed) {
                        myWebviewImp.loadFinish();
                    }
                }
            }
        });
    }

    /**
     * 对外接口
     *
     * @param myWebviewImp
     * @return
     */
    public BaWebView setWebViewImp(WebViewImp myWebviewImp) {
        this.myWebviewImp = myWebviewImp;
        return this;
    }

    public interface WebViewImp {
        /**
         * 加载失败
         */
        void loadFailed();

        /**
         * 加载中
         */
        void loading();

        /**
         * 加载完成
         */
        void loadFinish();
    }
}
