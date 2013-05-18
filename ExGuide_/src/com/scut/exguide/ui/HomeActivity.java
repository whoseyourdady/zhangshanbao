/**
 * 这是首页
 * 顶部可浏览展览会图片
 * 下面可以对应信息
 * 能查看详情
 */

package com.scut.exguide.ui;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.oauth.BaiduOAuth;
import com.baidu.oauth.BaiduOAuth.BaiduOAuthResponse;
import com.example.exguide.R;

import com.scut.exguide.listener.MyLocationListener;
import com.scut.exguide.listener.WeiboOAuthListener;
import com.scut.exguide.mulithread.GetExhList;
import com.scut.exguide.mulithread.LoaderImageTask;
import com.scut.exguide.utility.AccessTokenKeeper;
import com.scut.exguide.utility.Constant;
import com.scut.exguide.utility.DBUtility;
import com.scut.exguide.utility.Location;
import com.scut.exguide.utility.MyActivity;
import com.scut.exguide.utility.TaskHandler;
import com.scut.exguide.utility.ToastShow;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.app.ActionBar;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class HomeActivity extends Activity implements MyActivity {

	// 菜单、VIEW等UI
	private ActionBar mActionBar;// 主页的actionbar
	private Menu mMenu;
	private ExhListView mExhListView;

	// 定位API
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener;
	public static Location myLocation;// 全局标识的地理位置
	public String myPoint = "当前位置为:";// 当前位置

	public LoaderImageTask LoaderImage1 = new LoaderImageTask();
	public LoaderImageTask LoaderImage2 = new LoaderImageTask();

	// PCS API
	BaiduOAuth oauthClient = null;
	public static String mbOauth = null;// AcessToken

	public Weibo mWeibio;
	public static Oauth2AccessToken mOauth2AccessToken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_home);
		ToastShow.setActivity(this);
		
		mExhListView = new ExhListView(this);

		setContentView(mExhListView.getView());

		SQLiteDatabase db  = openOrCreateDatabase(Constant.dbname, MODE_WORLD_WRITEABLE, null);
		DBUtility.setDb(db);
		DBUtility.CreateDB();
		
		LoaderImage1.start();
		LoaderImage2.start();

		if (Build.VERSION.SDK_INT >= 11) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
					.penaltyLog().penaltyDeath().build());
		}

		initialLocService();

		intialActionbar();

	}

	/**
	 * 初始化actionbar
	 */
	private void intialActionbar() {
		mActionBar = getActionBar();
		// mActionBar.setDisplayHomeAsUpEnabled(true);
		// mActionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bg_trans));

		mActionBar.show();
	}

	/**
	 * 初始化定位服务
	 */
	private void initialLocService() {

		mLocationClient = new LocationClient(getApplicationContext());
		myListener = new MyLocationListener(this);

		LocationClientOption option = new LocationClientOption();

		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.disableCache(true);// 禁止启用缓存定位

		mLocationClient.setLocOption(option);
		mLocationClient.registerLocationListener(myListener);
		mLocationClient.start();

		if (mLocationClient != null && mLocationClient.isStarted())
			mLocationClient.requestLocation();
		else
			Log.d("LocSDK3", "locClient is null or not started");

		// mLocationClient.

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.option, menu);
		mMenu = menu;
		mMenu.getItem(0).setTitle(myPoint);
		return true;
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	/**
	 * actionbar的菜单判断
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getOrder()) {
		case 1: {
			// 定位

		}
			break;
		case 2: {
			// 设置绑定PCS
			GotoPcsOAuth();
		}
			break;
		case 3: {
			// 设置绑定Weiob
			mWeibio = Weibo.getInstance(Constant.APP_KEY,
					Constant.REDIRECT_URL, Constant.SCOPE);
			mWeibio.anthorize(HomeActivity.this, new WeiboOAuthListener(mOauth2AccessToken));
			AccessTokenKeeper.keepAccessToken(getApplicationContext(), mOauth2AccessToken);
		}
			break;
		case 4: {
			finish();
		}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public Handler mhandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case Constant.SetLoaction: {
				myLocation = (Location) msg.obj;
				String l = myPoint + myLocation.City;
				mMenu.getItem(0).setTitle(l);
				GetExhList asyGet = new GetExhList(mExhListView);
				String city = String.copyValueOf(myLocation.City.toCharArray(),
						0, myLocation.City.length() - 1);
				String path = Constant.RequestExhlistUrl + "/city/" + city;
				asyGet.execute(path);

			}
				break;
			case Constant.UpdateUI: {
				TaskHandler task = (TaskHandler) msg.obj;
				Bitmap bp = LoaderImageTask.PicsMap.get(task.getPath());
				// ImageView iv = (ImageView) LoaderImageTask.ViewsMap.get(task
				// .getPath());
				task.Update(bp);
				// iv.setImageBitmap(bp);
				Log.d("TEST", "设置");
			}
				break;

			}

			super.handleMessage(msg);
		}

	};

	public String getTag() {
		// TODO Auto-generated method stub
		return null;
	}

	public void GotoPcsOAuth() {
		BaiduOAuth oauthClient = new BaiduOAuth();

		oauthClient.startOAuth(this, Constant.mbApiKey, new String[] { "basic",
				"netdisk" }, new BaiduOAuth.OAuthListener() {
			@Override
			public void onException(String msg) {
				Toast.makeText(getApplicationContext(), "Login failed " + msg,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onComplete(BaiduOAuthResponse response) {
				if (null != response) {
					mbOauth = response.getAccessToken();
					Toast.makeText(
							getApplicationContext(),
							"Token: " + mbOauth + "    User name:"
									+ response.getUserName(),
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onCancel() {
				Toast.makeText(getApplicationContext(), "Login cancelled",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		LoaderImageTask.Toggle();
		mLocationClient.stop();
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK: {

		}
		}
		return false;

	}

	@Override
	public void Update(Object... param) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = Constant.UpdateUI;
		msg.obj = param[0];
		mhandler.sendMessage(msg);
	}
}
