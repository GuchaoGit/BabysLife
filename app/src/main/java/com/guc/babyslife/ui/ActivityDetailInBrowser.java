package com.guc.babyslife.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.guc.babyslife.R;
import com.guc.babyslife.app.BaseActivity;
import com.guc.babyslife.widget.ToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guc on 2019/12/9.
 * 描述：通过url加载详情
 */
public class ActivityDetailInBrowser extends BaseActivity {

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.webview_content)
    WebView mWebviewContent;
    @BindView(R.id.toolbar)
    ToolBar mToolbar;

    private String mUrl;
    private String mTitle;

    public static void showDetail(Context context, String url) {
        showDetail(context, url, "详情");
    }

    public static void showDetail(Context context, String url, String title) {
        Intent intent = new Intent(context, ActivityDetailInBrowser.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_in_browser);
        ButterKnife.bind(this);
        initView();
        initWebView();
    }

    private void initWebView() {
        //加上下面这段代码可以使网页中的链接不以浏览器的方式打开
        mWebviewContent.setWebViewClient(new WebViewClient());
        WebSettings webSettings = mWebviewContent.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setBlockNetworkImage(false);//解决图片不显示
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        mWebviewContent.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mProgressBar.setProgress(newProgress);
                if (newProgress < 100) {
                    mProgressBar.setVisibility(View.VISIBLE);
                } else {
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });
        mWebviewContent.loadUrl(mUrl);
    }

    private void initView() {
        mUrl = getIntent().getStringExtra("url");
        mTitle = getIntent().getStringExtra("title");
        mProgressBar.setVisibility(View.VISIBLE);
        mToolbar.setTitle(mTitle);
    }

    // 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebviewContent.canGoBack()) {
            mWebviewContent.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
