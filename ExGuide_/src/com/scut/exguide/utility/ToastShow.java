package com.scut.exguide.utility;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class ToastShow {
	private static Activity mActivity;
	
	public static void setActivity(Activity activity) {
		mActivity = activity;
	}
	
	public static void Show(String string) {
		Toast.makeText(mActivity, string, Toast.LENGTH_SHORT).show();
	}
}
