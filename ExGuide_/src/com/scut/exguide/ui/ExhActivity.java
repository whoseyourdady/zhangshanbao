/**
 * �����չ��activity,��Ӧ�Ŵ�homeҳ��������չ��
 * ����������tab
 * ��һ����չ�᱾�����Ϣ
 * �ڶ�����չ���ͼƬ
 * ����������չ���е����Ų�Ʒ
 */
package com.scut.exguide.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.exguide.R;
import com.scut.exguide.adapter.ExPagerAdapter;
import com.scut.exguide.adapter.ExhotProductAdapter;
import com.scut.exguide.entity.Exhibition;
import com.scut.exguide.entity.Task;
import com.scut.exguide.mulithread.AsyGetExhDescription;
import com.scut.exguide.mulithread.AsyGetExhList;
import com.scut.exguide.mulithread.AsyGetPics;
import com.scut.exguide.mulithread.LoaderImageTask;

import com.scut.exguide.mulithread.LoadImageThread;
import com.scut.exguide.ui.LazyScrollView.OnScrollListener;
import com.scut.exguide.utility.Constant;
import com.scut.exguide.utility.MyActivity;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ExhActivity extends Activity implements MyActivity {

	public ActionBar mActionBar;// ��ҳ��actionbar

	private ViewPager mViewPager;
	private PagerTabStrip mPagerTab;
	private ArrayList<View> mViewList;
	private ArrayList<String> mTitle;

	private LayoutInflater mLayoutInflater;

	private LazyScrollView waterfall_scroll;// �Զ���scrollview����
	private LinearLayout waterfall_container;// �Զ���scroll�ڵ�LinearLayout
	private ArrayList<LinearLayout> waterfall_items;
	private Display display;// ��Ļ
	private AssetManager assetManager;// assetsĿ¼�������
	private List<String> image_filenames;// �ļ���
	private final String image_path = "images";// �ļ�·��
	private int itemWidth;// ÿһ��item�Ŀ�ȣ���ÿ��ͼƬ��ռ��Ļ���
	private final int column_count = 2;// ��ʾ����

	private int current_page = 1;// ��ǰ��ʾҳ
	private View mPinterest;// �ٲ�����view

	private View mExhDescription;// ����ҳ��view
	private View mDescriptionLoading;// ����

	private ListView mHotProduct;// ���Ų�Ʒ��view
	private ExhotProductAdapter mHotProAdapter;// ���Ų�Ʒ��������

	private int mID;// ÿ��������ѡ���չ��ID

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exhome);

		intialActionbar();

		mViewPager = (ViewPager) findViewById(R.id.exhome_viewpager);
		mPagerTab = (PagerTabStrip) findViewById(R.id.exhome_pagertab);

		mViewList = new ArrayList<View>();
		mTitle = new ArrayList<String>();

		mID = 1;
		initalViewPager();
		
		HomeActivity.LoaderImage = new LoaderImageTask();
		HomeActivity.LoaderImage.start();

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
//		mID = bundle.getInt("id");
		

//		String remotepath = Constant.urlPrefix_getExhById + mID;
//		AsyGetExhDescription asyGetDescription = new AsyGetExhDescription(this);
//		asyGetDescription.execute(remotepath);

	}

	/**
	 * ��ʼ��actionbar
	 */
	private void intialActionbar() {
		mActionBar = getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.show();
	}

	/*
	 * ��ʼ��tap
	 */
	private void initalViewPager() {

		mLayoutInflater = LayoutInflater.from(this);

//		initialExhDecription();
//		mTitle.add("չ��ſ�");
//		mViewList.add(mExhDescription);

		InitWaterLayout();// ��ʼ��
		mTitle.add("����ͼƬ");
		mViewList.add(mPinterest);

		mViewPager.setAdapter(new ExPagerAdapter(mViewList, mTitle));
	}

	public Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.UpdateUI: {
				Task task = (Task) msg.obj;
				Bitmap bp = LoaderImageTask.PicsMap.get(task.path);
				View iv = (View) LoaderImageTask.ViewsMap
						.get(task.path);
							
				int width = bp.getWidth();// ��ȡ��ʵ���
				int height = bp.getHeight();
				LayoutParams lp = iv.getLayoutParams();
				lp.height = (height * itemWidth) / width;// �����߶�
				iv.setLayoutParams(lp);
				ImageView imageview = (ImageView) iv
						.findViewById(R.id.waterfall_image);
				View _view = (View) iv.findViewById(R.id.waterloading);
				_view.setVisibility(View.GONE);
				imageview.setImageBitmap(bp);
				
			}
				break;

			default:
				break;
			}
		}

	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.option, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home: {
			Intent intent = new Intent(ExhActivity.this, HomeActivity.class);

			// intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			overridePendingTransition(0, 0);
			return true;
		}
		default: {
			return super.onOptionsItemSelected(item);
		}
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK: {
			Intent intent = new Intent(ExhActivity.this, HomeActivity.class);

			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
			overridePendingTransition(0, 0);

			return true;
		}

		default:
			return super.onKeyDown(keyCode, event);
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		// mGestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	/**
	 * ��ʼ��չ������ҳ
	 */
	private void initialExhDecription() {
		mExhDescription = mLayoutInflater
				.inflate(R.layout.exhdescription, null);
		mDescriptionLoading = mExhDescription.findViewById(R.id.loading);
	}

	private void initialHotProduction() {
		mHotProduct = (ListView) (mLayoutInflater.inflate(
				R.layout.hotproduction, null))
				.findViewById(R.id.hotprolistview);
		// mHotProduct.setAdapter(adapter);
	}

	/**
	 * ��ʼ���ٲ���
	 */
	private void InitWaterLayout() {
		display = this.getWindowManager().getDefaultDisplay();
		itemWidth = display.getWidth() / column_count;// ������Ļ��С����ÿ�д�С
		// assetManager = this.getAssets();// ��ȡassetsĿ¼�������

		mPinterest = mLayoutInflater.inflate(R.layout.waterfall, null);
		waterfall_scroll = (LazyScrollView) mPinterest
				.findViewById(R.id.waterfall_scroll);
		waterfall_scroll.getView();// ��Ҫ�ǻ�ȡ�߶�
		// ��������
		waterfall_scroll.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onTop() {
				// ���������
				Log.d("LazyScroll", "Scroll to top");
			}

			@Override
			public void onScroll() {
				// ������
				Log.d("LazyScroll", "Scroll");
			}

			@Override
			public void onBottom() {
				// ��������׶�ʱ��������item
				AddItemToContainer(++current_page);
			}
		});

		waterfall_container = (LinearLayout) mPinterest
				.findViewById(R.id.waterfall_container);
		waterfall_items = new ArrayList<LinearLayout>();// ʵ����item����
		// ѭ���������
		for (int i = 0; i < column_count; i++) {
			LinearLayout itemLayout = new LinearLayout(this);
			LinearLayout.LayoutParams itemParam = new LinearLayout.LayoutParams(
					itemWidth, LayoutParams.WRAP_CONTENT);// ÿ��item���Ϊ��Ļ��1/3���߶Ȱ�������
			itemLayout.setPadding(2, 2, 2, 2);// ÿ��item��ƫ2������������һ���
			itemLayout.setOrientation(LinearLayout.VERTICAL);// ����Ϊ��ֱ����
			itemLayout.setLayoutParams(itemParam);
			waterfall_items.add(itemLayout);// ��LinearLayout���뵽������
			waterfall_container.addView(itemLayout);// ѭ����item���뵽���Բ�����
		}

		// ��һ�μ���,Ĭ�ϵĽ���
		AddItemToContainer(current_page);

	}

	/**
	 * ���item�����Բ�����
	 * 
	 * @param pageindex
	 *            ҳ��
	 * @param pagecount
	 *            ÿҳitem������ͼƬ����
	 */
	private void AddItemToContainer(int pageindex) {
		// int j = 0;
		// int imagecount = image_filenames.size();
		// for (int i = pageindex * pagecount; i < pagecount * (pageindex + 1)
		// && i < imagecount; i++) {
		// j = j >= column_count ? j = 0 : j;
		// AddImage(image_filenames.get(i), j++);
		// }
		AsyGetPics asyGetPics = new AsyGetPics(this);
		String path = Constant.urlPrefix_getPics + mID +"/p/"+pageindex;
		asyGetPics.execute(path);
	}

	/**
	 * ���ͼƬ��item��
	 * 
	 * @param filename
	 *            �ļ���
	 * @param columnIndex
	 */
	private void AddImage(String filename, int columnIndex) {
		// ImageView item = (ImageView) (LayoutInflater.from(this).inflate(
		// R.layout.waterfallitem, null));
		View view = (LayoutInflater.from(this).inflate(R.layout.waterfallitem,
				null));

		waterfall_items.get(columnIndex).addView(view);
		//
		// TaskParam param = new TaskParam();
		// param.setAssetManager(assetManager);
		// param.setFilename(image_path + "/" + filename);
		// param.setItemWidth(itemWidth);
		// ImageLoaderTask task = new ImageLoaderTask(view);
		// task.execute(param);

	}

	/**
	 * �߳�ִ�к󣬸�������ҳUI
	 * 
	 * @param ex
	 */
	public void SetDescription(Exhibition ex) {
		mDescriptionLoading.setVisibility(View.GONE);

		ImageView _imageview = (ImageView) mExhDescription
				.findViewById(R.id._dp_exhlogo);
		LoadImageThread t = new LoadImageThread(_imageview, ex.logo_url);
		t.run();

		TextView _e_name = (TextView) mExhDescription
				.findViewById(R.id._dp_exhname);
		_e_name.setText(ex.name_cn);

		TextView _e_hall = (TextView) mExhDescription
				.findViewById(R.id._dp_hallname);
		_e_hall.setText(ex.hall);

		TextView _e_schedule = (TextView) mExhDescription
				.findViewById(R.id._dp_exhsechedule);
		_e_schedule.setText(ex.period_start + "��" + ex.period_end);

		TextView _e_date = (TextView) mExhDescription
				.findViewById(R.id._dp_exhdate);
		_e_date.setText(ex.day_start + "��" + ex.day_end);

		TextView _e_desc = (TextView) mExhDescription
				.findViewById(R.id._dp_description);
		_e_desc.setText(ex.description);

	}

	/**
	 * �����ٲ�UI
	 */
	public void SetWaterfallUI(ArrayList<String> result) {
		for (int i = 0; i < result.size(); i++) {
			View view = mLayoutInflater.inflate(R.layout.waterfallitem, null);

			waterfall_items.get(i % 2).addView(view);

			String s_ = String.copyValueOf(result.get(i).toCharArray(), 1,
					result.get(i).length() - 1);
			String path = Constant.urlPrefix_getLogo + s_;

			Task task = new Task();
			task.mView =  view;
			task.myActivity = this;
			task.path = path;

			//HomeActivity.LoaderImage.ViewsMap.put(path, view);
			
			HomeActivity.LoaderImage.addTask(task);

		}
	}

	@Override
	public String getTag() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void Update(Object... param) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = Constant.UpdateUI;
		msg.obj = (Task) param[0];
		myHandler.sendMessage(msg);
	}
}
