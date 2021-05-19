/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.huawei.training.java.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.databinding.DataBindingUtil;

import com.huawei.training.R;
import com.huawei.training.databinding.ActivityDocumentViewBinding;
import com.huawei.training.java.utils.AppUtils;
import com.huawei.training.java.utils.video.Constants;

/**
 * @author Huawei DTSE India
 * @since 2020
 */
public class DocumentViewActivity extends BaseActivity {
    /**
     * The Binding.
     */
    ActivityDocumentViewBinding binding;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_document_view);
        WebSettings webSettings = binding.webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        Intent intent = getIntent();
        String url = intent.getStringExtra(Constants.COURSE_DOCUMENTURL);
        String course_name = intent.getStringExtra(Constants.COURSE_NAME);
        if (course_name != null) {
            setToolbar(course_name);
        }
        assert url != null;
        binding.webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == Constants.PROGRESS) AppUtils.cancelLoading();
            }
        });
        binding.webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                AppUtils.showLoadingDialog(DocumentViewActivity.this);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // Do something when page loading finished
            }
        });
        binding.webview.loadUrl(url);
    }

    // To handle "Back" key press event for WebView to go back to previous screen.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && binding.webview.canGoBack()) {
            binding.webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
