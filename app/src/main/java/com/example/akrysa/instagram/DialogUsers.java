package com.example.akrysa.instagram;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
public class DialogUsers extends Dialog {

    public GetTokenInstagram contToken;

	private String StrAuthUrl;
	private String StrRedirectUri;

	public DialogUsers(Context context, String StrAutUrl, String StrRedUri, GetTokenInstagram cont) {
		super(context);
        contToken = cont;
        StrAuthUrl	= StrAutUrl;
        StrRedirectUri	= StrRedUri;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addContentView(setUpWebView(), new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

	private LinearLayout setUpWebView() {

        LinearLayout linearLayoutWEB = new LinearLayout(getContext());
        linearLayoutWEB.setOrientation(LinearLayout.VERTICAL);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        WebView webView = new WebView(getContext());
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebViewClient(new CurrentWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(StrAuthUrl);
        webView.setLayoutParams(layoutParams);
		WebSettings webSettings = webView.getSettings();
		webSettings.setSavePassword(false);
		webSettings.setSaveFormData(false);
        linearLayoutWEB.addView(webView);
        return linearLayoutWEB;
	}

	private class CurrentWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			if (url.startsWith(StrRedirectUri)) {
				if (url.contains("code")) {
					String temp[] = url.split("=");
                    contToken.setCodeRedirect(temp[1]);
 				} else if (url.contains("error")) {
					String temp[] = url.split("=");
                    contToken.setErrorRedirect(temp[temp.length-1]);
				}
	        	DialogUsers.this.dismiss();
	        	return true;
			}
			return false;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
            contToken.setErrorRedirect(description);
			DialogUsers.this.dismiss();
		}
	}
}