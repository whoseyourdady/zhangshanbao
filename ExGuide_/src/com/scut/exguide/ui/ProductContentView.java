package com.scut.exguide.ui;

import java.util.ArrayList;

import com.example.exguide.R;
import com.scut.exguide.adapter.HotProViewHolder;
import com.scut.exguide.entity.Product;
import com.scut.exguide.entity.TaskForLogo;
import com.scut.exguide.entity.TaskForProductPics;
import com.scut.exguide.mulithread.LoaderImageTask;
import com.scut.exguide.utility.MyActivity;
import com.scut.exguide.utility.TaskHandler;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ProductContentView {

	private ViewPager mPicsViewPager;
	private ListView mListView;
	private View picsLoading;
	private ViewGroup dotLine;
	private View top;
	private MyAdapter myAdapter;
	private MyActivity mActivity;

	public ProductContentView(View _top, MyActivity activity) {
		top = _top;
		mActivity = activity;
		initialUI();
	}

	private void initialUI() {
		mPicsViewPager = (ViewPager) top
				.findViewById(R.id.product_pics_viewpager);
		mListView = (ListView) top.findViewById(R.id.product_listview);
		dotLine = (ViewGroup) top.findViewById(R.id.picsdotline);
		picsLoading = top.findViewById(R.id.picsloding);
	}

	public void UpdateListView(Product result) {

	}

	public void UpdatePics(ArrayList<String> path) {
		TaskForProductPics task = new TaskForProductPics();
		task.mViewPager = mPicsViewPager;
		task.path = path;
		task.myActivity =  mActivity;
		task.mLoading = picsLoading;
		task.DotLine = dotLine;
		LoaderImageTask.addTask(task);
	}

	// ÊÊÅäÆ÷
	public class MyAdapter extends BaseAdapter {

		private Product mData;// Êý¾ÝÔ´

		public MyAdapter(Product data) {
			mData = data;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			HotProViewHolder viewHolader = null;
			if (null == convertView) {

			} else {
				viewHolader = (HotProViewHolder) convertView.getTag();
			}

			return convertView;
		}

	}

}
