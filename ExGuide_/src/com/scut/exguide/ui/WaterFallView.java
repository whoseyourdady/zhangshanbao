package com.scut.exguide.ui;

import java.util.ArrayList;
import java.util.List;

import com.example.exguide.R;


import com.scut.exguide.entity.TaskForWaterFall;
import com.scut.exguide.mulithread.GetPics;
import com.scut.exguide.mulithread.LoaderImageTask;
import com.scut.exguide.ui.LazyScrollView.OnScrollListener;
import com.scut.exguide.utility.Constant;
import com.scut.exguide.utility.MyActivity;
import com.scut.exguide.utility.TaskHandler;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class WaterFallView {
	private Activity mActivity;
	private LayoutInflater mInflater;
	private View mView;

	private LazyScrollView waterfall_scroll;// 自定义scrollview对象
	private LinearLayout waterfall_container;// 自定义scroll内的LinearLayout
	private ArrayList<LinearLayout> waterfall_items;
	private Display display;// 屏幕
	private int itemWidth;// 每一个item的宽度，即每张图片所占屏幕宽度
	private final int column_count = 2;// 显示列数
	private int current_page = 1;// 当前显示页

	private int mID;// 当前页面的ID

	public WaterFallView(Activity _a) {
		mActivity = _a;
		
		initial();
	}

	// 初始化布局
	public void initial() {
		mInflater = LayoutInflater.from(mActivity);

		display = mActivity.getWindowManager().getDefaultDisplay();
		itemWidth = display.getWidth() / column_count;// 根据屏幕大小计算每列大小

		mView = mInflater.inflate(R.layout.waterfall, null);
		waterfall_scroll = (LazyScrollView) mView
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
//				Message msg = new Message();
//				msg.what = -1;
//				((ExhActivity)mActivity).myHandler.sendMessage(msg);
			}
		});

		waterfall_container = (LinearLayout) mView
				.findViewById(R.id.waterfall_container);
		waterfall_items = new ArrayList<LinearLayout>();// 实例化item数组
		// 循环添加数据
		for (int i = 0; i < column_count; i++) {
			LinearLayout itemLayout = new LinearLayout(mActivity);
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

	public View getView() {
		return mView;
	}

	public void AddItemToContainer(int pageindex) {

		GetPics gp = new GetPics(this, mActivity);
		String path = Constant.urlPrefix_getPics + mID + "/p/" + pageindex;
		gp.execute(path);
		//((ExhActivity)mActivity).AddItemToContainer(current_page);
	}

	public void SetUI(ArrayList<String> result) {
		for (int i = 0; i < result.size(); i++) {
			View iv = mInflater.inflate(R.layout.waterfallitem, null);
			
			waterfall_items.get(i % 2).addView(iv);

			String s_ = String.copyValueOf(result.get(i).toCharArray(), 1,
					result.get(i).length() - 1);
			String path = Constant.urlPrefix_getLogo + s_;
//
			 TaskHandler task = new TaskForWaterFall();
			 ((TaskForWaterFall)task).mView = iv;
			 ((TaskForWaterFall)task).myActivity = (MyActivity) mActivity;
			 ((TaskForWaterFall)task).path = path;
			
			 // HomeActivity.LoaderImage.ViewsMap.put(path, view);
			//
			LoaderImageTask.addTask(task);
//			 Bitmap bp = BitmapFactory.decodeResource(
//			 mActivity.getResources(), R.drawable.adf);
//			
//			 int width = bp.getWidth();// 获取真实宽高
//			 int height = bp.getHeight();
//			 LayoutParams lp = iv.getLayoutParams();
//			 lp.height = (height * itemWidth) / width;// 调整高度
//			 iv.setLayoutParams(lp);
//			 ImageView imageview = (ImageView) iv
//			 .findViewById(R.id.waterfall_image);
//			 View _view = (View) iv.findViewById(R.id.waterloading);
//			 _view.setVisibility(View.GONE);
//			 imageview.setImageBitmap(bp);

		}
	}


}
