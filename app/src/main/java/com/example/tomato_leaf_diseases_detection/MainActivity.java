package com.example.tomato_leaf_diseases_detection;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private ValueCallback<Uri[]> mUploadCallback;
    private static final int FILE_CHOOSER_RESULT_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.webview);

        // Enable JavaScript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // Handle file upload
        webView.setWebChromeClient(new WebChromeClient() {
            // For Android 5.0+
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                mUploadCallback = filePathCallback;
                Intent intent = fileChooserParams.createIntent();
                startActivityForResult(intent, FILE_CHOOSER_RESULT_CODE);
                return true;
            }
        });

        // Load the website URL
        webView.loadUrl("https://tomato-leaf-diseases-detection-anjan.streamlit.app/");

        // Open links in the WebView itself
        webView.setWebViewClient(new WebViewClient());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (mUploadCallback == null) {
                return;
            }

            Uri[] results = null;

            // Check if the response is a positive result
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }

            mUploadCallback.onReceiveValue(results);
            mUploadCallback = null;
        }
    }

}