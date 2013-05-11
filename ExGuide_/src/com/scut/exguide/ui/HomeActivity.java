/**
 * 这是首页
 * 顶部可浏览展览会图片
 * 下面可以对应信息
 * 能查看详情
 */

package com.scut.exguide.ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.oauth.BaiduOAuth;
import com.baidu.oauth.BaiduOAuth.BaiduOAuthResponse;

import com.example.exguide.R;
import com.scut.exguide.adapter.ExhListAdapter;
import com.scut.exguide.entity.Exhibition;
import com.scut.exguide.entity.Task;
import com.scut.exguide.listener.MyLocationListener;
import com.scut.exguide.mulithread.AsyGetExhList;
import com.scut.exguide.mulithread.LoaderImageTask;

import com.scut.exguide.utility.Constant;
import com.scut.exguide.utility.Location;
import com.scut.exguide.utility.MyActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class HomeActivity extends Activity implements MyActivity {

	// 菜单、VIEW等UI
	private ActionBar mActionBar;// 主页的actionbar
	private ListView mListView;
	private View LocationRequesting;
	private Menu mMenu;

	// 定位API
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener;
	public static Location myLocation;// 全局标识的地理位置
	public String myPoint = "当前位置为:";// 当前位置

	public static LoaderImageTask LoaderImage = new LoaderImageTask();

	// PCS API
	BaiduOAuth oauthClient = null;
	public static String mbOauth = null;//AcessToken
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		// TEST test = new TEST();
		// test.execute(0);
		LoaderImage.start();

		if (Build.VERSION.SDK_INT >= 11) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
					.penaltyLog().penaltyDeath().build());
		}

		initialLocService();

		// LoaderImage = new LoaderImageTask();
		// LoaderImage.start();

		// setContentView(R.layout.exhdescription);
		// mLoading = (View) findViewById(R.id.loading);
		// mLoading.setVisibility(View.GONE);
		intialActionbar();
		//
		mListView = (ListView) findViewById(R.id.exhilist);
		mListView.setVisibility(View.GONE);

		LocationRequesting = (View) findViewById(R.id.homeloading);
		// LocationRequesting.setVisibility(View.GONE);
		// intialExhiListView(mListView);
		// setContentView(R.layout.waterfall);

		

	}

	/**
	 * 初始化actionbar
	 */
	private void intialActionbar() {
		mActionBar = getActionBar();
		// mActionBar.setDisplayHomeAsUpEnabled(true);
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

	// /**
	// * 初始化listview
	// */
	// private void intialExhiListView(ListView _listview) {
	// _listview.setAdapter(new ArrayAdapter<String>(this,
	// android.R.layout.simple_expandable_list_item_1, getData()));
	//
	// // 设置事件监听器
	// OnItemClickListener listener = new OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	// long arg3) {
	// // TODO Auto-generated method stub
	// Intent intent = new Intent();
	// intent.setClass(getApplication(), ExhActivity.class);
	// intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	// startActivity(intent);
	//
	// }
	// };
	// _listview.setOnItemClickListener(listener);
	// }

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
			//设置绑定
			GotoPcsOAuth();
		}
			break;
		case 3: {
			//finish();
			Intent intent = new Intent(HomeActivity.this, OffLineUpload.class);
			startActivity(intent);
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
				AsyGetExhList asyGet = new AsyGetExhList(HomeActivity.this);
				String city = String.copyValueOf(myLocation.City.toCharArray(),
						0, myLocation.City.length() - 1);
				String path = Constant.RequestExhlistUrl + "/city/" + city;
				asyGet.execute(path);

			}
				break;
			case Constant.UpdateUI: {
				Task task = (Task) msg.obj;
				Bitmap bp = LoaderImageTask.PicsMap.get(task.path);
				ImageView iv = (ImageView) LoaderImageTask.ViewsMap
						.get(task.path);
				iv.setImageBitmap(bp);
				Log.d("TEST", "设置");
			}
				break;

			}

			super.handleMessage(msg);
		}

	};

	public int GetExhibitonID(int position) {
		ExhListAdapter ad = (ExhListAdapter) mListView.getAdapter();
		return ad.getId(position);
	}

	public void SetListUI(ArrayList<Exhibition> list) {

		LocationRequesting.setVisibility(View.GONE);

		mListView.setAdapter(new ExhListAdapter(this, list));

		OnItemClickListener listener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(HomeActivity.this, ExhActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				Bundle bundle = new Bundle();
				int id = GetExhibitonID(arg2);
				bundle.putInt("id", id);
				intent.putExtras(bundle);
				startActivity(intent);

			}
		};
		mListView.setOnItemClickListener(listener);

		mListView.setVisibility(View.VISIBLE);

	}

	public String getTag() {
		// TODO Auto-generated method stub
		return null;
	}

	public void Update(Object... param) {
		// TODO Auto-generated method stub
		Task task = (Task) param[0];
		Message msg = new Message();
		msg.what = 0;
		msg.obj = task;
		mhandler.sendMessage(msg);
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

}
