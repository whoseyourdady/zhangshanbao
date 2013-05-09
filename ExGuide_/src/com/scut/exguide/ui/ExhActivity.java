/**
 * 这个是展会activity,对应着从home页跳过来的展会
 * 其中有三个tab
 * 第一个是展会本身的信息
 * 第二个是展会的图片
 * 第三个则是展会中的热门产品
 */
package com.scut.exguide.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.exguide.R;
import com.scut.exguide.adapter.ExPagerAdapter;
import com.scut.exguide.adapter.ExhotProductAdapter;
import com.scut.exguide.mulithread.AsyGetExhDescription;
import com.scut.exguide.mulithread.AsyGetExhList;
import com.scut.exguide.utility.Constant;
import com.scut.exguide.utility.ImageLoaderTask;
import com.scut.exguide.utility.LazyScrollView;
import com.scut.exguide.utility.LazyScrollView.OnScrollListener;
import com.scut.exguide.utility.TaskParam;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
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

public class ExhActivity extends Activity {

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
	private AssetManager assetManager;// assets目录管理对象
	private List<String> image_filenames;// 文件名
	private final String image_path = "images";// 文件路径
	private int itemWidth;// 每一个item的宽度，即每张图片所占屏幕宽度
	private final int column_count = 2;// 显示列数
	private final int page_count = 15;// 每次加载15张图片
	private int current_page = 0;// 当前显示页
	private View mPinterest;// 瀑布流的view

	private View mExhDescription;// 详情页的view
	private View mDescriptionLoading;// 加载

	private ListView mHotProduct;// 热门产品的view
	private ExhotProductAdapter mHotProAdapter;// 热门产品的适配器

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

		initalViewPager();

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		int id = bundle.getInt("id");
		String url = bundle.getString("url");
		String remotepath = Constant.urlPrefix_getExhById+id;
		AsyGetExhDescription asyGetDescription = new AsyGetExhDescription(this);
		asyGetDescription.execute(remotepath);
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
		mViewList.add(mExhDescription);

		InitWaterLayout();// 初始化
		mTitle.add("热门图片");
		mViewList.add(mPinterest);

		mViewPager.setAdapter(new ExPagerAdapter(mViewList, mTitle));
	}

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
	 * 初始化展会详情页
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

	private void InitWaterLayout() {
		display = this.getWindowManager().getDefaultDisplay();
		itemWidth = display.getWidth() / column_count;// 根据屏幕大小计算每列大小
		assetManager = this.getAssets();// 获取assets目录管理对象

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
				AddItemToContainer(++current_page, page_count);
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

		// 获取所有图片路径，从/assets/images目录下，并存入List中
		try {
			image_filenames = Arrays.asList(assetManager.list(image_path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 第一次加载,默认的界面
		AddItemToContainer(current_page, page_count);

	}

	/**
	 * 添加item到线性布局中
	 * 
	 * @param pageindex
	 *            页数
	 * @param pagecount
	 *            每页item数，即图片总数
	 */
	private void AddItemToContainer(int pageindex, int pagecount) {
		int j = 0;
		int imagecount = image_filenames.size();
		for (int i = pageindex * pagecount; i < pagecount * (pageindex + 1)
				&& i < imagecount; i++) {
			j = j >= column_count ? j = 0 : j;
			AddImage(image_filenames.get(i), j++);
		}
	}

	/**
	 * 添加图片到item中
	 * 
	 * @param filename
	 *            文件名
	 * @param columnIndex
	 */
	private void AddImage(String filename, int columnIndex) {
		// ImageView item = (ImageView) (LayoutInflater.from(this).inflate(
		// R.layout.waterfallitem, null));
		View view = (LayoutInflater.from(this).inflate(R.layout.waterfallitem,
				null));

		waterfall_items.get(columnIndex).addView(view);

		TaskParam param = new TaskParam();
		param.setAssetManager(assetManager);
		param.setFilename(image_path + "/" + filename);
		param.setItemWidth(itemWidth);
		ImageLoaderTask task = new ImageLoaderTask(view);
		task.execute(param);

	}

}
