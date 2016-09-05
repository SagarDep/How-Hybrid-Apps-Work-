package com.hiteshsahu.webview_playground;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebViewPlayground extends AppCompatActivity {

    private WebView webViewInNative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playground);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        webViewInNative = (WebView) findViewById(R.id.waveform_generator_view);

        webViewInNative.getSettings().setJavaScriptEnabled(true);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            webViewInNative.evaluateJavascript("enable();", null);
        } else {
            webViewInNative.loadUrl("javascript:enable();");
        }

        webViewInNative.getSettings().setDomStorageEnabled(true);
        webViewInNative.addJavascriptInterface(new WebAppInterface(this), "AndroidFunction");
        webViewInNative.setWebViewClient(new MyBrowser());
        webViewInNative.loadUrl("file:///android_asset/www/index.html");


        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/hiteshsahu/How-Hybrid-Apps-Work-/")));

            }
        });

        findViewById(R.id.java_to_js).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                webViewInNative.loadUrl("javascript:callJSFunctionFromJava(\"" + "Hello From Java" + "\")");

                Snackbar.make(view, "Call JS from java", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        findViewById(R.id.java_to_js_callback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                webViewInNative.loadUrl("javascript:callJSFunctionFromJavaAndReturnValue(\"" + "Hello From Java With Callaback" + "\")");

                Snackbar.make(view, "Call JS from java with callback", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webViewInNative.canGoBack()) {
            webViewInNative.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public class WebAppInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /**
         * Perform some Native operation currently Show a toast from the web page
         */
        @JavascriptInterface
        public void doSomethingOnNativeSide(String messageFromJS) {

            someJavaFunction(messageFromJS);
        }
    }


    private void someJavaFunction(String toast) {
        Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
    }
}
