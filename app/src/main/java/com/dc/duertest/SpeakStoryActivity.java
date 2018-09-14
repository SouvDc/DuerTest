package com.dc.duertest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class SpeakStoryActivity extends AppCompatActivity {

	private WebView mWebView;

	private String webPath = "http://www.ximalaya.com/explore/";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_speak_story);

		initView();
	}

	private void initView() {
		String webStr = getIntent().getStringExtra("webPath");
		if(!TextUtils.isEmpty(webStr)){
			webPath = webStr;
		}
		mWebView = (WebView) findViewById(R.id.web_view);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				mWebView.loadUrl(url);
				return true;
			}
		});
		mWebView.loadUrl(webPath);
	}
}
