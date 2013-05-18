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

	private LazyScrollView waterfall_scroll;// �Զ���scrollview����
	private LinearLayout waterfall_container;// �Զ���scroll�ڵ�LinearLayout
	private ArrayList<LinearLayout> waterfall_items;
	private Display display;// ��Ļ
	private int itemWidth;// ÿһ��item�Ŀ�ȣ���ÿ��ͼƬ��ռ��Ļ���
	private final int column_count = 2;// ��ʾ����
	private int current_page = 1;// ��ǰ��ʾҳ

	private int mID;// ��ǰҳ���ID

	public WaterFallView(Activity _a) {
		mActivity = _a;
		
		initial();
	}

	// ��ʼ������
	public void initial() {
		mInflater = LayoutInflater.from(mActivity);

		display = mActivity.getWindowManager().getDefaultDisplay();
		itemWidth = display.getWidth() / column_count;// ������Ļ��С����ÿ�д�С

		mView = mInflater.inflate(R.layout.waterfall, null);
		waterfall_scroll = (LazyScrollView) mView
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
//				Message msg = new Message();
//				msg.what = -1;
//				((ExhActivity)mActivity).myHandler.sendMessage(msg);
			}
		});

		waterfall_container = (LinearLayout) mView
				.findViewById(R.id.waterfall_container);
		waterfall_items = new ArrayList<LinearLayout>();// ʵ����item����
		// ѭ���������
		for (int i = 0; i < column_count; i++) {
			LinearLayout itemLayout = new LinearLayout(mActivity);
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
//			 int width = bp.getWidth();// ��ȡ��ʵ���
//			 int height = bp.getHeight();
//			 LayoutParams lp = iv.getLayoutParams();
//			 lp.height = (height * itemWidth) / width;// �����߶�
//			 iv.setLayoutParams(lp);
//			 ImageView imageview = (ImageView) iv
//			 .findViewById(R.id.waterfall_image);
//			 View _view = (View) iv.findViewById(R.id.waterloading);
//			 _view.setVisibility(View.GONE);
//			 imageview.setImageBitmap(bp);

		}
	}


}
