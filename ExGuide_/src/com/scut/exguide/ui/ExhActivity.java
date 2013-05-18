/**
 * 这个是展会activity,对应着从home页跳过来的展会
 * 其中有三个tab
 * 第一个是展会本身的信息
 * 第二个是展会的图片
 * 第三个则是展会中的热门产品
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

	public ActionBar mActionBar;// 主页的actionbar

	private ViewPager mViewPager;
	private PagerTabStrip mPagerTab;
	private ArrayList<View> mViewList;
	private ArrayList<String> mTitle;

	private LayoutInflater mLayoutInflater;

	private LazyScrollView waterfall_scroll;// 自定义scrollview对象
	private LinearLayout waterfall_container;// 自定义scroll内的LinearLayout
	private ArrayList<LinearLayout> waterfall_items;
	private Display display;// 屏幕
	private int itemWidth;// 每一个item的宽度，即每张图片所占屏幕宽度
	private final int column_count = 2;// 显示列数

	private int current_page = 1;// 当前显示页
	private View mPinterest;// 瀑布流的view

	private ProuductListView mProuductListView;
	private ExhibitionView mExhibitionView;
	private WaterFallView mWaterFallView;
	private int mID;// 每次启动所选择的展会ID

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
	 * 初始化actionbar
	 */
	private void intialActionbar() {
		mActionBar = getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.show();
	}

	/*
	 * 初始化tap
	 */
	private void initalViewPager() {

		mLayoutInflater = LayoutInflater.from(this);

		initialExhDecription();
		mTitle.add("展会概况");
		mViewList.add(mExhibitionView.getView());

		InitWaterLayout();// 初始化
		mTitle.add("展会图片");
		mViewList.add(mPinterest);
		//mViewList.add(mWaterFallView.getView());
		//AddItemToContainer(current_page);

		mTitle.add("热门产品");
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
//				int width = bp.getWidth();// 获取真实宽高
//				int height = bp.getHeight();
//				LayoutParams lp = iv.getLayoutParams();
//				lp.height = (height * itemWidth) / width;// 调整高度
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
	 * 初始化展会详情页
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
	 * 初始化瀑布流
	 */
	private void InitWaterLayout() {

		//mWaterFallView = new WaterFallView(this);
		 display = this.getWindowManager().getDefaultDisplay();
		 itemWidth = display.getWidth() / column_count;// 根据屏幕大小计算每列大小
		
		 mPinterest = mLayoutInflater.inflate(R.layout.waterfall, null);
		 waterfall_scroll = (LazyScrollView) mPinterest
		 .findViewById(R.id.waterfall_scroll);
		 waterfall_scroll.getView();// 主要是获取高度
		 // 监听滑动
		 waterfall_scroll.setOnScrollListener(new OnScrollListener() {
		
		 @Override
		 public void onTop() {
		 // 滚动到最顶端
		 Log.d("LazyScroll", "Scroll to top");
		 }
		
		 @Override
		 public void onScroll() {
		 // 滚动中
		 Log.d("LazyScroll", "Scroll");
		 }
		
		 @Override
		 public void onBottom() {
		 // 滚动到最底端时，再增加item
		 AddItemToContainer(++current_page);
		 }
		 });
		
		 waterfall_container = (LinearLayout) mPinterest
		 .findViewById(R.id.waterfall_container);
		 waterfall_items = new ArrayList<LinearLayout>();// 实例化item数组
		 // 循环添加数据
		 for (int i = 0; i < column_count; i++) {
		 LinearLayout itemLayout = new LinearLayout(this);
		 LinearLayout.LayoutParams itemParam = new LinearLayout.LayoutParams(
		 itemWidth, LayoutParams.WRAP_CONTENT);// 每个item宽度为屏幕的1/3，高度包裹内容
		 itemLayout.setPadding(2, 2, 2, 2);// 每个item内偏2，看起来隔开一点点
		 itemLayout.setOrientation(LinearLayout.VERTICAL);// 设置为垂直布局
		 itemLayout.setLayoutParams(itemParam);
		 waterfall_items.add(itemLayout);// 把LinearLayout加入到数组中
		 waterfall_container.addView(itemLayout);// 循环把item加入到线性布局中
		 }
		
		 // 第一次加载,默认的界面
		 AddItemToContainer(current_page);

	}

	/**
	 * 添加item到线性布局中
	 * 
	 * @param pageindex
	 *            页数
	 * @param pagecount
	 *            每页item数，即图片总数
	 */
	public void AddItemToContainer(int pageindex) {
		GetPics asyGetPics = new GetPics(this);
		String path = Constant.urlPrefix_getPics + mID + "/p/" + pageindex;
		asyGetPics.execute(path);
	}

	/**
	 * 线程执行后，更新详情页UI
	 * 
	 * @param ex
	 */

	/**
	 * 更新瀑布UI
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
