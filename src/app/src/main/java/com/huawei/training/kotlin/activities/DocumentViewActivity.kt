package com.huawei.training.kotlin.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebView
import android.webkit.WebViewClient
import com.huawei.training.R
import com.huawei.training.kotlin.utils.video.Constants
import kotlinx.android.synthetic.main.activity_document_view.*

/**
 * @author Huawei DTSE India
 * @since 2020
 */
class DocumentViewActivity : BaseActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document_view)
        val webSettings = webview.settings
        webSettings.javaScriptEnabled = true
        val intent = intent
        val course_url = intent.getStringExtra(Constants.COURSE_DOCUMENTURL)
        val course_name = intent.getStringExtra(Constants.COURSE_NAME)
        course_name?.let { setToolbar(it) }
        webview.settings.setJavaScriptEnabled(true)

        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null) {
                    view?.loadUrl(url)
                }
                return true
            }
        }
        if (course_url != null) {
            webview.loadUrl(course_url)
        }
    }

    // To handle "Back" key press event for WebView to go back to previous screen.
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
            webview.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}