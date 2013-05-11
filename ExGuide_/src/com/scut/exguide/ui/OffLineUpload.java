package com.scut.exguide.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.oauth.BaiduOAuth;
import com.baidu.oauth.BaiduOAuth.BaiduOAuthResponse;
import com.baidu.pcs.BaiduPCSActionInfo;
import com.baidu.pcs.BaiduPCSClient;
import com.example.exguide.R;
import com.scut.exguide.utility.Constant;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class OffLineUpload extends Activity {
	private Button mButton;
	private Button mOAuth;
	private String mbOauth = null;
	private Handler mbUiThreadHandler = null;
	private final static String mbRootPath =  "/apps/pcstest_oauth";
	 private final static String mbApiKey = "L6g70tBRRIXLsY0Z3HwKqlRE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offlin);
		mButton = (Button) findViewById(R.id.button4);
		mOAuth = (Button) findViewById(R.id.button1);
		
		mbUiThreadHandler = new Handler();

		if (Build.VERSION.SDK_INT >= 11) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
					.penaltyLog().penaltyDeath().build());
		}

		mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				test_clouddownload();

			}
		});
		
		mOAuth.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 test_login();
			}
		});

	}

	private void test_clouddownload() {
		if (null != mbOauth) {

			Thread workThread = new Thread(new Runnable() {
				public void run() {
					BaiduPCSClient api = new BaiduPCSClient();

					api.setAccessToken(mbOauth);
					String destPath = mbRootPath + "/Skycn_1.2.1.exe";
					String sourceUrl = "http://tk.wangyuehd.com/soft/Skycn_1.2.1.exe";// http://59.108.246.24:82/down/kis12.0.0.374zh-Hans_cn.zip";
					// String sourceUrl =
					// "http://59.108.246.24:82/down/QQsetup.zip";
					final BaiduPCSActionInfo.PCSCloudDownloadResponse ret = api
							.cloudDownload(sourceUrl, destPath);
					mbUiThreadHandler.post(new Runnable() {
						public void run() {
							Toast.makeText(
									getApplicationContext(),
									"cloudDownload:  " + ret.status.errorCode
											+ "    " + ret.status.message,
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			});
			workThread.start();
		}
	}
	
	 private void test_login(){

//	    	try {
//	    		BaiduPCSClient pcsApi = new BaiduPCSClient();
//	    		
//	    		pcsApi.startOAuth(this, mbApiKey, new BaiduPCSClient.OAuthListener() {
//					
//					public void onException(String msg) {
//						// TODO Auto-generated method stub
//						Toast.makeText(getApplicationContext(), "Login failed " + msg, Toast.LENGTH_SHORT).show();
//					}
//					
//					public void onComplete(Bundle values) {
//						// TODO Auto-generated method stub
//						if(null != values){
//							mbOauth = values.getString(BaiduPCSClient.Key_AccessToken);
//							Toast.makeText(getApplicationContext(), "Token: " + mbOauth + "    User name:" + values.getString(BaiduPCSClient.Key_UserName), Toast.LENGTH_SHORT).show();
//						}
//					}
//					
//					public void onCancel() {
//						// TODO Auto-generated method stub
//						Toast.makeText(getApplicationContext(), "Login cancelled", Toast.LENGTH_SHORT).show();
//					}
//				});
//	    		
//	    	} catch (Exception e) {
//	    		// TODO Auto-generated catch block
//	    		e.printStackTrace();
//	    	}
			BaiduOAuth oauthClient = new BaiduOAuth();
			oauthClient.startOAuth(this, mbApiKey, new String[]{"basic", "netdisk"}, new BaiduOAuth.OAuthListener() {
				@Override
				public void onException(String msg) {
					Toast.makeText(getApplicationContext(), "Login failed " + msg, Toast.LENGTH_SHORT).show();
				}
				@Override
				public void onComplete(BaiduOAuthResponse response) {
					if(null != response){
						mbOauth = response.getAccessToken();
						Toast.makeText(getApplicationContext(), "Token: " + mbOauth + "    User name:" + response.getUserName(), Toast.LENGTH_SHORT).show();
					}
				}
				@Override
				public void onCancel() {
					Toast.makeText(getApplicationContext(), "Login cancelled", Toast.LENGTH_SHORT).show();
				}
			});
	    }
}
