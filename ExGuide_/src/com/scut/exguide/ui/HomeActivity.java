/**
 * ������ҳ
 * ���������չ����ͼƬ
 * ������Զ�Ӧ��Ϣ
 * �ܲ鿴����
 */

package com.scut.exguide.ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.exguide.R;
import com.scut.exguide.listener.MyLocationListener;
import com.scut.exguide.mulithread.AsyGetExhList;
import com.scut.exguide.utility.Constant;
import com.scut.exguide.utility.LazyScrollView;
import com.scut.exguide.utility.Location;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
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

public class HomeActivity extends Activity {

	// �˵���VIEW��UI
	private ActionBar mActionBar;// ��ҳ��actionbar
	private ListView mListView;
	private View LocationRequesting;
	private Menu mMenu;

	// ��λAPI
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener;
	public static Location myLocation;// ȫ�ֱ�ʶ�ĵ���λ��
	public String myPoint = "��ǰλ��Ϊ:";// ��ǰλ��

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		// setContentView(R.layout.exhdescription);
		// mLoading = (View) findViewById(R.id.loading);
		// mLoading.setVisibility(View.GONE);
		intialActionbar();
		//
		mListView = (ListView) findViewById(R.id.exhilist);
		mListView.setVisibility(View.GONE);
		LocationRequesting = (View) findViewById(R.id.homeloading);

		// intialExhiListView(mListView);
		// setContentView(R.layout.waterfall);

		initialLocService();

		if (Build.VERSION.SDK_INT >= 11) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
					.penaltyLog().penaltyDeath().build());
		}

	}

	/**
	 * ��ʼ��actionbar
	 */
	private void intialActionbar() {
		mActionBar = getActionBar();
		// mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.show();
	}

	/**
	 * ��ʼ����λ����
	 */
	private void initialLocService() {

		mLocationClient = new LocationClient(getApplicationContext());
		myListener = new MyLocationListener(this);

		LocationClientOption option = new LocationClientOption();

		option.setAddrType("all");// ���صĶ�λ���������ַ��Ϣ
		option.setCoorType("bd09ll");// ���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02
		option.disableCache(true);// ��ֹ���û��涨λ

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
	// * ��ʼ��listview
	// */
	// private void intialExhiListView(ListView _listview) {
	// _listview.setAdapter(new ArrayAdapter<String>(this,
	// android.R.layout.simple_expandable_list_item_1, getData()));
	//
	// // �����¼�������
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
	 * actionbar�Ĳ˵��ж�
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getOrder()) {
		case 1: {
			// ��λ

		}
			break;
		case 3: {
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
				AsyGetExhList asyGet = new AsyGetExhList(LocationRequesting,
						mListView, HomeActivity.this);
				String path = Constant.RequestExhlistUrl + "/city/" + "����";
				asyGet.execute(path);

			}
				break;
			}
			super.handleMessage(msg);
		}

	};

}
