/**
 * �����չ��activity,��Ӧ�Ŵ�homeҳ��������չ��
 * ����������tab
 * ��һ����չ�᱾�����Ϣ
 * �ڶ�����չ���ͼƬ
 * ����������չ���е����Ų�Ʒ
 */
package com.scut.exguide.ui;

import java.util.ArrayList;
import java.util.List;
import com.example.exguide.R;
import com.scut.exguide.adapter.ExPagerAdapter;
import com.scut.exguide.entity.Exhibition;
import com.scut.exguide.entity.TaskForWaterFall;

import com.scut.exguide.mulithread.GetExhDescription;
import com.scut.exguide.mulithread.GetPics;
import com.scut.exguide.mulithread.GetProList;
import com.scut.exguide.mulithread.LoaderImageTask;
import com.scut.exguide.ui.LazyScrollView.OnScrollListener;
import com.scut.exguide.utility.Constant;
import com.scut.exguide.utility.MyActivity;
import com.scut.exguide.utility.TaskHandler;

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
	private int itemWidth;// ÿһ��item�Ŀ�ȣ���ÿ��ͼƬ��ռ��Ļ���
	private final int column_count = 2;// ��ʾ����

	private int current_page = 1;// ��ǰ��ʾҳ
	private View mPinterest;// �ٲ�����view

	private ProuductListView mProuductListView;
	private ExhibitionView mExhibitionView;
	private WaterFallView mWaterFallView;
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

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		mID = bundle.getInt("id");
		
		initalViewPager();

		

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

		initialExhDecription();
		mTitle.add("չ��ſ�");
		mViewList.add(mExhibitionView.getView());

		InitWaterLayout();// ��ʼ��
		mTitle.add("չ��ͼƬ");
		mViewList.add(mPinterest);
		//mViewList.add(mWaterFallView.getView());
		//AddItemToContainer(current_page);

		mTitle.add("���Ų�Ʒ");
		initialHotProduction();
		mViewList.add(mProuductListView.getView());

		mViewPager.setAdapter(new ExPagerAdapter(mViewList, mTitle));
	}

	public Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.UpdateUI: {
				TaskHandler task = (TaskHandler) msg.obj;
				Bitmap bp = LoaderImageTask.PicsMap.get(task.getPath());
//				//View iv = (View) LoaderImageTask.ViewsMap.get(task.path);
//				View iv = task.getView();
//				
//				int width = bp.getWidth();// ��ȡ��ʵ���
//				int height = bp.getHeight();
//				LayoutParams lp = iv.getLayoutParams();
//				lp.height = (height * itemWidth) / width;// �����߶�
//				iv.setLayoutParams(lp);
//				ImageView imageview = (ImageView) iv
//						.findViewById(R.id.waterfall_image);
//				View _view = (View) iv.findViewById(R.id.waterloading);
//				_view.setVisibility(View.GONE);
				//imageview.setImageBitmap(bp);
				//imageview.setVisibility(View.VISIBLE);
				task.Update(bp, itemWidth);

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
			finish();
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
		mExhibitionView = new ExhibitionView(this);
		
		String remotepath = Constant.urlPrefix_getExhById + mID;
		GetExhDescription asyGetDescription = new GetExhDescription(
				mExhibitionView);
		asyGetDescription.execute(remotepath);
		
		
		// mDescriptionLoading = mExhDescription.findViewById(R.id.loading);
	}

	private void initialHotProduction() {
		mProuductListView = new ProuductListView(this);

		String path = Constant.urlPrefix_getHotProList+mID;
		GetProList gpl = new GetProList(mProuductListView);
		gpl.execute(path);
	}

	/**
	 * ��ʼ���ٲ���
	 */
	private void InitWaterLayout() {

		//mWaterFallView = new WaterFallView(this);
		 display = this.getWindowManager().getDefaultDisplay();
		 itemWidth = display.getWidth() / column_count;// ������Ļ��С����ÿ�д�С
		
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
	public void AddItemToContainer(int pageindex) {
		GetPics asyGetPics = new GetPics(this);
		String path = Constant.urlPrefix_getPics + mID + "/p/" + pageindex;
		asyGetPics.execute(path);
	}

	/**
	 * �߳�ִ�к󣬸�������ҳUI
	 * 
	 * @param ex
	 */

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

			TaskHandler task = new TaskForWaterFall();
			((TaskForWaterFall)task).mView = view;
			((TaskForWaterFall)task).myActivity = this;
			((TaskForWaterFall)task).path = path;

			// HomeActivity.LoaderImage.ViewsMap.put(path, view);

			LoaderImageTask.addTask(task);

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
		msg.obj = param[0];
		myHandler.sendMessage(msg);

	}
	
	
}
