package com.scut.exguide.ui;

import java.util.ArrayList;

import com.example.exguide.R;
import com.scut.exguide.adapter.HotProViewHolder;
import com.scut.exguide.entity.Exhibition;
import com.scut.exguide.entity.Product;
import com.scut.exguide.entity.TaskForLogo;
import com.scut.exguide.mulithread.LoaderImageTask;
import com.scut.exguide.utility.Constant;
import com.scut.exguide.utility.MyActivity;
import com.scut.exguide.utility.TaskHandler;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/*
 * 这个类对展品列表做了封装
 */
public class ProuductListView {

	private Activity mActivity;
	private ListView mListView;
	private LayoutInflater mInflater;
	private View mLoading;
	private View mView;

	private MyAdapter mAdapter;

	public ProuductListView(Activity _activity) {
		mActivity = _activity;

		initial();
	}

	// 初始化布局
	public void initial() {
		mInflater = LayoutInflater.from(mActivity);

		mView = mInflater.inflate(R.layout.listview_hotproduction, null);
		mListView = (ListView) mView.findViewById(R.id.hotprolistview);
		mLoading = mView.findViewById(R.id.hotproloading);

	}

	public View getView() {
		return mView;
	}

	public void setAdapter(ArrayList<Product> data) {
		mAdapter = new MyAdapter(data);
		mListView.setAdapter(mAdapter);
		mLoading.setVisibility(View.GONE);

	}

	// 适配器
	public class MyAdapter extends BaseAdapter {

		private ArrayList<Product> mData;// 数据源

		public MyAdapter(ArrayList<Product> data) {
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
				viewHolader = new HotProViewHolder();
				convertView = mInflater.inflate(R.layout.item_product, null);
				viewHolader.image = (ImageView) convertView
						.findViewById(R.id.proimageview);
				viewHolader.name = (TextView) convertView
						.findViewById(R.id.protextview);
				convertView.setTag(viewHolader);
			} else {
				viewHolader = (HotProViewHolder) convertView.getTag();
			}

			TaskHandler task = new TaskForLogo();
			

			((TaskForLogo) task).myActivity = (MyActivity) mActivity;
			((TaskForLogo) task).mImageView = viewHolader.image;
			((TaskForLogo) task).path = mData.get(position).LogoUrl;

			LoaderImageTask.addTask(task);
			
			viewHolader.name.setText(mData.get(position).Name);

			return convertView;
		}

	}
}
