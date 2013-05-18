package com.scut.exguide.listener;

import android.os.Bundle;

import com.scut.exguide.utility.AccessTokenKeeper;
import com.scut.exguide.utility.ToastShow;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;

public class WeiboOAuthListener implements WeiboAuthListener {

	private Oauth2AccessToken mOauth2AccessToken;
	
	public WeiboOAuthListener(Oauth2AccessToken o2at) {
		mOauth2AccessToken = o2at;
	}
	
	@Override
	public void onCancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onComplete(Bundle arg0) {
		// TODO Auto-generated method stub
		String token = arg0.getString("access_token");
		String expires_in = arg0.getString("expires_in");
		mOauth2AccessToken = new Oauth2AccessToken(token, expires_in);
		ToastShow.Show("ÊÚÈ¨³É¹¦");
	}

	@Override
	public void onError(WeiboDialogError arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onWeiboException(WeiboException arg0) {
		// TODO Auto-generated method stub

	}

}
