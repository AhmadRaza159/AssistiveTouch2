package com.example.assistivetouch

import android.annotation.TargetApi
import android.app.ProgressDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.assistivetouch.databinding.ActivityPrivacyPolicyBinding

class PrivacyPolicy : AppCompatActivity() {

    private lateinit var binding: ActivityPrivacyPolicyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pd = ProgressDialog(this)
        pd.setMessage("Loading...")
        pd.show()
        binding.privacyPolicyWebView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                Toast.makeText(applicationContext, description, Toast.LENGTH_SHORT).show()
            }

            @TargetApi(Build.VERSION_CODES.M)
            override fun onReceivedError(
                view: WebView,
                req: WebResourceRequest,
                rerr: WebResourceError
            ) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(
                    view,
                    rerr.errorCode,
                    rerr.description.toString(),
                    req.url.toString()
                )
            }
        }
        binding.privacyPolicyWebView.loadUrl("https://galaxystudioapps.blogspot.com/2020/12/privacy-policy.html")

        binding.privacyPolicyWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                pd.dismiss()
            }
        }
    }
}