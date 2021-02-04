package com.mohsenoid.utils.widget;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class FocusFixedWebViewClient extends WebViewClient { // fix webview focus
    // problem in scroll
    // view

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

}
