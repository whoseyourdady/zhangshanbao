package com.scut.exguide.entity;

import java.util.ArrayList;
import java.util.zip.Inflater;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.example.exguide.R;
import com.scut.exguide.adapter.PicsPageAdapter;
import com.scut.exguide.adapter.PicsPageChangeListener;
import com.scut.exguide.mulithread.LoaderImageTask;
import com.scut.exguide.ui.ProductActivity;
import com.scut.exguide.utility.MyActivity;
import com.scut.exguide.utility.TaskHandler;

public class TaskForProductPics implements TaskHandler {

	public MyActivity myActivity;
	public Activity mActivity;
	public ArrayList<String> path;
	
	public final int Tag = 3;
	public LayoutInflater mInflater;
	
	public ViewPager mViewPager;
	public View mLoading;
	public ViewGroup DotLine;	
	private ImageView[] DotImage;

	@Override
	public void Update(Object... param) {
		// TODO Auto-generated method stub
		mLoading.setVisibility(View.GONE);
		mInflater = (LayoutInflater) param[0];
		mActivity = (Activity) param[1];
		
		
		mViewPager.setAdapter(new PicsPageAdapter(CreatePics()));
		initalDot();
		mViewPager.setOnPageChangeListener(new PicsPageChangeListener(DotImage));
	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MyActivity getActivity() {
		// TODO Auto-generated method stub
		return myActivity;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTag() {
		// TODO Auto-generated method stub
		return Tag;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return path.size();
	}

	@Override
	public String getPath(int position) {
		// TODO Auto-generated method stub
		return path.get(position);
	}

	private ArrayList<View> CreatePics() {
		DotImage = new ImageView[path.size()];
		ImageView imageView;
		ArrayList<View> viewList = new ArrayList<View>();
		for (int i = 0; i < path.size(); i++) {
			View temp = mInflater.inflate(R.layout.pics_product, null);
			imageView = (ImageView) temp.findViewById(R.id.pics);
			Bitmap bp = LoaderImageTask.PicsMap.get(path.get(i));
			//final Bitmap poster = bitmaps.get(i);
			imageView.setImageBitmap(bp);			
			viewList.add(temp);
		}
		return viewList;
	}
	
	/**
	 * 初始化换页的小圆点
	 */
	private void initalDot() {
		// 初始化小圆点
		DotLine.removeAllViews();
		for (int i = 0; i < DotImage.length; i++) {
			ImageView imageview = new ImageView(mActivity);
			imageview.setLayoutParams(new LayoutParams(20, 20));
			imageview.setPadding(20, 0, 20, 0);
			DotImage[i] = imageview;
			if (i == 0) {
				// 默认选中第一张图片
				DotImage[i]
						.setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				DotImage[i]
						.setBackgroundResource(R.drawable.page_indicator);
			}

			DotLine.addView(DotImage[i]);
		}
	}

}
